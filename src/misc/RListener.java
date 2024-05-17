package misc;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import gui.GameScene;
import gui.Window;

/**
 * this class listens solely to the R button and rotates the ships if pressed.
 */
public class RListener extends KeyAdapter {

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_R) {
      Window.setReverse(!Window.isReverse());
    }
    if (!Objects.isNull(GameScene.getGameScene().getGameBoard())) {
      GameScene.getGameScene().getGameBoard().reevaluateField();
    }
  }

}
