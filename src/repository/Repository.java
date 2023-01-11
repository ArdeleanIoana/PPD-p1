package repository;

import model.Reservation;
import model.Tax;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Repository {

    private final String reservationFile = "rezervations.txt";
    private final String paymentFile = "payments.txt";
    public Repository(){
        initReservation();
        initTax();
    }
    private void initReservation(){
        try {

            FileOutputStream outputStream = new FileOutputStream(reservationFile);
            outputStream.write("".getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initTax(){
        try {
            FileOutputStream fileOut = new FileOutputStream(paymentFile);
            fileOut.write("".getBytes());
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void deleteReservation(Reservation Reservation) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(reservationFile));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            String ReservationFileString = Reservation.toFileString();

            while ((line = file.readLine()) != null) {
                if (!line.equals(ReservationFileString)) {
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
            }
            file.close();

            FileOutputStream outputStream = new FileOutputStream(reservationFile);
            outputStream.write(inputBuffer.toString().getBytes());
            outputStream.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public synchronized void addReservation(Reservation Reservation) {
        try {
            FileOutputStream outputStream = new FileOutputStream(reservationFile, true);
            outputStream.write(Reservation.toFileString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void addTax(Tax tax) {
        try {
            FileOutputStream outputStream = new FileOutputStream(paymentFile, true);
            outputStream.write(tax.toFileString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Vector<String> getAllTax() {
        Vector<String> result = new Vector<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(paymentFile));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public synchronized List<String> getAllReservations() {
        List<String> result = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(reservationFile));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
