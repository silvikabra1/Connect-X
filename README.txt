=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: skabra
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D Arrays
  	I used 2-D arrays when making my game board, which was the connect 4 board. The size of the 2-D
  	array changed based on the number of tokens the user wanted to input. The 2-D array was first 
  	created with Circle objects that are equally spaced from one another. Then, as the user played,
  	when a token was dropped onto the board, the token was set to that position in the array itself.
  	So, the array contained tokens and circles. This is an appropriate use of the concept because, 
  	one, it stored whether the object at every position on the board was a circle (meaning a token
  	could go there) or a token. This was used in making the win algorithms, since not every row and
  	column needed to be searched through to determine if there was a horizontal or vertical or 
  	diagonal win.

  2. Collections
  	I used collections to store the "moves" that were made in the game. I used a LinkedList for this
  	collection, and every time a token was dropped, that token was added to the end of the moves 
  	LinkedList. The undo button removes the last 2 elements in the linked list and updates the 
  	screen accordingly. This is an appropriate use because the LinkedList collection was needed 
  	since the order of the moves is necessary for the undo function.

  3. File I/O
  	I used File I/O to store the fastest times it took to win and the names of the winners 
  	associated with those times. The times are stored in a txt file, where the name and time are 
  	separated by a dash and each time is separated from the other by a new line. This is an 
  	appropriate use of I/O because every time a game is won, the time and winner are written to the 
  	file by a FileReader, and every time the game is reloaded or reset, the file is read by a 
  	FileReader. WHen the file is read, each time is put into an array list which is then sorted, 
  	and the first 3 elements get put on the screen. 
  	

  4. Testable component
  	The game board is a testable component, as it is possible to test the size of the board based on
  	different values of X that the user inputs. It's also possible to test the number of tokens
  	that are made when the board is made. It also tests that the time sets to 0 when the mode is
  	PREGAME.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Circle: This creates a GameObj in the shape of a circle with a specified color. This is what fills
  		  the game board.
  Game: This class creates an InfoWindow, then a GameBoard and gets input from the users.
  GameBoard: This class holds the game board, where the user is able to move tokens left and right
  			 and drop them in a column. This class also has all the File I/O components for the 
  			 times. 
  GameObj: This class is an abstract class that has methods of moving left and right.
  InfoWindow: This class creates a window that has fields to get info from the players. It also has
  			  the instructions for the game.
  Mode: This class holds enums for the current state of the game
  Time: This class is responsible for creating Time objects, each which hold a name and time. These
  		are created when the times are read from the file and are displayed when the board is 
  		created.
  Token: This class extends Circle and creates a Token object. The tokens are used as the pieces in 
  		  the game and can be moved left and right or dropped. 


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Getting the board to change size while keeping the spacing the same and not changing the size of
  the board was quite confusing.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  The functionality is pretty dependent on the UI, which I would refactor. I would create more 
  classes so that the tokens weren't dependent on the board. I would add a larger class separation
  because the GameBoard class pretty much has all of the code and does more than just making the 
  board.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
