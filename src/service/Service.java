package service;

import model.Reservation;
import model.Tax;
import repository.ReservationRepository;
import repository.TaxRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Service {

    Planner[][] planner;
    AtomicInteger cancellationsNumber = new AtomicInteger(0);
    Lock lock;
    Condition noCancellations;
    ReservationRepository reservationRepository;
    TaxRepository taxRepository;
    Verifier verifier;

    public Service() {
        int prices[] = {50, 20, 40, 100, 30};
        int durations[] = {120, 20, 30, 60, 30};
        int capacities[] = {3, 1, 1, 2, 1};
        verifier = new Verifier(prices, durations, capacities);

        planner = new Planner[5][5];
        for (int i = 0; i < 5; i++) {//locatii
            for (int j = 0; j < 5; j++) {//tratamente
                int actualCapacity;
                if (i == 0)
                    actualCapacity = capacities[j];
                else
                    actualCapacity = capacities[j] * i;
                planner[i][j] = new Planner(prices[j], actualCapacity, durations[j]);
            }
        }
        lock = new ReentrantLock();
        noCancellations = lock.newCondition();
        reservationRepository = new ReservationRepository("rezervations.txt");
        taxRepository = new TaxRepository("payments.txt");

        TimerTask task = new TimerTask() {
            public void run() {
                verify();
            }
        };
        long time = 1000 * 5;
        new Timer().schedule(task, time, time);
    }

    public boolean tryBooking(Reservation reservation) {
        int location = reservation.getLocationId();
        int treatment = reservation.getTreatmentId();
        String hour = reservation.getTreatmentHour();

        if (planner[location][treatment].tryCreateBooking(hour)) {
            reservationRepository.addReservation(reservation);
            return true;
        }

        return false;
    }

    public void pay(Reservation reservation) {

        String date = reservation.getReservationDate();
        String cnp = reservation.getCnpClient();

        int location = reservation.getLocationId();
        int type = reservation.getTreatmentId();
        int sum = planner[location][type].getPrice();

        Tax tax = new Tax(date, cnp, sum);
        taxRepository.addTax(tax);
    }

    public void cancelTax(Reservation reservation) {
        lock.lock();
        cancellationsNumber.incrementAndGet();
        lock.unlock();

        String date = reservation.getReservationDate();
        String cnp = reservation.getCnpClient();
        String hour = reservation.getTreatmentHour();
        int location = reservation.getLocationId();
        int type = reservation.getTreatmentId();
        int sum = planner[location][type].getPrice();

        planner[location][type].cancelBooking(hour);

        reservationRepository.deleteReservation(reservation);
        Tax tax = new Tax(date, cnp, -sum);
        taxRepository.addTax(tax);

        lock.lock();
        if (cancellationsNumber.decrementAndGet() == 0)
            noCancellations.signalAll();
        lock.unlock();
    }

    private void verify() {
        try {
            lock.lock();
            while (cancellationsNumber.get() > 0)
                noCancellations.await();

            List<String> reservations = reservationRepository.getAllReservations();
            List<String> tax = taxRepository.getAllTax();

            verifier.verify(reservations, tax);

            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
