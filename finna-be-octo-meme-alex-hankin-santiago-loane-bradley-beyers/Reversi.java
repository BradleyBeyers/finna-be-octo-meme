import java.util.*;
import java.io.*;
// driver program
public class Reversi {
	public final static boolean WHITE = true; // Used sometimes for clarity instead of true
	public final static boolean BLACK = false; // Used sometimes for clarity instead of false
	public static int depthLim;
	public static int timeLim;
	public static int totalTimeLim;
	public static int exploredStates;
	public static int white = 0;
	public static int black = 0;
	public static Board currBoard;
	public static boolean AiPlayer = false;
	public static boolean HumanPlayer = false;
	public static long startTime;
	public static long endTime;
	public static long elapsed;
	
	//public static Board board = new Board(8);

	public static void main (String[] args) 
	{
		System.out.println("Skynet Online.");
		gameAI();
		System.out.println("Skynet Offline.");
		
		/*for (int i = 0; i < currBoard.pieces.length; i++)
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
		}*/
	}

	public static void gameAI() {
		currBoard = new Board(8); //Create a board and declare needed variables for alphabeta
		currBoard.startup();

		Scanner GameScanner = new Scanner(System.in); //Scanner to parse the initial input that starts the game
		String init = GameScanner.nextLine();
		String[] initSplit = init.split(" ");
		boolean currPlayer = BLACK; // Represents who the current player is (true for white, false for black)
		int[] nextMove = null;
		int[] newMove = null;
		depthLim = Integer.valueOf(initSplit[2]);
		timeLim = Integer.valueOf(initSplit[3]);
		totalTimeLim = Integer.valueOf(initSplit[4]);

		if  (timeLim != 0) {
			depthLim = 0;
		} else if (depthLim == 0) { // Depth limit defaults to 3 if there is no time limit or depth limit specified
			depthLim = 3;
		}

		if (initSplit[1].equals("B")) {
			AiPlayer = BLACK;
			HumanPlayer = WHITE;
		} else if (initSplit[1].equals("W")) {
			AiPlayer = WHITE;
			HumanPlayer = BLACK;
		}

		boolean cont = true;

		while (cont) { 
			currBoard.render();
			//to prevent turn skipping if the player tries to enter an invalid move
			if (currPlayer == AiPlayer) {
				if (!currBoard.MoveDetection(AiPlayer)) {
					System.out.println("No valid moves for computer player");
				}
				else {
					exploredStates = 0;
				
					if (timeLim != 0) { // If we've got a time limit...
						depthLim = 0; // Reset the depth limit
						nextMove = null;
						startTime = System.currentTimeMillis(); // Measure the time before the move is found
						endTime = System.currentTimeMillis();
						elapsed = endTime - startTime;
						while (elapsed + 50 < timeLim) { // Perform iterative deepening with alphabeta to find the move. Cuts off if within 50ms of the time limit
							nextMove = newMove; // Since alphabeta sometimes returns before the depth limit is reached, we store the current best move here
							newMove = alphaBeta(currBoard.copy(), 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, AiPlayer)[1];
							depthLim++;
						}
						System.out.println("Took " + elapsed + "ms to find move and explored " + exploredStates + " states to a depth of " +  depthLim);
					} else { // Otherwise, we have a depth limit
						elapsed = -Integer.MAX_VALUE; // Set the elapsed time to the lowest possible value to avoid triggering the time limit cutoff
						nextMove = alphaBeta(currBoard.copy(), 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, AiPlayer)[1];
					}

					if(currBoard.MoveDetection(AiPlayer) && nextMove != null) {
						//if (nextMove != null && nextMove[0] != -1 && nextMove[1] != -1) {
							currBoard.place(new Piece(nextMove[0], nextMove[1], AiPlayer), true);
						//}
					}
					else
						System.out.println("No available moves.");
				}
			} else if (currPlayer == HumanPlayer) {

				if (!currBoard.MoveDetection(HumanPlayer)) {
					System.out.println("No valid moves for human player");
				}
				else {
					int humanMoveX = GameScanner.nextInt();
					int humanMoveY = GameScanner.nextInt();

					if (humanMoveX < 0 || humanMoveX > 7 || humanMoveY < 0 || humanMoveY > 7) {
						while (humanMoveX < 0 || humanMoveX > 7 || humanMoveY < 0 || humanMoveY > 7) {
							System.out.println("Not a valid number, Try Again");
							humanMoveX = GameScanner.nextInt();
							humanMoveY = GameScanner.nextInt();
						}
					}
					else {
						boolean validMove = false;
						//returns true if valid placement
						if (currBoard.place(new Piece(humanMoveY, humanMoveX, HumanPlayer), true)) {
							validMove = false;
						} else {
							while (!validMove) {
								System.out.println("Invalid move, Enter another.");
								humanMoveX = GameScanner.nextInt();
								humanMoveY = GameScanner.nextInt();
								if (currBoard.place(new Piece(humanMoveY, humanMoveX, HumanPlayer), true)) {
									validMove = true;
								}
								else
									validMove = false;
							}
						}
					}
				}
			}

			currPlayer = !currPlayer;

			//checks for move availability in both parties, ends game if no available moves
			if (currBoard.MoveDetection(currPlayer) || currBoard.MoveDetection(!currPlayer)) {
				cont = true;
			} else if (currBoard.GameOver()) {
					System.out.println("Game Over. Board is Filled");
					cont = false;
			} else {
				System.out.println("Game Over. No available moves");
				cont = false;

			}
		}

		for (int i = 0; i < currBoard.pieces.length; i++) {
			for (int j = 0; j < currBoard.pieces[i].length; j++) {
				if (currBoard.pieces[i][j] != null && currBoard.pieces[i][j].color == WHITE)
					white++;
				else if (currBoard.pieces[i][j] != null && currBoard.pieces[i][j].color == BLACK)
					black++;
			}
		}
		
		if (black>white) {
			System.out.println("White: " + white + "  Black: " + black);
			System.out.println("Black Wins!! Nice!!");
		}
		else if (white>black) {
			System.out.println("White: " + white + "  Black: " + black);
			System.out.println("White Wins!! Sweet!!");
		}
		else {
			System.out.println("White: " + white + "  Black: " + black);
			System.out.println("It's a tie!! Woah!!");
		}
	}

