import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/** 
 * The AI class extends from the player class,
 * inheriting all attributes of a player. This class
 * will have a computer take a turn to attack the 
 * human board. This class will also place and validate
 * the ship placements of the AI as well. 
 * 
 * @author Mike Cutalo
 * @version 2.0
 */ 
public class AI extends Player
{
	/** Has the AI hit a ship. */
	private boolean isAiHit = false;
	/** If the AI hits a ship two times it will be set to the direction the second hit occurred.*/
	private int aiDoubleHit = 0;
	/** Last good AI row hit. */
	private int hitROW = 0;
	/** Last good AI col hit. */
	private int hitCOL = 0;

	/** If boat didnt sink this is the first AI row hit. */
	private int OGhitROW = 0;
	/** If boat didnt sink this is the first Last good AI col hit. */
	private int OGhitCOL = 0;
	/** The last direction the computer was attacking*/
	private int oldDoubleHit=0;
	/** The current ship the computer is trying to sink*/
	private char currentShipAttacking=' ';

	/**
	 * Calls the Super class constructor,
	 * in this case the Player Class.
	 */
	public AI()
	{
		super();
	}

	/**
	 * Will generate a hit for the AI
	 *
	 *This method will generate a hit for the AI, it will check to make sure
	 *this hit is in the bounds of the gird. If it has hit a ship it will remember that spot,
	 *if it is a double hit it will traverse and try to sink this ship it currently hit. 
	 *Also this method will check on every hit, if all ships have been sunk so it may end the game
	 *and announce the winner.
	 *
	 * @param n/a
	 * @return n/a
	 */
	public void aITurn(Player enemy)
	{
		Random randNum = new Random();

		int row = this.hitROW, col = this.hitCOL;
		char shipHit;
		boolean shotOK = false;


		if(this.isAiHit == true)
		{	
			if(currentShipAttacking == ' ')
			{
				this.OGhitROW = row;
				this.OGhitCOL = col;
			}

			if(this.aiDoubleHit == 1)
			{
				row = this.hitROW +1;
			}
			else if(this.aiDoubleHit == 2)
			{
				col = this.hitCOL +1;
			}
			else if(this.aiDoubleHit == 3)
			{
				row = this.hitROW - 1;
			}
			else if(this.aiDoubleHit == 4)
			{
				col = this.hitCOL - 1;
			}
			else
			{
				if(row+1 < 10 && enemy.getPlayerBoard().getBoard()[row+1][col].isSpaceEmpty()==false) //down
				{
					row = this.hitROW + 1;
					this.currentShipAttacking = enemy.getPlayerBoard().getBoard()[row][col].getOccupyingShip();
					this.aiDoubleHit = 1;
					this.oldDoubleHit = 1;
				}
				else if(col+1 < 10 && enemy.getPlayerBoard().getBoard()[row][col+1].isSpaceEmpty()==false)//right
				{
					col = this.hitCOL + 1;
					this.currentShipAttacking = enemy.getPlayerBoard().getBoard()[row][col].getOccupyingShip();
					this.aiDoubleHit = 2;
					this.oldDoubleHit = 2;
				}
				else if(row-1 > 0 && enemy.getPlayerBoard().getBoard()[row-1][col].isSpaceEmpty()==false)//up
				{
					row = this.hitROW - 1;
					this.currentShipAttacking = enemy.getPlayerBoard().getBoard()[row][col].getOccupyingShip();
					this.aiDoubleHit = 3;
					this.oldDoubleHit = 3;
				}else if(col-1 > 0 && enemy.getPlayerBoard().getBoard()[row][col-1].isSpaceEmpty()==false)//left
				{
					col = this.hitCOL - 1;
					this.currentShipAttacking = enemy.getPlayerBoard().getBoard()[row][col].getOccupyingShip();
					this.aiDoubleHit = 4;
					this.oldDoubleHit = 4;
				}

			}

			if(row >= 10 || col >= 10 || row < 0 || col < 0 ||
				enemy.getPlayerBoard().getBoard()[row][col].isHit() == true ||
				enemy.getPlayerBoard().getBoard()[row][col].isMiss() == true){

				while(shotOK == false)
				{
					row = randNum.nextInt(10);
					col = randNum.nextInt(10);
					this.hitCOL = 0;
					this.hitROW = 0;

					if(enemy.getPlayerBoard().getBoard()[row][col].isHit() == false &&
					   enemy.getPlayerBoard().getBoard()[row][col].isMiss() == false){
						shotOK = true;
					}
				}
			}
		} 
		else
		{				
			if(currentShipAttacking != ' ')
			{
				if(enemy.getAllShips().get(currentShipAttacking).isAlive() == true)
				{
					row = this.OGhitROW;
					col = this.OGhitCOL;

					if(this.oldDoubleHit == 1)
					{
						row = this.OGhitROW - 1;
						this.aiDoubleHit = 3;
					}
					else if(this.oldDoubleHit == 2)
					{
						col = this.OGhitCOL - 1;
						this.aiDoubleHit = 4;
					}
					else if(this.oldDoubleHit == 3)
					{
						row = this.OGhitROW +1;
						this.aiDoubleHit = 1;
					}
					else if(this.oldDoubleHit == 4)
					{
						col = this.OGhitCOL +1;
						this.aiDoubleHit = 2;
					}					
				}
				else
				{
					this.OGhitCOL =0;
					this.OGhitROW =0;
					this.oldDoubleHit = 0;
					currentShipAttacking = ' ';
				}
			}

			if(row >= 10 || col >= 10 || row < 0 || col < 0 ||
			   enemy.getPlayerBoard().getBoard()[row][col].isHit() == true ||
			   enemy.getPlayerBoard().getBoard()[row][col].isMiss() == true){

				while(shotOK == false)
				{
					row = randNum.nextInt(10);
					col = randNum.nextInt(10);
					this.hitCOL = 0;
					this.hitROW = 0;

					if(enemy.getPlayerBoard().getBoard()[row][col].isHit() == false &&
					   enemy.getPlayerBoard().getBoard()[row][col].isMiss() == false){
						shotOK = true;
					}
				}
			}
		}

		BufferedImage tmpImage;
		try{
		if(enemy.getPlayerBoard().getBoard()[row][col].getOccupyingShip() == ' ')
		{
			enemy.getPlayerBoard().getBoard()[row][col].setMiss(true); //Change single space to a miss on human board

			tmpImage = ImageIO.read(getClass().getResource("/images/black.jpg"));
			enemy.getPlayerBoard().getBoard()[row][col].setIcon(new ImageIcon(tmpImage));	//Set human board with miss image

			this.numTurns += 1;
			this.numMissed += 1;

			this.isAiHit = false;
			this.aiDoubleHit = 0;
			this.hitCOL = 0;
			this.hitROW = 0;

			enemy.checkShips(); 

			if(currentShipAttacking != ' ')
			{
				if(enemy.getAllShips().get(currentShipAttacking).isAlive() == false)
				{
					this.isAiHit = false;
					this.aiDoubleHit = 0;
					this.hitCOL = 0;
					this.hitROW = 0;

					this.OGhitCOL =0;
					this.OGhitROW =0;
					this.oldDoubleHit = 0;
					this.currentShipAttacking = ' ';
				}	
			}

		}
		else //We got a hit
		{
			//Changing board icon
			tmpImage = ImageIO.read(getClass().getResource("/images/hit.jpg"));					
			enemy.getPlayerBoard().getBoard()[row][col].setIcon(new ImageIcon(tmpImage));

			enemy.getPlayerBoard().getBoard()[row][col].setHit(true); //Change human board single space to hit
			shipHit = enemy.getPlayerBoard().getBoard()[row][col].getOccupyingShip(); //Get the occupyingShip of that space
			enemy.getAllShips().get(shipHit).setSumHit(enemy.getAllShips().get(shipHit).getSumHit() + 1); //Increment the sum hit on the ship

			Animation hitShip = new Animation();
			hitShip.setPlayer(enemy);
			hitShip.shipSinking(row, col);
			
			
			this.isAiHit = true;
			this.hitROW = row;
			this.hitCOL = col;

			this.numTurns += 1;
			this.numHits += 1;

			enemy.checkShips();

			if(currentShipAttacking != ' ')
			{
				if(enemy.getAllShips().get(currentShipAttacking).isAlive() == false)
				{
					this.isAiHit = false;
					this.aiDoubleHit = 0;
					this.hitCOL = 0;
					this.hitROW = 0;

					this.OGhitCOL =0;
					this.OGhitROW =0;
					this.oldDoubleHit = 0;
					this.currentShipAttacking = ' ';
				}	
			}
		}
		}catch(IOException err){
			err.getStackTrace();
		}
	}

