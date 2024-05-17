package gui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import misc.Phases;
import misc.ShipType;

/**
 * this class represents the Lower fields of the GameBoard and is crucial for
 * the Placement phase
 */
public class LowerField extends Field {

	private static final long serialVersionUID = 1L;
	private ShipType anchoredShip;
	private boolean isReversed;

	/**
	 * generates the Field and establishes the required functionality necessary for
	 * placing and removing ships by clicking.
	 *
	 * @param x the x location on the board
	 * @param y the y location on the board
	 */
	public LowerField(int x, int y) {
		super(x, y);
		isReversed = false;
		anchoredShip = ShipType.NONE;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (GameScene.getPhase() == Phases.PLACEMENT) {

					GameBoard board = GameScene.getGameScene().getGameBoard();
					boolean validPlacement = board.validatePlacement(x, y);
					int[] xy = getCoords();

					GameScene.getGameScene().refreshCoordinates(xy[0], xy[1]);
					if (ship == ShipType.NONE && validPlacement) {
						board.commitPlacement(x, y);
						isReversed = Window.isReverse();
						anchoredShip = GameScene.getSelectedShip();
						GameScene.addBoat();
					} else {
						if (ship != ShipType.NONE && GameScene.getSelectedShip() == ShipType.NONE) {
							isReversed = false;
							GameScene.setSelectedShip(ship);
							board.removePlacement(ship);
							anchoredShip = ShipType.NONE;
							mouseEntered(e);
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

				if ((GameScene.getSelectedShip() != ShipType.NONE && GameScene.getPhase() == Phases.PLACEMENT)
						&& ship == ShipType.NONE && GameScene.getGameScene().getGameBoard().validatePlacement(x, y)) {
					updateSkew();
					int[] xy = getCoords();
					anchoredShip = GameScene.getSelectedShip();
					GameScene.getGameScene().refreshCoordinates(xy[0], xy[1]);
					GameBoard.setCurrentY(y);
					GameBoard.setCurrentX(x);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				if (GameScene.getSelectedShip() == anchoredShip) {
					anchoredShip = ShipType.NONE;
				}
			}
		});
	}

	/**
	 * when called will paint the ship on this field. Called by the GameBoard to
	 * prevent clipping issues.
	 *
	 * @param g the graphic element that will be used
	 */

	public void paintShip(Graphics g) {
		int[] xy = getCoords();
		Graphics2D g2 = (Graphics2D) g;

		if (anchoredShip != ShipType.NONE) {
			if (anchoredShip == GameScene.getSelectedShip()) {
				g2.setComposite(AlphaComposite.SrcOver.derive(0.8f));
			}
			AffineTransform temp = g2.getTransform();
			if ((Window.isReverse() && anchoredShip == GameScene.getSelectedShip()) || isReversed) {
				g2.rotate(Math.toRadians(90), xy[0] + ((double) getWidth() / 2), xy[1] + ((double) getHeight() / 2));
			}
			g2.drawImage(anchoredShip.getModel(), xy[0], xy[1], this);
			g2.setComposite(AlphaComposite.SrcOver.derive(1f));
			g2.setTransform(temp);// resets the transformation
		}
	}

	public ShipType getAnchoredShip() {
		return anchoredShip;
	}

	public void setAnchoredShip(ShipType anchoredShip) {
		this.anchoredShip = anchoredShip;
	}
}
