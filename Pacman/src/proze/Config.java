package proze;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**Klasa odpowiedzialna za parsowanie plików konfiguracyjnych*/
public class Config {
    /**przechowuje szerokość okna głównego*/
    static int width;
    /**przechowuje wysokość okna głównego*/
    static int height;
    /**przechowuje informacje o aktualnie wczytywanym poziomie gry*/
    static int level;
    /**przechowuje informacje punktach za małą kulke*/
    static int bonusBall;
    /**przechowuje informacje punktach za dużą kulke*/
    static int bonusBigBall;
    /**ilość dużych kulek*/
    static int bigBall;
    /**ilość małych kulek*/
    static int ball;
    /**ilość żyć*/
    static int lifes;
    /**ilość dodatkowych punktów za zachowane życie*/
    static int bonusLife;
    /**szybkość poruszania się Pacmana*/
    static int speed;
    /**przechowuje zasady gry*/
    static String[] rules;
    /**wielkość małej kulki*/
    static int sizeOfBall;
    /**wielość dużej kulki*/
    static int sizeOfBigBall;
    /**wielość pacmana*/
    static int sizeOfPacman;
    /**wielość duszka*/
    static int sizeOfGhost;
    /**ilość duszków*/
    static int amountOfGhost;
    /**wielość piksela planszy*/
    static int blockSize;
    /**ilość pikseli na krawędzi planszy*/
    static int nBlocks;
    /**wielość planszy*/
    static int sizeOfScreen;
    /**tablica przechowywująca informacje o wyglądzie planszy*/
    static int[] loadLevel;
    /**Zmienna przechowujaca animacje*/
    public static Image down, right, heart, ghost, up, left;
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
    /**Przechowywuje adres IP*/
    static String ipAddress;


    /**Przypisuje nr portu i IP*/
    static void portAndIP(String ipAddress, int port) {
        Config.ipAddress = ipAddress;
        Config.port = port;
    }

    /**metoda wczytująca pliki konfiguracujne z pliku*/
    static void configParse() throws IOException{
        InputStream file = new FileInputStream("src/proze/configs/con1.txt");
        Properties config = new Properties();
        config.load(file);
        width = Integer.parseInt(config.getProperty("width"));
        height = Integer.parseInt(config.getProperty("height"));
        speed = Integer.parseInt(config.getProperty("speed"));
        bonusBall = Integer.parseInt(config.getProperty("bonusBall"));
        bonusBigBall = Integer.parseInt(config.getProperty("bonusBigBall"));
        lifes = Integer.parseInt(config.getProperty("lifes"));
        bonusLife = Integer.parseInt(config.getProperty("bonusLife"));
        sizeOfBall = Integer.parseInt(config.getProperty("sizeOfBall"));
        sizeOfBigBall = Integer.parseInt(config.getProperty("sizeOfBigBall"));
        amountOfGhost = Integer.parseInt(config.getProperty("amountOfGhost"));
        amountOfLevels= Integer.parseInt(config.getProperty("amountOfLevels"));
        file.close();
    }

    /**Wczytuje pliki konfiguracyjne gry z serwera*/
    static void serversConfigParse() throws IOException {
        String resp = Client.getConfig();
        System.out.println(resp);
        int[] config;
        config = Arrays.stream(resp.split(" ")).mapToInt(Integer::parseInt).toArray();
        width = config[0];
        height = config[1];
        amountOfLevels = config[2];
        speed = config[3];
        bonusBall = config[4];
        bonusBigBall =  config[5];
        bigBall = config[6];
        ball = config[7];
        lifes = config[8];
        bonusLife = config[9];
        sizeOfBall = config[10];
        sizeOfBigBall = config[11];
        sizeOfPacman = config[12];
        sizeOfGhost = config[13];
        amountOfGhost = config[14];

    }


    /**metoda wczytująca informacje o planszy z pliku*/
    static void loadMap(int level) throws IOException {
        InputStream file = new FileInputStream("src/proze/configs/map.txt");
        Properties mapConfig = new Properties();
        mapConfig.load(file);
        maxScore = Integer.parseInt(mapConfig.getProperty("maxScore" + level));
        startPacman_x = Integer.parseInt(mapConfig.getProperty("startPacman_x" + level));
        startPacman_y = Integer.parseInt(mapConfig.getProperty("startPacman_y" + level));
        startGhosts_x = Integer.parseInt(mapConfig.getProperty("startGhosts_x" + level));
        startGhosts_y= Integer.parseInt(mapConfig.getProperty("startGhosts_y" + level));

        loadLevel = Arrays.stream(mapConfig.getProperty("ll" + level).split(" ")).mapToInt(Integer::parseInt).toArray();
        blockSize= Integer.parseInt(mapConfig.getProperty("bs" + level));
        nBlocks = Integer.parseInt(mapConfig.getProperty("nBlocks" + level));
        sizeOfScreen = blockSize * nBlocks;
        //resize();
    }

    /**Metoda wczytująca informacje o planszy z serwera*/
    static void serversLoadMap(int level) throws IOException {
        String resp = Client.getMap(level);
        System.out.println(resp);
        String[] config = resp.split("/");
        //config = Arrays.stream(resp.split(" ")).mapToInt(Integer::parseInt).toArray();
        loadLevel = Arrays.stream(config[0].split(" ")).mapToInt(Integer::parseInt).toArray();
        blockSize = Integer.parseInt(config[1]);
        nBlocks = Integer.parseInt(config[2]);
        maxScore = Integer.parseInt(config[3]);
        startPacman_x = Integer.parseInt(config[4]);
        startPacman_y = Integer.parseInt(config[5]);
        startGhosts_x = Integer.parseInt(config[6]);
        startGhosts_y = Integer.parseInt(config[7]);
        System.out.println(startPacman_x);
        sizeOfScreen =  blockSize * nBlocks;
        //resize();
    }



    /**metoda wczytująca zasady gry z pliku*/
    static void loadRules() throws IOException {
        File file = new File("src/proze/configs/rules.txt");

        Scanner in = new Scanner(file);
        rules = new String[30];
        if (file.exists()) {
            int i = 0;
            while (in.hasNext()) {
                rules[i] = in.nextLine();
                i++;

            }
        }
    }

    /**Metoda wczytujaca animacje */
    public static void loadImages(){
        down = new ImageIcon("src/proze/images/down.gif").getImage();
        up = new ImageIcon("src/proze/images/up.gif").getImage();
        right = new ImageIcon("src/proze/images/right.gif").getImage();
        left = new ImageIcon("src/proze/images/left.gif").getImage();
        heart = new ImageIcon("src/proze/images/heart.gif").getImage();
        ghost = new ImageIcon("src/proze/images/ghost.gif").getImage();

    }







}
