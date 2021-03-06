import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** 
 * This class will handle all actions that have to do with the grid.
 * Creation of panels for the player, sets the default images to empty water.
 * Also Validation of any ship placement occurs here. This class will also
 * print a text base board to the console for debug and testing purposes.
 * 
 * @author Mike Cutalo
 * @version 3.0
 */
public class Grid extends JPanel
{
	/**Holds the board of buttons */
	private Singlespace [][] board;

	/**
	 * Grid Constructor 
	 * 
	 * This will construct a new grid object,
	 * creating a 10 X 10 array of single space objects.
	 * While also setting all of the default images to empty
	 * water upon creation.
	 */
	public Grid()
	{
		this.board = new Singlespace[10][10];

		Image img = null;
		try {
			img = ImageIO.read(getClass().getResource("/images/Space.jpg"));		
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i=0; i < 10; i++)
		{
			for(int j=0; j < 10; j++)
			{
				this.board[i][j] = new Singlespace();
				this.board[i][j].setOpaque(true);
				this.board[i][j].setIcon(new ImageIcon(img));
			}
		}
	}

	/**
	 * Simply will generate the panel
	 * 
	 * This will create the players panel, if the game mode it true
	 * then the board will be printed with empty space and you will not be
	 * able to see any ships. if the game mode is false then the panel will be
	 * generated with ships displaying.
	 * 
	 * @param isGameMode If game mode is true the opponent panel will print without the ships.
	 * @return JPanel This will be applied to the board when the game starts
	 */
	public JPanel CreateGridPanel(boolean isGameMode) 
	{	
		try{
			BufferedImage tmpImage = null;

			this.setLayout(new GridLayout(this.board.length, this.board.length));
			this.setPreferredSize(new Dimension(390,160));
			this.setMaximumSize(new Dimension(390,160));
			this.setSize(390,160);

			for(int i=0; i < 10; i++)
			{
				for(int j=0; j < 10; j++)
				{
					this.add(this.board[i][j]);  
					this.board[i][j].setActionCommand(i+""+j);
					this.board[i][j].setName(i+""+j);


					if(isGameMode == false)
					{
						if(this.board[i][j].getOccupyingShip() == 'A'){
							tmpImage = ImageIO.read(getClass().getResource("/images/AC.jpg"));
							this.board[i][j].setIcon(new ImageIcon(tmpImage));

						}else if(this.board[i][j].getOccupyingShip() == 'B'){
							tmpImage = ImageIO.read(getClass().getResource("/images/battleship.jpg"));
							this.board[i][j].setIcon(new ImageIcon(tmpImage));

						}else if(this.board[i][j].getOccupyingShip() == 'S'){
							tmpImage = ImageIO.read(getClass().getResource("/images/submarine.jpg"));
							this.board[i][j].setIcon(new ImageIcon(tmpImage));

						}else if(this.board[i][j].getOccupyingShip() == 'D'){
							tmpImage = ImageIO.read(getClass().getResource("/images/Destroyer.jpg"));
							this.board[i][j].setIcon(new ImageIcon(tmpImage));

						}else if(this.board[i][j].getOccupyingShip() == 'P'){
							tmpImage = ImageIO.read(getClass().getResource("/images/PatrolBoat.jpg"));
							this.board[i][j].setIcon(new ImageIcon(tmpImage));						
						}else{
							tmpImage = ImageIO.read(getClass().getResource("/images/Space.jpg"));	
							this.board[i][j].setIcon(new ImageIcon(tmpImage));
						}
					}else{
						tmpImage = ImageIO.read(getClass().getResource("/images/Space.jpg"));	
						this.board[i][j].setIcon(new ImageIcon(tmpImage));
					}
				}
			}				
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}


	/**
	 * Will display possible spots to place your ship
	 * 
	 * When placing a ship this will display possible spots to plae your ships,
	 * if you un-select your ship it will remove the possible places. Once a
	 * ship has been placed this method will fire and remove the remainder 
	 * of the possible places.
	 * 
	 * @param isPicked will dictate if the board should remove the possible images or not
	 */
	public void PossibleBoatSelect(boolean isPicked)
	{		
		int row = Integer.parseInt(Character.toString(Game.firstChoice.charAt(0)));
		int col = Integer.parseInt(Character.toString(Game.firstChoice.charAt(1)));
		boolean showRight=true,
				showLeft=true,
				showUp=true,
				showDown=true;

		if(isPicked)
		{
			for(int i=1; i < (Game.currentShipSize); i++)
			{		
				if((col + i) >= 10){
					showRight = false;
				}else if (this.board[row][col + i].isSpaceEmpty() == false){
					showRight = false;
				}else if(showRight == true && this.board[row][col + i].isSpaceEmpty() == true){
					showRight = true;
				}				

				if((col - i) < 0){
					showLeft = false;
				}else if(this.board[row][col - i].isSpaceEmpty() == false){	
					showLeft = false;
				}else if(showLeft == true && this.board[row][col - i].isSpaceEmpty() == true){
					showLeft = true;
				}

				if((row + i) >= 10){
					showUp = false;
				}else if(this.board[row + i][col].isSpaceEmpty() == false){
					showUp = false;
				}else if(showUp == true && this.board[row + i][col].isSpaceEmpty() == true){
					showUp = true;
				}

				if((row-i) < 0){
					showDown = false;
				}else if(this.board[row - i][col].isSpaceEmpty() == false){
					showDown = false;					
				}else if(showDown == true && this.board[row - i][col].isSpaceEmpty() == true){
					showDown = true;
				}						
			}

			for(int m=1; m < (Game.currentShipSize); m++)
			{
				if(showRight == true){
					this.board[row][col + m].setIcon(new ImageIcon(getClass().getResource("/images/black.jpg")));
					this.board[row][col + (Game.currentShipSize-1)].setIcon(Game.currShipPlaceImg);
				}

				if(showLeft == true){
					this.board[row][col - m].setIcon(new ImageIcon(getClass().getResource("/images/black.jpg")));
					this.board[row][col - (Game.currentShipSize-1)].setIcon(Game.currShipPlaceImg);
				}

				if(showUp == true){
					this.board[row + m][col].setIcon(new ImageIcon(getClass().getResource("/images/black.jpg")));
					this.board[row + (Game.currentShipSize-1)][col].setIcon(Game.currShipPlaceImg);
				}

				if(showDown == true){
					this.board[row - m][col].setIcon(new ImageIcon(getClass().getResource("/images/black.jpg")));
					this.board[row - (Game.currentShipSize-1)][col].setIcon(Game.currShipPlaceImg);
				}						
			}
		}
		else
		{
			int secondRow = Integer.parseInt(Character.toString(Game.secondChoice.charAt(0)));
			int secondCol = Integer.parseInt(Character.toString(Game.secondChoice.charAt(1)));

			for(int i=1; i <= (Game.currentShipSize-1); i++)
			{
				if(secondCol != (col + i) &&
				  (col + i) < 10 &&
				  this.board[row][col + i].isSpaceEmpty() == true)
				{
					this.board[row][col + i].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));
				}else if( (col + i) < 10 && this.board[row][col + i].isSpaceEmpty() == true){
					this.board[row][col + i].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));
				}

