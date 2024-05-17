package gui;

import java.awt.Dimension;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JPanel;

import misc.RListener;

/**
 * This is the parent Frame of the Game that houses the different Scenes and
 * Elements. It's primary functionality is holding important static game
 * variables and switching between Scenes.
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Window window;
	static int xSkew;
	static int ySkew;
	static boolean reverse;

	/**
	 * generates the Window as a Singleton object and sets the necessary variables
	 * as well as the Listeners up.
	 */
	private Window() {
		xSkew = 0;
		ySkew = 0;
		window = this;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.addKeyListener(new RListener());
		GameScene.createGameScene();
		MenuScene.createMenu();
		switchToMenu(false);
		this.pack();

	}

	/**
	 * switches the visible screen to the Menu screen and sets it to already waiting
	 * if necessary.
	 *
	 * @param waiting if the screen should be switched to waiting or not.
	 */
	public static void switchToMenu(boolean waiting) {
		MenuScene menuSceneTemp = MenuScene.getMenuScene();
		menuSceneTemp.setWaiting(waiting);
		Window.getWindow().setPreferredSize(new Dimension(500, 400));
		Window.getWindow().pack();
		if (!waiting) {
			menuSceneTemp.switchToMain();

		}
		Window.getWindow().setScene(menuSceneTemp);
	}

	/**
	 * switches the current scene to the Game scene and scales the window
	 * accordingly.
	 */
	public static void switchToGame() {
		Window.getWindow().setPreferredSize(new Dimension(500, 770));
		Window.getWindow().pack();
		GameScene.getGameScene().start();
		Window.getWindow().setScene(GameScene.getGameScene());
	}

	/**
	 * This method sets the currently visible scene.
	 *
	 * @param scene the scene that should be visible.
	 */
	private void setScene(JPanel scene) {
		this.setContentPane(scene);
		repaint();
		revalidate();
	}

	public static int getxSkew() {
		return xSkew;
	}

	public static int getySkew() {
		return ySkew;
	}

	public static void setxSkew(int xSkew) {
		Window.xSkew = xSkew;
	}

	public static void setySkew(int ySkew) {
		Window.ySkew = ySkew;
	}

	public static Window getWindow() {
		return window;
	}

	/**
	 * Ensures a seamless closure of the Game.
	 */
	public static void close() {
		if (!Objects.isNull(window)) {
			window.dispose();
		}
	}

	/**
	 * Creates the Window as a Singleton object.
	 */
	public static void startWindow() {
		if (!Objects.isNull(window)) {
			window = null;
		}
		window = new Window();
	}

	public static boolean isReverse() {
		return reverse;
	}

	public static void setReverse(boolean reverse) {
		Window.reverse = reverse;
		getWindow().repaint();
	}

}
