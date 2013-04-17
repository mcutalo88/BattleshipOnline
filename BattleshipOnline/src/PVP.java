import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This will handle Player vs. Player Game Play.
 * 
 * 
 * @author Mike Cutalo
 */
public class PVP extends Turn{
	private final String SERVERNAME = "bill.kutztown.edu";
	private final int PORT = 15009;
	
	public PVP(){
		
	}
	
	public void userSocket() throws UnknownHostException, IOException, InterruptedException{
		
		Socket socket = new Socket(SERVERNAME, PORT);		
		System.out.println("Starting connection ...");
		
		Thread.sleep(1000);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		System.out.println("Server:Out Stream " + out);
		
		Thread.sleep(1000);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Server: In Stream " +in);
	}
}
