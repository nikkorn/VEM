package lotto;

/**
 * A participant in the lotto draw.
 * @param <TParticipant> The type of participant.
 */
public class Participant<TParticipant> {
	/**
	 * The number of tickets that this participant holds.
	 */
	private int tickets = 1;
	/**
	 * The participant.
	 */
	private TParticipant participant;

	/**
	 * Create a new instance of the Participant class.
	 * @param participant The participant.
	 */
	public Participant(TParticipant participant) {
		this.participant = participant;
	}

	/**
	 * Create a new instance of the Participant class.
	 * @param participant The participant.
	 * @param tickets The number of tickets the participant holds.
	 */
	public Participant(TParticipant participant, int tickets) {
		this.participant = participant;
		this.tickets     = tickets;
	}

	/**
	 * Get the number of participant tickets.
	 * @return tickets.
	 */
	public int getTickets() {
		return tickets;
	}

	/**
	 * Get the participant.
	 * @return participant
	 */
	public TParticipant getParticipant() {
		return participant;
	}
}
