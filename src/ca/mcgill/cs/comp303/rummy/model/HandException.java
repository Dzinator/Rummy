package ca.mcgill.cs.comp303.rummy.model;

/**
 * Indicates any problem with the state of the hand.
 */
@SuppressWarnings("serial")
public class HandException extends RuntimeException 
{
	/**
	 * @param pMessage The exception message.
	 * @param pException The wrapped exception.
	 */
	public HandException( String pMessage, Throwable pException ) 
	{
		super( pMessage, pException );
	}

	/**
	 * @param pMessage The exception message.
	 */
	public HandException( String pMessage ) 
	{
		super( pMessage );
	}

	/**
	 * @param pException The wrapped exception
	 */
	public HandException( Throwable pException )
	{
		super( pException );
	}
}
