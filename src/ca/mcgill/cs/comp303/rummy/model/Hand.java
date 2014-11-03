package ca.mcgill.cs.comp303.rummy.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Models a hand of 10 cards. The hand is not sorted. Not threadsafe.
 * The hand is a set: adding the same card twice will not add duplicates
 * of the card.
 * @inv size() > 0
 * @inv size() <= HAND_SIZE
 */
public class Hand
{
	private final int COMPLETE_HAND_SIZE = 10;
	private Set<Card> aUnmatched;
	private Set<ICardSet> aMatchedSets;
	private SetMatcher setMatcher;

	/**
	 * Creates a new, empty hand.
	 */
	public Hand()
	{
		aUnmatched = new HashSet<Card>();
		aMatchedSets = new HashSet<ICardSet>();
		setMatcher = new SetMatcher();
	}
	
	/**
	 * Adds pCard to the list of unmatched cards.
	 * If the card is already in the hand, it is not added.
	 * @param pCard The card to add.
	 * @throws HandException if the hand is complete.
	 * @throws HandException if the card is already in the hand.
	 * @pre pCard != null
	 */
	public void add( Card pCard )
	{
		if (this.isComplete())
		{
			throw new HandException("Cannot add card: " + pCard.toString() + "; hand is already complete.");
		}
		else if (this.contains(pCard))
		{
			throw new HandException("Card: " + pCard.toString() + " is already in the hand.");
		}
		else
		{
			aUnmatched.add(pCard);
		}
	}
	
	/**
	 * Remove pCard from the hand and break any matched set
	 * that the card is part of. Does nothing if
	 * pCard is not in the hand.
	 * @param pCard The card to remove.
	 * @pre pCard != null
	 */
	public void remove( Card pCard )
	{
		aUnmatched.remove(pCard);
		
		for (ICardSet matchedSet : aMatchedSets)
		{
			if (matchedSet.contains(pCard)){
				// This matched set is now broken; add other cards to the unmatched set.
				for (Card c : matchedSet)
				{
					if (!c.equals(pCard)){
						aUnmatched.add(c);
					}
				}
				aMatchedSets.remove(matchedSet);
				break;
			}
		}
	}
	
	/**
	 * @return True if the hand is complete.
	 */
	public boolean isComplete()
	{
		return this.size() == COMPLETE_HAND_SIZE;
	}
	
	/**
	 * Removes all the cards from the hand.
	 */
	public void clear()
	{
		aUnmatched.clear();
		aMatchedSets.clear();
	}
	
	/**
	 * @return A copy of the set of matched sets
	 */
	public Set<ICardSet> getMatchedSets()
	{
		return aMatchedSets;
	}
	
	/**
	 * @return A copy of the set of unmatched cards.
	 */
	public Set<Card> getUnmatchedCards()
	{
		return aUnmatched;
	}
	
	/**
	 * @return The number of cards in the hand.
	 */
	public int size()
	{
		int handSize = aUnmatched.size();
		
		for (ICardSet matchedSet : aMatchedSets)
		{
			handSize += matchedSet.size();
		}
		
		return handSize;
	}
	
	/**
	 * Determines if pCard is already in the hand, either as an
	 * unmatched card or as part of a set.
	 * @param pCard The card to check.
	 * @return true if the card is already in the hand.
	 * @pre pCard != null
	 */
	public boolean contains( Card pCard )
	{
		if (aUnmatched.contains(pCard))
		{
			return true;
		}
		
		for (ICardSet matchedSet : aMatchedSets)
		{
			if (matchedSet.contains(pCard))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @return The total point value of the unmatched cards in this hand.
	 */
	public int score()
	{
		int score = 0;
		
		for (Card card : getUnmatchedCards())
		{
			score += card.value();
		}
		
		return score;
	}
	
	/**
	 * Creates a group of cards of the same rank.
	 * @param pCards The cards to groups
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	public void createGroup( Set<Card> pCards )
	{
		if (!this.cardsAreUnmatched(pCards))
		{
			throw new HandException("A card in this set is already matched.");
		}
		
		ICardSet cardSet = new CardSet(pCards);
		
		if (!cardSet.isGroup())
		{
			throw new HandException("Cards do not form a valid group.");
		}
		
		this.removeFromUnmatched(pCards);
		aMatchedSets.add(cardSet);
	}

	/**
	 * Creates a run of cards of the same suit.
	 * @param pCards The cards to group in a run
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	public void createRun( Set<Card> pCards )
	{
		if (!this.cardsAreUnmatched(pCards))
		{
			throw new HandException("A card in this set is already matched.");
		}
		
		ICardSet cardSet = new CardSet(pCards);
		
		if (!cardSet.isRun())
		{
			throw new HandException("Cards do not form a valid run.");
		}
		
		this.removeFromUnmatched(pCards);
		aMatchedSets.add(cardSet);
	}
	
	/**
	 * Calculates the matching of cards into groups and runs that
	 * results in the lowest amount of points for unmatched cards.
	 */
	public void autoMatch()
	{
		List<Card> allCards = this.getAllCards();
		this.clear();

		Set<ICardSet> solution = setMatcher.matchSets(allCards);
		aMatchedSets = solution;
		
		this.updateUnmatchedCards(allCards);
	}

	private void updateUnmatchedCards(List<Card> allCards)
	{
		for (Card card : allCards)
		{
			boolean matched = false;
			for (ICardSet matchedSet : aMatchedSets)
			{
				if (matchedSet.contains(card)) matched = true;
			}
			
			if (!matched) aUnmatched.add(card);
		}
	}
	
	private void removeFromUnmatched(Set<Card> pCards)
	{
		for (Card c : pCards)
		{
			aUnmatched.remove(c);
		}
	}
	
	private boolean cardsAreUnmatched(Set<Card> pCards)
	{
		for (Card c : pCards)
		{
			if (!aUnmatched.contains(c))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private List<Card> getAllCards()
	{
		ArrayList<Card> allCards = new ArrayList<>();
		
		for (Card c : aUnmatched)
		{
			allCards.add(c);
		}
			
		for (ICardSet cS : aMatchedSets)
		{
			for (Card c : cS)
			{
				allCards.add(c);
			}
		}

		return allCards;
	}
}