	/**
	 * Place ships for the AI
	 *
	 *This method will decide where a ship will be placed
	 *randomly choosing row and column as well as orientation.
	 *As input in generated it will validate it make sure it is valid.
	 *
	 * @param aIShip Ship that is ready to be placed
	 */
	public void placeAIShips(Ship aIShip)
	{
		Random randNum = new Random();
		int row, col, orientation;
		char ori;

		row = randNum.nextInt(10);
		col = randNum.nextInt(10);
		orientation = randNum.nextInt();

		if((orientation % 2) == 0){
			ori='h'; //even will be Horizontal
		}else{
			ori='v'; //odd will be vertical
		}

		while(this.validateAIPiece(row,col,aIShip.getMaxHit(),ori) == false)
		{
			row = randNum.nextInt(10);
			col = randNum.nextInt(10);
			orientation = randNum.nextInt();

			if((orientation % 2) == 0){
				ori='h';
			}else{
				ori='v';
			}	
		}

		if(ori == 'h') 
		{
			for(int j=0; j < aIShip.getMaxHit(); j++)
			{
				this.playerBoard.getBoard()[row][col+j].setSpaceEmpty(false);
				this.playerBoard.getBoard()[row][col+j].setOccupyingShip(aIShip.getBoatInit());
			}
		}
		else
		{ 
			for(int j=0; j < aIShip.getMaxHit(); j++)
			{
				this.playerBoard.getBoard()[row+j][col].setSpaceEmpty(false);
				this.playerBoard.getBoard()[row+j][col].setOccupyingShip(aIShip.getBoatInit());
			}
		}

		aIShip.setPlayerOwner(this.getPlayerName());
		this.allShips.put(aIShip.getBoatInit(), aIShip); 			
	}

	/**
	 *Validate ship placement for the AI
	 *
	 * @param row
	 * @param col
	 * @param shipPlaces Places the ship will take up
	 * @param ori Orientation 
	 * @return n/a
	 */
	private boolean validateAIPiece(int row, int col, int shipPlaces, char ori)
	{		
		boolean isOk = true;

		try{
			if(ori == 'h' || ori == 'H')
			{
				for(int i=0; i < shipPlaces; i++)
				{
					if(this.playerBoard.getBoard()[row][col+i].isSpaceEmpty() != true)
					{
						isOk = false;
						break;
					}
				}	
			}else{
				for(int i=0; i < shipPlaces; i++)
				{
					if(this.playerBoard.getBoard()[row+i][col].isSpaceEmpty() != true)
					{
						isOk = false;
						break;
					}
				}
			}
		}
		catch(Exception e){
			isOk = false;
			e.getMessage();
		}
		return isOk;
	}	
}