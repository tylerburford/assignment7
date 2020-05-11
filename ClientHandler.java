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
				String split = " ";
				String msgArr[] = message.split(split);
				message = message.replaceFirst(msgArr[0], "");
				message = message.replaceFirst(" ", ""); //get rid of leading space
				//System.out.println("new String:" + message);
				if(msgArr[0].equals("username:"))
					tryUsername(message);
				else if(msgArr[0].equals("bid:"))
					server.processBid(message);
				else if(msgArr[0].equals("quit:")) {
					System.out.println("removing client");
					toClient.println("quit");
					toClient.flush();
					server.customers.remove(message);
					server.removeClient(this);
				}
				else
					System.out.println("invalid command");
			}
    	}catch (IOException e){e.printStackTrace();}
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		toClient.println((String) arg1);
		toClient.flush();
	}
	
	public void initializeClientServer() {
		String toJSON = gson.toJson(server.auctions, Item[].class);
		toClient.println(toJSON);
		toClient.flush();
	}
	
	public void tryUsername(String userName) {
		boolean sameNames = false;
		for(int i=0; i<server.customers.size(); i++) {
			if(server.customers.get(i).equals(userName))
				sameNames = true;
		}
		if(sameNames == false) {
			server.customers.add(userName);
			toClient.println("username: success");
			toClient.flush();
		}
		else if(sameNames == true) {
			toClient.println("username: fail");
			toClient.flush();
		}
	}
}

	