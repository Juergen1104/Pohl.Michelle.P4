package misc;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import gui.Window;

/**
 * this class holds all the different ship types present in the game. Do note that every ship is
 * identified with its ordinal, meaning that no identical ships can be placed on the board.
 */
public enum ShipType {
  NONE, CARRIER, SUBMARINE, DESTROYER, BATTLESHIP, CRUISER;
	
  private static final String path = "resources/ships/";
  /**
   * this method returns the actual ship size depending on the ShipType
   *
   * @return the ships size
   */
  public int[] getSize() {
    int xSkew = Window.getxSkew();
    int ySkew = Window.getySkew();
    switch (this) {
      case CARRIER:
        return new int[]{5 * xSkew + 8, ySkew};
      case BATTLESHIP:
        return new int[]{4 * xSkew + 6, ySkew};
      case DESTROYER:
        return new int[]{2 * xSkew + 2, ySkew};
      case SUBMARINE, CRUISER:
        return new int[]{3 * xSkew + 4, ySkew};
      default:
        return new int[2];
    }
  }

  /**
   * this method returns the relative grid size of each ship.
   *
   * @return the grid size
   */
  public int[] getRelativeSize() {
    switch (this) {
      case CARRIER:
        return new int[]{5, 1};
      case BATTLESHIP:
        return new int[]{4, 1};
      case DESTROYER:
        return new int[]{2, 1};
      case SUBMARINE, CRUISER:
        return new int[]{3, 1};
      default:
        return new int[2];
    }
  }

  /**
   * similar to getIcon. this method will return a scaled instance of each ship.
   *
   * @return the scaled Instance of the ship as an Image
   */
  public Image getModel() {
    int[] arr = this.getSize();
    return getIcon().getScaledInstance(arr[0], arr[1], Image.SCALE_DEFAULT);
  }

  /**
   * this method returns the ships model that represents it.
   *
   * @return the model as an Icon.
   */
  public Image getIcon() {
    BufferedImage bI = new BufferedImage(1, 1, Image.SCALE_DEFAULT);
    try {
      switch (this) {
        case CARRIER -> bI = ImageIO.read(new File(path+"carrier.png"));
        case SUBMARINE -> bI = ImageIO.read(new File(path+"submarine.png"));
        case BATTLESHIP -> bI = ImageIO.read(new File(path+"battleship.png"));
        case DESTROYER -> bI = ImageIO.read(new File(path+"destroyer.png"));
        case CRUISER -> bI = ImageIO.read(new File(path+"cruiser.png"));
        case NONE -> {
        }
      }
      return bI;
    } catch (IOException e) {
      e.printStackTrace();
      return bI;
    }
  }
}
