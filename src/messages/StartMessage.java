package messages;
/**
 * is sent to inform the player that a game has been found and the match can begin.
 */

public class StartMessage extends Message {

	private static final long serialVersionUID = 1L;
	private String opponentName;

	public StartMessage(String opponentName) {
		super(MessageType.START);
		this.opponentName = opponentName;
	}

	public String getOpponentName() {
		return opponentName;
	}
}
