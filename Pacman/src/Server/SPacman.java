package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;

public class SPacman {
    public static void main(String[] args) throws IOException {
        SGameWindow menu = new SGameWindow();
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
        MyServer server = new MyServer();
        System.out.println("IP Address: " + InetAddress.getLocalHost());
        System.out.println("Port: " + SConfig.port);
        server.runServer();
    }
}
