package ca.mcgill.cs.comp303.rummy.model;

import java.util.*;

public interface GameObserver extends Observer
{
	void newGame(GameEngine g);
	void playerHasPlayed(GameEngine g);
	void gameEnded();

}
