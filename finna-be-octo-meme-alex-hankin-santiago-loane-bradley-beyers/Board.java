/* Contains the 2D array used to represent the board as well as most of the logic used to alter game state
 * Also has methods to find whether there are valid moves on the current board or get a list of valid moves
 */

import java.util.ArrayList;

public class Board {

	public Piece[][] pieces;
	public int size;
	public int turncount;
	public boolean validate;
	public int pathlength;
	public int[][] scoreBoard = {{99,	 -8,	 8,	 6,	 6,	 8,	-8,		99},
								 {-8,	-24,	-4,	-3,	-3,	-4,	-24,	-8},
								  {8,	 -4,	 7,	 4,	 4,	 7,	 -4,	 8},
								  {6,	 -3,	 4,	 0,	 0,	 4,	 -3,	 6},
								  {6,	 -3,	 4,	 0,	 0,	 4,	 -3,	 6},
								  {8,	 -4,	 7,	 4,	 4,	 7,	 -4,	 8},
								 {-8,	-24,	-4,	-3,	-3,	-4,	-24,	-8},
								 {99,	 -8, 	 8,	 6,	 6,	 8,	 -8,	99}};
	
	// Creates a SizeXSize board
	public Board(int Size) {
		pieces = new Piece[Size][Size];
		size = Size;
	}
	
	// Alternate contructor for a board which takes an already-created board in the form of its pieces array
	// Used to easily create copies of boards to avoid pointer shenanigans
	public Board(Piece[][] p) {
		this.pieces = p;
	}

	// Places all pieces at their initial positions to start up the game
	public void startup() {
		pieces[size/2][size/2] = new Piece(size/2, size/2, true);
		pieces[(size/2)-1][size/2] = new Piece((size/2)-1, size/2, false);
		pieces[size/2][(size/2)-1] = new Piece(size/2, (size/2)-1, false);
		pieces[(size/2)-1][(size/2)-1] = new Piece((size/2)-1, (size/2)-1, true);
		
		turncount = 0;
	}
	
	// Attempts to place a piece. Returns a boolean representing whether placing said piece results in a valid move
	// doFlip is true if the move should actually be executed and false if we just want to know if the move is valid
	public boolean place(Piece piece, boolean doFlip) { 
		validate = false;
		if (pieces[piece.x][piece.y] == null) {
			check(piece, doFlip);
			if (validate) {
				if (doFlip) pieces[piece.x][piece.y] = piece;
				turncount++;
				return true;
			} else
				return false;
		} else
			return false;
	}
	
	// Tells checkLine which direction to look in when making a move
	// doFlip is passed in from place, and again decides if a move is actually executed or not
	public boolean check(Piece piece, boolean doFlip) {
		int dx = 0, dy = 0;
		int i = 0;
		boolean valid = false;
		for(i = 1; i<=8; i++) { //check in a line from the piece in each direction
			switch(i) {
				case 1: dx = 0; 
						dy = 1;
						break;
				case 2:
						dx = 0;
						dy = -1;
						break;
				case 3:
						dx = 1;
						dy = 0;
						break;
				case 4:
						dx = -1;
						dy = 0;
						break;
				case 5:
						dx = -1;
						dy = 1;
						break;
				case 6:
						dx = 1;
						dy = 1;
						break;
				case 7:
						dx = -1;
						dy = -1;
						break;
				case 8:
						dx = 1;
						dy = -1;
						break;
			}
			pathlength = 0;
			boolean temp = checkLine(piece, dx, dy, doFlip); // check down the line for each piece as listed above
			if (temp)
				valid = true;
			
		}
		return valid; //if valid = true, then it's a valid move and at least one piece has been flipped
	}

