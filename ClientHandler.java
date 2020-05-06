package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    }

    public void run() {
    	/*try {
			toClient = new PrintWriter(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	toClient.println("Can you hear me?");
    	toClient.flush();
    	System.out.println("Sending message");
    	*/
    	
    	builder = new GsonBuilder();
    	gson = builder.create();
		try {
			toClient = new PrintWriter(this.clientSocket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Something went wrong in ClientHandler");
		}
		String toJSON = gson.toJson(server.auctions, Item[].class);
		toClient.println(toJSON);
		toClient.flush();
		System.out.println(toJSON);
		
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		toClient.println((String) arg1);		
	}
}
