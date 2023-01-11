package client;

import domain.Reservation;
import domain.Confirmation;
import utilsProp.Properties;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private String name;
    private String cnp;
    private String date,date_reservation;
    private Timer timer;

    public Client() {
        this.name = randomString(10);
        this.cnp = randomStringNumber(10);
        this.date = "1/11/2023";
        this.date_reservation="1/15/2023";

        TimerTask task = new TimerTask() {
            public void run() {
//                System.out.println("Sending Programare");
                SendReservation();
            }
        };

        this.timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 2000);

    }

    private String randomHour(){
        String s = "";
        int hour = (int)(Math.random()*8)+10;
        int minutes = (int)(Math.random()*60);
        if(hour<10) s+="0";
        s+=hour;
        s+=":";
        if(minutes<10) s+="0";
        s+=minutes;
        return s;
    }

    private String randomStringNumber(int length){
        String s = "";
        for(int i=0;i<length;i++){
            s += (int)(Math.random()*10);
        }
        return s;
    }

    public static String randomString(int length){
        String s = "";
        for(int i=0;i<length;i++){
            s += (char)(Math.random()*26 + 'a');
        }
        return s;
    }

    public void SendReservation(){
        try {
            Socket socket = new Socket("localhost", Properties.getPort());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.writeUTF(generateReservation().toString());
            out.flush();
            Confirmation c = new Confirmation(in.readUTF());

            if(!c.getAccepted()){
                System.out.println("Programare nereusita");
            }else{
                System.out.println("Programare reusita");

                out.writeUTF((new Confirmation(true)).toString());
                out.flush();
                c = new Confirmation(in.readUTF());

                boolean cancelled = false;
                if( Math.random() < 0.5)
                    cancelled = true;
                out.writeUTF((new Confirmation(cancelled)).toString());
                //c = new Confirmation(in.readUTF());
                out.flush();

            }

            try {
                out.close();
                in.close();
                socket.close();
            }
            catch (IOException e) {
                System.out.println("Socket deja inchis");
            }

        } catch (IOException e) {
            timer.cancel();
            timer.purge();
        }
    }

    public Reservation generateReservation(){
        return new Reservation(name, cnp, date, (int)(Math.random()*5), new Random().nextInt(5) + 1, date_reservation, randomHour());
    }

}
