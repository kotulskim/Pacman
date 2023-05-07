package proze;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


/** Klasa odpowiedzialna za komunikacje z serwerem*/
public class Client {
    static Socket socket;
    /** Przechowuje informacje czy serwer jest dostepny */
    static boolean online = false;

    /**
     * Przesyla zadanie do serwera o dane z pliku konfiguracyjnego
     * @return odpowiedz od serwera
     */
    static String getConfig() throws IOException {
        String respond = Client.connect("getConfig");
        socket.close();
        return respond;
    }

    /**
     * Przesyla zadanie do serwera o dane z pliku konfiguracyjnego na temat danego poziomu
     * @param level poziom gry, ktory ma byc wczytany i wyswietlony
     * @return odpowiedz od serwera
     */
    static String getMap(int level) throws IOException {
        String respond = connect("getMap" + "/" + level);
        socket.close();
        return respond;
    }

    /**
     * Przesyla zadanie do serwera o liste z najlepszymi wynikami
     * @return odpowiedz serwera
     */
    static String getRecords() throws IOException {
        String respond = connect("getRecords");
        socket.close();
        return respond;
    }

    /**
     * Sprawdza czy serwer jest dostepny
     * @return wartosc true: serwer dostepny, wartosc false: serwer niedostepny
     */
    static boolean checkIfOnline(){
        try {
            online = true;
            socket = new Socket("localhost", Config.port);
        } catch (IOException e) {
            System.out.println("server offline");
            online = false;
        }
        return online;
    }

    /**
     * Laczy sie z serwerem
     * @param command zawartosc zadania
     * @return odpowiedz serwera
     */
    private static String connect(String command) throws IOException {
        try {
            socket = new Socket("localhost", Config.port);
        } catch (Exception e){
            System.out.println("Serw offline");
            disconnectedFrame();
        }
        socket = new Socket("localhost", Config.port);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(command);
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();
    }

    /**
     * Wyświetla okno informujące o utracie polaczenia z serwerem
     */
    private static void disconnectedFrame() {
        JFrame lostConnectionFrame = new JFrame();
        lostConnectionFrame.setSize(350,200);
        lostConnectionFrame.setTitle("Utrata połączenia");
        lostConnectionFrame.setLayout(null);

        JLabel lostConnectionLabel = new JLabel("Stracono połączenie z serwerem");
        lostConnectionLabel.setBounds(90,50,200,30);
        JButton exitButton = new JButton("Zamknij");
        exitButton.setBounds(120,120,100,30);
        exitButton.addActionListener(event -> {
            System.exit(1);
        });

        lostConnectionFrame.add(lostConnectionLabel);
        lostConnectionFrame.add(exitButton);
        lostConnectionFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        lostConnectionFrame.setVisible(true);
    }

    /**
     * Odpowiada za zapisywanie wyniku na serwerze
     * @param save nazwa uzytkownika i wynik
     */
    public static void saveScore(String save) throws IOException {
        connect("save" + "/" + save);
        socket.close();
    }
}
