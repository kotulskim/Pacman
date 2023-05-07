package Server;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Properties;

/**Klasaa odpowiedzialna za obsluge listy wynikow*/
public class SRecords {
    /**Przechowuje wyniki uzyskane przez gracza*/
    static ArrayList<String> records;

    /**Zapisuje wynik na zadanie wyslane z klienta. Sortuje liste i usuwa nadmiarowy wyniku*/
    static void scored(String score, String nick) throws IOException {
        serversLoadRecords();
        records.add(nick +  "-" + score);
        records.sort(new MyComparator());
        if(records.size() > 5)
            records.remove(records.size()-1);
        saveRecords();
    }

    /**Wczytuje liste wynikow z pliku i zapisuje je do listy, a nastepnie sortuje*/
    public static void serversLoadRecords() throws IOException {
        InputStream file = new FileInputStream("src/Server/configs/records.txt");
        Properties prop = new Properties();
        prop.load(file);
        records = new ArrayList<>();
        for(int i=0 ; i<5; i++){
                records.add(prop.getProperty("nick"+(i+1)) + "-" + prop.getProperty("score"+(i+1)));
        }
        file.close();
        records.sort(new MyComparator());
    }

    /**Wysyla liste wynikow na zadanie klienta*/
    public static String getRecords() throws IOException {
        InputStream file = new FileInputStream("src/Server/configs/records.txt");
        Properties prop = new Properties();
        prop.load(file);
        String resp = "";
        for(int i=0; i<5; i++){
            if(i < 4) resp += records.get(i) + ", ";
            else if(i == 4) resp += records.get(i);
        }
        return resp;
    }

    /**Zapisuje najlepsze wyniki do pliku*/
    public static void saveRecords() throws IOException {
        InputStream file = new FileInputStream("src/Server/configs/records.txt");
        Properties prop = new Properties();
        prop.load(file);
        for(int i = 0; i< records.size(); i++) {
            prop.setProperty("nick" + (i+1), records.get(i).split(  "-")[0]);
            prop.setProperty("score" + (i+1), records.get(i).split( "-")[1]);
        }
        prop.store(new FileOutputStream("src/Server/configs/records.txt"), null);
        file.close();
    }

    /**Implementacja klasy Comparator z biblioteki standardowej*/
    static class MyComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2){
            Integer a = Integer.parseInt(o1.split( "-")[1]);
            Integer b = Integer.parseInt(o2.split( "-")[1]);
            return -a.compareTo(b);
        }
    }
}