package messages;
/**
 * is sent to inform the player that their opponent has left the match.
 */
public class QuitMessage extends Message {

	private static final long serialVersionUID = 1L;

	public QuitMessage() {
		super(MessageType.QUIT);
	}

}
