package model;

import java.net.Socket;

public class ReservationDTO {
    public int state;
    public Reservation reservation;
    public Socket socket;

    public ReservationDTO(int state, Reservation reservation, Socket socket) {
        this.state = state;
        this.reservation = reservation;
        this.socket = socket;
    }
}