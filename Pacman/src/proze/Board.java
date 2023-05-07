package proze;


import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;


import static proze.Config.*;


/**Klasa odpowiedzialna za rysowanie*/
public class Board extends JPanel implements ActionListener {

    /**Rodzaj i wielkosc czcionki*/
    public  final Font smallFont = new Font("Arial", Font.BOLD, 14);
    /**Zmienna przechowujaca informacje o stanie gry*/
    public boolean inGame = false;
    /**Zmienna przechowujaca inroamcje o tym czy gracz zyje czy nie*/
    public boolean dying = false;
    /**Zmienna przechowujca informacje o wyniku*/
    int score;
    /**Przechowwuje informacje o przejściu pociomu*/
    boolean win = false;
    /**Przechowywuje aktualne ruchy duszkóow*/
    public int[] dx, dy;
    /**Zmiennie przechowujace informacje o aktualnym polozeniu duszkow, oraz o kierunku w jakim sie one poruszaja*/
    public int[] ghost_x, ghost_y, ghost_dx, ghost_dy;
    /**Zmienne przechowujace informacje o aktualnym polozeniu Pacmana, oraz o kierunku w jakim on sie porusza*/
    public int pacman_x, pacman_y, pacman_dx, pacman_dy;
    /**Przechowywuje aktualne ruchy Pacmana*/
    public int req_x, req_y;
    /**Zmienna robocza przechowywująca informacje o wygladzie labiryntu*/
    public int[] screenData;
    /**Licznik odpowiedzialny za animacje*/
    public Timer timer;
    /**Zmienna przechowujaca informacje o zatrzymaniu gry*/
    public boolean isPaused = false;
    /**Zmienna przechowujaca informacje o aktualnej liczbie zyc*/
    public  int lives;
    /**Przechowwuje informacje o przegraniu gry*/
    boolean endGame = false;
    /**Przechowywuje informacje gdzie aktualnie w tablicy screenData znajduje się pacman*/
    public int pos;
    /**Przechowywuje informacje gdzie aktualnie w tablicy screenData znajdują się duszki*/
    public int pos_gh;




    /**Konstruktor klasy Board. Wczytuje poziom gry*/
    Board(int level) throws IOException {
        if(Client.online == false) Config.loadMap(level);
        else Config.serversLoadMap(level);
        Config.loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();

    }

    /**Metoda odpowiedzialna za przypisanie zmiennych*/

    private void initVariables() {
        screenData = new int[nBlocks * nBlocks];
        ghost_x = new int[amountOfGhost];
        ghost_dx = new int[amountOfGhost];
        ghost_y = new int[amountOfGhost];
        ghost_dy = new int[amountOfGhost];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(40, this);

        timer.stop();

        timer.start();
    }


