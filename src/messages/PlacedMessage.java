package messages;
/**
 * is sent to the server when the player has placed all their ships.
 * The map attribute informs the server about the ship placements.
 */

public class PlacedMessage extends Message {

	private static final long serialVersionUID = 1L;
	private int[][] map;

	public PlacedMessage(int[][] map) {
		super(MessageType.PLACED);
		this.map = map;
	}

	public int[][] getMap() {
		return map;
	}
}
