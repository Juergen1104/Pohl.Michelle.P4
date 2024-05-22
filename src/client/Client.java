package client;

import gui.GameScene;
import gui.LowerField;
import gui.UpperField;
import gui.Window;
import messages.*;
import misc.Phases;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Acts as the Connection to the Server from the Player and coordinates every
 * interaction.
 */
public class Client {

    private static Client client;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    boolean running;
    private String username;
    private String opponentName;

    /**
     * Generates the connection to the server
     *
     * @param host the host id
     * @param port the open port
     */
    private Client(String host, int port) {

        try (Socket s = new Socket(host, port)) {
            socket = s;
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            client = this;
            running = true;
            opponentName = "none";
            Window.startWindow();
            ServerListener serverListener = new ServerListener();
            serverListener.interact();

        } catch (UnknownHostException e) {
            JOptionPane.showInternalMessageDialog(null, "ERROR! NO CONNECTION", "ERROR", JOptionPane.ERROR_MESSAGE);
            enterIP();
            running = false;
        } catch (IOException e) {
            JOptionPane.showInternalMessageDialog(null, "ERROR! CONNECTION TO SERVER TERMINATED!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            running = false;
            enterIP();
        }
    }

    public static void setUsername(String username) {
        Client.getClient().username = username;
    }

    public String getUsername() {
        return username;
    }

    public static Client getClient() {
        return client;
    }

    /***
     * Send a Message element to the server
     *
     * @param message the Message sent
     */
    // Aufgabe b) : 1P
    public static void sendMessage(Message message) {

        /**
         * HIER LOESUNG IMPLEMENTIEREN
         */
        try {
            Client.getClient().output.writeObject(message);
            Client.getClient().output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This Class handles all the messages received by the Server
     */
    class ServerListener {
        // Aufgabe 2): 11P
        private void task2() throws IOException, ClassNotFoundException {
            /**
             * HIER LOESUNG IMPLEMENTIEREN
             */
            Message msg = (Message) input.readObject();
            switch (msg.getType()) {
                case START:
                    StartMessage startMsg = (StartMessage) msg;
                    opponentName = startMsg.getOpponentName();
                    Window.switchToGame();
                    break;
                case QUIT:
                    JOptionPane.showMessageDialog(null, "Dein Gegner hat das Spiel verlassen.", "Gegner gibt auf", JOptionPane.INFORMATION_MESSAGE);
                    Window.switchToMenu(false);
                    break;
                case ATTACK:
                    GameScene.setPhase(Phases.FIGHTING);
                    GameScene.getGameScene().setNotification("Angriff!",Color.GREEN);
                    break;
                case RESULT:
                    ResultMessage resultMsg = (ResultMessage) msg;
                    int x = resultMsg.getX();
                    int y = resultMsg.getY();
                    boolean isHit = resultMsg.isHit();
                    boolean fromOpponent = resultMsg.isByEnemy();

                    LowerField[][] playerField = GameScene.getGameScene().getGameBoard().getPlayerField();
                    UpperField[][] enemyField = GameScene.getGameScene().getGameBoard().getEnemyField();
                    if (fromOpponent) {
                        playerField[x][y].saveResult(isHit);
                    } else {
                        enemyField[x][y].saveResult(isHit);
                    }
                    if (isHit) {
                        GameScene.getGameScene().shakeScreen();
                    }
                    Window.getWindow().repaint();
                    break;
                case WIN:
                    WinMessage winMsg = (WinMessage) msg;
                    boolean isWinner = winMsg.isOpponent();

                    int response = JOptionPane.showConfirmDialog(null, isWinner ? "Gewonnen! Nochmal?" : "Verloren! Noch mal?",
                            isWinner ? "You Win" : "You Lost", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        sendMessage(new ReadyMessage(client.getUsername()));
                        Window.switchToMenu(true);
                    } else {
                        sendMessage(new CancelMessage());
                        Window.switchToMenu(false);
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * this method decides the actions taken based on the Message type
         */
        public void interact() {
            running = true;
            while (running) {
                try {
                    task2();
                } catch (IOException e) {
                    JOptionPane.showInternalMessageDialog(null, "ERROR! CONNECTION TO SERVER TERMINATED!", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    Window.close();
                    enterIP();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    running = false;
                }
            }
        }
    }

        public String getOpponentName() {
            return opponentName;
        }

        public static void enterIP() {
            String hostName = JOptionPane.showInputDialog(null, "Write the IP Adress", "IP Adress",
                    JOptionPane.INFORMATION_MESSAGE);
            if (!Objects.isNull(hostName)) {
                int port = 3221;
                client = new Client(hostName, port);
            } else {
                System.exit(0);// automatically closes the connection
            }
        }

        public static void main(String[] args) {
            enterIP();
        }
    }