	public static int[][] alphaBeta(Board state, int depth, int alpha, int beta, boolean player) {
		// state.render();
		int a = alpha;
		int b = beta;
		int value;
		Board child;
		int[] bestMove = {-1, -1};
		int[][] retvalue = {{state.h(AiPlayer)}, bestMove}; // Moves are coded as an integer array of the form [X, Y]
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>(0);

		if (timeLim != 0) { // If there's a time limit, get the current elapsed time
			endTime = System.currentTimeMillis();
			elapsed = endTime - startTime;
		}

		if (depth == depthLim || elapsed + 50 > timeLim) return retvalue; // If we're at the depth limit or we're within a 50ms buffer time of the time limit, return

		if (player == AiPlayer) {
			possibleMoves = state.getValidMoves(AiPlayer);
			for (int[] move : possibleMoves) {
				exploredStates++;
				child = state.child(move, AiPlayer);
				value = alphaBeta(child, depth + 1, a, b, !AiPlayer)[0][0];
				if (value > a) {
					a = value;
					bestMove = move;
				}
				if (beta <= alpha)
					break;
			}
			retvalue[0][0] = a;
			if (bestMove[0] == -1 && bestMove[1] == -1)
				retvalue[1] = null;
			else 
				retvalue[1] = bestMove;
			return retvalue;
		} else {
			possibleMoves = state.getValidMoves(!AiPlayer);
			for (int[] move : possibleMoves) {
				exploredStates++;
				child = state.child(move, !AiPlayer);
				value = alphaBeta(child, depth + 1, a, b, AiPlayer)[0][0];
				if (value < b) {
					b = value;
					bestMove = move;
				}
				if (beta <= alpha)
					break;
			}
			retvalue[0][0] = b;
			if (bestMove[0] == -1 && bestMove[1] == -1)
				retvalue[1] = null;
			else 
				retvalue[1] = bestMove;
			return retvalue;
		}
	}
}