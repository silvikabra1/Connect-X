import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.JLabel;

import org.junit.Test;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {

	JLabel status;
	JLabel time;
	String p1;
	String p2;
	
	public void initLabels()
	{
		status = new JLabel();
		time = new JLabel();
		p1 = "playerOne";
		p2 = "playerTwo";
	}
	
    @Test
    public void testGameBoardSizeXUnderFour() {
    	initLabels();
        GameBoard d = new GameBoard(status, time, 3, p1, p2);
        d.drawBoard();
        Circle[][] board = d.getBoard();
        assertEquals(board.length, 7);
        assertEquals(board[0].length, 6);
    }
    
    @Test
    public void testGameBoardSizeXOverNine() {
    	initLabels();
        GameBoard d = new GameBoard(status, time, 10, p1, p2);
        assertEquals(d.getXValue(), 4);
    }
    
    @Test
    public void testGameBoardSizeXBetweenFourAndNine() {
    	initLabels();
        GameBoard d = new GameBoard(status, time, 6, p1, p2);
        d.drawBoard();
        Circle[][] board = d.getBoard();
        assertEquals(board.length, 9);
        assertEquals(board[0].length, 8);
    }
    
    @Test
    public void testNumTotalTokens() {
    	initLabels();
    	GameBoard d = new GameBoard(status, time, 4, p1, p2);
    	d.drawBoard();
    	assertEquals(d.getTokens().size(), GameBoard.getTokensInRow() * GameBoard.getTokensInCol());
    }
    
    @Test
    public void testVerticalWin() {
    	initLabels();
    	GameBoard d = new GameBoard(status, time, 4, p1, p2);
    	d.drawBoard();
        Circle[][] board = d.getBoard();
        System.out.println(board[1][1]);
        board[1][1] = new Token(5, 5, Color.RED);
        board[1][2] = new Token(5, 5, Color.RED);
        board[1][3] = new Token(5, 5, Color.RED);
        board[1][4] = new Token(5, 5, Color.RED);
        assertTrue(d.checkVertical(board));
    }
    
    @Test
    public void testHorizontalWin() {
    	initLabels();
    	GameBoard d = new GameBoard(status, time, 4, p1, p2);
    	d.drawBoard();
        Circle[][] board = d.getBoard();
        System.out.println(board[1][1]);
        board[1][2] = new Token(5, 5, Color.RED);
        board[2][2] = new Token(5, 5, Color.RED);
        board[3][2] = new Token(5, 5, Color.RED);
        board[4][2] = new Token(5, 5, Color.RED);
        assertTrue(d.checkHorizontal(board));
    }
    
    @Test
    public void testDiagonalWin() {
    	initLabels();
    	GameBoard d = new GameBoard(status, time, 4, p1, p2);
    	d.drawBoard();
        Circle[][] board = d.getBoard();
        board[1][2] = new Token(5, 5, Color.RED);
        board[2][3] = new Token(5, 5, Color.RED);
        board[3][4] = new Token(5, 5, Color.RED);
        board[4][5] = new Token(5, 5, Color.RED);
        assertTrue(d.checkDiagonalDownwards(board) || d.checkDiagonalUpwards(board));
    }
    
    @Test
    public void testTimeResetsToZero() {
    	initLabels();
    	GameBoard d = new GameBoard(status, time, 4, p1, p2);
    	d.drawBoard();
    	d.setMode(Mode.PREGAME);
    	assertEquals(0, d.getSeconds());
    }
    

    

}
