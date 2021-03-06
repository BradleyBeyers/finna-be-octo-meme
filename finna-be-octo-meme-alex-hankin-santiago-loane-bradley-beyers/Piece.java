/* Very small class used to represent pieces on the board. Most methods should be entirely self-explanatory
 */

public class Piece {

	public int x;
	public int y;
	// white = true, black = false
	public boolean color;
	
	public Piece (int xPos, int yPos, boolean Color) {
		x = xPos;
		y = yPos;
		color = Color;
	}
	
	public void move(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}

	public void setColor (boolean Color) {
		color = Color;
	}
	
	public void flip() {
		color = !color;
	}

	public String toString() {
		if(color)
			return "W";
		else return "B";
	}

	// Used to avoid pointer ugliness
	public Piece copy() { 
		return new Piece(this.x, this.y, this.color);
	}
}
