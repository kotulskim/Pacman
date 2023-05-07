package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**Klasa, ktora odpowiada za obslugiwanie zadan klienta*/
public class MyServer {
    private static ServerSocket serverSocket;
    int port;

    /**W konstruktorze przypisywany jest numer portu z pliku konfiguracyjnego*/
    public MyServer() throws IOException {
        SConfig.loadPort();
        port = SConfig.port;
    }

    /**W momencie, w ktorym pojawia sie klient tworzony jest nowy watek, nastepnie serwer oczekuje na nastepne klienty*/
    public void runServer() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Serwer dostepny");
        while(true){
            Socket socket = serverSocket.accept();
            new Thread(new ServerThread(socket)).start();
        }
    }
}
