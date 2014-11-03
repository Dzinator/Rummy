package ca.mcgill.cs.comp303.rummy.model;

import java.util.*;

public class RandomPlayer implements Player
{
   
    /*
     * destroy these variables afterwards
     * */
    private final Random RNG = new Random();
   
   
    /*-------------------*/
    private Card tempCard;
    private GameEngine g;
    private Hand h = new Hand();
    //private int handScore;
    private String name;
    
    public RandomPlayer(String pName, GameEngine pG)
    {
    	name = pName;
    	g = pG;
    }
   
    @Override
    public Card playTurn()
    {
        boolean b = RNG.nextBoolean();
       
        if(b) tempCard = g.getDeckCard();
        else tempCard = g.getDiscardTop();
        
        return DiscardCard();
       
    }
    @Override
    public Hand getHand()
    {    
          return h;
    }
    @Override
    public String getName()
    {
        return name;
    }
   
    @Override
    public void knock(Card c)
    {
        g.hasKnocked(c, h);
    }
   
    @Override
    public void addCard(Card c)
    {
        if (h.isComplete())
            tempCard = c;
        else
            h.add(c);   
    }
   
   
    @Override
    public boolean pass()
    {
        boolean aBool = RNG.nextBoolean();
        if ( !aBool)
        {
            tempCard = g.getDiscardTop();
            g.hasDiscarded(DiscardCard());       
        }
        return aBool;
    }
   
    private Card DiscardCard()
    {
        int index = RNG.nextInt(11);
        if (index == 10)
        {
            return tempCard;
        }
        else
        {
            Set<Card> s = h.getUnmatchedCards();
            index = RNG.nextInt(s.size() - 1);
            Iterator<Card> i = s.iterator();
            int j = 0; Card c = null;
            while (i.hasNext() )
            {
                if (index == j)
                    break;
                 c =  i.next();
               
            }
           
            s.remove(c);
            s.add(tempCard);
            return c;
           
        }
    }
  

}
