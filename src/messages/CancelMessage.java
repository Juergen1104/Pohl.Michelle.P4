package messages;
/**
 *  is sent to the server when the player wants to cancel matchmaking.
 */
public class CancelMessage extends Message {

	private static final long serialVersionUID = 1L;

	public CancelMessage() {
		super(MessageType.CANCEL);
	}

}