    /**Metoda odpowiedzialna za rozpoczęcie gry*/
    private void playGame(Graphics2D g2d)  {
        if (dying) {
            death(g2d);
        } else {
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }

    }
    /**Metoda odpowiedzialna za pojawienie sie przed rozpoczeciem gry komunikatu informujacego w jaki sposob rozpoczac gre*/
    public void showIntroScreen(Graphics2D g2d){
        lives = lifes;
        score=0;
        String start = "Najcisnij spacje aby rozpocząć grę";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (sizeOfScreen)/4, 150);
    }
    /**Metoda rysujaca aktualny wynik, czyli zdobyta liczbe punktow oraz aktualna ilosc zyc*/
    public void drawScore(Graphics2D g){
        g.setFont(smallFont);
        g.setColor(new Color(5, 151,77));
        String sc = "Wynik: " + score;
        g.drawString(sc, sizeOfScreen / 3 + 200, sizeOfScreen + 16);
        String li = "Życia: " + lives;
        g.drawString(li, sizeOfScreen /3 -96 , sizeOfScreen +16);
        String lev = "Level: " + GameWindow.currentLevel;
        g.drawString(lev, sizeOfScreen /3 + 50 , sizeOfScreen +16);

    }

    public void checkMaze() {
        int i = 0;
        boolean finished = true;

        while (i < nBlocks * nBlocks && finished) {
            if ((screenData[i])  != 0) {
                finished = false;
            }
            i++;
        }


        if (finished) {
            score += 50;
            initLevel();
        }
    }

    /**Metoda odpowiedzialna za odjecie zycia, i w zaleznosci od ilosci pozostalych zyc kontynuowania gry lub jej zakonczenie*/
    private void death(Graphics2D g2d) {
        lives--;

        if (lives > 0) {

            continueLevel();
        } else if (lives == 0){
             endGame = true;


        }

    }

    /**Metoda odpowiedzialna za poruszanie sie duszkow po planszy*/
    public void moveGhosts(Graphics2D g2d) {

        int count;
        if(!isPaused) {

            for (int i = 0; i < amountOfGhost; i++) {
                if (ghost_x[i] % blockSize == 0 && ghost_y[i] % blockSize == 0) {
                    pos_gh = ghost_x[i] / blockSize + nBlocks * (int) (ghost_y[i] / blockSize);
                    count = 0;
                    if ((screenData[pos_gh] & 1) == 0 && ghost_dx[i] != 1) {
                        dx[count] = -1;
                        dy[count] = 0;
                        count++;
                    }
                    if ((screenData[pos_gh] & 2) == 0 && ghost_dy[i] != 1) {
                        dx[count] = 0;
                        dy[count] = -1;
                        count++;
                    }
                    if ((screenData[pos_gh] & 4) == 0 && ghost_dx[i] != -1) {
                        dx[count] = 1;
                        dy[count] = 0;
                        count++;
                    }
                    if ((screenData[pos_gh] & 8) == 0 && ghost_dy[i] != -1) {
                        dx[count] = 0;
                        dy[count] = 1;
                        count++;
                    }

                    if (count == 0) {
                        if ((screenData[pos_gh] & 15) == 15) {
                            ghost_dy[i] = 0;
                            ghost_dx[i] = 0;
                        } else {
                            ghost_dy[i] = -ghost_dy[i];
                            ghost_dx[i] = -ghost_dx[i];
                        }
                    } else {
                        count = (int) (Math.random() * count);
                        if (count > 3) {
                            count = 3;
                        }
                        ghost_dx[i] = dx[count];
                        ghost_dy[i] = dy[count];
                    }
                }
                ghost_x[i] = ghost_x[i] + (ghost_dx[i] * speed);
                ghost_y[i] = ghost_y[i] + (ghost_dy[i] * speed);

                drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

                if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                        && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                        && inGame) {
                    dying = true;
                }
            }
        }

    }
    /**Metoda odpowiedzialna za rysowanie duszkow na planszy*/
    private void drawGhost(Graphics2D g2d, int x, int y) {
        g2d.drawImage(ghost, x, y, this);
    }
    /**Metoda odpowiedzialna za poruszanie sie Pacmana po planszy*/
    public void movePacman() {
        int ch;

        if (!isPaused) {

            if (pacman_x % blockSize == 0 && pacman_y % blockSize == 0) {
                pos = pacman_x / blockSize + nBlocks * (int) (pacman_y / blockSize);
                ch = screenData[pos];

                if ((ch & 16) != 0) {
                    screenData[pos] = (int) (ch & 15);
                    score += bonusBall;
                }

                if ((ch & 32) != 0) {
                    screenData[pos] = (int) (ch & 15);
                    score += bonusBigBall;
                }
                if (score == maxScore) {
                    win = true ;
                    lifesBonus();




                }
                if (req_x != 0 || req_y != 0) {
                    if (!((req_x == -1 && req_y == 0 && (ch & 1) != 0)
                            || (req_x == 1 && req_y == 0 && (ch & 4) != 0)
                            || (req_x == 0 && req_y == -1 && (ch & 2) != 0)
                            || (req_x == 0 && req_y == 1 && (ch & 8) != 0))) {
                        pacman_dx = req_x;
                        pacman_dy = req_y;
                    }
                }
                if ((pacman_dx == -1 && pacman_dy == 0 && (ch & 1) != 0)
                        || (pacman_dx == 1 && pacman_dy == 0 && (ch & 4) != 0)
                        || (pacman_dx == 0 && pacman_dy == -1 && (ch & 2) != 0)
                        || (pacman_dx == 0 && pacman_dy == 1 && (ch & 8) != 0)) {
                    pacman_dx = 0;
                    pacman_dy = 0;
                }
            }
            pacman_x = pacman_x + speed * pacman_dx;
            pacman_y = pacman_y + speed * pacman_dy;
        } else{


        }


    }

    /**Metoda odpowiedzialna za dodanie do wyniku bonusowych puntkow za pozostawione zycia*/
    private void lifesBonus(){
        for (int i = lives; i > 0; i-- ){
            score += bonusLife;



        }

    }
    /**Metoda odpowiedzialna za rysowanie Pacmana na planszy*/
    private void drawPacman(Graphics2D g2d) {
        if (req_x == -1) {
            g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_x == 1) {
            g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_y == -1) {
            g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
            g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    /**Metoda odpowiedzialna za rysowanie planszy*/
    private void drawMaze(Graphics2D g2d) {


        short i = 0;
        int x, y;
        for (y = 0; y < sizeOfScreen; y += blockSize) {
            for (x = 0; x < sizeOfScreen; x += blockSize) {
                g2d.setColor(new Color(0, 72, 252));
                g2d.setStroke(new BasicStroke(5));

                if ((loadLevel[i] == 0)) {
                    g2d.fillRect(x, y, blockSize, blockSize);

                }
                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + blockSize - 1);
                }
                if ((screenData[i] & 2) !=0 ) {
                    g2d.drawLine(x, y, x + blockSize - 1, y);
                }
                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + blockSize - 1, y, x + blockSize - 1, y + blockSize - 1);
                }
                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + blockSize -1 , x + blockSize - 1, y + blockSize - 1);
                }
                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 15, y + 15, sizeOfBall, sizeOfBall);
                }
                if ((screenData[i] & 32) !=0) {
                    g2d.setColor(Color.green);
                    g2d.fillOval(x + 10, y + 10, sizeOfBigBall, sizeOfBigBall);
                }

                i++;
            }
        }
    }

    /**Metoda odpowiedzialna za przypisanie wartosci wyniku i zyc na poczatku gry*/
    public void initGame() {
        score = 0;
        lives = lifes;
        initLevel();

    }

    /**Przypisanie zmiennej z pliku konfiguracyjnego do zmiennej roboczej na ktorej pracujemy*/
    public void initLevel() {
        
        int i;
        for (i = 0; i < nBlocks * nBlocks; i++) {
            screenData[i] = loadLevel[i];

        }
        continueLevel();
    }



    /**uruchmienie zmiennych na początku levelu*/
    public void continueLevel() {
        int dx = 1;

        for (int i = 0; i < amountOfGhost; i++) {
            ghost_y[i] = startGhosts_x * blockSize;
            ghost_x[i] = startGhosts_y * blockSize;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
        }
        pacman_x = startPacman_x * blockSize;
        pacman_y = startPacman_y * blockSize;
        pacman_dx = 0;
        pacman_dy = 0;
        req_x = 0;
        req_y = 0;
        dying = false;
    }

    /**Ustawienie zmiennych po pauzie*/
    public void afterPaused() {
        int dx = 1;

        for (int i = 0; i < amountOfGhost; i++) {
            ghost_y[i] = pos_gh % nBlocks * blockSize;
            ghost_x[i] = (int)(pos_gh/nBlocks) * blockSize;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
        }
        pacman_x = pos % nBlocks * blockSize;
        pacman_y = (int)(pos/nBlocks) * blockSize;

        pacman_dx = 0;
        pacman_dy = 0;
        req_x = 0;
        req_y = 0;
        dying = false;
    }




    /**Metoda odswiezajaca rysowanie labiryntu i Pacmana*/
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.darkGray);
        g2d.fillRect(0, 0, width, height);

        drawMaze(g2d);
        drawScore(g2d);
        if(inGame){
            playGame(g2d);

        }
        else {
            showIntroScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


/**Klasa obslugujaca zdarzenia z klawiatury*/
    class TAdapter extends KeyAdapter{

        @Override
        public void keyPressed (KeyEvent e ){
            int key = e.getKeyCode();
            if (inGame){
                if(key == KeyEvent.VK_LEFT){
                    req_x= -1;
                    req_y = 0;
                }
                else if(key == KeyEvent.VK_RIGHT){
                    req_x = 1;
                    req_y = 0;
                }
                else if(key == KeyEvent.VK_UP){
                    req_x = 0;
                    req_y = -1;
                }
                else if(key == KeyEvent.VK_DOWN){
                    req_x = 0;
                    req_y = 1;
                }
                if (key == KeyEvent.VK_P ){
                    isPaused = true;
                }
                if(isPaused && key == KeyEvent.VK_S){
                    isPaused = false;
                    afterPaused();
                }
            }
            else {
                if(key == KeyEvent.VK_SPACE){
                    inGame = true;
                    continueLevel();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {


            repaint();


    }

}
