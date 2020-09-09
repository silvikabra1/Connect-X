/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {

    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Connect X");
        frame.setLocation(1000, 550);
        frame.setLocationRelativeTo(null); //frame does not open in rightmost corner of screen

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel(" ");
        status.setVisible(false);
        status_panel.add(status);
        
        // Control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        // Timer label (in status panel)
        final JLabel time = new JLabel("");
        time.setText("time goes here");
        time.setVisible(false);
        status_panel.add(time);
        
        // InfoWindow (first screen)
        final InfoWindow infoWindow = new InfoWindow();
        frame.add(infoWindow, BorderLayout.WEST);

        // Submit button (in InfoWindow)
        final JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
        	
        	// method checks the values inputted by user and makes sure names are strings
        	// and X is an integer. If violated, inputs are changed by program
        	public void checkFields() {
        		String x = InfoWindow.xInputter.getText();
        		// if X is not an integer from 4-9, it is set to 4
        		if(!x.matches("^[4-9]") || x.isEmpty()) {
        			InfoWindow.xInputter.setText("4");
        		}
        		
        		String n1 = InfoWindow.nameOneInputter.getText();
        		String n2 = InfoWindow.nameTwoInputter.getText();
        		
        		// if name 1 is not a string or is empty, name 1 is set to PlayerOne
        		if(!n1.matches("^[a-zA-Z]*$") || n1.isEmpty()) {
        			InfoWindow.nameOneInputter.setText("PlayerOne");
        		}
        		// if name 2 is not a string or is empty or is same as name 1, name 2 is set to 
        		// PlayerTwo
        		if(!n2.matches("^[a-zA-Z]*$") || n2.isEmpty() || n2.equals(n1)) {
        			InfoWindow.nameTwoInputter.setText("PlayerTwo");
        		}
        	}
        	// action listener for submit button
        	// checks/edits fields, disables them, gets the inputs from the fields
        	// makes a new gameboard, adds it to the frame, sets components on status panel visible
        	// makes and adds reset and undo buton to control panel
        	// resets board (starts game)
        	public void actionPerformed(ActionEvent e) {
        		checkFields();
        		
        		submit.setEnabled(false);
        		infoWindow.disableTextFields();
        		
        		int x = Integer.parseInt(InfoWindow.xInputter.getText());
        		String p1 = InfoWindow.nameOneInputter.getText();
        		String p2 = InfoWindow.nameTwoInputter.getText();
        		
        		final GameBoard board = new GameBoard(status, time, x, p1, p2);
        		frame.add(board, BorderLayout.EAST); 
        		
        		time.setVisible(true);
        		status.setVisible(true);
        		
        		final JButton reset = new JButton("Reset");
                reset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.reset();
                    }
                });
                control_panel.add(reset);
                
                final JButton undo = new JButton("Undo");
                undo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.undo();
                    }
                });
                control_panel.add(undo);
                
        		frame.pack();
        		board.reset();
            }
        });
        
        // adds submit button to info window
        infoWindow.add(submit);

        // Put the frame on the screen
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}