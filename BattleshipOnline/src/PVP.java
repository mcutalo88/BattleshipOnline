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
	
	private PrintWriter out;
	private BufferedReader in;
	
	public PVP(){
		
	}
	
	public void connetToServer() throws UnknownHostException, IOException, InterruptedException{
		
		Socket socket = new Socket(SERVERNAME, PORT);		
		System.out.println("Starting connection ...");
		
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		sendData("From Client : testing connection");
		//Thread.sleep(1000);
		//Thread.sleep(1000);	
		getData();
		socket.close();
	}

	public void getData() throws InterruptedException{
		String inputData;
		while(true){
			try {
				inputData = in.readLine();
				
				if(inputData.equals(null)){
					System.out.println("Sleeping GetData");
					Thread.sleep(500);
				}else{
					System.out.println("Data from server:" + inputData);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void sendData(String data){
		out.println(data);
	}
}