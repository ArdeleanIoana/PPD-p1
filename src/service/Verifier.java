package service;

import model.Reservation;
import model.Tax;

import java.io.FileOutputStream;
import java.util.*;

public class Verifier {
    private int prices[];
    private int duration[];
    private int capacity[];
    private int verificationCounter = 0;
    private String filePath = "verification";


    public Verifier(int prices[], int duration[], int capacity[]) {
        this.prices = prices;
        this.duration = duration;
        this.capacity = capacity;
    }

    public void verify(List<String> reservations, List<String> payments) {
        List<List<List<Integer>>> slotsLocations = new ArrayList<>();

        for (int i = 0; i < 5; i++) {//locatii
            List<List<Integer>> slots = new ArrayList<>();
            for (int j = 0; j < 5; j++) {//tratamente
                int numberSlots = (18 - 10) * 60 / duration[j];
                List<Integer> actualSlot = new ArrayList<>();
                for (int k = 0; k < numberSlots; k++) {
                    actualSlot.add(0);
                }
                slots.add(actualSlot);
            }
            slotsLocations.add(slots);
        }

        var time = java.time.LocalTime.now();

        HashMap<String, Queue<Reservation>> possiblyUnpaid = new HashMap<>();//cnp->rezervari client
        HashMap<String, Integer> finalisedPayments = new HashMap<>();

        List<List<Reservation>> unpaid = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            unpaid.add(new ArrayList<Reservation>());
        }

        int totalSums[] = new int[5];

        for (int i = 0; i < 5; i++) {
            totalSums[i] = 0;
        }

        for (String reservationString : reservations) {
            Reservation reservation = new Reservation(reservationString);
            int idx = computeSlot(reservation.getTreatmentHour(), duration[reservation.getTreatmentId()]);
            int nr = slotsLocations.get(reservation.getLocationId()).get(reservation.getTreatmentId()).get(idx);
            slotsLocations.get(reservation.getLocationId()).get(reservation.getTreatmentId()).set(idx, nr + 1);

            if (!possiblyUnpaid.containsKey(reservation.getCnpClient()))
                possiblyUnpaid.put(reservation.getCnpClient(), new LinkedList<>());
            possiblyUnpaid.get(reservation.getCnpClient()).add(reservation);
        }

        for (String paymentString : payments) {
            Tax payment = new Tax(paymentString);
            if (!finalisedPayments.containsKey(payment.getCnp()))
                finalisedPayments.put(payment.getCnp(), 0);
            if (payment.getSum() > 0)
                finalisedPayments.put(payment.getCnp(), finalisedPayments.get(payment.getCnp()) + 1);
            else
                finalisedPayments.put(payment.getCnp(), finalisedPayments.get(payment.getCnp()) - 1);
        }

        for (String cnp : possiblyUnpaid.keySet()) {
            int numberPayments = 0;
            if (finalisedPayments.containsKey(cnp))
                numberPayments = finalisedPayments.get(cnp);
            for (int i = 0; i < numberPayments; i++) {
                Reservation resv = possiblyUnpaid.get(cnp).poll();
                totalSums[resv.getLocationId()] += prices[resv.getTreatmentId()];
            }
            while (!possiblyUnpaid.get(cnp).isEmpty()) {
                Reservation p = possiblyUnpaid.get(cnp).poll();
                unpaid.get(p.getLocationId()).add(p);
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(filePath + verificationCounter + ".txt");
            verificationCounter++;
            outputStream.write((time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "\n").getBytes());
            for (int i = 0; i < 5; i++) {
                outputStream.write(("Locatia " + i + " are soldul " + totalSums[i] + "\n").getBytes());
                outputStream.write("Tratamentele neplatite sunt:\n".getBytes());
                for (Reservation p : unpaid.get(i)) {
                    outputStream.write(p.toFileString().getBytes());
                }
                
                outputStream.write("\n".getBytes());
            }

            outputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public int computeSlot(String hourString, int duration) {
        int hour = Integer.parseInt(hourString.substring(0, 2));
        int minute = Integer.parseInt(hourString.substring(3, 5));

        return (int) Math.floor(((hour - 10) * 60 + minute) / (float) duration);
    }
}
