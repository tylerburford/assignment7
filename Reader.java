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
		} catch (IOException e) {e.printStackTrace();}
		bidder.setUpAuctions();
		
		try{
			while((input = fromServer.readLine()) != null) {
				//input processing
				System.out.println("From Server:" + input);
				String split = " ";
				String msgArr[] = input.split(split);
				input = input.replaceFirst(msgArr[0], "");
				String command = msgArr[0];
				//
				
				if(command.equals("username:")) {
					if(input.equals(" success"))
						bidder.loginSuccess();
					else if(input.equals(" fail"))
						bidder.serverConsole.setText(bidder.serverConsole.getText() + "That username has already been taken. \n");
				}
				else if(command.equals("bid:")) {
					String inputArr[] = input.split("\\|");
					String user = inputArr[0];
					Double bid = Double.valueOf(inputArr[1]);
					String itemName = inputArr[2];
					bidder.updateAuctions(itemName, user, bid);
					bidder.serverConsole.setText(bidder.serverConsole.getText() + user + " bids $" + bid + " for " + itemName + "\n");
				}
				else if(command.equals("buy:")) {
					String inputArr[] = input.split("\\|");
					String user = inputArr[0];
					Double bid = Double.valueOf(inputArr[1]);
					String itemName = inputArr[2];
					bidder.itemSold(itemName);
					bidder.serverConsole.setText(bidder.serverConsole.getText() + user + " bought " + itemName + " for $" + bid + "!\n");
				}
				else if(command.equals("quit")) {
					fromServer.close();
					bidder.exit();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
