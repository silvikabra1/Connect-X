import java.awt.Color;

// Token is a Circle that serves as a piece in ConnectX. It has a row and column position as well as
// a color. It can also be moved left right and down depending on the bounds of the board. It can 
// also be compared to another Token to see if they have the same color
public class Token extends Circle
{
	// x and y coordinates of token
	private int px;
	private int py;
	
	// row and column positions of token
	private int rowPos;
	private int colPos;
	
	// Color of token
	private Color color;
	
	// boolean determining if token has been dropped yet
	private boolean dropped; 
	
	public Token(int px, int py, Color color) {
		super(px, py, color);
		this.px = px;
		this.py = py;
		this.color = color;
		rowPos = -1;
		colPos = 0;
		dropped = false;
	}
	
	public Color getColor() {
		return color;
	}
	
	// compared token to another token to see if they have the same color
	public boolean hasSameColor(Token t) {
		if(this.getColor().equals(t.getColor())) {
			return true;
		}
		return false;
	}

	public int getRowPos() {
		return rowPos;
	}
	
	public int getColPos() {
		return colPos;
	}
	
	public void setRowPos(int r) {
		rowPos = r;
	}
	
	public void setColPos(int c) {
		colPos = c;
	}

	
	// moves token left but makes sure that it can't cross the limits of the board
	@Override
	public void moveLeft() {
		super.moveLeft();
		colPos = Math.max(0, colPos - 1);
		colPos = Math.min(colPos, GameBoard.getNumCols());
	}
	
	// moves token right but makes sure that it can't cross the limits of the board
	@Override
	public void moveRight() {
		super.moveRight();
		colPos = Math.max(0, colPos + 1);
		colPos = Math.min(colPos, GameBoard.getNumCols());

	}
	
	// moves token down but makes sure that it doesn't move down further than the rows on screen
	@Override
	public void moveDown() {
		super.moveDown();
		rowPos = Math.max(0, rowPos + 1);
		rowPos = Math.min(rowPos, GameBoard.getNumRows());
		dropped = true;
	}
	
	public boolean isDropped() {
		return dropped;
	}

}
