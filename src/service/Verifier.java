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
        int suma=0;
        for(String payment: payments)
        {
            Tax pay=new Tax(payment);
            suma=suma+pay.getSum();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath + verificationCounter + ".txt");
            verificationCounter++;
            outputStream.write((time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "\n").getBytes());



            outputStream.write("\n".getBytes());
            outputStream.write(Integer.toString(suma).getBytes());
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
