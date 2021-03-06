import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Set up connection with clients.
 * 
 * This class will handle all online game play,
 * it will wait for two players to join. Once two players
 * have been matched it will proceed to spawn threads for these clients.
 * This server will handle concurrent online game play, many online games 
 * can take place.
 * 
 * @author Mike Cutalo
 * @version 1.0
 */
public class ClientServer{
	
	/** The server connection */
	private ServerSocket server = null;
	/** My port to establish TCP connection with client */
	private final int PORT = 15009;
		
	/**
	 * Constructs a new ClientSrver object.
	 */
	public ClientServer(){
		this.server = null;
	}

	/**
	 * Starts listening for two players to join.
	 * 
	 * This method will wait for two players to join a game,
	 * once two players have joined then they will be paired up 
	 * and the online game will proceed. But if two more players
	 * decide to play an online game the same process will occur.
	 * Thus allowing many online games to be played at once. 
	 * 
	 * @throws IOException
	 */
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
			
	/**
	 * This class represents a client.
	 * 
	 * A client is the online player, in this class
	 * a reference to the opponent player is stored,
	 * so I may send data to this player. This class will
	 * handle any and all transaction of data during an 
	 * instance of the online game.
	 * 
	 * @author Mike Cutalo
	 * @version 1.0
	 */
	public class Client implements Runnable{
		/** Name of the Client - for testing purposes*/
		private String name = "";
		/** The socket connection for this client */
		private Socket client;
		/** The opponent*/
		private Client clientTwo;

		/**
		 * Constructs a new socket
		 */
		Client(){
			this.client = new Socket();
		}

		/**
		 * Constructs a new socket with the connected player socket.
		 * @param client
		 */
		Client(Socket client){
			this.client = client;
		}
		
		/**
		 * Thread code for a client.
		 * 
		 * This run method will handle data transfer to
		 * and from the client. It will stay in the while loop
		 * Until the game has been ended. When the game is over
		 * it will break down the connection and dispose of this
		 * thread.
		 * 
		 * @throws IOException 
		 * @throws Exception
		 */
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