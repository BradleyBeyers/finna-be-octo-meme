import java.util.*;
import java.io.*;
// driver program
public class Reversi {
	public final static boolean WHITE = true;
	public final static boolean BLACK = false;
	public static int depthLim;
	public static int timeLim;
	public static int totalTimeLim;
	public static int exploredStates;
	public static int white = 0;
	public static int black = 0;
	public static Board currBoard;
	public static boolean AiPlayer = false;
	
	//public static Board board = new Board(8);

	public static void main (String[] args) 
	{
		System.out.println("Skynet Online.");
		gameAI();
		System.out.println("Skynet Offline.");
		
		for (int i = 0; i < currBoard.pieces.length; i++)
		{
			for (int j = 0; j < currBoard.pieces[i].length; j++)
			{
				if (currBoard.pieces[i][j] != null && currBoard.pieces[i][j].color == WHITE)
					white++;
				else if (currBoard.pieces[i][j] != null && currBoard.pieces[i][j].color == BLACK)
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

	public static void gameAI() {
		currBoard = new Board(8); //Create a board and declare needed variables for alphabeta
		boolean currPlayer = false;
		currBoard.startup();

		Scanner GameScanner = new Scanner(System.in); //Parse initial input to start up the game
		String init = GameScanner.nextLine();
		String[] initSplit = init.split(" ");
		depthLim = Integer.valueOf(initSplit[2]);
		timeLim = Integer.valueOf(initSplit[3]);
		totalTimeLim = Integer.valueOf(initSplit[4]);

		boolean human = false;

		if (initSplit[1].equals("B")) {
			currPlayer = BLACK;
			AiPlayer = BLACK;
			human = false;
		} else if (initSplit[1].equals("W")) {
			currPlayer = WHITE;
			AiPlayer = WHITE;
			human = true;
			currBoard.render();
		}

		boolean cont = true;

		while (cont) { // Loops forever for now, later will have logic to detect gamestate and end when appropriate

			//to prevent turn skipping if the player tries to enter an invalid move
			if (!human) {
				if (!currBoard.MoveDetection(currPlayer))
				{
					human = true;
					System.out.println("No valid moves for computer player");
				}
				else
				{
				exploredStates = 0;
				long startTime = System.currentTimeMillis(); // Measures the time before the move is found
				int[] nextMove = alphaBeta(currBoard.copy(), 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, currPlayer)[1]; // AI always goes first (for now)
				long endTime = System.currentTimeMillis(); // Measures the time after the move is found
				long timeDiff = endTime - startTime; // Takes the difference between the two times to determine how long the move took
				System.out.println("Took " + timeDiff + "ms to find move and explored " + exploredStates + " states");

				if (nextMove != null && nextMove[0] != -1 && nextMove[1] != -1) {
					currBoard.place(new Piece(nextMove[0], nextMove[1], currPlayer), true);
					currBoard.render();
				}
				else
					System.out.println("No available moves.");
				}
			}
			currPlayer = !currPlayer;
			human = true;

			if (!currBoard.MoveDetection(currPlayer))
			{
				human = false;
				System.out.println("No valid moves for human player");
			}
			else
			{
				int humanMoveX = GameScanner.nextInt();
				int humanMoveY = GameScanner.nextInt();

				if (humanMoveX < 0 || humanMoveX > 7 || humanMoveY < 0 || humanMoveY > 7)
				{
					while (humanMoveX < 0 || humanMoveX > 7 || humanMoveY < 0 || humanMoveY > 7)
					{
						System.out.println("Not a valid number, Try Again");
						humanMoveX = GameScanner.nextInt();
						humanMoveY = GameScanner.nextInt();
					}
				}
				else
				{

					//returns true if valid placement
					if (currBoard.place(new Piece(humanMoveY, humanMoveX, currPlayer), true)) {
						human = false;
					}

					else {
						while (human)
						{
							System.out.println("Invalid move, Enter another.");
							humanMoveX = GameScanner.nextInt();
							humanMoveY = GameScanner.nextInt();
							if (currBoard.place(new Piece(humanMoveY, humanMoveX, currPlayer), true)) {
								human = false;
							}
							else
								human = true;
						}
					}
				}
			}
			currPlayer = !currPlayer;

			//checks for move availability in both parties, ends game if no available moves
			if (currBoard.MoveDetection(currPlayer) || currBoard.MoveDetection(!currPlayer)) {
				cont = true;
			}
			else {
				System.out.println("Game Over. No available moves");
				cont = false;

			}
			if (currBoard.GameOver())
			{
				System.out.println("Game Over. Board is Filled");
				cont = false;
			}
		}
	}

	public static int[][] alphaBeta(Board state, int depth, int alpha, int beta, boolean player) {
		// state.render();
		int a = alpha;
		int b = beta;
		int value;
		Board child;
		int[] bestMove = {-1, -1};
		int[][] retvalue = {{state.h(player)}, bestMove}; // Moves are coded as an integer array of the form [X, Y]
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>(0);
		if (depth == depthLim) return retvalue;

		if (player == WHITE) {
			possibleMoves = state.getValidMoves(WHITE);
			for (int[] move : possibleMoves) {
				exploredStates++;
				child = state.child(move, WHITE);
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
				exploredStates++;
				child = state.child(move, BLACK);
				value = alphaBeta(child, depth + 1, a, b, WHITE)[0][0];
				if (value < b) {
					b = value;
					bestMove = move;
				}
				if (beta <= alpha) break;
			}
			retvalue[0][0] = b;
			if (bestMove[0] == -1 && bestMove[1] == -1)
				retvalue[1] = null;
			else 
				retvalue[1] = bestMove;
			return retvalue;
		}
	}

	/*public static void startGame() {
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
	}*/
}

// Turn framework, performs all the functions of a turn
// Generates successor board state.
// AI players CAN use this!!!!
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
