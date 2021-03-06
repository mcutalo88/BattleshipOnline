import java.io.IOException;
import javax.swing.ImageIcon;

/**
 * This class will handle picture animations.
 * 
 * This class is responsible for one animation
 * and that is the picture animation.
 * 
 * @version 1.0
 * @author Mike Cutalo
 */
public class Animation implements Runnable
{
	/** The thread the animation runs in */
	private Thread animationThread;
	/** Current image being displayed */
	private int imgCounter=0;	
	/** Row to animate on */
	private int row;
	/** Col to animate on */
	private int col;
	/** Array of images for animation effect */
	public ImageIcon[] shipEffect = new ImageIcon[3];
	/** The player that the animation will take place on */
	public Player player;
	/** Temporary icon holder */
	public ImageIcon tmpImage;
	
	/**
	 * Constructs new animation object,
	 * sets player, row and col to default values.
	 */
	public Animation(){
		this.player = new Player();
		this.row = 0;
		this.col = 0;
	}
	
	/**
	 * Starts the animation.
	 * 
	 * Will start the animation on the row and column of this
	 * objects current player. Also reading in and populating 
	 * the images, as well as starting the thread.
	 * 
	 * @param row
	 * @param col
	 * @throws IOException
	 */
	public void shipSinking(int row, int col) throws IOException
	{
		this.animationThread = new Thread(this, "Animation Thread");		
		this.row = row;
		this.col = col;
		this.shipEffect[0] = new ImageIcon(getClass().getResource("/turnImg/warn.jpg"));
		this.shipEffect[1] = new ImageIcon(getClass().getResource("/turnImg/bomb.jpg"));
		this.shipEffect[2] = new ImageIcon(getClass().getResource("/turnImg/boom.jpg"));
		
		this.animationThread.start();
	}
			
	/**
	 * The code a thread will execute.
	 * 
	 * Will run until the thread is not the current thread.
	 * Animating pictures on the current instance of the player.
	 * Also this will do some clean up on the players board before leaving,
	 * such as marking the place to hit and setting the last icon to the 
	 * hit image.
	 */
	public void run() 
	{
		Thread myThread = Thread.currentThread();
		
		while(this.animationThread == myThread)
		{	
			if(this.imgCounter < 3)
			{
				this.player.getPlayerBoard().getBoard()[this.row][this.col].setEnabled(false);
				this.player.getPlayerBoard().getBoard()[this.row][this.col].setDisabledIcon(shipEffect[imgCounter]);
		
				this.imgCounter++;
			}
			else
			{
				this.player.getPlayerBoard().getBoard()[row][col].setHit(true);	
				tmpImage = new ImageIcon(getClass().getResource("/images/hit.jpg"));
				this.player.getPlayerBoard().getBoard()[row][col].setIcon(tmpImage);
				this.player.getPlayerBoard().getBoard()[row][col].setEnabled(false);
				this.player.getPlayerBoard().getBoard()[row][col].setDisabledIcon(tmpImage);
			
				this.player.checkShips();		
				
				stopThread();
				this.imgCounter=0;
			}
								
			try{
				Thread.sleep(300);
			}catch(InterruptedException e){
				e.printStackTrace();
				return;
			}
		}	
	}
	
	/**
	 * Will set this thread to null
	 * 
	 * I do this to ensure the thread stops.
	 */
	public void stopThread(){
		this.animationThread = null;
	}

	//Getters & Setters//
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player p) {
		this.player = p;
	}
}