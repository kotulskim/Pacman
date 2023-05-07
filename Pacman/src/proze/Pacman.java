package proze;

import javax.swing.*;
import java.io.IOException;


/**Klasa główna*/
public class Pacman extends JFrame {

    public Pacman() {

    }

    public static void main(String[] args) throws IOException {
        GameWindow menu = new GameWindow();
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.launch();
        menu.setVisible(true);

    }
}
