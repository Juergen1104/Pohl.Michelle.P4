package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

import messages.Message;
import messages.QuitMessage;

/**
 * this class handles a Single client that is connected by evaluating their
 * Messages and sending Responses.
 */
class ClientHandler extends Thread {

	private int[][] opponentMap;   
	private Socket s;
	private boolean active;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Server server;
	private boolean isOccupied;
	private boolean placed;
	private ClientHandler opponent;
	private int playerId;
	private String playerName;

	/**
	 * Generates the ClientHandler based on the Server it is in and the Players
	 * socket
	 *
	 * @param s        the Socket the player is connected to
	 * @param server   the Server?
	 * @param playerId the id that the player is identified with
	 */
	public ClientHandler(Socket s, Server server, int playerId) {
		this.s = s;
		this.server = server;
		active = true;
		this.playerId = playerId;
		placed = false;
		isOccupied = true;

		try {
			output = new ObjectOutputStream(s.getOutputStream());
			input = new ObjectInputStream(s.getInputStream());
			System.out.println("connected. \nIP4-Address: " + s.getInetAddress());

		} catch (IOException e) {
			System.out.println("Socket closed");
		}
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	/**
	 * closes the connection between the Server and the client and, if the Client
	 * was in a match, will send a Message to the opponent
	 */
	protected void closeConnection() {
		try {
			output.close();
			input.close();
			synchronized (server.getAllClients()) {
				server.getAllClients().remove(this);
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will be called when the player closed the window and will send a
	 * QuitMessage if he was in a Match.
	 */
	protected void playerForfeit() {
		if (!Objects.isNull(opponent) && server.acceptClients) {

			opponent.sendMessage(new QuitMessage());
			opponent.setOpponent(null);
		}
		closeConnection();
	}

	public ClientHandler getOpponent() {
		return opponent;
	}

	public void setOpponent(ClientHandler opponent) {
		this.opponent = opponent;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}

	public int getPlayerId() {
		return playerId;
	}

	private void task1() throws IOException, ClassNotFoundException {
		// Aufgabe 1): 10.5P
		/**
		 * HIER LOESUNG IMPLEMENTIEREN
		 */
	}

	/**
	 * sends a message to the client
	 *
	 * @param message the Message that needs to be sent.
	 */
	// Aufgabe 1) 1P
	public void sendMessage(Message message) {
		/**
		 * HIER LOESUNG IMPLEMENTIEREN
		 */

	}

	/**
	 * this method handles the messages received by the client.
	 */
	public void run() {

		while (active) {
			try {
				task1();

			} catch (NullPointerException e) {
				e.printStackTrace();
				closeConnection();
			} catch (IOException e) {
				playerForfeit();
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	/**
	 * this method calculates if the player has won based on the opponents map.
	 *
	 * @return if the player has won or not
	 */
	private boolean calculateVictory() {
		for (int[] ints : opponentMap) {
			for (int j = 0; j < opponentMap[0].length; j++) {
				if (ints[j] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean hasPlaced() {
		return placed;
	}

	public void setOpponentMap(int[][] opponentMap) {
		this.opponentMap = opponentMap;
	}

}
