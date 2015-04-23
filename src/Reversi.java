import java.util.*;
import java.io.*;
// driver program
public class Reversi {
	public static void main (String[] args)
	{
		boolean GAMEOVER = false;
		String WINNER;
		
		Board board = new Board(8);
		board.startup();
		board.render();
	
		
		//default: player one is white
		while (!GAMEOVER)
		{
			if(!GAMEOVER)
			{
				boolean player = true;
				Turn myTurnHuman = new Turn(true);
				//Turn.move(myTurnHuman.newPiece, board);
			}
			if(!GAMEOVER)
			{
				boolean player = false;
				Turn myTurnAI = new Turn(false);
			}
		}
	}
}
class Turn
{
	Turn previous;
	//true = white, false = black
	public boolean player;
	boolean moved;
	
	public Turn(boolean Player)
	{
		Scanner TurnScanner = new Scanner(System.in);
		int x;
		int y;
		player = Player;
		moved = false;
		System.out.println("Where would you like to place your piece?");
		x = TurnScanner.nextInt();
		y = TurnScanner.nextInt();
		Piece newPiece = new Piece(x, y, player);
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
