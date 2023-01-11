import client.Client;
import server.Server;

public class Main {
    public static void main(String[] args) {
        for(int i=0;i<30;i++){
            new Client();
        }
        new Server();
    }
}