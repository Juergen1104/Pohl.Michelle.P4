package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import misc.ShipType;

/**
 * This class is the parent class for the Upper and Lower field and establishes
 * a foundation that both require.
 */
public abstract class Field extends JPanel {

	private static final long serialVersionUID = 1L;
	Condition condition;
	int x;
	int y;
	ShipType ship;

	public ShipType getShip() {
		return ship;
	}

	/**
	 * Generates the Field. since the Fields are located on the Game Board, their
	 * location needs to be saved to ease access.
	 *
	 * @param x the x coordinate in the array
	 * @param y the y coordinate in the array
	 */
	public Field(int x, int y) {
		super();
		condition = Condition.NONE;

		this.setPreferredSize(new Dimension(27, 27));
		this.x = x;
		this.y = y;
		ship = ShipType.NONE;
		Color fieldColor = new Color(116, 139, 151);
		this.setBackground(fieldColor);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

	}

	/**
	 * This enum decides the current state of the current Field to ensure flawless
	 * visualization of the current state.
	 */
	enum Condition {
		NONE, HIT, MISS
	}

	/**
	 * saves the Result of an attempted shot by the player or the opponent.
	 *
	 * @param hit if a ship was actually hit
	 */
	public void saveResult(boolean hit) {
		condition = (hit) ? Condition.HIT : Condition.MISS;
		repaint();
	}

	/**
	 * this method visualizes the result of a possible hit or miss on the field. It
	 * is called by the GameBoard to remove clipping issues.
	 *
	 * @param g the graphics element that will be used
	 */
	// Aufgabe 4): 4P
	public void paintSymbol(Graphics g) {
		int[] xy = getCoords();
		int x = xy[0];
		int y = xy[1];
		Graphics2D g2 = (Graphics2D) g;
		Stroke defaultStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		switch (condition) {
		case HIT:
			/**
			 * HIER LOESUNG IMPLEMENTIEREN
			 */
			break;
		case MISS:
			/**
			 * HIER LOESUNG IMPLEMENTIEREN
			 */
		default:

		}
		g2.setStroke(defaultStroke);
	}

	/**
	 * this method returns the coordinates of the field relative to the actual
	 * GameBoard.
	 *
	 * @return returns the relative coordinates of the field.
	 */
	public int[] getCoords() {
		// liefert die Koordinaten des ausgewählten Feldes im Verhältnis zum Fenster. 
		// Wichtig für das Zeichnen der Boote.
		int x = (int) (getLocationOnScreen().getX()
				- GameScene.getGameScene().getGameBoard().getLocationOnScreen().getX());
		int y = (int) (getLocationOnScreen().getY()
				- GameScene.getGameScene().getGameBoard().getLocationOnScreen().getY());
		return new int[] { x, y };
	}

	/**
	 * updates the skew if it has been changed.
	 */
	void updateSkew() {
		if (this.getWidth() != Window.getxSkew() || this.getHeight() != Window.getySkew()) {
			Window.setxSkew(this.getWidth());
			Window.setySkew(this.getHeight());
		}
	}

	public void setShip(ShipType ship) {
		this.ship = ship;
	}

}
