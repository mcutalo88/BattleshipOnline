import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This will handle Player vs. Player Game Play.
 * 
 * Handling all interaction with the socket, this class will
 * send and retrieve all data necessary for online game play.
 * 
 * @author Mike Cutalo
 * @version 1.0
 */
public class PVP implements Runnable{
	
	/** The server that the client will connect to.*/
	private final String SERVERNAME = "bill.kutztown.edu";
	/** The port the client will be directed to set up a connection */
	private final int PORT = 15009;	
	/** Will output data to the server to be sent to the other client */
	private PrintWriter out;
	/** Read in all information sent by the server */
	private BufferedReader in;
	
	/** A thread that will sit and wait for server data */
	private Thread runClient;
	/** The local player in the game */
	private Player localPlayer;
	/** The online player in the game*/
	private Player remotePlayer;
	/** The socket connection for this client */
	private Socket socket;
	/** the online turn object, this holds the state of the game play */
	private Turn onLineTurn;
	
	private boolean isSocketClosed = false;
	
	
	/**
	 * Constructs a new local and remote player
	 * at the creation of this object.
	 */
	public PVP(){
		localPlayer = new Player();
		remotePlayer = new Player();
	}
	
	/**
	 * Connect client to server.
	 * 
	 * This will connect the client to the server,
	 * set up in and out streams and will call the getData() method
	 * which will start listening for server output.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void connetToServer() throws UnknownHostException, IOException, InterruptedException
	{	
		socket = new Socket(SERVERNAME, PORT);		
		System.out.println("Starting connection ...");
		
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
		getData();				
	}
	
	/**
	 * Start a new thread for online
	 * 
	 * Will start a new thread that will sit and
	 * listen for server data.
	 * 
	 * @throws InterruptedException
	 */
	public void getData() throws InterruptedException{
		runClient = new Thread(this,"ClientConnection");
		runClient.start();
	}
	
	/**
	 * Sends player board to opponent.
	 * 
	 * This will simply send the local players board
	 * to the opponent. When it is done it will send the value
	 * 00x which indicates the board data was all sent.
	 */
	public void sendBoardData()
	{
		System.out.println("Sending Board data now...");
		
		for(int i=0; i < 10; i++)
		{
			for(int j=0; j < 10; j++)
			{
				if(localPlayer.getPlayerBoard().getBoard()[i][j].getOccupyingShip() == ' '){
					out.println(i+""+j+"E");
				}else{
					out.println(i+""+j+""+localPlayer.getPlayerBoard().getBoard()[i][j].getOccupyingShip());	
				}				
			}
		}
		out.println("00x");
	}
	
	/**
	 * Sends data over the socket to opponent.
	 * 
	 * @param data String to send to online opponent.
	 */
	public void sendData(String data){
		out.println(data);
	}

	/**
	 * Creates new ships for online opponent.
	 * 
	 * Once the online player has sent over all
	 * board data, this method will run and create new
	 * ships. I do this because it is simpler and more efficient
	 * than sending over ship data.
	 * 
	 */
	public void createShips(){
		
		int [] shipMaxHit = {5,4,3,3,2};
		char [] shipInit = {'A','B','S','D','P'};
		String [] shipType ={"Aircraft Carrier", "BattleSship","Submarine","Destroyer","Patrol Boat"};		
		Ship newShip;
		
		for(int i=0; i<5; i++){
			
			newShip = new Ship();
			
			newShip.setMaxHit(shipMaxHit[i]);
			newShip.setBoatInit(shipInit[i]);
			newShip.setBoatType(shipType[i]);
			
			remotePlayer.allShips.put(shipInit[i], newShip);
		}							
	}
		
	/**
	 * Closes the socket connection.
	 * 
	 * @throws IOException
	 */
	public void closeSocket(){
		if(isSocketClosed == false){
			try {
				this.in.close();
				this.out.close();
				
				this.socket.close();
				this.runClient.interrupt();
				this.runClient = null;
	
				isSocketClosed = true;
				System.out.println("Closing the socket");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	/**
	 * Starts the Game.
	 * 
	 * Once Both players are ready, this method will be called to start
	 * the game.
	 * 
	 * @throws InterruptedException
	 */
	public void startTurnListen(){	
		while(onLineTurn == null){
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.onLineTurn.getThisGame().TellUser("Starting Game");
		onLineTurn.setOnLineGame(this);
		onLineTurn.startListening();
	}
	
	/**
	 * When thread is started this code will execute.
	 * 
	 * Handles all client server transaction, sending and
	 * Retrieving data in the forms of strings.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void run(){
		String inputData;
		boolean boardDataDone = false;
		
		while(true){
			try {
				inputData = in.readLine();
				
				if(inputData.equals(null)){
					Thread.sleep(500);
				}else{
					
					System.out.println("Data from server:" + inputData);
					
					int row = Integer.parseInt(String.valueOf(inputData.charAt(0)));
					int col = Integer.parseInt(String.valueOf(inputData.charAt(1)));
					char key = inputData.charAt(2);
					
					if(boardDataDone == false){	
						if(key == 'A'){
							remotePlayer.getPlayerBoard().getBoard()[row][col].setOccupyingShip('A');
							remotePlayer.getPlayerBoard().getBoard()[row][col].setSpaceEmpty(false);
						}else if(key == 'B'){
							remotePlayer.getPlayerBoard().getBoard()[row][col].setOccupyingShip('B');
							remotePlayer.getPlayerBoard().getBoard()[row][col].setSpaceEmpty(false);
						}else if(key == 'S'){
							remotePlayer.getPlayerBoard().getBoard()[row][col].setOccupyingShip('S');
							remotePlayer.getPlayerBoard().getBoard()[row][col].setSpaceEmpty(false);
						}else if(key == 'D'){
							remotePlayer.getPlayerBoard().getBoard()[row][col].setOccupyingShip('D');
							remotePlayer.getPlayerBoard().getBoard()[row][col].setSpaceEmpty(false);
						}else if(key == 'P'){
							remotePlayer.getPlayerBoard().getBoard()[row][col].setOccupyingShip('P');
							remotePlayer.getPlayerBoard().getBoard()[row][col].setSpaceEmpty(false);
						}else if(key == 'x'){
							createShips();
							boardDataDone = true;
							startTurnListen();
						}
					}
									
					if(key == 'H'){						
						this.getOnLineTurn().onLinePlayerHit(row, col);
					}else if(key == 'M'){
						this.getOnLineTurn().onLinePlayerMiss(row, col);
					}else if(key == 'Z'){
						System.out.println("Closing Socket from run..");
						closeSocket();
						break;
					}
				}
			} catch (IOException e) {	
				closeSocket();			
				e.printStackTrace();
			} catch (InterruptedException e) {
				closeSocket();
				e.printStackTrace();
			}
		}
	}
	
	//Getters & Setters//
	public Player getLocalPlayer() {
		return localPlayer;
	}
	public void setLocalPlayer(Player localPlayer) {
		this.localPlayer = localPlayer;
	}
	public Player getRemotePlayer() {
		return remotePlayer;
	}
	public void setRemotePlayer(Player remotePlayer) {
		this.remotePlayer = remotePlayer;
	}
	public Turn getOnLineTurn() {
		return onLineTurn;
	}
	public void setOnLineTurn(Turn onLineTurn) {
		this.onLineTurn = onLineTurn;
	}
}