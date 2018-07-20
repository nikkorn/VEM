package gaia.lotto;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a lotto in which a number of participants hold tickets and a winner can be drawn.
 * @param <TParticipant> The type of participant.
 */
public class Lotto<TParticipant> {
	/** 
	 * The list of participants.
	 */
	private ArrayList<Participant<TParticipant>> participants = new ArrayList<Participant<TParticipant>>();
	/** 
	 * The RNG to use in drawing a winner. 
	 */
	private Random rng = new Random();

	/**
	 * Create a new instance of the Lotto class.
	 */
	public Lotto() {}

	/**
	 * Create a new instance of the Lotto class.
	 * @param random The RNG to use in picking a winner.
	 */
	public Lotto(Random random) {
		this.rng = random;
	}
	
	/**
	 * Set the RNG to use in picking a winner.
	 * @param random The RNG to use in picking a winner.
	 */
	public void setRNG(Random random) {
		this.rng = random;
	}

	/**
	 * Add a participant with a single ticket.
	 * @param participant The participant.
	 * @returns this
	 */
	public Lotto<TParticipant> add(TParticipant participant) {
		participants.add(new Participant<TParticipant>(participant));
		return this;
	}

	/**
	 * Add a participant with a number of tickets.
	 * @param participant The participant.
	 * @param tickets The number of tickets.
	 * @returns this
	 */
	public Lotto<TParticipant> add(TParticipant participant, int tickets) {
		participants.add(new Participant<TParticipant>(participant, tickets));
		return this;
	}

	/**
	 * Draw a winner.
	 * @return The winning participant.
	 */
	public TParticipant draw() {
		// Create a pot of all tickets.
		ArrayList<TParticipant> tickets = new ArrayList<TParticipant>();
		for (Participant<TParticipant> participant : participants) {
			for (int ticketIndex = 0; ticketIndex < participant.getTickets(); ticketIndex++) {
				tickets.add(participant.getParticipant());
			}
		}
		// Check to make sure we even have any tickets.
		if (tickets.isEmpty()) {
			throw new NoTicketsRuntimeException();
		}
		// Pick a winner!
		return tickets.get(this.rng.nextInt(tickets.size()));
	}
}
