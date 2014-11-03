package ca.mcgill.cs.comp303.rummy.model;

/**
 * Implements a player
 * @author yhatta1
 *
 */
public interface Player
{

	/**
	 * Player plays his turn which consists of taking a card and discarding another (that is returned) or deciding to knock
	 * @return the card the player decices to discard
	 */
	Card playTurn();
	
	/**
	 * Returns the current hand of the player
	 * @return aHand current hand
	 */
	Hand getHand();
	
	/**
	 * Returns the name of the player
	 * @return
	 */
	String getName();
	
	/**
	 * Must verify if it is possible to knock and if so calls g.hasKnocked(c)
	 * @param c card to discard when knocking
	 */
	void knock(Card c);
	
	/**
	 * Adds the provided card to the hand of the player
	 * @param c card to add to the hand
	 */
	void addCard(Card c);
	
	/**
	 * Asks player if he passes on first discarded, if not player MUST take it and 
	 * discard a card (g.hasDiscarded(c)) then return false
	 * @return Decision of passing or not
	 */
	boolean pass();
	
	
}
