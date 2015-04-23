
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
	public void render()
	{
		//System.out.println("Size: " + size);
		System.out.println("Turn: " + turncount);
		for(int i = 0; i< pieces.length; i++)
		{
			for(int j=0; j<pieces[i].length; j++)
			{
				if (pieces[i][j] == null)
					System.out.print("-\t");
				else System.out.print(pieces[i][j].toString() + "\t");
			}
			System.out.println();
		}
		System.out.println("---------------------------------------------------------");
	}
}
