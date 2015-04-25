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
				Turn.move(myTurnHuman.newPiece, board);
				board.render();
			}

			if(!GAMEOVER) {
				new Turn(false);
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
			System.out.println("white");
		} else {
			System.out.println("black");
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
