import java.util.*;
import java.io.*;
// driver program
public class Reversi {
	
public static Board board = new Board(8); 
	public static void main (String[] args) {
		int white = 0;
		int black = 0;
		//Board board = new Board(8);
		board.startup();
		board.render();
	
		
		//default: player one is white
		while (!board.GameOver() && board.MoveDetection(true) && board.MoveDetection(false)) {

			if(!board.MoveDetection(true) && board.MoveDetection(false))
				System.out.println("White has no moves, it's Black's Turn");
			if(!board.GameOver() && board.MoveDetection(true)) {
				Turn myTurnHuman = new Turn(true);
				while(Turn.move(myTurnHuman.newPiece, board) == false)
				{
					myTurnHuman = new Turn(true);
				}
				board.render();
			}
			if(!board.MoveDetection(false) && board.MoveDetection(true))
				System.out.println("Black has no moves, it's White's Turn");
			else if (!board.MoveDetection(false) && !board.MoveDetection(true))
				System.out.println("No one has any moves, the game is over");
			if(!board.GameOver() && board.MoveDetection(false)) {
				Turn myTurnAI = new Turn(false);
				while(Turn.move(myTurnAI.newPiece, board) == false)
				{
					myTurnAI = new Turn(false);
				}
				board.render();
			}
		}
		if (board.GameOver())
			System.out.println("The board has been filled");
		for (int i = 0; i < board.pieces.length; i++)
		{
			for (int j = 0; j < board.pieces[i].length; j++)
			{
				if (board.pieces[i][j] != null && board.pieces[i][j].color == true)
					white++;
				else if (board.pieces[i][j] != null && board.pieces[i][j].color == false)
					black++;
			}
		}
		if (black>white)
		{
			System.out.println("White: " + white + "  Black: " + black);
			System.out.println("Black Wins!! Nice!!");
		}
		else if (white>black)
		{
			System.out.println("White: " + white + "  Black: " + black);
			System.out.println("White Wins!! Sweet!!");
		}
		else
		{
			System.out.println("White: " + white + "  Black: " + black);
			System.out.println("It's a tie!! Woah!!");
		}
	}
}

class Turn {
	public Piece newPiece;
	Turn previous;
	//true = white, false = black
	public static boolean player;
	boolean moved;
	boolean invalidMove;
	
	public Turn(boolean Player) {
		Scanner TurnScanner = new Scanner(System.in);
		int xint = 0;
		int yint = 0;
		player = Player;
		moved = false;
		if (player) {
			System.out.println("White,");
		} else {
			System.out.println("Black,");
		}
		if (!invalidMove)
			System.out.println("Where would you like to place your piece?");
		else
			invalidMove = false;
		
		try
		{
			xint = TurnScanner.nextInt();
			yint = TurnScanner.nextInt();
			if (xint < 0 || xint > 7 || yint < 0 || yint > 7)
			{
				System.out.println("Not a valid number, Try Again");
				while (xint < 0 || xint > 7 || yint < 0 || yint > 7)
				{
					xint = TurnScanner.nextInt();
					yint = TurnScanner.nextInt();
				}
			}
			newPiece = new Piece(xint, yint, player);
			
		}
		catch (InputMismatchException floyd)
		{
			System.out.println("Error: invalid input. Try again");
			invalidMove = false;
		}
		
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
