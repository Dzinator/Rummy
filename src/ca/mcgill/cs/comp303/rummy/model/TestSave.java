package ca.mcgill.cs.comp303.rummy.model;

public class TestSave
{

	/**
	 * @param args
	 * @throws NumberOfPlayersException 
	 */
	public static void main(String[] args) throws NumberOfPlayersException
	{
		HumanPlayer p1 = new HumanPlayer();
		HumanPlayer p2 = new HumanPlayer();
		GameEngine g = GameEngine.getInstance(); 
		g.addPlayer(p1);
		g.addPlayer(p2);
		
		System.out.println(g.hashCode());
		g.saveGame("Yolo");
		g = null;
		g = GameSaver.loadEngine("Yolo");
		System.out.println(g.hashCode());

	}

}
