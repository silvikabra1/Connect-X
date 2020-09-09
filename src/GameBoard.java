/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.*;

/**
 * GameBoard
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel 
{

    // the state of the game logic
	private Circle[][] boardCircles; // the circles that make up the board "holes"
	private LinkedList<Token> tokens; // as many tokens as board holes, all of alternating color
	private LinkedList<Token> moves; // the tokens that have been dropped by the player
	private ArrayList<Token> yellowTokens; // the yellow dropped tokens
	private ArrayList<Token> redTokens; // the red dropped tokens
	private Timer timer; // timer to keep track of time elapsed
	
	private String playerOne; // name of player one
	private String playerTwo; // name of player two
	
	private String winner; // either playerOne or playerTwo if win
	   
	private static int X; // the number of tokens that the user wants to connect to win
	private static int tokensInRow; // number of tokens in each row
	private static int tokensInCol; // number of tokens in each column
	
	private static int numRows; // index numbers of rows
	private static int numCols; // index numbers of columns
	
	private int numDroppedInCol; // number of tokens already in a given column
	
	private int numKeysPressed = 0; // number of keys pressed (resets to 0 when game is reset)
	
	private Mode mode; // current mode of game
	
	private boolean gameWon; // if game has a winner
	private boolean gameDraw; // if game resulted in draw
	
    private JLabel status; // Current turn status text, i.e. "Opponent's Turn"
    private JLabel time; // Current time elapsed in text
    
    private int seconds = 0; // Number of seconds elapsed

    // Game constants
    private static final int BOARD_WIDTH = 600;
    private static int BOARD_HEIGHT;

    // Update interval for timer, in milliseconds
    private static final int INTERVAL = 1000;
    

    public GameBoard(JLabel status, JLabel time, int x, String p1, String p2) {
    	
        // creates border around the board area, JComponent method 	
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // creates a blue background for the board
        setBackground(Color.BLUE);
        
        // instantiates timer with tick method based on interval
        timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        
        // Enable keyboard focus on the board area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	
            	// if game has just been won or draw, clicking any key will reset the game
            	if(getMode() == Mode.DRAW || getMode() == Mode.WIN){
            		reset();
            	} else {
	            	int key = e.getKeyCode();
	            	
	            	//updates number of arrow keys/enter keys clicked by user
	            	if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || 
	            			key == KeyEvent.VK_ENTER){
	                	numKeysPressed++;
	            	}
	            	
	            	updateMode();
	            	updateStatus();
	            	
	            	try {
	            		// gets last token on linked list of tokens
	            		Token token = tokens.getLast();
	            		
	            		//moves token left/right
		                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		                     token.moveLeft();
		                     repaint(); 
		                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		                	token.moveRight();
		                    repaint();     
		                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		                	
		                	// counts number of tokens that are already in the column that the
		                	// current token is above
		                	numDroppedInCol=0;
		                	for(int i = 0; i < numCols; i++) {
		                		if(boardCircles[token.getColPos()][i] instanceof Token) {
		                			numDroppedInCol++;
		                		}
		                	}
		                	
		                	// if the token is able to be dropped into the column
		                	if(!columnIsFull()) {
		                		moves.add(token);
		                		if(token.getColor() == Color.YELLOW) {
		                			yellowTokens.add(token);
		                		} else {
		                			redTokens.add(token);
		                		}
		                		
		                		// removes token from linked list of remaining tokens 
		                		tokens.removeLast();
		                		
		                		// sets board Circle to the token dropped
		                		boardCircles[token.getColPos()][numRows - numDroppedInCol] = token;
		                		
		                		// drops token the correct amount of spaces in the column
		                		int i = 0;
		                		while(i <= numRows - numDroppedInCol) {
		                			token.moveDown();
		                			i++;
		                		}
			                	updateMode();
			                	updateStatus();
		                	}
		                	updateMode();
		                	checkWin();
		                	updateStatus();
		                	repaint();
		                } 
	            	} catch(NoSuchElementException exception) {
	            		//exception for no tokens left
	            	}
            }
          }
        });

        this.status = status;
        this.time = time;
        playerOne = p1;
        playerTwo = p2;
        
        // modifies value of X
        if(x < 4 || x > 9) 
        	x = 4;
        
        GameBoard.X = x;
        tokensInRow = Math.max(7, X + 3); // number of tokens in each row
    	tokensInCol = Math.max(6, X + 2); // number of tokens in each column
    	
    	numRows = tokensInCol - 1; // index numbers of rows
    	numCols = tokensInRow - 1; // index numbers of columns
    	
    	// sets the board height based on the number of tokens in each column/row and bounds of
    	// circle
    	BOARD_HEIGHT = tokensInCol * (BOARD_WIDTH/tokensInRow) + Circle.BOUND; 
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() 
    {
    	// resets values to 0, sets mode to PREGAME, and displays top times
    	numKeysPressed = 0;
    	numDroppedInCol = 0;
    	setMode(Mode.PREGAME);
    	updateStatus();
    	updateTimer();
    	displayTopTimes();
    	drawBoard();
    	repaint();
    }
    
    public void drawBoard() 
    {
    	// instantiate the BoardCircles, tokens, moves, yellowTokens, redTokens
        boardCircles = new Circle[tokensInRow][tokensInCol];
        tokens = new LinkedList<Token>();
        moves = new LinkedList<Token>();
        yellowTokens = new ArrayList<Token>();
        redTokens = new ArrayList<Token>();
        
        // draw board with circles
        for(int i = 0; i < boardCircles.length; i++) {
        	for(int j = 0; j < boardCircles[i].length; j++) {
        		boardCircles[i][j] = new Circle((Circle.BOUND * i) + 5, 
        				(Circle.BOUND * j) + Circle.BOUND + 5, Color.WHITE);		
        	}
        }
        
        // add tokens equal to the total number of circles with alternating color, where last one is
        // always red because the number of rows and columns are consecutive integers, so one is 
        // even and the other is odd, so the product is always even.
        for(int i = 0; i <= (numRows + 1) * (numCols + 1) - 1; i++) {
        	if(i % 2 == 0) {
        		Token t = new Token(5, 5, Color.YELLOW);
        		tokens.add(t);
        	} else {
        		Token t = new Token(5, 5, Color.RED);
        		tokens.add(t);
        	}
        }
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
    	// add a second every time timer ticks (every second)
    	seconds++;
    	
    	// if mode is PREGAME, set seconds to 0 because game hasn't started yet
    	if(getMode() == Mode.PREGAME) {
    		seconds = 0;
    	} else if (getMode() == Mode.GAMESTART) {
        	setMode(Mode.PLAYING);
        } else if (getMode() == Mode.DRAW || getMode() == Mode.WIN) {
        	// stop timer and reset it after game has been won/draw
        	timer.stop();
        	seconds = 0;
        }
    	// update text of timer label
    	updateTimer();
    }
    
    
    // removes past 2 moves of user since it is player v player, so if a player wants to undo their
    // turn, they will also undo the turn of the other player so they can't play the other player's
    // turn
    public void undo() {
    	// if 0 or only 1 token has been dropped or if game has been won/draw, moves can't be undone
    	if(moves.isEmpty() || moves.size() == 1 || mode == Mode.GAMEEND || mode == Mode.WIN ||
    			mode == Mode.DRAW) {
    		System.out.println("no moves to undo");
    	} else {
    		System.out.println("undo");
    		// remove last 2 tokens from moves
    		Token t = moves.removeLast();
    		Token t1 = moves.removeLast();
    		
    		// set the board circles back to an empty circle where the 2 last moves were
    		boardCircles[t.getColPos()][t.getRowPos()] = new Circle(t.getPx(), t.getPy(), 
					Color.WHITE);
    		boardCircles[t1.getColPos()][t1.getRowPos()] = new Circle(t1.getPx(), t1.getPy(), 
					Color.WHITE);
    	
    		// remove tokens from their respective colors
    		if(t.getColor() == Color.YELLOW) {
    			yellowTokens.remove(t);
    			redTokens.remove(t1);
    		} else {
    			redTokens.remove(t);
    			yellowTokens.remove(t1);
    		}
    		
    		// add duplicate version of those tokens back to linked list of remaining tokens
    		Token newToken1 = new Token(5, 5, t.getColor());
    		Token newToken2 = new Token(5, 5, t1.getColor());
    		tokens.add(newToken1);
    		tokens.add(newToken2);
    		repaint();
    	}
    	
    	// focus on game again
		requestFocusInWindow();
		repaint();
    }
    
    public boolean checkWin() {
    	
    	// if the number of red tokens dropped and the number of yellow tokens dropped is less than 
    	// X, return false automatically
    	if(redTokens.size() < X && yellowTokens.size() < X) {
    		return false;
    	}
    	
    	// if there is a horizontal, vertical, or diagonal win, game is won
    	// if there are no more tokens and game hasn't been won, then game is a draw
    	if(checkHorizontal(boardCircles) || checkVertical(boardCircles) 
    			|| checkDiagonalUpwards(boardCircles) || checkDiagonalDownwards(boardCircles)) {
    		System.out.println("game won");
    		setMode(Mode.WIN);
    		gameWon = true;
    		return true;
    	} else if(tokens.isEmpty()) {
    		System.out.println("game draw");
    		setMode(Mode.DRAW);
    		gameDraw = true;
    		return false;
    	}
    	return false;
    }
    
    public boolean checkVertical(Circle[][] boardCircles) {
    	
    	// start from bottom left corner of board
    	for(int r = 0; r < boardCircles.length; r++) {
    		for(int col = boardCircles[r].length- 1; col > X - 2; col--) {
    			
    			Circle c1 = boardCircles[r][col];
    			
    			// checks if there is a token at specified location
    			if(c1 instanceof Token) {
    				
    				// cast it to a token
    				Token t = (Token) c1;
    				
    				// array list of circles to check and tokens to check
    				ArrayList<Circle> circlesToCheck = new ArrayList<Circle>();
    				ArrayList<Token> tokensToCheck = new ArrayList<Token>();
    				
    				// adds the X circles above Token t to an array list 
        			for(int i = 0; i < X; i ++) {
        				circlesToCheck.add(boardCircles[r][col-i]);
        			}
        			
        			// if any of the circles added to the array list are not an instance of a token
        			// remove them from array
        			circlesToCheck.removeIf(c -> !(c instanceof Token));
        			
        			// if no circles were removed, then add all the circles to array list for tokens
        			if(circlesToCheck.size() == X) {
        				
        				for(Circle c : circlesToCheck) {
        					tokensToCheck.add((Token) c);
        				}
        				
        				// if any of the tokens don't have the same color as Token t, remove them
        				tokensToCheck.removeIf(tempToken -> !(t.hasSameColor(tempToken)));
        				
        				// if no tokens were removed and there are X tokens of the same color in a
        				// vertical row, game won!
        				// red colored tokens is player One, yellow is player Two.
        				if(tokensToCheck.size() == X){
            				if(tokensToCheck.get(1).getColor().equals(Color.RED)) {
            					winner = playerOne;
            				} else {
            					winner = playerTwo;
            				}
            				return true;
            			}
        			}
    			}
    		}
    	}
    	return false;
    }
    
    
    public boolean checkHorizontal(Circle[][] boardCircles) {
    	
    	// start from bottom left corner of board
    	for(int r = 0; r < boardCircles.length - X + 1; r++) {
    		for(int col = boardCircles[r].length- 1; col > 0; col--) {
    			
    			Circle c1 = boardCircles[r][col];
    			
    			// checks if there is a token at specified location
    			if(c1 instanceof Token) {
    				
    				// cast it to a token
    				Token t = (Token) c1;
    				
    				// array list of circles to check and tokens to check
    				ArrayList<Circle> circlesToCheck = new ArrayList<Circle>();
    				ArrayList<Token> tokensToCheck = new ArrayList<Token>();
    				
    				// adds the X circles to right of Token t to an array list 
        			for(int i = 0; i < X; i ++) {
        				circlesToCheck.add(boardCircles[r+i][col]);
        			}
        			
        			// if any of the circles added to the array list are not an instance of a token
        			// remove them from array
        			circlesToCheck.removeIf(c -> !(c instanceof Token));
        			
        			// if no circles were removed, then add all the circles to array list for tokens
        			if(circlesToCheck.size() == X) {
        				
        				for(Circle c : circlesToCheck) {
        					tokensToCheck.add((Token) c);
        				}
        				
        				// if any of the tokens don't have the same color as Token t, remove them
        				tokensToCheck.removeIf(tempToken -> !(t.hasSameColor(tempToken)));
        			}
        			
        			// if no tokens were removed and there are X tokens of the same color in a
    				// vertical row, game won!
    				// red colored tokens is player One, yellow is player Two.
        			if(tokensToCheck.size() == X) {
        				if(tokensToCheck.get(1).getColor().equals(Color.RED)) {
        					winner = playerOne;
        				} else {
        					winner = playerTwo;
        				}
        				return true;
        			}
    			}
    		}
    	}
    	return false;
    }
    
    public boolean checkDiagonalUpwards(Circle[][] boardCircles) {
    	
    	// start from bottom left corner of board
    	for(int r = 0; r < boardCircles.length - X + 1; r++) {
    		for(int col = boardCircles[r].length- 1; col > X - 2; col--) {
    			
    			Circle c1 = boardCircles[r][col];
    			
    			// checks if there is a token at specified location
    			if(c1 instanceof Token) {
    				
    				// cast it to a token
    				Token t = (Token) c1;
    				
    				// array list of circles to check and tokens to check
    				ArrayList<Circle> circlesToCheck = new ArrayList<Circle>();
    				ArrayList<Token> tokensToCheck = new ArrayList<Token>();
    				
    				// adds the X circles upwardly diagonal to Token t to an array list 
        			for(int i = 0; i < X; i ++) {
        				circlesToCheck.add(boardCircles[r + i][col - i]);
        			}
        			
        			// if any of the circles added to the array list are not an instance of a token
        			// remove them from array
        			circlesToCheck.removeIf(c -> !(c instanceof Token));
        			
        			// if no circles were removed, then add all the circles to array list for tokens
        			if(circlesToCheck.size() == X) {
        				for(Circle c : circlesToCheck) {
        					tokensToCheck.add((Token) c);
        				}
        				
        				// if any of the tokens don't have the same color as Token t, remove them
        				tokensToCheck.removeIf(tempToken -> !(t.hasSameColor(tempToken)));
        			}
        			// if no tokens were removed and there are X tokens of the same color in a
    				// vertical row, game won!
    				// red colored tokens is player One, yellow is player Two.
        			if(tokensToCheck.size() == X) {
        				if(tokensToCheck.get(1).getColor().equals(Color.RED)) {
        					winner = playerOne;
        				} else {
        					winner = playerTwo;
        				}
        				return true;
        			}
    			}
    		}
    	}
    	return false;
    }
    
    public boolean checkDiagonalDownwards(Circle[][] boardCircles) {
    	// start from top left corner of board
    	for(int r = 0; r < boardCircles.length - X + 1; r++) {
    		for(int col = 0; col < boardCircles[r].length - X + 1; col++) {
    			
    			Circle c1 = boardCircles[r][col];
    			
    			// checks if there is a token at specified location
    			if(c1 instanceof Token)
    			{
    				// cast it to a token
    				Token t = (Token) c1;
    				
    				// array list of circles to check and tokens to check
    				ArrayList<Circle> circlesToCheck = new ArrayList<Circle>();
    				ArrayList<Token> tokensToCheck = new ArrayList<Token>();
    				
    				// adds the X circles downwardly diagonal to Token t to an array list 
        			for(int i = 0; i < X; i ++) {
        				circlesToCheck.add(boardCircles[r + i][col + i]);
        			}
        			
        			// if any of the circles added to the array list are not an instance of a token
        			// remove them from array
        			circlesToCheck.removeIf(c -> !(c instanceof Token));
        			
        			// if no circles were removed, then add all the circles to array list for tokens
        			if(circlesToCheck.size() == X) {
        				for(Circle c : circlesToCheck) {
        					tokensToCheck.add((Token) c);
        				}
        				// if any of the tokens don't have the same color as Token t, remove them
        				tokensToCheck.removeIf(tempToken -> !(t.hasSameColor(tempToken)));
        			}
        			
        			// if no tokens were removed and there are X tokens of the same color in a
    				// vertical row, game won!
    				// red colored tokens is player One, yellow is player Two.
        			if(tokensToCheck.size() == X) {
        				if(tokensToCheck.get(1).getColor().equals(Color.RED)) {
        					winner = playerOne;
        				} else {
        					winner = playerTwo;
        				}
        				return true;
        			}
    			}
    		}
    	}
    	return false;
    }
    
    private void updateStatus() {
    	
    	// resets game if game ends
    	// adds time if game won
    	// drawed game doesn't add time
    	// game start starts timer
    	// before game updates tells user to press key before starting timer
    	if(mode == Mode.GAMEEND) {
    		System.out.println("GAME OVER.");
    		reset();
    	} else if(mode == Mode.WIN) {
    		status.setText("Game has been won by " + winner + "! Press any key to reset");
    		addTimes(seconds, winner);
    	} else if(mode == Mode.DRAW) {
    		status.setText("Game ended in a draw. Press any key to reset");
    	} else if(mode == Mode.GAMESTART) {
    		status.setText("Game in progress");
    		timer.start();
    	} else if(mode == Mode.PREGAME) {
    		status.setText("Start the game by moving a token");
    	}
    }
    
    private void addTimes(int sec, String winner) {
    	writeTimeToFile(sec, winner, "TIME_LOG");
    }
        
    // updates the text of the timer with the formatted time
    public void updateTimer() {
    	
    	// before game starts, time should be 0. Otherwise, time should just be formatted
    	if(getMode() == Mode.PREGAME) {
    		time.setText("| 00:00");
    	} else if (getMode() == Mode.PLAYING) {
        	time.setText("| " + formatTime(seconds));
        }
    }
    
    // formats time so that it is in "MM:SS" format given the number of seconds
    public String formatTime(int seconds) {
    	int minutes = seconds/60;
    	boolean secondsNeeds0 = false;
    	boolean minutesNeeds0 = false;
    	
    	if(seconds % 60 < 10) {
    		secondsNeeds0 = true;
    	}
    	if(minutes < 10) {
    		minutesNeeds0 = true;
    	}
    	if(secondsNeeds0 && minutesNeeds0) {
    		return "0" + minutes + ":0" + (seconds % 60);
    	} else if(minutesNeeds0) {
    		return "0" + minutes + ":" + (seconds % 60);
    	} else if(secondsNeeds0) {
    		return minutes + ":0" + (seconds % 60);
    	} else {
    		return minutes + ":" + (seconds % 60);
    	}
    	
    }
    
    // checks if the column that the token ready to be dropped is currently at is full
    public boolean columnIsFull()
    {
    	if(numDroppedInCol > numRows) {
    		System.out.println("column is full");
    		return true;
    	}
    	return false;
    }
    
    public void updateMode() {
    	if(numKeysPressed == 1)
    	{
    		// if user has pressed the left, right, or enter key, the game officially starts
    		setMode(Mode.GAMESTART);
    	}
    	else if(numKeysPressed > 1)
    	{
    		// if user has pressed more than one key, game is in progress
    		setMode(Mode.PLAYING);
    	}
    }
    
    public Mode getMode() {
    	return mode;
    }
    
    public void setMode(Mode m) {
    	mode = m;
    }
    

    

    
    public void writeTimeToFile(int sec, String winner, String filePath) {
    	
    	// makes new time
		Time t = new Time(winner, sec);
		
    	File file = Paths.get(filePath).toFile();
    	FileWriter fr;
		try {
			if(!file.exists()) {
				
				// makes new file if none with the string filePath exists
				file.createNewFile();
			}
			fr = new FileWriter(file, true);
			BufferedWriter br = new BufferedWriter(fr);
			
			// writes name separated by dash with time and adds a new line
			br.write(t.getName() + "-" + t.getTime());
			br.newLine();
			br.close();
		} catch (IOException e) {
			System.out.println("io exception");
		}
    }
    
	public List<Time> getThreeFastestTimes(String filePath) {
    	File file = Paths.get(filePath).toFile();
    	FileReader fr;
    	
    	// array list to hold all times on file
		List<Time> allTimes = new ArrayList<Time>();

    	try {
    		fr = new FileReader(file);
    		BufferedReader br = new BufferedReader(fr);
    		
    		// read file and create new Time based on values on lines with text and add to allTimes
    		while(br.ready()) {
    			String temp = br.readLine();
    			if(temp != null) {
    				String[] tempSplit = temp.split("-");
    				int time = Integer.parseInt(tempSplit[1]);
    				String name = tempSplit[0];
    				Time t = new Time(name, time);
    				allTimes.add(t);
    			}
    		}
    		// close buffered reader
    		br.close();
    		
    		// sort Times in ascending order by their seconds value
    		Collections.sort(allTimes);
    		
    	} catch (IOException e) {
			System.out.println("io exception");
    	}
    	
		return allTimes;
    }
    
	public void displayTopTimes() {
		List<Time> topTimes = getThreeFastestTimes("TIME_LOG");
		
		// if there are no times in file yet print no times available
		if(topTimes.isEmpty()) {
			InfoWindow.topTimes.setText("No times available");
		} else {
			
			// reset topTimes text to nothing
			InfoWindow.topTimes.setText("");
			int counter = 0;
			
			// only take up to shortest 3 times by using a counter
			for(Time t : topTimes) {
				if(counter < 3) {
					InfoWindow.topTimes.append(t.getName() + " : " + formatTime(t.getTime()));
					InfoWindow.topTimes.append("\n");
				}
				counter++;
			}
		}
		
	}
	
	public Circle[][] getBoard() {
		return boardCircles;
	}
	
	public int getXValue() {
		return X;
	}
	
	public static int getNumCols() {
		return numCols;
	}
	
	public static int getNumRows() {
		return numRows;
	}
	
	public static int getTokensInRow() {
		return tokensInRow;
	}
	
	public static int getTokensInCol() {
		return tokensInCol;
	}
	
	public static int getBoardWidth() {
		return BOARD_WIDTH;
	}
	
	public static int getBoardHeight() {
		return BOARD_HEIGHT;
	}
	
	public LinkedList<Token> getTokens() {
		return tokens;
	}
	
	public LinkedList<Token> getMoves() {
		return moves;
	}
	
	public boolean getWin() {
		return gameWon;
	}
	
	public int getSeconds() {
		return seconds;
	}
    


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < boardCircles.length; i++) {
        	for(int j = 0; j < boardCircles[i].length; j++) {
        		boardCircles[i][j].draw(g);
        	}
        }
        
        // draw last element in token on screen 
        if(!tokens.isEmpty())
        	tokens.getLast().draw(g);

        // draw a line between where token is moving on screen and actual circles
        g.drawLine(0, Circle.BOUND-5, BOARD_WIDTH, Circle.BOUND-5);
    }
    

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}