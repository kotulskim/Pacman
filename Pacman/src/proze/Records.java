package proze;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.*;
import java.util.*;

/**Klasa odpowiadajaca za liste rekordow*/
public class Records {
    /**Lista przechowujaca informacje na temat wynikow*/
    static ArrayList<String> record;

    /**Metoda odpowiedzialna za zapisywanie wyniku gracza do listy. Automatycznie sortuje liste, ustala i sprawdza jej wielkosc, usuwajac nadmiarowy wynik.*/
    static void scored(int score, String nick) throws IOException {
        loadRecords();
        record.add(nick +  "-" + score);
        record.sort(new MyComparator());
        if(record.size() > 5)
            record.remove(record.size()-1);
        saveRecords();
    }

    /**Zapisywanie wynikow do pliku*/
    public static void saveRecords() throws IOException {
        InputStream file = new FileInputStream("src/proze/configs/records.txt");
        Properties prop = new Properties();
        prop.load(file);
        for(int i = 0; i< record.size(); i++) {
            prop.setProperty("nick" + (i+1), record.get(i).split(  "-")[0]);
            prop.setProperty("score" + (i+1), record.get(i).split( "-")[1]);
        }
        prop.store(new FileOutputStream("src/proze/configs/records.txt"), null);
        file.close();
    }

    /**Odczytywanie wynikow z pliku*/
    public static void loadRecords() throws IOException {
        InputStream file = new FileInputStream("src/proze/configs/records.txt");
        Properties prop = new Properties();
        prop.load(file);
        record = new ArrayList<>();
        for(int i=0 ; i<5; i++) {
            record.add(prop.getProperty("nick"+(i+1)) + "-" + prop.getProperty("score"+(i+1)));
        }
        file.close();
        record.sort(new MyComparator());
    }

    /**Wczytywanie wynikow poprzez wyslanie zadania pobrania listy z serwera*/
    public static void serversLoadRanking() throws IOException {
        record = new ArrayList<>();
        String serverRecords = "";
        serverRecords += Client.getRecords();
        record.addAll(Arrays.asList(serverRecords.split(", ")).subList(0,5));
    }

    /**Wysyla zadanie zapisania wynikow na serwerze*/
    public static void serversSaveScore(String nick, int score) throws IOException {
        String req = "";
        req += nick + "/" + score;
        Client.saveScore(req);
    }

    /**Implementacja klasy porownujacej z biblioteki standardowej. Uzywana do sortowania wynikow*/
    static class MyComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2){
            Integer a = Integer.parseInt(o1.split( "-")[1]);
            Integer b = Integer.parseInt(o2.split( "-")[1]);
            return -a.compareTo(b);
        }
    }
}

