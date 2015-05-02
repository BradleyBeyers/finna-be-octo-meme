/* Program that drives the AI and handles turn flow control for the game.
 */

import java.util.*;
import java.io.*;

public class Reversi {
	public static final boolean WHITE = true; // Used sometimes for clarity instead of true
	public static final boolean BLACK = false; // Used sometimes for clarity instead of false
	public static int depthLim;
	public static int timeLim;
	public static int totalTimeLim;
	public static Board currBoard;
	public static boolean AiPlayer = false;
	public static boolean HumanPlayer = false;
	public static long startTime;
	public static long endTime;
	public static long elapsed;

	public static void main (String[] args) 
	{
		gameAI();
	}

	// Contains the code for our AI
	public static void gameAI() {
		currBoard = new Board(8); //Create a board and declare needed variables for alphabeta
		currBoard.startup();

		boolean currPlayer = BLACK; // Represents who the current player is (true for white, false for black)
		boolean gameOver = false;
		int[] nextMove = null;
		int[] newMove = null;

		Scanner GameScanner = new Scanner(System.in); //Scanner to parse the initial input that starts the game
		String init = GameScanner.nextLine();
		String[] initSplit = init.split(" ");

		depthLim = Integer.valueOf(initSplit[2]);
		timeLim = Integer.valueOf(initSplit[3]);
		totalTimeLim = Integer.valueOf(initSplit[4]);

		if (totalTimeLim != 0) {
			timeLim = totalTimeLim / 64;
		} else if (timeLim != 0) {
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

		// Turn flow control as well as the AI handled here
		while (true) { 
			currBoard.render();

			//checks for move availability in both parties, ends game if no available moves
			if ((!currBoard.MoveDetection(WHITE) && !currBoard.MoveDetection(BLACK)) || currBoard.GameOver()) {
				gameOver = true;
				String gameEnd = GameScanner.nextLine();
				if (gameEnd != null) {
					System.out.println("pass");
				}
			}

			// Handles the AI's turn
			if (currPlayer == AiPlayer && !gameOver) { // If it's the AI's turn...
				if (!currBoard.MoveDetection(AiPlayer)) { // If the AI has no moves...
					System.out.println("pass");
				} else {
					if (timeLim != 0) { // If we've got a time limit...
						depthLim = 0; // Reset the depth limit
						nextMove = null;
						startTime = System.currentTimeMillis(); // Measure the time before the move is found
						endTime = System.currentTimeMillis();
						elapsed = endTime - startTime;
						while (elapsed + 50 < timeLim) { // Perform iterative deepening with alphabeta to find the move. Cuts off if within 50ms of the time limit
							nextMove = newMove; // Since alphabeta sometimes returns before the depth limit is reached, we store the current best move here
							newMove = alphaBeta(currBoard.copy(), 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, AiPlayer)[1]; // Attempt to find a new best move with the new depth limit
							depthLim++;
						}
					} else { // Otherwise, we have a depth limit
						elapsed = -Integer.MAX_VALUE; // Set the elapsed time to the lowest possible value to avoid triggering the time limit cutoff
						nextMove = alphaBeta(currBoard.copy(), 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, AiPlayer)[1];
					}
					currBoard.place(new Piece(nextMove[0], nextMove[1], AiPlayer), true);
					System.out.println(nextMove[1] + " " +nextMove[0]);
				}
			} else if (currPlayer == HumanPlayer && !gameOver) { // Handles the human's (opponent's) turn
				boolean validMove = false;
				while (!validMove) { // If the opponent hasn't given a valid move yet...
					String humanMove = GameScanner.nextLine(); // Read the opponent's input
					if (!humanMove.equals("pass")) { // If they didn't pass...
						String[] humanMoveSplit = humanMove.split(" "); // Parse opponent's move
						int humanMoveX = Integer.valueOf(humanMoveSplit[0]);
						int humanMoveY = Integer.valueOf(humanMoveSplit[1]);

						if (humanMoveX < 0 || humanMoveX > 7 || humanMoveY < 0 || humanMoveY > 7) { // Don't let the opponent play out of bounds
							validMove = false;
						} else { // Check if the move that was given is valid
							if (currBoard.place(new Piece(humanMoveY, humanMoveX, HumanPlayer), true)) {
								validMove = true;
							}
						}
					}
				}
			}
			currPlayer = !currPlayer;
		}
	}

	public static int[][] alphaBeta(Board state, int depth, int alpha, int beta, boolean player) {
		int a = alpha;
		int b = beta;
		int value;
		Board child;
		int[] bestMove = {-1, -1}; // Moves are coded as an integer array of the form [X, Y]
		int[][] retvalue = {{state.h(AiPlayer)}, bestMove};
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>(0);

		if (timeLim != 0) { // If there's a time limit, get the current elapsed time
			endTime = System.currentTimeMillis();
			elapsed = endTime - startTime;
		}

		if (depth == depthLim || elapsed + 50 > timeLim) return retvalue; // If we're at the depth limit or we're within a 50ms buffer time of the time limit, return

		if (player == AiPlayer) { // The AI is always treated as the max player for this situation
			possibleMoves = state.getValidMoves(AiPlayer);
			for (int[] move : possibleMoves) { // For each possible move...
				child = state.child(move, AiPlayer); // Generate the result of playing that move
				value = alphaBeta(child, depth + 1, a, b, !AiPlayer)[0][0]; // Recursively find the heuristic value of that move
				if (value > a) { // Perform alpha-beta pruning
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
			for (int[] move : possibleMoves) { // For each possible move...
				child = state.child(move, !AiPlayer); // Generate the result of playing that move
				value = alphaBeta(child, depth + 1, a, b, AiPlayer)[0][0]; // Recursively find the heuristic value of that move
				if (value < b) { // Perform alpha-beta pruning
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