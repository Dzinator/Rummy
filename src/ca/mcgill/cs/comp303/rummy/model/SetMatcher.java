package ca.mcgill.cs.comp303.rummy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetMatcher
{
	public List<Card> aCards;

	
	/**
	 * Calculates the matching of cards into groups and runs that
	 * results in the lowest amount of points for unmatched cards.
	 */
	public Set<ICardSet> matchSets(List<Card> cards)
	{
		aCards = cards;
		List<ICardSet> allSets = this.getAllPossibleSets();
		return this.computeOptimalMatching(allSets);
	}
	
	private List<ICardSet> getAllPossibleSets()
	{
		List<ICardSet> allSets = new ArrayList<ICardSet>();
		Collections.sort(aCards);
		
		allSets.addAll(getAllGroups());
		allSets.addAll(getAllRuns());

		return allSets;
	}

	private Set<ICardSet> getAllRuns()
	{
		Set<ICardSet> allRuns = new HashSet<>();
		Set<Card> partOfPotentialRun = new HashSet<>();
		
		for (int i = 0; i < aCards.size() - 3; i++)
		{
			Card current = aCards.get(i);
			
			// Skip card if it is already part of potential run.
			if (partOfPotentialRun.contains(current)) continue;
			
			List<Card> potentialRun = new ArrayList<>();
			potentialRun.add(current);
			partOfPotentialRun.add(current);
			
			for (int j = i + 1; j < aCards.size(); j++)
			{
				Card next = aCards.get(j);
				
				if (next.getOrdinal() - current.getOrdinal() == 1)
				{
					if (next.getSuit() == current.getSuit())
					{
						potentialRun.add(next);
						partOfPotentialRun.add(next);
						current = next;
					}
					else continue;
				}
				else if (next.getOrdinal() == current.getOrdinal()) continue;
				else break;
			}

			allRuns.addAll(this.getRunCombinations(potentialRun));
		}
		
		return allRuns;
	}

	private Set<ICardSet> getAllGroups()
	{
		Set<ICardSet> allGroups = new HashSet<>();
		
		for (int i = 0; i < aCards.size() - 3; i++)
		{
			CardSet possibleSet = new CardSet(aCards.subList(i, i + 3));
			if (possibleSet.isGroup())
			{
				// Check if next card can also be part of group.
				if (aCards.get(i).getRank() == aCards.get(i + 3).getRank())
				{
					allGroups.addAll(this.getGroupCombinations(aCards.subList(i, i + 4)));
					i += 3;
				}
				else
				{
					allGroups.add(possibleSet);
					i += 2;
				}
			}
		}
		
		return allGroups;
	}
	
	private Collection<ICardSet> getGroupCombinations(List<Card> group)
	{
		Collection<ICardSet> combinations = new HashSet<ICardSet>();
		combinations.add(new CardSet(group));
		
		Set<Card> temp = new HashSet<Card>();

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (j != i)
				{
					temp.add(group.get(j));
				}
			}
			
			combinations.add(new CardSet(temp));
			temp.clear();
		}
		
		return combinations;
	}
	
	private Collection<ICardSet> getRunCombinations(List<Card> potentialRun)
	{
		Collection<ICardSet> combinations = new HashSet<ICardSet>();
		
		if (potentialRun.size() < 3) return combinations;
		
		CardSet cs = new CardSet(potentialRun);
		if (cs.isRun())
		{
			for (int i = 0; i <= cs.size() - 3; i++)
			{
				for (int j = i + 3; j <= cs.size(); j++)
				{
					CardSet combination = new CardSet(potentialRun.subList(i, j));
					combinations.add(combination);
				}
			}
		}

		return combinations;
	}
	
	/**
	 * Determine optimal set matching.
	 * @param pSets List of all possible sets for a given hand.
	 * @return Optimal hand matching.
	 */
	public HashSet<ICardSet> computeOptimalMatching(List<ICardSet> pSets)
	{
		// Sort ArrayList in order of the value of the set
		Comparator<ICardSet> comparator = new Comparator<ICardSet>(){
			@Override
			public int compare(ICardSet pS1, ICardSet pS2)
			{
				int v1 = 0;
				int v2 = 0;
				for (Card c : pS1)
				{
					v1 += c.value();
				}
				for (Card c : pS2)
				{
					v2 += c.value();
				}
				return v2-v1;
			}
		};
		Collections.sort(pSets, comparator);

		// Compute optimal solution
		return opt(pSets, 0);
	}

	/**
	 * Recursive optimization function to select best hand. Break the problem into
	 * two cases at each recursive step: either the current element under consideration
	 * is included or excluded.
	 * @param pSets A list of all possible sets given a hand of cards.
	 * @param pCurrent The index of the current set under consideration.
	 * @return The optimal matching.
	 */
	private HashSet<ICardSet> opt(List<ICardSet> pSets, int pCurrent)
	{
		// Base Case
		if (pSets.size() == pCurrent)
		{
			// Return empty set
			return new HashSet<ICardSet>();
		}

		// Recursive parameters
		HashSet<ICardSet> opt1;
		HashSet<ICardSet> opt2;

		// Recursive option 1: set_current is included
		opt1 = opt(removeConflicts(pSets, pCurrent), 0);
		opt1.add(pSets.get(pCurrent)); // add current set

		// Recursion option 2: set_current is not included
		opt2 = opt(pSets, pCurrent+1);

		// Construct optimal solution recursively
		if (valueOfSet(opt1) >= valueOfSet(opt2))
		{
			return opt1;
		}
		return opt2;
	}

	/**
	 * Compute the value of a set of card sets.
	 * @param pHashSet Set of card sets.
	 * @return Total value of the composing card sets.
	 */
	private static int valueOfSet(HashSet <ICardSet> pHashSet)
	{
		int v = 0;
		for (ICardSet cS : pHashSet)
		{
			for (Card c : cS)
			{
				v += c.value();
			}
		}
		return v;
	}

	/**
	 * Check if two ICardSets have a common element.
	 * @param pS1 ICardSet 1
	 * @param pS2 ICardSet 2
	 * @return True if the ICardSets have a common card.
	 */
	private static boolean conflict(ICardSet pS1, ICardSet pS2)
	{
		for (Card c : pS1)
		{
			if (pS2.contains(c))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove conflicts from a list and construct a list of non-conflicting sets.
	 * @param pSets List of all sets.
	 * @param pCurrent Elements of consideration for conflict.
	 * @return Non-conflicting sets.
	 */
	private static ArrayList<ICardSet> removeConflicts(List<ICardSet> pSets, int pCurrent)
	{
		ArrayList<ICardSet> noConflicts = new ArrayList<>();
		for (int i = pCurrent+1; i < pSets.size(); i++)
		{
			if (!conflict(pSets.get(i), pSets.get(pCurrent)))
			{
				noConflicts.add(pSets.get(i));
			}
		}
		return noConflicts;
	}
}