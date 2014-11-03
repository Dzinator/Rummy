package ca.mcgill.cs.comp303.rummy.model;
/**
 * Indicates that there are not enough players to play the game
 * @author yhatta1
 *
 */

@SuppressWarnings("serial")
public class NumberOfPlayersException extends Exception
{

	/**
	 * @param pMessage The exception message.
	 * @param pException The wrapped exception.
	 */
	public NumberOfPlayersException( String pMessage, Throwable pException ) 
	{
		super( pMessage, pException );
	}

	/**
	 * @param pMessage The exception message.
	 */
	public NumberOfPlayersException( String pMessage ) 
	{
		super( pMessage );
	}

	/**
	 * @param pException The wrapped exception
	 */
	public NumberOfPlayersException( Throwable pException )
	{
		super( pException );
	}
}
