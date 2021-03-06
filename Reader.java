/*
 *  EE422C Final Project submission by
 *  Tyler Burford
 *  tlb3565
 *  16315
 *  Spring 2020
 */

package final_exam;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Reader implements Runnable {
	Client bidder;
	BufferedReader fromServer;

	public Reader(Client client, BufferedReader input) {
		bidder = client;
		fromServer = input;
	} 
	
	@Override
	public void run() {
		System.out.println("Starting reader thread");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create(); 
		String input;
		DecimalFormat df = new DecimalFormat("#.00");
		String awwMP3 = "Aww.mp3";
		Media Aww = new Media(new File(awwMP3).toURI().toString());
		MediaPlayer AwwPlayer = new MediaPlayer(Aww);
		
		
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
				input = input.replaceFirst(" ", ""); //get rid of leading space
				String command = msgArr[0];
				//
				
				if(command.equals("username:")) {
					if(input.equals("success"))
						bidder.loginSuccess();
					else if(input.equals("fail")) {
						AwwPlayer.play();
						bidder.serverConsole.setText(bidder.serverConsole.getText() + "That username has already been taken. \n");
					}
				}
				else if(command.equals("bid:")) {
					String inputArr[] = input.split("\\|");
					String user = inputArr[0];
					Double bid = Double.valueOf(inputArr[1]);
					String itemName = inputArr[2];
					bidder.updateAuctions(itemName, user, bid);
					bidder.serverConsole.setText(bidder.serverConsole.getText() + user + " bids $" + df.format(bid) + " for " + itemName + "\n");
				}
				else if(command.equals("buy:")) {
					String inputArr[] = input.split("\\|");
					String user = inputArr[0];
					Double bid = Double.valueOf(inputArr[1]);
					String itemName = inputArr[2];
					bidder.itemSold(itemName, user, bid);
					bidder.serverConsole.setText(bidder.serverConsole.getText() + user + " bought " + itemName + " for $" + df.format(bid) + "!\n");
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
