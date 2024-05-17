package gui;

import client.Client;
import messages.PlacedMessage;
import misc.Phases;
import misc.RListener;
import misc.ShipType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * this class houses all the elements relevant for the actual game.
 */
public class GameScene extends JPanel {

	private static final long serialVersionUID = 1L;
	private static GameScene gameScene;
	private GameBoard gamePanel;
	private JPanel sidePanel;
	private JPanel p1Panel;
	private JPanel p2Panel;
	private static ShipType selectedShip;
	private JLabel notifier;
	// Panelkoordinaten des zuletzt ausgewählten Felder im unteren Gitter. 
	// Wichtig für den Hovereffekt bei der Schiffsplatzierung.
	static int x1;
	static int y1;
	private JComponent[] shipButtons;
	private JButton switchPhase;
	private Phases phase;

	/**
	 * since the game scene needs the refresh every time a new game is started, this
	 * method only sets the Layout.
	 */
	private GameScene() {
		this.setLayout(new BorderLayout());
	}

	/**
	 * ensures a frictionless start of the game.
	 */
	public void start() {
		this.removeAll();
		setupScene();
	}

	/**
	 * sets up the scene for the game with all of its visual and functional
	 * elements.
	 */
	private void setupScene() {
		selectedShip = ShipType.NONE;
		this.addKeyListener(new RListener());
		phase = Phases.PLACEMENT;
		gamePanel = new GameBoard();
		gamePanel.setPreferredSize(new Dimension(400, 500));
		this.add(gamePanel, BorderLayout.CENTER);
		sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
		setupPlayerPanels();
		sidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(p2Panel, BorderLayout.NORTH);
		this.add(p1Panel, BorderLayout.SOUTH);
		this.add(sidePanel, BorderLayout.EAST);

		setupButtons();
		revalidate();
	}

	/**
	 * sets up the visual indications of the player and the opponent.
	 */
	private void setupPlayerPanels() {
		p1Panel = new JPanel();
		p1Panel.setLayout(new GridLayout(1, 3));
		p1Panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		p2Panel = new JPanel();
		p2Panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		p2Panel.setLayout(new GridLayout(1, 3));
		// Player names
		JPanel temp1 = new JPanel();
		JPanel temp2 = new JPanel();
		JLabel username = new JLabel(Client.getClient().getUsername());
		JLabel oppName = new JLabel(Client.getClient().getOpponentName());
		username.setHorizontalTextPosition(SwingConstants.CENTER);
		oppName.setHorizontalTextPosition(SwingConstants.CENTER);
		temp1.add(username);
		temp2.add(oppName);
		p1Panel.add(temp1);
		p2Panel.add(temp2);
		// Smiley
		p1Panel.add(createSmiley(Client.getClient().getUsername()));
		p2Panel.add(createSmiley(Client.getClient().getOpponentName()));
		// Ready Button
		notifier = new JLabel("waiting for opponent");
		switchPhase = new JButton("switch phase");
		switchPhase.addActionListener(e -> placementLocked());
		switchPhase.setEnabled(false);
		JPanel temp = new JPanel();
		temp.add(switchPhase);
		temp2 = new JPanel();
		p1Panel.add(temp);
		p2Panel.add(temp2);
	}

	/**
	 * will setup all the shipButtons depending on the different ShipTypes.
	 */
	private void setupButtons() {
		shipButtons = new JComponent[ShipType.values().length];
		shipButtons[0] = new JLabel("Ships:");
		sidePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		sidePanel.add(shipButtons[0]);
		for (int i = 1; i < shipButtons.length; i++) {
			ShipType type = ShipType.values()[i];
			ShipButton button = new ShipButton(type);
			shipButtons[i] = button;
			button.addKeyListener(new RListener());
			sidePanel.add(button);
		}
	}

	/**
	 * if called will finish the placement phase and notify the server.
	 */
	private void placementLocked() {
		int[][] map = gamePanel.createMap();
		Client.sendMessage(new PlacedMessage(map));
		switchPhase.setEnabled(false);
		phase = Phases.NONE;
		Container temp = switchPhase.getParent();
		temp.remove(switchPhase);
		temp.add(notifier);
		this.remove(sidePanel);
		repaint();
		revalidate();
	}

