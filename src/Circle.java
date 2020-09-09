/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

/**
 * A basic game object. It is displayed as a circle of a specified color. 
 */
public class Circle extends GameObj {
	public static int BOUND = GameBoard.getBoardWidth()/GameBoard.getTokensInRow();
    public static int SIZE = BOUND - 15;
    private Color color;

    public Circle(int px, int py, Color color) {
        super(px, py, SIZE, SIZE, BOUND, BOUND);
        this.color = color;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
    
}