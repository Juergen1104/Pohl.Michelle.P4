package messages;

public class ResultMessage extends Message {

	private static final long serialVersionUID = 1L;
	private boolean hit;
	private boolean byEnemy;
	// Gitterkoordinaten, wo der Angreifer hingeschossen hat. Wichtig für die Darstellung.
	//  x,y Werte aus ShotMessage
	private int x;
	private int y;

	public ResultMessage(int x, int y, boolean hit, boolean byEnemy) {
		super(MessageType.RESULT);
		this.x = x;
		this.y = y;
		this.hit = hit;
		this.byEnemy = byEnemy;
	}

	public boolean isByEnemy() {
		return byEnemy;
	}

	public boolean isHit() {
		return hit;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
