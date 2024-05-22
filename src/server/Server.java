package server;

import messages.StartMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * This class represents the Server and allows Clients to connect and play
 * battleships against each other.
 */
public class Server {

    private ServerSocket serverSocket;
    private final static int port = 3221;
    boolean acceptClients;
    private int playerNum;
    private final LinkedList<ClientHandler> allClients;
    private static Server server;

    /**
     * Creates the Server as a Singleton and generates the simple UI that is needed
     * to start and end the server.
     */
    private Server() {
        allClients = new LinkedList<>();
        JFrame frame = new JFrame();
        JPanel ui = new JPanel();
        acceptClients = true;
        frame.setPreferredSize(new Dimension(200, 100));
        frame.setVisible(true);
        frame.setContentPane(ui);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton button = new JButton("Open Connection");
        button.setBackground(Color.GREEN);
        ui.setLayout(new GridLayout(1, 2));
        ui.add(button);
        button.addActionListener(e -> {
            Color color = button.getBackground();
            if (color == Color.GREEN) {
                button.setText("Close Connection");
                new Thread(this::start).start();
                button.setBackground(Color.RED);
            } else {
                button.setText("Open Connection");
                stop();
                button.setBackground(Color.GREEN);
            }
        });
        frame.pack();
    }

    /**
     * this method starts the Server and sets up the waiting loop for new players.
     */
    private void start() {
        acceptClients = true;
        new MatchCreator().start();
        playerNum = 0;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server is open on port: " + port);
            serverSocket = server;
            while (acceptClients) {
                Socket s = serverSocket.accept();
                playerNum++;
                ClientHandler cH = new ClientHandler(s, this, playerNum);
                cH.start();
                synchronized (allClients) {
                    allClients.add(cH);
                }
            }
        } catch (IOException e) {
            if (!acceptClients) {
                System.out.println("Server Closed");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * this method closes the server and disconnects all the clients.
     */
    private void stop() {
        acceptClients = false;
        LinkedList<ClientHandler> tempList = new LinkedList<>(allClients);
        tempList.forEach(clientHandler -> {
            // clientHandler.sendMessage(new QuitMessage());
            clientHandler.closeConnection();
        });
        allClients.clear();
        try {
            if (!Objects.isNull(serverSocket)) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this Method initialises the Singleton of the Server.
     */
    public static void createServer() {
        if (Objects.isNull(server)) {
            server = new Server();
        }
    }

    /**
     * Main method to start the server application
     *
     * @param args the possible start arguments.
     */
    public static void main(String[] args) {
        createServer();
    }

    public LinkedList<ClientHandler> getAllClients() {
        return allClients;
    }

    /**
     * this class actively looks for players waiting for a game and puts them into a
     * match.
     */
    class MatchCreator extends Thread {

        /**
         * initializes the search loop.
         */
        @Override
        public void run() {
            while (acceptClients) {
                try {
                    findWaitingClients();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * this method finds all Clients that are waiting for a match and pairs two up
         * to create a new match.
         */
        // Aufgabe 3): 5P
        private void findWaitingClients() {

            /**
             * HIER LOESUNG IMPLEMENTIEREN
             */

            ArrayList<ClientHandler> waitingClients = new ArrayList<>();
            synchronized (allClients) {
                for (ClientHandler client : allClients) {
                    if (!client.isOccupied()) {
                        waitingClients.add(client);
                        if (waitingClients.size() == 2) {
                            break;
                        }
                    }
                }
            }

            if (waitingClients.size() == 2) {
                ClientHandler client1 = waitingClients.get(0);
                ClientHandler client2 = waitingClients.get(1);

                client1.setOccupied(true);
                client2.setOccupied(true);

                client1.setOpponent(client2);
                client2.setOpponent(client1);

                client1.sendMessage(new StartMessage(client2.getPlayerName()));
                client2.sendMessage(new StartMessage(client1.getPlayerName()));
            }
        }
    }
}
