import java.io.IOException;
import javax.swing.ImageIcon;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

public class Animation implements Runnable
{
	private Thread animationThread;
	private int imgCounter=0;	
	private int row;
	private int col;
	public ImageIcon[] shipEffect = new ImageIcon[3];
	public Player player;
	public ImageIcon tmpImage;
	
	public Animation(){
		this.player = new Player();
		this.row = 0;
		this.col = 0;
	}
	
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
			
	@Override
	public void run() 
	{
		Thread myThread = Thread.currentThread();
		
		while(this.animationThread == myThread)
		{
			System.out.println("In RUN METHOD!!!" + imgCounter);
			
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
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
				return;
			}
		}	
	}
	
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