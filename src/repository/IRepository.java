package repository;

import model.Reservation;
import model.Tax;

import java.util.List;
import java.util.Vector;

public interface IRepository {
    void deleteReservation(Reservation Reservation);
    void addReservation(Reservation Reservation);
    void addTax(Tax tax);
    Vector<String> getAllTax();
    List<String> getAllReservations();
}
