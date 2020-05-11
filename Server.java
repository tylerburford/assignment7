package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import com.google.gson.Gson;

/*
 * Author: Vallath Nandakumar and the EE 422C instructors.
 * Data: April 20, 2020
 * This starter code assumes that you are using an Observer Design Pattern and the appropriate Java library
 * classes.  Also using Message objects instead of Strings for socket communication.
 * See the starter code for the Chat Program on Canvas.  
 * This code does not compile.
 */
public class Server extends Observable {

    static Server server;
    public Item[] auctions;
    public ArrayList<String> customers = new ArrayList<String>();
    
    public static void main (String [] args) {
        server = new Server();
        server.populateItems();
        server.SetupNetworking();
    }

    private void populateItems() {
    	Gson gson = new Gson();
    	try {
			Reader reader = new FileReader("C:\\Users\\tburf\\eclipse-workspace\\Server\\src\\server\\auctions.json");
			auctions = gson.fromJson(reader, Item[].class);
		} catch (FileNotFoundException e) {
			System.out.println("File not found! D:");
		}
    	for(int i=0; i<auctions.length; i++) {
    		auctions[i].history = new ArrayList<String>();
    	}
    	startTimers();
	}

    
	private void startTimers() {
		Thread timerDecrement = new Thread() {
			public void run() {
				while(auctions[4].time > 0) {
					for(int i=0; i<auctions.length; i++)
						if(auctions[i].time > 0)
							auctions[i].time--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Something happened while decrementing Auction timers");
					}
				}
			}
		};
		timerDecrement.start();
	}

	private void SetupNetworking() {
        try {
        	System.out.println("Server Started");
            @SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(6969);
            while (true) {
                Socket clientSocket = ss.accept();
                ClientHandler handler = new ClientHandler(this, clientSocket);
                this.addObserver(handler);
                Thread t = new Thread(handler);
                t.start();
                System.out.println("got a connection");
            }
        }catch (IOException e) {}
	}
	
	public void removeClient(ClientHandler handler) {
		this.deleteObserver(handler);
	}
	
	public void processBid(String message){
		DecimalFormat df = new DecimalFormat("#.00");
		String msgArr[] = message.split("\\+");
		String user = msgArr[0];
		String itemName = msgArr[1];
		Double bid = Double.valueOf(msgArr[2]);
		System.out.println("user: " + user + " item Name: " + itemName + " bid: "+ bid);
		
		for(int i=0; i<auctions.length; i++) {
			if(auctions[i].name.equals(itemName)){
				if(bid >= auctions[i].sellPrice) {
					auctions[i].history.add(user + " bought " + itemName + " for $" + df.format(bid) + "!\n");
					auctions[i].sold = true;
					auctions[i].highestBidder = user;
					auctions[i].bidPrice = bid;
					this.setChanged(); //not sure if this is needed
					this.notifyObservers("buy: " + user + "|" + bid + "|" + itemName);
				}
				else if(bid > auctions[i].bidPrice) {
					auctions[i].history.add(user + " bids $" + df.format(bid) + " for " + itemName + "\n");
					auctions[i].bidPrice = bid;
					auctions[i].highestBidder = user;
					this.setChanged();
					this.notifyObservers("bid: " + user + "|" + bid + "|" + itemName);
				}
			}
		}
	}
}

	