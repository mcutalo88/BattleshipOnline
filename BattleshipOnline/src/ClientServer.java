import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientServer{
	private ServerSocket server = null;
	//private ServerSocket clientTwo = null;
	private final int PORT = 15009;
	
	public ClientServer()
	{
		server = null;
		//clientOne = null;
		//clientTwo = null;
	}
	
	public void StartListening() throws IOException, InterruptedException
	{
		server = new ServerSocket(PORT);
		
		while(true){
			Client player;
			Socket playerSocket = server.accept();
			player = new Client(playerSocket);
			System.out.println("Established Connection");
			
			Thread t = new Thread(player);
			t.start();
			
			Thread.sleep(2500);
		}
	}
	

	public class Client implements Runnable{
		private Socket client;
		
		Client(Socket client){
			this.client = client;
		}
		
		public void run(){
			String line;
			BufferedReader in = null;
			PrintWriter out = null;

			while(true){	
				try{
					System.out.println("Setting Up Streams");
					line = in.readLine();
					System.out.println("Something From user : " + line);
					
					if(line == null){
						Thread.sleep(500);
						System.out.print("Sleep");
					}
					out.println(line);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				
			}
		}

		
		
	}

	
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		ClientServer clientS = new ClientServer();
		clientS.StartListening();
	}
}