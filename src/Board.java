

public class Board {

	public Piece[][] pieces;
	public int size;
	public int turncount;
	public boolean validate;
	
	public Board(int Size)
	{
		pieces = new Piece[Size][Size];
		size = Size;
	}
	public void startup()
	{
		pieces[size/2][size/2] = new Piece(size/2, size/2, true);
		pieces[(size/2)-1][size/2] = new Piece((size/2)-1, size/2, false);
		pieces[size/2][(size/2)-1] = new Piece(size/2, (size/2)-1, false);
		pieces[(size/2)-1][(size/2)-1] = new Piece((size/2)-1, (size/2)-1, true);
		
		turncount = 0;
	}
	
	public boolean place(Piece piece) {
		validate = false;
		if (pieces[piece.x][piece.y] == null)
		{
			check(piece);
			if (validate)
			{
				pieces[piece.x][piece.y] = piece;
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
	public boolean check(Piece piece)
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
			boolean temp = checkLine(piece, dx, dy);
			if (temp)
				valid = true;
		}
		//if valid = true, then it's a valid move and at least one piece has been flipped
		return valid;
	}
	// checks in a straight line to find if the new placement sandwiches pieces together, and converts them if necessary.
	// return true if it is a valid placement to flip pieces
	public boolean checkLine(Piece piece, int dx, int dy)
	{
		Piece temp = piece.copy();
		if((temp.x + dx) > 0 && (temp.y + dy > 0) && (temp.x + dx) < pieces.length && (temp.y + dy < pieces.length))
		{
			temp.move(temp.x + dx,  temp.y + dy);
			if ((pieces[temp.x][temp.y] != null) && (pieces[temp.x][temp.y].color == piece.color))
					return true;
			else
				{
					if (checkLine(temp, dx, dy))
					{
						validate = true;
						pieces[temp.x][temp.y].flip();
						return true;
					}
					else return false;
				}
		}
		else return false;
	}
	public void render()
	{
		//System.out.println("Size: " + size);
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
}
