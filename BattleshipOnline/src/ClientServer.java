import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Set up connection with clients.
 * 
 * @author Mike Cutalo
 * @version 1.0
 */
public class ClientServer{
	
	/** The server connection */
	private ServerSocket server = null;
	/** My port to establish TCP connection with client */
	private final int PORT = 15009;
	
	private Client playerOne;
	private Client playerTwo;
	private boolean isNewGame;
	
	
	public ClientServer(){
		isNewGame = true;
		server = null;
		this.playerOne = new Client();
		this.playerTwo = new Client();
	}

	public void StartListening()
	{
		try {			
			server = new ServerSocket(PORT);
		
			while(true){
				Socket playerSocket;
				
				Client clientOne = new Client();
				Client clientTwo = new Client();
								
//				if(isNewGame == true){
//					clientOne = new Client();
//					clientTwo = new Client();			
//				}
								
				while(true){
					
					playerSocket = server.accept();
					
					if(clientOne.client.isConnected() != true)
					{
						System.out.println("Established Connection PlayerOne");
						clientOne = new Client(playerSocket);
						clientOne.setName("playerOne");					
	
						//isNewGame = false;
					}
					else if(clientOne.client.isConnected() == true)
					{
						System.out.println("Established Connection PlayerTwo");
						clientTwo = new Client(playerSocket);
						clientTwo.setName("playerTwo");
						
						//Bother players are connected so now sent them each other 
						clientOne.setClientTwo(clientTwo);
						clientTwo.setClientTwo(clientOne);
						
						//after things are ready start Both threads...
						Thread t1 = new Thread(clientOne);
						t1.start();
						
						Thread t2 = new Thread(clientTwo);
						t2.start();
										
						//isNewGame = true;
						
						break;
					}
					Thread.sleep(2500);									
				}
				Thread.sleep(2500);					
			}
			
		} catch (IOException e) {
			closeServer();
			e.printStackTrace();
		} catch (InterruptedException e) {
			closeServer();
			e.printStackTrace();
		}
	}	
	
	
	
//	public void StartListening()
//	{
//		try {
//			
//			server = new ServerSocket(PORT);
//		
//			while(true){
//				Socket playerSocket;
//	
//				playerSocket = server.accept();
//				
//				if(playerOne.client.isConnected() != true)
//				{
//					System.out.println("Established Connection PlayerOne");
//					playerOne = new Client(playerSocket);
//					playerOne.setName("playerOne");
//					
//				}
//				else if(playerOne.client.isConnected() == true)
//				{
//					System.out.println("Established Connection PlayerTwo");
//					playerTwo = new Client(playerSocket);
//					playerTwo.setName("playerTwo");
//					
//					//Bother players are connected so now sent them each other 
//					playerOne.setClientTwo(playerTwo);
//					playerTwo.setClientTwo(playerOne);
//					
//					//after things are ready start Both threads...
//					Thread t1 = new Thread(playerOne);
//					t1.start();
//					
//					Thread t2 = new Thread(playerTwo);
//					t2.start();
//					
//				}
//				Thread.sleep(2500);
//			}
//			
//		} catch (IOException e) {
//			closeServer();
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			closeServer();
//			e.printStackTrace();
//		}
//	}
	
	public void closeServer(){
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class Client implements Runnable{
		private String name ="";
		private Socket client;
		private Client clientTwo;

		Client(){
			this.client = new Socket();
		}

		Client(Socket client){
			this.client = client;
		}
		
		public void run(){
			String line;
			BufferedReader in = null;
			PrintWriter out = null;
			
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));				
				//This should send to the OTHER client 
				out = new PrintWriter(clientTwo.client.getOutputStream(), true);
			
			} catch (IOException e1) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				e1.printStackTrace();
			}
			
			//System.out.println("Setting Up Streams" + this.name);
			
			while(true){	
				try{		
					line = in.readLine();
										
					if(line.equals(null)){
						System.out.println("Sleep ....");
						Thread.sleep(500);
					}else{
						//System.out.println("Something From user : " + line);
						out.println(line);
					}				
				}catch(Exception e){
					try {
						client.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println(e.getMessage());
				}
			}
		}
		
		public void finalize(){
			try {
				client.close();
				System.out.println("Server Closed" + this.name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Getters && Setters //
		public Client getClientTwo() {
			return clientTwo;
		}
		public void setClientTwo(Client clientTwo) {
			this.clientTwo = clientTwo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		ClientServer gameServer = new ClientServer();
		gameServer.StartListening();
	}
}