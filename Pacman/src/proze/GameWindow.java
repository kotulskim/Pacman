package proze;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.io.*;
import static proze.Config.*;
import static proze.Records.*;

/**Klasa odpowiedzialna za głowne okno i obsługę menu*/
public class GameWindow extends JFrame {
    /**przyciski główne w menu*/
    JMenu options, help, start;
    /**przyciski dodatkowe w menu*/
    JMenuItem recordsList, quit, online, offline, gameRules;
    /**aktualny poziom gry*/
    static int currentLevel;
    /**okno gry*/
    private JFrame pacmanFrame;
    /**zmianna przechowywująca nazwe gracza*/
    public String nick;
    /**licznik będzie odpowiadał za animacje*/
    Timer timer = new Timer();
    /**Suma wyników ze wszystkich poziomów*/
    public int sumScore = 0;




/**Konstruktor klasy GameWindow. Wczytuje dane o oknie głównym i ustawia je.*/
    public GameWindow() throws IOException {

        configParse();
        this.setSize(Config.width, Config.height);
        setTitle("Pacman");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);


    }

/**metoda odpowiadające za główne okno menu*/
    public void mainMenu() {
        this.setLayout(null);
        currentLevel = 1;
        JMenuBar mb = new JMenuBar();
        options = new JMenu("Opcje");
        help = new JMenu("Pomoc");
        start = new JMenu("Start");
        recordsList = new JMenuItem("Lista Rekordów");
        recordsList.addActionListener(event -> {
            try {
                recordsMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        quit = new JMenuItem("Koniec");
        online = new JMenuItem("Graj Online");
        offline = new JMenuItem("Graj Offline");
        gameRules = new JMenuItem("Zasady Gry");
        options.add(start);
        options.add(recordsList);
        options.add(quit);
        quit.addActionListener(event -> System.exit(0));
        quit.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        help.add(gameRules);
        gameRules.addActionListener(event -> {
            try {
                gameRules();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        start.add(online);
        online.addActionListener(event ->

                ipMenu()
        );
        start.add(offline);
        offline.addActionListener(event -> {
            Client.online = false;
            nickMenu();
        });
        setJMenuBar(mb);
        mb.add(options);
        mb.add(help);

    }

    /**metoda tworzy okno z zasadami gry*/
    private void gameRules() throws IOException {
        JFrame rulesFrame = new JFrame("Zasady gry");
        rulesFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        rulesFrame.setLayout(null);
        rulesFrame.setVisible(true);
        rulesFrame.setSize(800, 600);
        rulesFrame.setMinimumSize(new Dimension(400, 300));
        rulesFrame.setLocationRelativeTo(null);

        JButton quitButton = new JButton("Zamknij");
        quitButton.addActionListener(event -> rulesFrame.dispose());
        quitButton.setBounds(350, 450, 100, 30);
        rulesFrame.add(quitButton);

        loadRules();
        loadRules();
        JLabel[] label = new JLabel[20];

        for (int i = 0; i < 19; i++) {
            label[i] = new JLabel(Config.rules[i]);
            label[i].setBounds(50, 50 + 20 * i, 700, 15);
            rulesFrame.add(label[i]);
        }
    }


    /**metoda tworzy okno do łączenia się z serwerem*/
    public void ipMenu() {
        JFrame ipFrame = new JFrame("Okno połączenia z serwerem");
        ipFrame.setSize(500, 300);
        ipFrame.setLayout(null);
        ipFrame.setVisible(true);
        ipFrame.setLocationRelativeTo(null);
        ipFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton exitButton = new JButton("Zamknij");
        JButton polaczButton = new JButton("Połącz");
        JLabel ipLabel = new JLabel("Adres IP");
        JLabel portLabel = new JLabel("Port:");
        JLabel bladLabel = new JLabel();
        JTextField ipText = new JFormattedTextField("192.168.11.111");
        JTextField portText = new JFormattedTextField("12321");

        exitButton.setBounds(210, 220, 80, 25);
        polaczButton.setBounds(210, 150, 80, 30);
        ipLabel.setBounds(160, 80, 60, 30);
        portLabel.setBounds(170, 110, 50, 30);
        bladLabel.setBounds(100, 50, 300, 30);
        ipText.setBounds(230, 90, 150, 20);
        portText.setBounds(230, 120, 100, 20);

        exitButton.addActionListener(event -> ipFrame.dispose());
        polaczButton.addActionListener(event -> {
            Config.portAndIP(ipText.getText(), Integer.parseInt(portText.getText()));
            try{
                Config.serversConfigParse();

            }

            catch (IOException e) {
                e.printStackTrace();
                bladLabel.setText("Nie udało się połączyć z serwerem");
                revalidate();
            }
            if(Client.checkIfOnline() == true){
                nickMenu();
                ipFrame.dispose();
            }

        });
        ipFrame.add(exitButton);
        ipFrame.add(polaczButton);
        ipFrame.add(ipLabel);
        ipFrame.add(portLabel);
        ipFrame.add(ipLabel);
        ipFrame.add(portLabel);
        ipFrame.add(ipText);
        ipFrame.add(portText);
        ipFrame.add(bladLabel);
    }

    /**Metoda tworzy okno do wpisania nazwy użytkownika*/
    private void nickMenu() {
        JDialog playerNick = new JDialog();
        playerNick.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        playerNick.setTitle("Nazwa gracza");
        playerNick.setSize(350, 180);
        playerNick.setResizable(false);
        playerNick.setLayout(null);
        playerNick.setVisible(true);
        playerNick.setLocationRelativeTo(null);

        JLabel lNick = new JLabel("Podaj nazwę użytkownika: ");
        lNick.setBounds(80, 30, 200, 15);
        playerNick.add(lNick);

        JTextField tNick = new JTextField("Wprowadź nazwę...");
        tNick.setBounds(80, 50, 200, 20);
        playerNick.add(tNick);

        JButton okButton = new JButton("OK");
        okButton.setBounds(80, 80, 80, 30);
        okButton.addActionListener(event -> {
            if (tNick.getText().equals("") || tNick.getText().equals("Wprowadź nazwę...")) {
                JOptionPane.showMessageDialog(null, "Niepoprawna nazwa!");
            } else {
                this.nick = tNick.getText();
                this.requestFocus();
                playerNick.dispose();
                this.dispose();
                try {
                    gameMenu(currentLevel);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        playerNick.add(okButton);

        JButton bCancel = new JButton("Anuluj");
        bCancel.setBounds(200, 80, 80, 30);
        bCancel.addActionListener(event -> playerNick.dispose());
        playerNick.add(bCancel);
    }


    public void newGame() throws IOException {

        JFrame finish = new JFrame("Koniec gry!");
        finish.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        finish.setLayout(null);
        finish.setVisible(true);
        finish.setSize(width, height);
        finish.setMinimumSize(new Dimension(400, 300));
        finish.setLocationRelativeTo(null);


        if(Client.online) Records.serversSaveScore(nick, sumScore);
        else Records.scored(sumScore, nick);


        JLabel sco = new JLabel("Twój wynik to: " + sumScore );
        sco.setBounds(100, 85, 200, 30);
        finish.add(sco);

        JButton quitButton = new JButton("Zamknij grę!");
        quitButton.addActionListener(event -> System.exit(0));
        quitButton.setBounds(100, 185, 200, 30);
        finish.add(quitButton);

        JButton ranking = new JButton("Pokaż listę rekordów");
        ranking.addActionListener(event -> {
            try {
                recordsMenu();
                finish.dispose();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ranking.setBounds(50, 250, 300, 30);
        finish.add(ranking);
    }



    /**Metoda wyświetla aktualny poziom gry oraz dodatkowe informacje*/
    private void gameMenu( int indexLevel ) throws IOException {
        pacmanFrame = new JFrame("Gra");
        pacmanFrame.setSize(width, height);
        pacmanFrame.setVisible(true);
        pacmanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pacmanFrame.setBackground(new Color(64, 75, 75));
        pacmanFrame.setLocationRelativeTo(null);


        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        MatteBorder matteBorder = new MatteBorder(0, 3, 0, 0, Color.lightGray);
        sideMenu.setBackground(Color.darkGray);
        sideMenu.setBorder(matteBorder);
        pacmanFrame.add(sideMenu, BorderLayout.LINE_END);
        this.timer = new Timer();

        Board board = new Board(indexLevel);
        pacmanFrame.add(board);


        new Thread(() -> {
            while(!board.endGame && !board.win) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            try {
                resetTimer();

                if(board.endGame) {
                    sumScore = board.score;
                    passedLevelMenu(false);
                    pacmanFrame.dispose();
                }
                if(board.win) {
                    passedLevelMenu(true);
                    sumScore += board.score;
                    pacmanFrame.dispose();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


    }


    /**Metoda wyświetla okno z wygranej lub przegranej grze*/
    private void passedLevelMenu(boolean hasWon) {
        if(hasWon) {
            JFrame successFrame = new JFrame("Poziom ukonczony pomyslnie");
            JLabel successLabel = new JLabel("Poziom ukonczony pomyslnie");
            JButton okButton = new JButton("Ok");
            successFrame.add(successLabel);
            successFrame.add(okButton);
            successFrame.setLayout(new GridBagLayout());
            successFrame.setSize(350,180);
            successFrame.setVisible(true);
            successFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            okButton.addActionListener(event -> {
                successFrame.dispose();
                try{
                    loadNextLevel();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            });
        }
        else {
            JFrame defeatFrame = new JFrame("Poziom nieukonczony");
            JLabel defeatLabel = new JLabel("Poziom nieukonczony");
            JButton okButton = new JButton("Ok");
            defeatFrame.add(defeatLabel);
            defeatFrame.add(okButton);
            defeatFrame.setLayout(new GridBagLayout());
            defeatFrame.setSize(350, 180);
            defeatFrame.setVisible(true);
            defeatFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);



            okButton.addActionListener(event -> {
                try {
                    newGame();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                defeatFrame.dispose();
            });
        }
    }

    /**Metoda załadowywująca nowy poiziom w grze*/
    public void loadNextLevel() throws IOException {
        resetTimer();
        if(currentLevel < amountOfLevels){
            currentLevel++;
            removeAll();
            gameMenu(currentLevel);
        }
        else {newGame();}
    }

    private void resetTimer() {
        timer.cancel();
        timer.purge();
    }




    /**tworzy okno z lista najlepszych graczy*/
    private void recordsMenu() throws IOException{
        JFrame recordsFrame = new JFrame("Lista rekordów");
        recordsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        recordsFrame.setLayout(null);
        recordsFrame.setVisible(true);
        recordsFrame.setSize(400, 300);
        recordsFrame.setResizable(false);
        recordsFrame.setLocationRelativeTo(null);

        if(Client.online) serversLoadRanking();
        else loadRecords();

        JLabel[] label = new JLabel[Records.record.size()];
        for(int i = 0; i< Records.record.size(); i++) {
            label[i] = new JLabel((i+1) + ". " + Records.record.get(i) + " pkt");
            label[i].setBounds(50,50 + 20*i,300,30);
            recordsFrame.add(label[i]);
        }



        JButton exitButton = new JButton("Zamknij");
        exitButton.setBounds(130,200,170,30);
        exitButton.addActionListener(event -> recordsFrame.dispose());
        recordsFrame.add(exitButton);
    }

     /**metoda wczytująca główne okno gry, używana w klasie głównej*/
    void launch() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(1);
            }
        });


        mainMenu();
        EventQueue.invokeLater(() -> setVisible(true));
    }




}
