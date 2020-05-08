package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class ClientHandler implements Observer, Runnable {
    private Server server;
    private Socket clientSocket;
    public PrintWriter toClient;
    private BufferedReader fromClient;
    private Gson gson;
    private GsonBuilder builder;
    
    public ClientHandler(Server server1, Socket clientSocket1) {
		clientSocket = clientSocket1;
		server = server1;
		builder = new GsonBuilder();
    	gson = builder.create();
    	try {
			toClient = new PrintWriter(this.clientSocket.getOutputStream());
			fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		}catch(IOException e) {System.out.println("Something went wrong in ClientHandler");}
    }

    public void run() { 
    	initializeClientServer();
    	String message;
    	try {
			while((message = fromClient.readLine()) != null) {
				System.out.println("From client: " + message);
				server.process(message);
			}
    	}catch (IOException e){e.printStackTrace();}
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		toClient.println((String) arg1);		
	}
	
	public void initializeClientServer() {
		String toJSON = gson.toJson(server.auctions, Item[].class);
		toClient.println(toJSON);
		toClient.flush();
	}
	
	
	public void clientBid() {
		
	}
	
	public void clientBought(){
		
	}
	
}

	