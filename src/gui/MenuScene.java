package gui;

import client.Client;
import messages.CancelMessage;
import messages.ReadyMessage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * This Scene Represents the first Panel the player will see after connecting to
 * a server. Here the player can choose his username and start looking for a
 * game.
 */
public class MenuScene extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField username;
	private JPanel usernamePanel;
	private JPanel mainPanel;
	private JButton startButton;
	private JPanel waitingPanel;
	private JLabel waitingLabel;
	private JButton cancelButton;
	private WaitingThread waitingThread;
	private boolean waiting;
	private static MenuScene menuScene;

	/**
	 * Generates the Menu
	 *
	 */
	private MenuScene() {
		super();
		this.setLayout(new BorderLayout());
		Color backgroundColor = new Color(116, 139, 151);
		this.setBackground(backgroundColor);
		setupElements();
	}

	public static void createMenu() {
		if (Objects.isNull(menuScene)) {
			menuScene = new MenuScene();
		}
	}

	/**
	 * sets up all elements and SubScenes in the Menu Scene.
	 */
	private void setupElements() {
		Image image = new ImageIcon("resources/header.png").getImage();
		JLabel header = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_AREA_AVERAGING)));
		header.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
		this.add(header, BorderLayout.NORTH);
		setupMain();
		setupWaiting();

	}

	/**
	 * Sets up all the visual elements in the Main Menu Screen.
	 */

	private void setupMain() {
		usernamePanel = new JPanel();
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
		username = new JTextField("user");
		username.setMaximumSize(new Dimension(300, 50));
		JLabel userText = new JLabel("username:");
		userText.setHorizontalAlignment(SwingConstants.LEFT);
		usernamePanel.add(userText);
		usernamePanel.add(username);
		usernamePanel.add(Box.createRigidArea(new Dimension(10, 10)));
		startButton = new JButton("Start Game");
		startButton.addActionListener(e -> startGame());
		usernamePanel.add(startButton);
		usernamePanel.setBorder(
				new CompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));
		mainPanel = new JPanel();
		mainPanel.setBackground(getBackground());
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(usernamePanel);
		mainPanel.add(Box.createHorizontalGlue());
		this.add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * sets up all the visual elements in the Waiting Screen.
	 */
	private void setupWaiting() {
		waiting = false;
		waitingPanel = new JPanel();
		BorderLayout bL = new BorderLayout();
		waitingPanel.setLayout(bL);
		JLabel text = new JLabel("waiting for Match");
		text.setHorizontalTextPosition(SwingConstants.CENTER);
		waitingPanel.add(text, BorderLayout.NORTH);
		waitingLabel = new JLabel(".");
		waitingLabel.setFont(new Font(waitingLabel.getFont().getName(), Font.BOLD, 24));
		waitingPanel.setMinimumSize(new Dimension(400, 400));
		waitingLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		waitingPanel.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		waitingPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
		waitingPanel.add(waitingLabel, BorderLayout.CENTER);
		waitingThread = new WaitingThread();
		waitingPanel.setBorder(
				new CompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2), new EmptyBorder(10, 10, 10, 10)));
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> stopSearch());
		waitingPanel.add(cancelButton, BorderLayout.SOUTH);
	}

	/**
	 * if called, will tell the server the required information entered by the
	 * player.
	 */
	private void startGame() {
		String text = username.getText();
		if (text.equals("")) {
			text = "user";
		}
		Client.setUsername(text);
		Client.sendMessage(new ReadyMessage(text));
		switchToWaiting();
	}

	/**
	 * stops the searching of a game.
	 */
	private void stopSearch() {
		Client.sendMessage(new CancelMessage());
		switchToMain();
	}

	/**
	 * this Thread represents the moving dots in the Waiting screen.
	 */
	private class WaitingThread extends Thread {

		@Override
		public void run() {
			int num = 0;
			StringBuilder stringBuffer = new StringBuilder();
			try {
				while (waiting) {
					num = (num + 1) % 3;
					stringBuffer.setLength(0);
					stringBuffer.append(".".repeat(Math.max(0, num + 1)));
					waitingLabel.setText(stringBuffer.toString());

					Thread.sleep(2000);
				}
			} catch (InterruptedException ignored) {

			}
		}
	}

	/**
	 * Switches the Scene to the Waiting Scene.
	 */
	private void switchToWaiting() {
		mainPanel.remove(usernamePanel);
		mainPanel.add(waitingPanel);
		waiting = true;
		waitingThread = new WaitingThread();
		waitingThread.start();
		repaint();
		revalidate();
	}

	/**
	 * Switches the Scene to the Main Menu Scene.
	 */

	public void switchToMain() {
		mainPanel.remove(waitingPanel);
		mainPanel.add(usernamePanel);
		waiting = false;
		waitingThread.interrupt();
		repaint();
		revalidate();
	}

	public static MenuScene getMenuScene() {
		return menuScene;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

}
