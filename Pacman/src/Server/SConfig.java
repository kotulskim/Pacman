package Server;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**Klasa odpowiedzialna za odczytywanie plikow konfiguracyjnych*/
public class SConfig {

    /**wielość piksela planszy*/
    static int blockSize;
    /**ilość pikseli na krawędzi planszy*/
    static int nBlocks;
    /**wielość planszy*/
    static int sizeOfScreen;
    /**tablica przechowywująca informacje o wyglądzie planszy*/
    static int[] loadLevel;
    /**Zmienna przechowujaca informacje o maksymalnej liczbie punktow za level*/
    static int maxScore;
    /**Zmienna przechowujaca informacje o wspolrzednej x polozenia Pacmana*/
    static int startPacman_x;
    /**Zmienna przechowujaca informacje o wspolrzednej y polozenia Pacmana*/
    static int startPacman_y;
    /**Zmienna przechowujaca informacje o wspolrzednej x polozenia duszkow*/
    static int startGhosts_x;
    /**Zmienna przechowujaca informacje o wspolrzednej y polozenia duszkow*/
    static int startGhosts_y;
    /**Zmienna przechowujaca informacje o ilosci poziomow*/
    static int amountOfLevels;
    /**Przechowywuje nr portu*/
    static int port;


    /**Wczytywany jest numer portu z  pliku konfiguracyjnego*/
    static void loadPort() throws IOException {
        InputStream file = new FileInputStream("src/Server/configs/portConfig.txt");
        Properties prop = new Properties();
        prop.load(file);
        port = Integer.parseInt(prop.getProperty("port"));
    }

    /**Metoda wczytujaca pliki konfiguracyjne
     * @return tekst, w ktorym sa wszystkie dane z pliku
     * */
    static String loadConfig() throws IOException {
        InputStream file = new FileInputStream("src/Server/configs/conf2.txt");
        Properties config = new Properties();
        config.load(file);
        String resp = "";
        for(int i=0; i<15; i++) {

            System.out.println(i);
            resp += (config.getProperty("par" + i) + " ");
        }
        System.out.println("resp = " + resp);
        file.close();
        return resp;
    }

    /**
     * Metoda wczytujaca wyglad planszy
     * @return tekst, w ktorym sa dane na temat wygladu planszy
     */
    static String loadMap(int level) throws IOException {
        InputStream file = new FileInputStream("src/Server/configs/map.txt");
        Properties mapConfig = new Properties();
        mapConfig.load(file);
        loadLevel = Arrays.stream(mapConfig.getProperty("ll" + level).split(" ")).mapToInt(Integer::parseInt).toArray();
        blockSize= Integer.parseInt(mapConfig.getProperty("bs" + level));
        nBlocks = Integer.parseInt(mapConfig.getProperty("nBlocks" + level));
        maxScore = Integer.parseInt(mapConfig.getProperty("maxScore" + level));
        startPacman_x = Integer.parseInt(mapConfig.getProperty("startPacman_x" + level));
        startPacman_y = Integer.parseInt(mapConfig.getProperty("startPacman_y" + level));
        startGhosts_x = Integer.parseInt(mapConfig.getProperty("startGhosts_x" + level));
        startGhosts_y= Integer.parseInt(mapConfig.getProperty("startGhosts_y" + level));
        sizeOfScreen = blockSize * nBlocks;
        return (Arrays.toString(loadLevel) +"/" + blockSize + "/" + nBlocks + "/" + maxScore + "/" + startPacman_x + "/" + startPacman_y + "/"
                + startGhosts_x + "/" + startGhosts_y + "/" +sizeOfScreen).replace("[", "").replace("]", "").replace(",", "").trim();


    }
}