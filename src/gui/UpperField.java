package gui;

import messages.ShotMessage;
import misc.Phases;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This Class represents the UpperField that is crucial for the attack phase
 */
public class UpperField extends Field {

	private static final long serialVersionUID = 1L;

	/**
	 * generates the Field and establishes the required functionality necessary for
	 * attacking the opponent trough clicking.
	 *
	 * @param x the x location on the board
	 * @param y the y location on the board
	 */
	public UpperField(int x, int y) {
		super(x, y);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (GameScene.getPhase() == Phases.FIGHTING && condition == Condition.NONE) {
					client.Client.sendMessage(new ShotMessage(x, y));
					GameScene.setPhase(Phases.NONE);
					GameScene.getGameScene().setNotification("waiting for opponent", Color.BLACK);
				}
			}
		});

	}

}
