package server;

import domain.Reservation;
import domain.Confirmation;
import domain.DTO;
import domain.Reservation;
import service.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Worker {

    private static Service service = new Service();

    static DTO createReservation(Socket clientSocket) {
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
            return new DTO(-1, null, null);
        }

//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return new DTO(accepted ? 1 : 0, reservation, clientSocket);
    }

    static DTO pay(DTO dto) {
        if(dto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(dto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(dto.socket.getInputStream());

                in.readUTF();
                service.pay(dto.reservation);

                out.writeUTF((new Confirmation(true)).toString());
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
                return new DTO(-1, null, null);
            }
        }

        return dto;
    }

    static void cancelPayment(DTO dto) {
        if(dto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(dto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(dto.socket.getInputStream());

                Confirmation confirmation =  new Confirmation(in.readUTF());
                //daca e falsa atunci inseamna ca nu se da cancel(sanse de 50%)
                if(confirmation.getAccepted())
                    service.cancelTax(dto.reservation);

                out.writeUTF((new Confirmation(true)).toString());
                out.flush();
                dto.socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(dto.state == 0) {
            try {
                dto.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
