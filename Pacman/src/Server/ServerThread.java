package Server;

import java.io.*;
import java.net.Socket;

/**Klasa odpowiedzialna za tworzenie watku, gdzie serwer obsluguje zadania klienta*/
public class ServerThread implements  Runnable{
    private Socket socket;
    public ServerThread(Socket socket){
        this.socket = socket;
    }

    /**Metoda odczytujaca zadanie klienta i wywolujaca odpowiednie metody w celu spelnienia zadania*/
    @Override
    public void run() {
        try {
            while(true) {
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                String client = br.readLine();

                if(client != null) {
                    System.out.println("From client: [" + client + "]");
                    String responseFromServer = Commands.serverAnswer(client);
                    System.out.println("Response from Server: " + responseFromServer);
                    pw.println(responseFromServer);
                    pw.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lost connection");
        }
    }
}
