import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class InfoWindow extends JPanel {
	
	//Labels, textareas, and textfields for information window
	JLabel instructionsJLabel;
	JLabel topThreeTimesJLabel;
	JTextArea instructions;
	static JTextArea topTimes;
	JLabel xJLabel;
	static JTextField xInputter;
	JLabel nameOneJLabel;
	static JTextField nameOneInputter;
	JLabel nameTwoJLabel;
	static JTextField nameTwoInputter;
	
	// panels for layout
	JPanel instructionsPanel;
	JPanel getXInfoPanel;
	JPanel getP1InfoPanel;
	JPanel getP2InfoPanel;
	JPanel timesPanel;
	
	// Panel size constants
    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 550;
	
	public InfoWindow()
	{
		// sets a flow layout to top-level panel
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		// creates border around the window area, JComponent method 	
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        drawWindow();
        
	}
	
	 public void drawWindow() 
	 {
		 // instantiates panels with border layouts
		 instructionsPanel = new JPanel(new BorderLayout());
		 getXInfoPanel = new JPanel(new BorderLayout());
		 getP1InfoPanel = new JPanel(new BorderLayout());
		 getP2InfoPanel = new JPanel(new BorderLayout());
		 timesPanel = new JPanel(new BorderLayout());
		 
		 // instantiates labels, text areas, and text fields
		 instructionsJLabel = new JLabel("Instructions");
		 topThreeTimesJLabel = new JLabel("Top 3 Times");
		 instructions = new JTextArea();
		 instructions.setEditable(false);
		 instructions.setLineWrap(true);
		 instructions.setWrapStyleWord(true);
		 instructions.setText("\nInstead of regular old Connect 4,"
		 		+ " you get to pick how many tokens you want to connect to win! Please type an integer from 4"
		 		+ " to 9 for your X value, and type in the name of the player who will be going "
		 		+ "first for Player One, and second for Player two. \n\nNow, to play, you will have"
		 		+ " to use the left and right arrow keys to move your designated token, and to drop"
		 		+ " it into the column it's above, just hit the enter key! Keep in mind you can't"
		 		+ " put a token into a column that is already full.");
		 instructions.setPreferredSize(new Dimension(250, 250));
		 topTimes = new JTextArea();
		 topTimes.setEditable(false);
		 topTimes.setPreferredSize(new Dimension(190, 60));
		 xJLabel = new JLabel("X: ");
		 xInputter = new JTextField();
		 xInputter.setPreferredSize(new Dimension(50, 20));
		 nameOneJLabel = new JLabel("Player 1 Name: ");
		 nameOneInputter = new JTextField();
		 nameOneInputter.setPreferredSize(new Dimension(160, 20));
		 nameTwoJLabel = new JLabel("Player 2 Name: ");
		 nameTwoInputter = new JTextField();
		 nameTwoInputter.setPreferredSize(new Dimension(160, 20));
	
		 // adds fields to panels with different layout constraints
		 instructionsPanel.add(instructionsJLabel, BorderLayout.NORTH);
		 instructionsPanel.add(instructions, BorderLayout.SOUTH);
		 
		 getXInfoPanel.add(xJLabel, BorderLayout.WEST);
		 getXInfoPanel.add(xInputter, BorderLayout.EAST);
		 getP1InfoPanel.add(nameOneJLabel, BorderLayout.WEST);
		 getP1InfoPanel.add(nameOneInputter, BorderLayout.EAST);
		 getP2InfoPanel.add(nameTwoJLabel, BorderLayout.WEST);
		 getP2InfoPanel.add(nameTwoInputter, BorderLayout.EAST);
		 		 
		 timesPanel.add(topThreeTimesJLabel, BorderLayout.NORTH);
		 timesPanel.add(topTimes, BorderLayout.SOUTH);
		 
		 // adds panels to top-level panel
		 this.add(instructionsPanel);
		 this.add(getXInfoPanel);
		 this.add(getP1InfoPanel);
		 this.add(getP2InfoPanel);
		 this.add(timesPanel);
	 }
	 
	 // disables the text fields
	 public void disableTextFields()
	 {
		 xInputter.setEnabled(false);
		 nameOneInputter.setEnabled(false);
		 nameTwoInputter.setEnabled(false);
	 }
	 
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}

