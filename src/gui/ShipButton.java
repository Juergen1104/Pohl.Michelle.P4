package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import misc.ShipType;

/**
 * This class represents the diffrent ships the player is able to place. They
 * are being generated based on the ShipTypes.
 */
public class ShipButton extends JLabel {

	private static final long serialVersionUID = 1L;
	private boolean enabled;
	ShipType shipType;

	/**
	 * Generates the Button based on the ShipType
	 *
	 * @param shipType the ShipType that should be represented.
	 */
	public ShipButton(ShipType shipType) {
		this.shipType = shipType;
		enabled = true;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setBackground(Color.cyan);
		Image img = shipType.getIcon();
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setText(shipType.name());
		int[] size = shipType.getRelativeSize();
		this.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.setIcon(new ImageIcon(img.getScaledInstance(size[0] * 25, size[1] * 25, Image.SCALE_DEFAULT)));
		this.setMinimumSize(new Dimension(100, 50));

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (enabled) {
					GameScene.setSelectedShip(shipType);
					requestFocus();
					GameBoard.setCurrentY(-1);
					GameBoard.setCurrentY(-1);
				}
			}
		});
	}

	@Override
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		enabled = enable;
	}

}
