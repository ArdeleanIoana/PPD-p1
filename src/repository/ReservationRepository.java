package repository;

import model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    private String reservationFile;

    public ReservationRepository(String reservationFile) {
        try {
            this.reservationFile = reservationFile;
            FileOutputStream outputStream = new FileOutputStream(reservationFile);
            outputStream.write("".getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
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
