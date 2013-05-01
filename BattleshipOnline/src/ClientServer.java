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
	
	
	public ClientServer(){
		this.server = null;
		this.playerOne = new Client();
		this.playerTwo = new Client();
	}

	public void StartListening() throws IOException
	{
		try {			
			server = new ServerSocket(PORT);
		
			while(true){
				Socket playerSocket;
				
				Client clientOne = new Client();
				Client clientTwo = new Client();
												
				//Will stay in here until both clients are connected...
				while(true){
					
					playerSocket = server.accept();
					
					if(clientOne.client.isConnected() != true)
					{
						System.out.println("Established Connection PlayerOne");
						clientOne = new Client(playerSocket);
						clientOne.setName("playerOne");					
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
						
						break;
					}
					Thread.sleep(2500);									
				}
				Thread.sleep(2500);					
			}
			
		} catch (IOException e) {
			server.close();
			e.printStackTrace();
		} catch (InterruptedException e) {
			server.close();
			e.printStackTrace();
		}
	}	
		

	public class Client implements Runnable{
		private String name = "";
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
				out = new PrintWriter(clientTwo.client.getOutputStream(), true);
			
			} catch (IOException e1) {
				try {
					in.close();
					out.close();
					client.close();
					clientTwo.client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
				
				e1.printStackTrace();
			}
			
			while(true){	
				try{		
					line = in.readLine();
										
					if(line.equals(null)){
						Thread.sleep(500);
					}else{
						out.println(line);
					}
					
				}catch(Exception e){
					try {
						in.close();
						out.close();
						client.close();
						
						clientTwo.client.close();
						
					} catch (IOException e1) {
						e1.printStackTrace();
						break;
					}
					
					System.out.println(e.getMessage());
					break;
				}
			}
		}
		
		public void finalize(){
			try {
				client.shutdownInput();
				client.shutdownOutput();
				client.close();
				
				clientTwo.client.shutdownInput();
				clientTwo.client.shutdownOutput();
				clientTwo.client.close();
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