	// checks in a straight line to find if the new placement sandwiches pieces together
	// return true if it is a valid placement to flip pieces
	// flips relevant pieces if doFlip (passed down from place) is true
	public boolean checkLine(Piece piece, int dx, int dy, boolean doFlip) {
		pathlength++; 
		Piece temp = piece.copy();
		// Make sure we're not going out of bounds or testing a piece that doesn't exist
		if((temp.x + dx >= 0 || dx == 0) && (temp.y + dy >= 0 || dy == 0) && (temp.x + dx < pieces.length || dx == 0) && (temp.y + dy < pieces.length || dy == 0) && pieces[temp.x + dx][temp.y + dy] != null) {
			temp.move(temp.x + dx,  temp.y + dy);
			if (pieces[temp.x][temp.y].color == piece.color) { // if piece i'm currently looking at is of the same color as the original piece
				if (pathlength >= 1) // if it has at least one other piece between the two, it's valid
					return true;
				else return false; //otherwise invalid
			} else {
				if (checkLine(temp, dx, dy, doFlip)) { //recursively see if it eventually makes a valid chain
					if(pieces[temp.x][temp.y] == null) //if there's a gap in the middle, move isn't valid
						return false; 
					else { //otherwise valid, flip current piece
						validate = true;
						if (doFlip) pieces[temp.x][temp.y].flip();
						return true;
					}
				} else 
					return false; // isn't a valid chain
			}
		} else 
			return false;
	}

	// Prints out the board with labeled rows / columns (used primarily for testing)
	public void render() {
		System.out.println("   0   1   2   3   4   5   6   7  ");
		for(int i = 0; i< pieces.length; i++) {
			System.out.print(" " + i);
			for(int j=0; j<pieces[i].length; j++) {
				if (pieces[i][j] == null)
					System.out.print(" -  ");
				else System.out.print(" " + pieces[i][j].toString() + "  ");
			}
			System.out.println();
			System.out.println();
		}
		System.out.println("------------------------------");
	}
	
	// Detects whether the board is full
	public boolean GameOver() {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				if (pieces[i][j] == null)
					return false;
			}
		}
		return true;
	}
	
	// Detects whether the given player has any valid moves
	public boolean MoveDetection(boolean color) {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				Piece PieceTester = new Piece(i,j,color);
				if (place(PieceTester, false))
					return true;
			}
		}
		return false;
	}

	// Returns an ArrayList with all the valid moved for the player passed in (again, each move is coded as [X, Y])
	public ArrayList<int[]> getValidMoves(boolean color) {
		ArrayList<int[]> out = new ArrayList<int[]>(0);

		for (int i = 0; i < pieces.length; i++) { // For each space on the board...
			for (int j = 0; j < pieces[i].length; j++) {
				Piece PieceTester = new Piece(i,j,color);
				if (place(PieceTester, false)) { // If placing a piece here would result in a valid move...
					int[] newMove = {i, j};
					out.add(newMove); // Add the coordinates of the move to the output list
				}
			}
		}
		
		return out;
	}

	// Takes in an array representing a move with the form [X, Y] and the player making the move, the returns the resulting board
	public Board child(int[] move, boolean color) {
		Board temp = this.copy();
		temp.place(new Piece(move[0], move[1], color), true);
		return temp;
	}

	// Simply returns a copy of the board (used to avoid pointer shenanigans)
	public Board copy() { 
		Piece[][] c = new Piece[pieces.length][pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces.length; j++) {
				if (pieces[i][j] == null) {
					c[i][j] = null;
				} else if (pieces[i][j].color) {
					c[i][j] = new Piece(i,j,true);
				} else {
					c[i][j] = new Piece(i,j,false);
				}
			}
		}
		return new Board(c);
	}

	// Heuristic function which returns a score for the current board for a given player according to who owns which spaces
	public int h(boolean player) {
		int score = 0;
		for (int i = 0; i < pieces.length; i++) { // For each space on the board...
			for (int j = 0; j < pieces.length; j++) {
				if (pieces[i][j] != null && pieces[i][j].color == player) { // If the space belongs to the player passed in...
					score += scoreBoard[i][j]; // Add the heuristic value of the current space to the score
				} else if (pieces[i][j] != null && pieces[i][j].color != player) { // If it belongs to the opponent...
					score -= scoreBoard[i][j]; // Subtract the heuristic value of the current space from the score
				}
			}
		}
		return score;
	}
}