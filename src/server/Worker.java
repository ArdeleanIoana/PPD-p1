package server;

import model.Reservation;
import model.Confirmation;
import model.RezervationDTO;
import service.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Worker {

    private static Service service = new Service();

    static RezervationDTO createReservation(Socket clientSocket) {
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
            return new RezervationDTO(-1, null, null);
        }

//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return new RezervationDTO(accepted ? 1 : 0, reservation, clientSocket);
    }

    static RezervationDTO pay(RezervationDTO rezervationDto) {
        if(rezervationDto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(rezervationDto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(rezervationDto.socket.getInputStream());

                in.readUTF();
                service.pay(rezervationDto.reservation);

                out.writeUTF((new Confirmation(true)).toString());
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
                return new RezervationDTO(-1, null, null);
            }
        }

        return rezervationDto;
    }

    static void cancelPayment(RezervationDTO rezervationDto) {
        if(rezervationDto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(rezervationDto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(rezervationDto.socket.getInputStream());

                Confirmation confirmation =  new Confirmation(in.readUTF());
                //daca e falsa atunci inseamna ca nu se da cancel(sanse de 50%)
                if(confirmation.getAccepted())
                    service.cancelTax(rezervationDto.reservation);

                out.writeUTF((new Confirmation(true)).toString());
                out.flush();
                rezervationDto.socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(rezervationDto.state == 0) {
            try {
                rezervationDto.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
