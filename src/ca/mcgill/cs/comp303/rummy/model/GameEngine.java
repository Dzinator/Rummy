package ca.mcgill.cs.comp303.rummy.model;

import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public class GameEngine extends Observable implements Serializable
{
	private Random generator = new Random();
	
	public enum GameState
	{Init, Player0Turn, Player1Turn, EndGame}
	private GameState currentState = GameState.Init;
	
	private static final GameEngine ENGINE = new GameEngine();
	
	//this integer will keep track of games left to play if autoplay has been called
	private int gamesToPlay = 0;
	
	private Stack<Card> discardedCards;
	private Deck gameDeck;
	
	//Boolean to remember who is the dealer during the game
	private boolean whosTheDealer;
	//player 0 (false)
	private Player humanPlayer;
	//player 1 (true)
	private Player AIPlayer;
	
	private int[] playerScores;
	//When a player knocks the game groups the player cards
	Set<ICardSet>[] finishingGroups;
	
	private GameEngine(){}
	
	public static GameEngine getInstance()
	{
		return ENGINE;
	}
	
	
	private void myNotify()
	{
		super.setChanged();
		super.notifyObservers();
	}
	
	/**
	 * Adds a player to the game if the is still room
	 * @param pPlayer player to add to the game
	 * @throws NumberOfPlayersException
	 */
	public void addPlayer(Player pPlayer) throws NumberOfPlayersException
	{
		if(humanPlayer != null && AIPlayer != null ) throw new NumberOfPlayersException("Already 2 players in the game.");
		if(pPlayer instanceof HumanPlayer && humanPlayer == null)
		{
			humanPlayer = pPlayer;
		}
		//If we have an AIPlayer already and add another AI we put it in humanPlayer spot
		else if(humanPlayer == null && AIPlayer != null)
		{
			humanPlayer = pPlayer;
		}
		else
		{
			AIPlayer = pPlayer;
		}
	}
	
	/**
	 * Initializes the game and distributes cards, also handles the first round where players pass or not
	 * @throws NumberOfPlayersException
	 */
	public void newGame() throws NumberOfPlayersException
	{
		if(humanPlayer == null || AIPlayer == null ) throw new NumberOfPlayersException("Not enough players in the game.");
		
		
		gameDeck = new Deck();
		gameDeck.shuffle();
		playerScores[0] = 0;
		playerScores[1] = 0;
		discardedCards = new Stack<Card>();
		
		for(int i = 0; i < 10; i++)
		{
			humanPlayer.addCard(gameDeck.draw());
			AIPlayer.addCard(gameDeck.draw());
			
		}
		discardedCards.push(gameDeck.draw());
		myNotify();
		
		//choosing who deals at random, true --> AIPlayer, false --> humanPlayer
		whosTheDealer = generator.nextBoolean();
		
		//asking players if they pass on the first discarded card
		//When the method pass() returns either the player passes or has already taken the card and discarded another
		if(whosTheDealer)
		{
			if(humanPlayer.pass())
			{
				startGame(!whosTheDealer);
			}
			else
			{
				startGame(whosTheDealer);
			}
		}
		else
		{
			if(AIPlayer.pass())
			{
				startGame(!whosTheDealer);
			}
			else
			{
				startGame(whosTheDealer);
			}
		}
		
	}
	
	/**
	 * Starts the game with the provided first player going first and then cycles through the players in turn
	 * @param firstPlayer boolean representing first player (0 => humanPlayer, 1 => AI)
	 */
	private void startGame(boolean firstPlayer)
	{
		if(firstPlayer)
		{
			currentState = GameState.Player1Turn;
		}
		else
		{
			currentState = GameState.Player0Turn;
		}
		
		Boolean currentPlayer = firstPlayer;
		while(currentState != GameState.EndGame)
		{
			Card aCard;
			Player currPlayer;
			if(currentPlayer) currPlayer = AIPlayer;
			else currPlayer = humanPlayer;
			
			aCard = currPlayer.playTurn();
			discardedCards.push(aCard);
			myNotify();
			currentPlayer = !currentPlayer;
		}
	}
	
	/**
	 * Ends the game and counts the score
	 * @param c 
	 * @param h
	 */
	private void endGame(Card c, Hand h)
	{
		h.autoMatch();
		if(currentState == GameState.Player0Turn)
		{
			finishingGroups[0] = h.getMatchedSets();
			
			
		}
		else if(currentState == GameState.Player1Turn)
		{
			finishingGroups[1] = h.getMatchedSets();
		}
		//Set<Card> unMatched = h.getUnmatchedCards();
		//TODO
		
		gamesToPlay--;
	}
	
	/**
	 * The player calls this method when he wants to knock
	 * @param c the card to discard when knocking
	 * @param h his current hand
	 */
	public void hasKnocked(Card c, Hand h)
	{
		//verify if the knock is legal
		if(h.score() <= 10){
			discardedCards.push(c);
			myNotify();
			//currentState = GameState.EndGame;
			endGame(c, h);
		}
		
	}
	
	/**
	 * Retrieves card on top of the discarded pile
	 * @return the card on top of discarded 
	 */
	public Card getDiscardTop()
	{
		Card aCard = discardedCards.pop();
		myNotify();
		return aCard;
	}
	
	/**
	 * Returns the card on top of the discarded pile without removing it
	 * @return aCard card on top od discarded
	 */
	public Card peekDiscardTop()
	{
		return discardedCards.peek();
	}
	
	/**
	 * Draw a card from the game deck
	 * @return card on top of the deck
	 */
	public Card getDeckCard()
	{
		Card aCard = gameDeck.draw();
		myNotify();
		return aCard;
	}
	
	/**
	 * Used only when a player chooses not to pass, he discards the card he chooses
	 * @param c the card to be discarded
	 */
	public void hasDiscarded(Card c)
	{
		discardedCards.push(c);
		myNotify();
	}
	
	/**
	 * Returns the Human Player (player 0) , the human player can be an AIPlayer
	 * @return the human player
	 */
	public Player getHumanPlayer()
	{
		return humanPlayer;
	}
	
	/**
	 * Returns the AI Player (player 1)
	 * @return the AI player
	 */
	public Player getAIPlayer()
	{
		return AIPlayer;
	}
	
	/**
	 * This method returns the current score as a 2 slots array, the human player is o, the ai is 1
	 * @return
	 */
	public int[] getScore()
	{
		return playerScores;
	}
	
	/**
	 * Plays a certain number of games
	 * @param n number of games to play
	 * @throws NumberOfPlayersException 
	 */
	public void autoPlay(int n) throws NumberOfPlayersException
	{
		gamesToPlay = n;
		newGame();
	}
	
	/**
	 * Saves the current game to file named in a specified name
	 * @param aName the name of the save file
	 */
	public void saveGame(String aName)
	{
		GameSaver.saveEngine(this, aName);
	}
}
