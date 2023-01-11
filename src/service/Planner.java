package service;

import java.util.ArrayList;
import java.util.List;

public class Planner {

    private int price;
    private int availableSpots;
    private int duration;
    List<Integer> appointmentSlots = new ArrayList<>();

    public Planner(int price, int availableSpots, int duration) {
        this.price = price;
        this.availableSpots = availableSpots;
        this.duration = duration;

        int capacity = (18 - 10) * 60 / duration;
        for (int i = 0; i < capacity; i++)
            appointmentSlots.add(0);
    }

    public int computeSlot(String hourString) {
        int hour = Integer.parseInt(hourString.substring(0, 2));
        int minute = Integer.parseInt(hourString.substring(3, 5));

        return (int) Math.floor(((hour - 10) * 60 + minute) / (float) duration);//
    }

    public synchronized boolean tryCreateBooking(String hourString) {
        int index = computeSlot(hourString);
            //end time???
        if (appointmentSlots.get(index) < availableSpots) {
            appointmentSlots.set(index, appointmentSlots.get(index) + 1);
            return true;
        }
        return false;
    }

    public synchronized void cancelBooking(String hourString) {
        int index = computeSlot(hourString);
        appointmentSlots.set(index, appointmentSlots.get(index) - 1);
    }

    public int getPrice() {
        return price;
    }


}
