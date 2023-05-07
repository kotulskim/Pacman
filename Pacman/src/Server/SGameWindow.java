package Server;

import javax.swing.*;

/**
 * Klasa odpowiadajaca za okno pomocnicze serwera, informuje o polaczeniu z serwerem i pozwala rozlaczyc sie z nim
 */
public class SGameWindow extends JFrame {
    public SGameWindow() {
        setSize(350,200);
        setTitle("Serwer");
        setLayout(null);
        JLabel connectionLabel = new JLabel("Udało ci się połączyć z serwerem!");
        connectionLabel.setBounds(70,50,200,30);

        JButton disconnectButton = new JButton("Rozłącz");
        disconnectButton.setBounds(120,120,100,30);
        disconnectButton.addActionListener(event -> {
            System.exit(1);
        });

        add(connectionLabel);
        add(disconnectButton);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
