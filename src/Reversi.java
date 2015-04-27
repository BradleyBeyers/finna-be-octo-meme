import java.util.*;
import java.io.*;
// driver program
public class Reversi {
	public final static boolean WHITE = true;
	public final static boolean BLACK = false;
	public static int depthLim;
	
	public static Board board = new Board(8);

	public static void main (String[] args) {
		gameAI();
	}

	public static void gameAI() {
		Board currBoard = new Board(8);
		boolean currPlayer = false;
		int timeLim;
		int totalTimeLim;
		currBoard.startup();

		Scanner GameScanner = new Scanner(System.in); //Parse initial input to start up the game
		String init = GameScanner.nextLine();
		String[] initSplit = init.split(" ");
		depthLim = Integer.valueOf(initSplit[2]);
		timeLim = Integer.valueOf(initSplit[3]);
		totalTimeLim = Integer.valueOf(initSplit[4]);

		if (initSplit[1].equals("B")) {
			currPlayer = BLACK;
		} else if (initSplit[1].equals("W")) {
			currPlayer = WHITE;
		}

		while (true) {
			int[] nextMove = alphaBeta(currBoard, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, currPlayer)[1];
			currBoard.place(new Piece(nextMove[0], nextMove[1], currPlayer), true);
			currBoard.render();
			currPlayer = !currPlayer;
			int humanMoveX = GameScanner.nextInt();
			int humanMoveY = GameScanner.nextInt();
			currBoard.place(new Piece(humanMoveY, humanMoveX, currPlayer), true);
			currPlayer = !currPlayer;
		}
	}

	public static int[][] alphaBeta(Board state, int depth, int alpha, int beta, boolean player) {
		int a = alpha;
		int b = beta;
		int value;
		int[] bestMove = {-1, -1};
		int[][] retvalue = {{state.getValue()}, bestMove};
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>(0);
		if (depth == depthLim) return retvalue;

		if (player == WHITE) {
			possibleMoves = state.getValidMoves(WHITE);
			for (int[] move : possibleMoves) {
				Board child = state.child(move, WHITE);
				value = alphaBeta(child, depth + 1, a, b, BLACK)[0][0];
				if (value > a) {
					a = value;
					bestMove = move;
				}
				if (beta <= alpha) break;
			}
			retvalue[0][0] = a;
			retvalue[1] = bestMove;
			return retvalue;
		} else {			
			possibleMoves = state.getValidMoves(BLACK);
			for (int[] move : possibleMoves) {
				Board child = state.child(move, BLACK);
				value = alphaBeta(child, depth + 1, a, b, BLACK)[0][0];
				if (value < b) {
					b = value;
					bestMove = move;
				}
				if (beta <= alpha) break;
			}
			retvalue[0][0] = a;
			retvalue[1] = bestMove;
			return retvalue;
		}
	}

	public static void startGame() {
		int white = 0;
		int black = 0;
		//Board board = new Board(8);
		board.startup();
		board.render();
	
		
		//default: player one is white
		while (!board.GameOver() && (board.MoveDetection(true) || board.MoveDetection(false))) {

			if(!board.MoveDetection(WHITE) && board.MoveDetection(BLACK))
				System.out.println("White has no moves, it's Black's Turn");
			if(!board.GameOver() && board.MoveDetection(WHITE)) {
				Turn myTurnHuman = new Turn(WHITE);
				while(Turn.move(myTurnHuman.newPiece, board) == false)
				{
					myTurnHuman = new Turn(WHITE);
				}
				board.render();
			}
			if(!board.MoveDetection(BLACK) && board.MoveDetection(WHITE))
				System.out.println("Black has no moves, it's White's Turn");
			else if (!board.MoveDetection(BLACK) && !board.MoveDetection(WHITE))
				System.out.println("No one has any moves, the game is over");
			if(!board.GameOver() && board.MoveDetection(BLACK)) {
				Turn myTurnAI = new Turn(BLACK);
				while(Turn.move(myTurnAI.newPiece, board) == false)
				{
					myTurnAI = new Turn(BLACK);
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
				if (board.pieces[i][j] != null && board.pieces[i][j].color == WHITE)
					white++;
				else if (board.pieces[i][j] != null && board.pieces[i][j].color == BLACK)
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
			newPiece = new Piece(yint, xint, player);
			
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
		boolean movement = board.place(piece, true);

		if (movement) {
			player = !player;
			return true;
		} else {
			System.out.println("Invalid move. Please make another.");
			return false;
		}
	}
}