				if(secondCol != (col - i) &&
				  (col - i) >= 0 &&
				  this.board[row][col - i].isSpaceEmpty() == true){
					this.board[row][col - i].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));	
				}else if((col - i) >= 0 && this.board[row][col - i].isSpaceEmpty() == true)
				{
					this.board[row][col - i].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));
				}

				if(secondRow != (row + i) &&
				(row + i) < 10 &&
				this.board[row + i][col].isSpaceEmpty() == true){
					this.board[row + i][col].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));
				}else if((row + i) < 10 &&
						this.board[row + i][col].isSpaceEmpty() == true){
					this.board[row + i][col].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));
				}

				if(secondRow != (row - i) &&
				  (row - i) >= 0 &&
				  this.board[row - i][col].isSpaceEmpty() == true){
					this.board[row - i][col].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));
				}else if((row - i) >= 0 && this.board[row - i][col].isSpaceEmpty() == true){
					this.board[row - i][col].setIcon(new ImageIcon(getClass().getResource("/images/Space.jpg")));			  
				}			
			}
		}		
	}

	/**
	 * Will display possible spots to place your ship
	 * 
	 * When placing a ship this will display possible spots to plae your ships,
	 * if you un-select your ship it will remove the possible places. Once a
	 * ship has been placed this method will fire and remove the remainder 
	 * of the possible places.
	 * 
	 * @param firstClick Coordinates of the players first selection.
	 * @param secondClick Coordinates of the players second selection.
	 * @param shipPlaces The length of the ship.
	 * @return isOk If the ship is valid returns True, else False. 
	 */
	public boolean validateClick(String firstClick, String secondClick, int shipPlaces)
	{
		boolean isOk = true;	
		int row = Integer.parseInt(Character.toString(firstClick.charAt(0)));
		int col = Integer.parseInt(Character.toString(firstClick.charAt(1)));

		if(firstClick.charAt(0) == secondClick.charAt(0)) //check if rows are equal
		{			
			for(int i=0; i < shipPlaces; i++)
			{
				if(firstClick.charAt(1) < secondClick.charAt(1))
				{
					if((col+i) >=10)
						return false;

					if(this.board[row][col+i].isSpaceEmpty() != true){
						isOk = false;
						break;
					}
				}
				else
				{
					if((col - i) < 0)
						return false;

					if(this.board[row][col-i].isSpaceEmpty() != true){
						isOk = false;
						break;
					}
				}
			}
		}
		else if(firstClick.charAt(1) == secondClick.charAt(1))//else the columns are equal 
		{
			for(int i=0; i < shipPlaces; i++)
			{
				if(firstClick.charAt(0) < secondClick.charAt(0))
				{
					if((row+i) >= 10)
						return false;

					if(this.board[row+i][col].isSpaceEmpty() != true){
						isOk = false;
						break;
					}
				}
				else
				{
					if((row - i) < 0)
						return false;

					if(this.board[row-i][col].isSpaceEmpty() != true){
						isOk = false;
						break;
					}
				}
			}
		}
		else{
			isOk = false;
		}
		return isOk;
	}


	/**
	 * Check if a ship can fit in the desired location.
	 *
	 * @param valPl The user input.
	 * @param shipPlaces The number of places the ship will take up.
	 * @return isOk If the ship can fit it will return true.
	 */
	public boolean validatePiece(char [] valPl, int shipPlaces)
	{
		int row = this.getIntPlace(valPl[0]);
		int col = Integer.parseInt(Character.toString(valPl[1]));
		String shipPos = Character.toString(valPl[2]);

		boolean isOk = true;

		try{
			if(shipPos.equals("h") || shipPos.equals("H"))
			{
				for(int i=0; i < shipPlaces; i++)
				{
					if(this.board[row][col+i].isSpaceEmpty() != true)
					{
						isOk = false;
						break;
					}
				}	
			}else{
				for(int i=0; i < shipPlaces; i++)
				{
					if(this.board[row+i][col].isSpaceEmpty() != true)
					{
						isOk = false;
						break;
					}
				}
			}
		}catch(Exception e){
			isOk = false;
			e.getMessage();
		}

		return isOk;
	}


	/**
	 * Simply will print the board for the player or opponent.
	 * 
	 * @param isGameMode If game mode is true the opponent board will print without the ships.
	 * @return n/a
	 */
	public void printBoard(boolean isGameMode)
	{
		char [] letter = {'A','B','C','D','E','F','G','H','I','J'};
		boolean isTopBar = false;

		for(int row=0; row < this.board.length; row++)
		{	
			if(isTopBar == false){
				for(int i=0; i < 10; i++){
					System.out.print("  |"+i);	
				}			
				isTopBar = true;
			}

			System.out.println(" ");
			System.out.print(letter[row]+"  |");


			if(isGameMode == false)
			{
				for(int col=0; col < this.board[row].length; col++)
				{

					if(this.board[row][col].getOccupyingShip() == ' ')
					{
						System.out.print("-  |");
					}
					else{
						System.out.print(" "+this.board[row][col].getOccupyingShip()+" |");
					}	
				}
			}else{
				//Game mode on....
				for(int col=0; col < this.board[row].length; col++)
				{

					if(this.board[row][col].getOccupyingShip() == ' ' && this.board[row][col].isMiss() == false)
					{
						System.out.print("-  |");
					}
					else if(this.board[row][col].isHit() == true)
					{
						System.out.print(" X |");
					}
					else if(this.board[row][col].isMiss() == true)
					{
						System.out.print(" O |");
					}
					else
					{
						System.out.print("-  |");
					}
				}
			}	
		}
	}

	/**
	 * This will change a letter to a number.
	 * 
	 * @param  letter 
	 * @return cord The corresponding value for the letter
	 */
	public int getIntPlace(char letter)
	{
		int cord = 0;	
		switch(letter)
		{
			case 'a': cord = 0; break;
			case 'b': cord = 1; break;
			case 'c': cord = 2; break;
			case 'd': cord = 3; break;
			case 'e': cord = 4; break;
			case 'f': cord = 5; break;
			case 'g': cord = 6; break;
			case 'h': cord = 7; break;
			case 'i': cord = 8; break;	
			case 'j': cord = 9; break;
			case 'A': cord = 0; break;
			case 'B': cord = 1; break;
			case 'C': cord = 2; break;
			case 'D': cord = 3; break;
			case 'E': cord = 4; break;
			case 'F': cord = 5; break;
			case 'G': cord = 6; break;
			case 'H': cord = 7; break;
			case 'I': cord = 8; break;	
			case 'J': cord = 9; break;
		}
		return cord;
	}

	/* Getters and Setters */
	public Singlespace[][] getBoard() {
		return board;
	}
	public void setBoard(Singlespace[][] board) {
		this.board = board;
	}
}