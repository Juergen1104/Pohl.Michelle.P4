package messages;
/**
 * is sent to inform the player about the winner of the match.
 */
public class WinMessage extends Message {

	private static final long serialVersionUID = 1L;
	private boolean opponent;

	public WinMessage(boolean opponent) {
		super(MessageType.WIN);
		this.opponent = opponent;
	}

	public boolean isOpponent() {
		return opponent;
	}
}
