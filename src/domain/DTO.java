package domain;

import java.net.Socket;

public class DTO {
    public int state;
    public Reservation reservation;
    public Socket socket;

    public DTO(int state, Reservation reservation, Socket socket) {
        this.state = state;
        this.reservation = reservation;
        this.socket = socket;
    }
}