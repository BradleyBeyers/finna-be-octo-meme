
// driver program
public class Reversi {
	public static void main (String[] args)
	{
		Board board = new Board(8);
		board.startup();
		board.render();
		
		//default: player one is white
		boolean player = true;
		
	}
}
class Turn
{
	//true = white, false = black
	public boolean player;
	boolean moved;
	
	public Turn (boolean Player)
	{
		player = Player;
		moved = false;
	}
	public Turn next()
	{
		Turn next = new Turn(!player);
			
		return next;
	}
}
