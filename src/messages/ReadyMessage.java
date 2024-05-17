package messages;
/**
 *  is sent to inform the player of the result of the last executed shot. 
 */

public class ReadyMessage extends Message {

	private static final long serialVersionUID = 1L;
	private String username;

	public ReadyMessage(String username) {
		super(MessageType.READY);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
