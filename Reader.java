package client;

import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Platform;

public class Reader implements Runnable {
	Bidder bidder;
	BufferedReader fromServer;

	public Reader(Bidder client, BufferedReader input) {
		bidder = client;
		fromServer = input;
	} 
	
	@Override
	public void run() {
		System.out.println("Starting reader thread");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create(); 
		String input;
		
		//Initialize auction house
		try {
			input = fromServer.readLine();
			bidder.auctions = gson.fromJson(input, Item[].class);
			System.out.println("getting a message: " + input);
			System.out.println(bidder.auctions);
		} catch (IOException e) {e.printStackTrace();}
		
		bidder.setUpAuctions();
		
		try {
			while((input = fromServer.readLine()) != null) {
				System.out.println(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
