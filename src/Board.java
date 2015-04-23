
public class Board {

	public Piece[][] pieces;
	public int size;
	public int turncount;
	
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
	public boolean place(Piece piece)
	{
		if (pieces[piece.x][piece.y] == null)
		{
			pieces[piece.x][piece.y] = piece;
			check(piece);
			return true;
		}
		else return false;
	}
	public void check(Piece piece)
	{
		int dx = 0, dy = 0;
		int i = 0;
		for(i = 1; i<=8; i++);
		{
			
			switch(i)
			{
				case 1: dx = 0; 
						dy = 1;
				case 2:
						dx = 0;
						dy = -1;
				case 3:
						dx = 1;
						dy = 0;
				case 4:
						dx = -1;
						dy = 0;
				case 5:
						dx = -1;
						dy = 1;
				case 6:
						dx = 1;
						dy = 1;
				case 7:
						dx = -1;
						dy = -1;
				case 8:
						dx = 1;
						dx = -1;
			}
			checkLine(piece, dx, dy);
		}
	}
	public boolean checkLine(Piece piece, int dx, int dy)
	{
		Piece temp = piece;
		if(pieces[temp.x + dx][temp.y + dy] != null)
		{
			temp.move(temp.x + dx,  temp.y + dy);
			if (pieces[temp.x][temp.y].color == piece.color)
					return true;
			else
				{
					if (checkLine(temp, dx, dy))
					{
						pieces[temp.x][temp.y].color = !pieces[temp.x][temp.y].color;
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
		for(int i = 0; i< pieces.length; i++)
		{
			for(int j=0; j<pieces[i].length; j++)
			{
				if (pieces[i][j] == null)
					System.out.print("-   ");
				else System.out.print(pieces[i][j].toString() + "   ");
			}
			System.out.println();
			System.out.println();
		
		}
		System.out.println("------------------------------");
	}
	public void addPiece(Piece piece)
	{
		
	}
}
