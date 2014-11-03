package ca.mcgill.cs.comp303.rummy.model;

import java.io.*;

public class GameSaver
{
	
	public static void saveEngine(GameEngine g, String aName)
	{
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream( new FileOutputStream(aName + ".dat"));
			out.writeObject(g);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(out != null)
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		
	}
	
	public static GameEngine loadEngine(String aName)
	{
		GameEngine g = null;
		ObjectInputStream in = null;
		try
		{
			in = new ObjectInputStream( new FileInputStream(aName + ".dat"));
			g = (GameEngine) in.readObject();
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(in != null)
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		
		return g;
	}
	
	

}
