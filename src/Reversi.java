import java.util.*;
import java.io.*;
// driver program
public class Reversi {
	
public static Board board = new Board(8); 
	public static void main (String[] args) {
		boolean GAMEOVER = false;
		//Board board = new Board(8);
		board.startup();
		board.render();
	
		
		//default: player one is white
		while (!GAMEOVER) {

			if(!GAMEOVER) {
				Turn myTurnHuman = new Turn(true);
				while(Turn.move(myTurnHuman.newPiece, board) == false)
				{
					myTurnHuman = new Turn(true);
				}
				board.render();
			}

			if(!GAMEOVER) {
				Turn myTurnAI = new Turn(false);
				while(Turn.move(myTurnAI.newPiece, board) == false)
				{
					myTurnAI = new Turn(false);
				}
				board.render();
			}
		}
	}
}

class Turn {
	public Piece newPiece;
	Turn previous;
	//true = white, false = black
	public static boolean player;
	boolean moved;
	
	public Turn(boolean Player) {
		Scanner TurnScanner = new Scanner(System.in);
		int xint;
		int yint;
		player = Player;
		moved = false;
		if (player) {
			System.out.println("White,");
		} else {
			System.out.println("Black,");
		}
		System.out.println("Where would you like to place your piece?");
		xint = TurnScanner.nextInt();
		yint = TurnScanner.nextInt();
		newPiece = new Piece(xint, yint, player);
	}

	public Turn next(Turn current) {
		Turn next = new Turn(!(current.player));
		next.previous = current;
		return next;
	}

	public static boolean move(Piece piece, Board board) {
		boolean movement = board.place(piece);
		
		if (movement) {
			player = !player;
			return true;
		} else {
			System.out.println("Invalid move. Please make another.");
			return false;
		}
	}
}
