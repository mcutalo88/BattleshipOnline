import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import sun.audio.*;


public class Sound extends Game implements Runnable
{
	private Thread soundThread = new Thread(this,"Sound Thread");
	public AudioClip clip = null;	
	
	public Sound(){
		
	}
	
	public void BattleStations(){
		clip = Applet.newAudioClip(getClass().getResource("/sounds/BattleStations.au"));
		this.soundThread.start();
	}
	
	public void ShipHit() throws MalformedURLException, FileNotFoundException{
        
		System.out.println("Playing ShipHit");
		//clip = Applet.newAudioClip(getClass().getResource("/sounds/Earthagain.au"));
		//clip = Applet.newAudioClip(getClass().getResource("/sounds/24thCentury.au"));
		//clip = Applet.newAudioClip(getClass().getResource("/sounds/Apologize.au"));
		//clip = Applet.newAudioClip(getClass().getResource("/sounds/Authorization.au"));
		//clip = Applet.newAudioClip(getClass().getResource("/sounds/UnderControl.wav"));
		
		clip = Applet.newAudioClip(getClass().getResource("/sounds/FIRE.wav"));
		
		
		this.soundThread.start();
//					
//	     new Thread() {
//	            public void run() {
//	                try {
//	        			System.out.println("Playing ShipHit In Thread");
//
//	        			clip.play();
//	                	//hitSound.play();
//	                	//hitSound.loop();
//	                }
//	                catch (Exception e) { System.out.println(e); }
//	            }
//	        }.start();


	}
	
	public void ShipMissed()
	{
	
	}

	@Override
	public void run() {
		clip.play();
		
	}
	

}
