import java.util.ArrayList;

public class Board {

	public Piece[][] pieces;
	public int size;
	public int turncount;
	public boolean validate;
	public int pathlength;
	
	public Board(int Size)
	{
		pieces = new Piece[Size][Size];
		size = Size;
	}

	public Board(Piece[][] p) {
		this.pieces = p;
	}

	public void startup()
	{
		pieces[size/2][size/2] = new Piece(size/2, size/2, true);
		pieces[(size/2)-1][size/2] = new Piece((size/2)-1, size/2, false);
		pieces[size/2][(size/2)-1] = new Piece(size/2, (size/2)-1, false);
		pieces[(size/2)-1][(size/2)-1] = new Piece((size/2)-1, (size/2)-1, true);
		
		turncount = 0;
	}
	
	public boolean place(Piece piece, boolean doFlip) {
		validate = false;
		if (pieces[piece.x][piece.y] == null)
		{
			check(piece, doFlip);
			if (validate)
			{
				if (doFlip) pieces[piece.x][piece.y] = piece;
				turncount++;
				return true;
			}
			else
				return false;
		}
		else return false;
	}
	
	//checks from the piece AND SWITCHES VALID PIECES
	//not a checker, more of a move() function
	public boolean check(Piece piece, boolean doFlip)
	{
		int dx = 0, dy = 0;
		int i = 0;
		boolean valid = false;
		for(i = 1; i<=8; i++)
		{
			//check in a line from the piece in each direction 
			switch(i)
			{
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
			// check down the line for each piece as listed above
			pathlength = 0;
			boolean temp = checkLine(piece, dx, dy, doFlip);
			if (temp)
				valid = true;
			
		}
		//if valid = true, then it's a valid move and at least one piece has been flipped
		return valid;
	}
	// checks in a straight line to find if the new placement sandwiches pieces together, and converts them if necessary.
	// return true if it is a valid placement to flip pieces
	public boolean checkLine(Piece piece, int dx, int dy, boolean doFlip)
	{
		pathlength++; 
		Piece temp = piece.copy();
		if((temp.x + dx >= 0 || dx == 0) && (temp.y + dy >= 0 || dy == 0) && (temp.x + dx < pieces.length || dx == 0) && (temp.y + dy < pieces.length || dy == 0) && pieces[temp.x + dx][temp.y + dy] != null)
		{
			temp.move(temp.x + dx,  temp.y + dy);
			// if piece i'm currently looking at exists and is of the same color as the original piece
			if ((pieces[temp.x][temp.y] != null) && (pieces[temp.x][temp.y].color == piece.color))
			{
				// if it has at least one other piece between the two, it's valid
				if (pathlength >= 1)
					return true;
				//otherwise false
				else return false;
			}
			else
				{
				//recursively see if it eventually makes a valid chain
					if (checkLine(temp, dx, dy, doFlip))
					{
						//validate = true;
						//if there's a gap in the middle
						if(pieces[temp.x][temp.y] == null)
							return false;
						//otherwise valid, flip current piece
						else
						{
							validate = true;
							if (doFlip) pieces[temp.x][temp.y].flip();
							return true;
						}
					}
					// isn't a valid chain
					else return false;
				}
		}
		else return false;
	}

	public void render()
	{
		System.out.println("Turn: " + turncount);
		System.out.println("   0  1  2  3  4  5  6  7  ");
		for(int i = 0; i< pieces.length; i++) {
			System.out.print(" " + i);
			for(int j=0; j<pieces[i].length; j++) {
				if (pieces[i][j] == null)
					System.out.print(" - ");
				else System.out.print(" " + pieces[i][j].toString() + " ");
			}
			System.out.println();
			System.out.println();
		}
		System.out.println("------------------------------");
	}
	
	public boolean GameOver()
	{
		for (int i = 0; i < pieces.length; i++)
		{
			for (int j = 0; j < pieces[i].length; j++)
			{
				if (pieces[i][j] == null)
					return false;
			}
		}
		return true;
	}
	
	public boolean MoveDetection(boolean color)
	{
		for (int i = 0; i < pieces.length; i++)
		{
			for (int j = 0; j < pieces[i].length; j++)
			{
				Piece PieceTester = new Piece(i,j,color);
				if (place(PieceTester, false))
					return true;
			}
		}
		return false;
	}

	public ArrayList<int[]> getValidMoves(boolean color) {
		ArrayList<int[]> out = new ArrayList<int[]>(0);

		for (int i = 0; i < pieces.length; i++)
		{
			for (int j = 0; j < pieces[i].length; j++)
			{
				Piece PieceTester = new Piece(i,j,color);
				if (place(PieceTester, false)) {
					int[] newMove = {i, j};
					out.add(newMove);
				}
			}
		}
		
		return out;
	}

	public Board copy() {
		Piece[][] c = new Piece[pieces.length][pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces.length; j++) {
				c[i][j] = pieces[i][j];
			}
		}
		return new Board(c);
	}
}