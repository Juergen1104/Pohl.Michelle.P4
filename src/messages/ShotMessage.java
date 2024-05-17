package messages;
/**
 *  is sent to the server when the player selects a field to attack during the attack phase.
 */
public class ShotMessage extends Message {

	private static final long serialVersionUID = 1L;
	//  x,y Gitterkoordinaten, die vom Server überprüft werden sollen, ob getroffen wurde oder nicht.
	private int x;
	private int y;

	public ShotMessage(int x, int y) {
		super(MessageType.SHOT);
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
