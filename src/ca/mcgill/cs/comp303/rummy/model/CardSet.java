package ca.mcgill.cs.comp303.rummy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An immutable card set implementing ICardSet.
 */
public class CardSet implements ICardSet
{

	private Set<Card> aCardSet;

	/**
	 * Constructor.
	 */
	public CardSet(Collection<Card> pCards)
	{
		aCardSet = new HashSet<Card>(pCards);
	}

	@Override
	public Iterator<Card> iterator()
	{
		return aCardSet.iterator();
	}

	@Override
	public boolean contains(Card pCard)
	{
		return aCardSet.contains(pCard);
	}

	@Override
	public int size()
	{
		return aCardSet.size();
	}

	@Override
	public boolean isGroup()
	{
		if (aCardSet.size() < 3)
		{
			return false;
		}

		Iterator<Card> iterator = aCardSet.iterator();
		Card first = iterator.next();
		
		for (Card c : aCardSet)
		{
			if (first.getRank() != c.getRank())
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean isRun()
	{
		if (aCardSet.size() < 3)
		{
			return false;
		}

		Iterator<Card> iterator = aCardSet.iterator();
		Card first = iterator.next();
		ArrayList<Integer> range = new ArrayList<>();
		
		for (Card card : aCardSet)
		{
			if (first.getSuit() != card.getSuit())
			{
				return false;
			}
			range.add(new Integer(card.getOrdinal()));
		}
		
		Collections.sort(range);
		for (int i = 1; i < range.size(); i++)
		{
			if (range.get(i) != range.get(i-1)+1)
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + aCardSet.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardSet other = (CardSet) obj;
		if (!aCardSet.equals(other.aCardSet))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "CardSet [aCardSet=" + aCardSet + "]";
	}
}