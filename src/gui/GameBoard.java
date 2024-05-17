package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import misc.Phases;
import misc.ShipType;

/**
 * This element represents the concept of the Playing field and houses the Upper
 * and Lower Board with all of its field. It manages and changes these fields
 * according to the Game state and events.
 */
public class GameBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel upperBoard;
	private JPanel lowerBoard;
	private LowerField[][] playField;
	private UpperField[][] enemyField;
	// Das momentan ausgewählte Feld im unteren Gitter. Hauptsächlich um visuelle Fehler
	// beim Drehen des Schiffes zu vermeiden (damit das Schiff z.B. nicht über den Rand geht). 
    // Wird gesetzt, wenn ein der Mauszeiger das Feld betreten hat.
	private static int currentX;
	private static int currentY;

	/**
	 * Initializes the board with default values to not cause Exceptions.
	 */
	public GameBoard() {
		developBoard();
		currentX = -1;
		currentY = -1;
	}

	/**
	 * generates the initial board used at the start of the game.
	 */
	private void developBoard() {
		AffineTransform rotation = new AffineTransform();
		rotation.rotate(Math.toRadians(90));
		this.setBackground(new Color(146, 131, 148, 76));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new GridLayout(2, 1, 0, 10));
		generateUpperLower();
	}

	/**
	 * generates the upper and lower board with 10x10 fields of each type.
	 */
	private void generateUpperLower() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		upperBoard = new JPanel();
		upperBoard.setLayout(new GridBagLayout());
		upperBoard.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		upperBoard.setBackground(Color.lightGray);
		lowerBoard = new JPanel();
		lowerBoard.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		lowerBoard.setLayout(new GridLayout(10, 10, 2, 2));
		lowerBoard.setBackground(Color.lightGray);
		lowerBoard.setLayout(new GridBagLayout());
		this.add(upperBoard);
		this.add(lowerBoard);
		enemyField = new UpperField[10][10];
		playField = new LowerField[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				gridBagConstraints.gridx = i;
				gridBagConstraints.gridy = j;
				enemyField[i][j] = new UpperField(i, j);
				playField[i][j] = new LowerField(i, j);
				upperBoard.add(enemyField[i][j], gridBagConstraints);
				lowerBoard.add(playField[i][j], gridBagConstraints);
			}
		}
	}

	/**
	 * if this method is called, it will check based on the coordinates if the
	 * currently selected ship can fit in the selected position.
	 *
	 * @param x the x coordinate at the tip of the ship
	 * @param y the y coordinate at the tip of the ship
	 * @return if the position can be used to place a ship
	 */
	public boolean validatePlacement(int x, int y) {
		int[] boatSize = GameScene.getSelectedShip().getRelativeSize();
		int h = (Window.isReverse()) ? boatSize[0] : boatSize[1];
		int w = (Window.isReverse()) ? boatSize[1] : boatSize[0];
		try {
			for (int i = x; i < (x + w); i++) {
				for (int j = y; j < (y + h); j++) {
					if (playField[i][j].getShip() != ShipType.NONE) {
						return false;
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	/**
	 * if this method is called, all LowerFields affected will get their status
	 * changed to the selected ship.
	 *
	 * @param x the x coordinate of the tip of the ship
	 * @param y the y coordinate of the tip of the ship
	 */
	public void commitPlacement(int x, int y) {
		int[] boatSize = GameScene.getSelectedShip().getRelativeSize();
		int h = (Window.isReverse()) ? boatSize[0] : boatSize[1];
		int w = (Window.isReverse()) ? boatSize[1] : boatSize[0];
		try {
			// only iterates over the selected ship.
			for (int i = x; i < (x + w); i++) {
				for (int j = y; j < (y + h); j++) {
					playField[i][j].setShip(GameScene.getSelectedShip());

				}
			}

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < playField.length; i++) {
			for (int j = 0; j < playField[0].length; j++) {
				playField[i][j].paintShip(g);
				if (GameScene.getPhase() != Phases.PLACEMENT) {
					playField[i][j].paintSymbol(g);
					enemyField[i][j].paintSymbol(g);
				}
			}
		}

	}

	/**
	 * creates the players map that will be sent to the server. it transforms the
	 * ships into the ordinal number of the corresponding enum.
	 *
	 * @return the mapped instance of the players placed ships
	 */
	public int[][] createMap() {
		int[][] map = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				map[i][j] = playField[i][j].getShip().ordinal();
			}

		}
		return map;

	}

	/**
	 * similar to the removePlacement method, this removes the ships anchor, meaning
	 * the main field the ship is positioned at.
	 *
	 * @param shipType the ship that will be unanchored
	 */
	public void removeAnchoredShip(ShipType shipType) {
		for (LowerField[] lowerFields : playField) {
			for (int j = 0; j < playField[0].length; j++) {
				if (lowerFields[j].getAnchoredShip() == shipType) {
					lowerFields[j].setAnchoredShip(ShipType.NONE);
					break;
				}
			}
		}
	}

	/**
	 * if the player decided to move the ship, this method ensures that it is
	 * removed from all fields
	 *
	 * @param ship the ship that will be removed
	 */
	public void removePlacement(ShipType ship) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (playField[i][j].getShip() == ship) {
					playField[i][j].setShip(ShipType.NONE);
				}
			}
		}
		GameScene.removeShip(ship);
	}

	public static void setCurrentX(int currentX) {
		GameBoard.currentX = currentX;
	}

	public static void setCurrentY(int currentY) {
		GameBoard.currentY = currentY;
	}

	public LowerField[][] getPlayerField() {
		return playField;
	}

	public UpperField[][] getEnemyField() {
		return enemyField;
	}

	/**
	 * this is to ensure there are no clipping issues while rotating a ship in the
	 * placement mode
	 */
	public void reevaluateField() {
		if (GameScene.getSelectedShip() != ShipType.NONE && currentX != -1 && currentY != -1) {
			LowerField field = playField[currentX][currentY];
			if (!validatePlacement(currentX, currentY)) {
				field.setAnchoredShip(ShipType.NONE);
				repaint();
			}

		}
	}
}
