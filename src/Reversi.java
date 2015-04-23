
// driver program
public class Reversi {
	public static void main (String[] args)
	{
		Board board = new Board(8);
		board.startup();
		board.render();
		
		//default: player one is white
		boolean player = true;
		
		// Internal Egit Test
		
	}
}
class Turn
{
	Turn previous;
	//true = white, false = black
	public boolean player;
	boolean moved;
	
	public Turn (boolean Player)
	{
		player = Player;
		moved = false;
	}
	public Turn next(Turn current)
	{
		Turn next = new Turn(!(current.player));
		next.previous = current;
		return next;
	}
	public boolean move(Piece piece, Board board)
	{
		boolean movement = board.place(piece);
		
		if (movement)
			return true;
		else 
		{
			System.out.println("Invalid move. Please make another.");
			return false;
		}
	}
}
