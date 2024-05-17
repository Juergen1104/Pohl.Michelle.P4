package messages;

import java.io.Serializable;

/**
 * This class acts as the vessel of data for transportation between server and
 * client.
 */
public abstract class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private MessageType type;

	public MessageType getType() {
		return type;
	}

	public Message(MessageType type) {
		this.type = type;
	}

}
