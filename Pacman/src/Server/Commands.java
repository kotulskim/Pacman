package Server;

import java.io.IOException;

/**
 * Klasa, ktora odpowiada za odpowiedzi serwera na wyslane zadania przez klienta
 */
public class Commands {
    /**
     * Klient wysyla zadani, a serwer wysyla odpowiedz
     * @param command zadanie klienta
     * @return odpowiedz serwera
     */
    public static String serverAnswer(String command) throws IOException {
        String serverMessage = null;
        String[] commands = command.split("/");
        switch(commands[0]) {
            case "getConfig":
                serverMessage = SConfig.loadConfig();
                System.out.println("przyszło");
                break;
            case "getRecords":
                serverMessage = SRecords.getRecords();
                break;
            case "getMap":
                serverMessage = SConfig.loadMap(Integer.parseInt(commands[1]));
                break;
            case "save":
                SRecords.scored(commands[2], commands[1]);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + commands[0]);
        }
        return serverMessage;
    }
}
