package server;

import model.Reservation;
import model.Confirmation;
import model.ReservationDTO;
import service.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Worker {

    private static Service service = new Service();

    static ReservationDTO createReservation(Socket clientSocket) {
        System.out.println("Client " + clientSocket.getPort()  + " ask for a reservation");
        boolean accepted = true;
        Reservation reservation = null;
        try {
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            reservation = new Reservation(in.readUTF());
            accepted = service.tryBooking(reservation);

            out.writeUTF((new Confirmation(accepted)).toString());
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return new ReservationDTO(-1, null, null);
        }



        return new ReservationDTO(accepted ? 1 : 0, reservation, clientSocket);
    }

    static ReservationDTO pay(ReservationDTO reservationDto) {
        if(reservationDto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(reservationDto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(reservationDto.socket.getInputStream());

                in.readUTF();
                service.pay(reservationDto.reservation);

                out.writeUTF((new Confirmation(true)).toString());
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
                return new ReservationDTO(-1, null, null);
            }
        }

        return reservationDto;
    }

    static void cancelPayment(ReservationDTO reservationDto) {
        if(reservationDto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(reservationDto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(reservationDto.socket.getInputStream());

                Confirmation confirmation =  new Confirmation(in.readUTF());
                //daca e falsa atunci inseamna ca nu se da cancel(sanse de 50%)
                if(confirmation.getAccepted())
                    service.cancelTax(reservationDto.reservation);

                out.writeUTF((new Confirmation(true)).toString());
                out.flush();
                reservationDto.socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(reservationDto.state == 0) {
            try {
                reservationDto.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
