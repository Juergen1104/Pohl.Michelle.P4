package messages;
/**
 * is sent to inform the player that it is their turn.
 */
public class AttackMessage extends Message {

	private static final long serialVersionUID = 1L;

	public AttackMessage() {
		super(MessageType.ATTACK);
	}

}
