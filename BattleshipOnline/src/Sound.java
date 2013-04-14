import java.applet.Applet;
import java.applet.AudioClip;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.*;
import sun.audio.*;

public class Sound extends Game implements Runnable
{
	
	AudioClip hitSound = null;
	
	public Sound(){
		
	}
	
	public void ShipHit() throws MalformedURLException, FileNotFoundException{
        
		System.out.println("Playing ShipHit");
		
		//URL hit = getClass().getResource("sounds/Earth.mp3");
		//URL hit = getClass().getResource("/sounds/AuthorizationCopy.au");
		hitSound = getAudioClip(getCodeBase(), "/AuthorizationCopy.au");
		
		

		
		//URL hit = new URL(getCodeBase(), "/sounds/AuthorizationCopy.au");
		//AudioClip hitSound = getAudioClip(hit);
		//hitSound = Applet.newAudioClip(hit);
		
		

		
	     new Thread() {
	            public void run() {
	                try {
	        			System.out.println("Playing ShipHit In Thread");

	                	hitSound.play();
	                	//hitSound.loop();
	                }
	                catch (Exception e) { System.out.println(e); }
	            }
	        }.start();


	}
	
	public void ShipMissed()
	{
	
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