	/**
	 * checks if all ships have been placed.
	 */
	public static void checkShipsPlaced() {
		JComponent[] buttons = getGameScene().getShipButtons();
		JButton switchPhase = getGameScene().getSwitchPhase();
		for (int i = 1; i < buttons.length; i++) {
			if (buttons[i].isEnabled()) {
				switchPhase.setEnabled(false);
				return;
			}
		}
		switchPhase.setEnabled(true);
	}

	/**
	 * refreshes the currently selected coordinates in the placement phase.
	 *
	 * @param x x coordinate of the current field
	 * @param y y coordinate of the current field
	 */
	public void refreshCoordinates(int x, int y) {
		x1 = x;
		y1 = y;
		this.revalidate();
		this.repaint();
	}

	/**
	 * this method is responsible for the shaking effect seen when a ship is hit by
	 * a shot.
	 */
	public void shakeScreen() {
		Point currLoc = this.getLocation();
		int shakeY = 10;
		int shakeX = 4;
		Point position1 = new Point(currLoc.x + shakeX, currLoc.y + shakeX);
		Point position2 = new Point(currLoc.x - shakeX, currLoc.y - shakeY);
		try {
			for (int i = 0; i < 2; i++) {
				this.setLocation(position1);
				Thread.sleep(50);
				this.setLocation(position2);
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setLocation(currLoc);
	}

	/**
	 * sets the Notification at the bottom of the screen to the desired text
	 *
	 * @param text  the desired text
	 * @param color the color in which the text should be displayed.
	 */
	public void setNotification(String text, Color color) {
		notifier.setText(text);
		notifier.setForeground(color);
	}

	/**
	 * adds a boat to the field and ensures that not a duplicate can be placed.
	 */
	public static void addBoat() {
		GameScene.getGameScene().augmentShipButton(selectedShip, false);
		GameScene.setSelectedShip(ShipType.NONE);
		GameScene.getGameScene().getGameBoard().repaint();
		checkShipsPlaced();

	}

	/**
	 * enables or disables the shipbutton corresponding to the placed or removed
	 * ship.
	 *
	 * @param shipType the ship that corresponds to the buttom
	 * @param enable   if the button should be enabled or disabled
	 */
	public void augmentShipButton(ShipType shipType, boolean enable) {
		shipButtons[shipType.ordinal()].setEnabled(enable);
	}

	/**
	 * this method removes the Ship if it was picked up by the player.
	 *
	 * @param selectedShip the selected ship
	 */
	public static void removeShip(ShipType selectedShip) {
		GameScene.getGameScene().getGameBoard().removeAnchoredShip(selectedShip);
		GameScene.getGameScene().getGameBoard().repaint();
		GameScene.getGameScene().augmentShipButton(selectedShip, true);
		checkShipsPlaced();
	}

	/**
	 * Since we only need one GameScene, this method initializes the gameScene
	 * itself.
	 *
	 * @return returns the scene created.
	 */
	public static GameScene createGameScene() {
		if (Objects.isNull(gameScene)) {
			gameScene = new GameScene();
		}
		return gameScene;
	}

	public static GameScene getGameScene() {
		return gameScene;
	}

	public static ShipType getSelectedShip() {
		return selectedShip;
	}

	public JComponent[] getShipButtons() {
		return shipButtons;
	}

	public JButton getSwitchPhase() {
		return switchPhase;
	}

	public static Phases getPhase() {
		return GameScene.getGameScene().phase;
	}

	public static void setPhase(Phases phase) {
		GameScene.getGameScene().phase = phase;
	}

	public GameBoard getGameBoard() {
		return gamePanel;
	}

	/**
	 * This method generates the Smiley ,used for the player and opponent,based on
	 * the usernames summed char value.
	 *
	 * @param name the name of the opponent or player
	 * @return the Smiley placed in a JPanel to be used in the GameScene
	 */
	private JPanel createSmiley(String name) {
		int choice = 0;
		for (int i = 0; i < name.length(); i++) {
			choice += Character.getNumericValue(name.charAt(i));
		}
		choice %= 6;
		String path = "resources/smiley/" + choice + ".png";
		try {
			BufferedImage image = ImageIO.read(new File(path));
			JLabel smiley = new JLabel(new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING)));
			smiley.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			JPanel temp = new JPanel();
			temp.add(smiley);
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
			return new JPanel();
		}
	}

	public static void setSelectedShip(ShipType selectedShip) {
		GameScene.selectedShip = selectedShip;
	}
}
