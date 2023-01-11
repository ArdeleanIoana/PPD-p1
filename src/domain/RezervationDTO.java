package domain;

import java.net.Socket;

public class RezervationDTO {
    public int state;
    public Reservation reservation;
    public Socket socket;

    public RezervationDTO(int state, Reservation reservation, Socket socket) {
        this.state = state;
        this.reservation = reservation;
        this.socket = socket;
    }
}