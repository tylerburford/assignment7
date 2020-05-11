package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import com.google.gson.Gson;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Author: Vallath Nandakumar and EE 422C instructors
 * Date: April 20, 2020
 * This starter code is from the MultiThreadChat example from the lecture, and is on Canvas. 
 * It doesn't compile.
 */

public class Bidder extends Application { 
	
	//Data
	public Item[] auctions;
	String username;
	DecimalFormat df = new DecimalFormat("#.00");
	
	// I/O streams 
	public Socket socket;
	public PrintWriter toServer; 
	public BufferedReader fromServer;
	public Gson gson;
	
	//JavaFX
	Stage stage;
	Scene scene;
	FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientFX.fxml"));
	@FXML ImageView pic1;
	@FXML Timeline timeline1;
	@FXML Timeline timeline2;
	@FXML Timeline timeline3;
	@FXML Timeline timeline4;
	@FXML Timeline timeline5;
	@FXML DialogPane login;
	@FXML Button logIn;
	@FXML Button bidButton;
	@FXML Button quit;
	@FXML Button history;
	@FXML TextField userName;
	@FXML TextField userBid;
	@FXML TextArea serverConsole = new TextArea();
	@FXML TextArea historyField;
	@FXML Label itemTimer1;
	@FXML Label itemTimer2;
	@FXML Label itemTimer3;
	@FXML Label itemTimer4;
	@FXML Label itemTimer5;
	@FXML Label usernameLabel;
	@FXML Label auction1;
	@FXML Label auction2;
	@FXML Label auction3;
	@FXML Label auction4;
	@FXML Label auction5;
	@FXML Label description1;
	@FXML Label description2;
	@FXML Label description3;
	@FXML Label description4;
	@FXML Label description5;
	@FXML Label bidPrice1;
	@FXML Label bidPrice2;
	@FXML Label bidPrice3;
	@FXML Label bidPrice4;
	@FXML Label bidPrice5;
	@FXML Label buyPrice1;
	@FXML Label buyPrice2;
	@FXML Label buyPrice3;
	@FXML Label buyPrice4;
	@FXML Label buyPrice5;
	@FXML Label winner1;
	@FXML Label winner2;
	@FXML Label winner3;
	@FXML Label winner4;
	@FXML Label winner5;
	@FXML Text itemSold1;
	@FXML Text itemSold2;
	@FXML Text itemSold3;
	@FXML Text itemSold4;
	@FXML Text itemSold5;
	@FXML ChoiceBox<String> itemChooser;
	@FXML ChoiceBox<String> historyChooser;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	 
	@Override
	public void start(Stage primaryStage) throws Exception { 
		loader.setController(this);
		Parent root = loader.load();
		stage = primaryStage; 
        scene = new Scene(root, 750, 800); 
        primaryStage.setTitle("Auction House");
        primaryStage.setScene(scene);
        primaryStage.show();
		try { 
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 6969);
			this.socket = socket;
			fromServer = new BufferedReader((new InputStreamReader(socket.getInputStream()))); 
			toServer = new PrintWriter(socket.getOutputStream());
			}catch (IOException ex) { ex.printStackTrace();}
		gson = new Gson();
		Reader reader  = new Reader(this, fromServer);
		Thread readerThread = new Thread(reader);
		readerThread.start();
	}
	
	public void setUpAuctions() {
		Thread thread = new Thread (new Runnable() {		
			@Override
			public void run() {
				Runnable initializer = new Runnable () {
					public void run() {
						auction1.setText(auctions[0].name);
						description1.setText(auctions[0].description);
						bidPrice1.setText("$" + String.valueOf(df.format(auctions[0].bidPrice)));
						buyPrice1.setText("$" + String.valueOf(df.format(auctions[0].sellPrice)));
						if(auctions[0].sold == true) {
							itemSold1.setVisible(true);
							winner1.setVisible(true);
							winner1.setText("WINNER: " + auctions[0].highestBidder);
						}					
						auction2.setText(auctions[1].name);
						description2.setText(auctions[1].description);
						bidPrice2.setText("$" + String.valueOf(df.format(auctions[1].bidPrice)));
						buyPrice2.setText("$" + String.valueOf(df.format(auctions[1].sellPrice)));
						if(auctions[1].sold == true) {
							itemSold2.setVisible(true);
							winner2.setVisible(true);
							winner2.setText("WINNER: " + auctions[1].highestBidder);
						}
						auction3.setText(auctions[2].name);
						description3.setText(auctions[2].description);
						bidPrice3.setText("$" + String.valueOf(df.format(auctions[2].bidPrice)));
						buyPrice3.setText("$" + String.valueOf(df.format(auctions[2].sellPrice)));
						if(auctions[2].sold == true) {
							itemSold3.setVisible(true);
							winner3.setVisible(true);
							winner3.setText("WINNER: " + auctions[2].highestBidder);
						}
						auction4.setText(auctions[3].name);
						description4.setText(auctions[3].description);
						bidPrice4.setText("$" + String.valueOf(df.format(auctions[3].bidPrice)));
						buyPrice4.setText("$" + String.valueOf(df.format(auctions[3].sellPrice)));
						if(auctions[3].sold == true) {
							itemSold4.setVisible(true);
							winner4.setVisible(true);
							winner4.setText("WINNER: " + auctions[3].highestBidder);
						}
						auction5.setText(auctions[4].name);		
						description5.setText(auctions[4].description);
						bidPrice5.setText("$" + String.valueOf(df.format(auctions[4].bidPrice)));
						buyPrice5.setText("$" + String.valueOf(df.format(auctions[4].sellPrice)));
						if(auctions[4].sold == true) {
							winner5.setVisible(true);
							winner5.setText("WINNER: " + auctions[4].highestBidder);
							itemSold5.setVisible(true);
						}
						//Setting up Countdown Clocks
						Timer timer = new Timer();
						TimerTask timer1Task = new TimerTask () {
							public void run() {
								if(auctions[0].sold == false) 
									serverConsole.setText(serverConsole.getText() + "Bidding for " + auctions[0].name + " has ended. \n");
								timerEnded(auctions[0]);
							}
						};
						TimerTask timer2Task = new TimerTask () {
							public void run() {
								if(auctions[1].sold == false) 
									serverConsole.setText(serverConsole.getText() + "Bidding for " + auctions[1].name + " has ended. \n");
								timerEnded(auctions[1]);
							}
						};
						TimerTask timer3Task = new TimerTask () {
							public void run() {
								if(auctions[2].sold == false) 
									serverConsole.setText(serverConsole.getText() + "Bidding for " + auctions[2].name + " has ended. \n");
								timerEnded(auctions[2]);
							}
						};
						TimerTask timer4Task = new TimerTask () {
							public void run() {
								if(auctions[3].sold == false) 
									serverConsole.setText(serverConsole.getText() + "Bidding for " + auctions[3].name + " has ended. \n");
								timerEnded(auctions[3]);
							}
						};
						TimerTask timer5Task = new TimerTask () {
							public void run() {
								if(auctions[4].sold == false) 
									serverConsole.setText(serverConsole.getText() + "Bidding for " + auctions[4].name + " has ended. \n");
								timerEnded(auctions[4]);
							}
						};
						timer.schedule(timer1Task, (auctions[0].time)*1000l);
						timer.schedule(timer2Task, (auctions[1].time)*1000l);
						timer.schedule(timer3Task, (auctions[2].time)*1000l);
						timer.schedule(timer4Task, (auctions[3].time)*1000l);
						timer.schedule(timer5Task, (auctions[4].time)*1000l);
			
						itemTimer1.setText(auctions[0].time.toString());
						itemTimer2.setText(auctions[1].time.toString());
						itemTimer3.setText(auctions[2].time.toString());
						itemTimer4.setText(auctions[3].time.toString());
						itemTimer5.setText(auctions[4].time.toString());
						IntegerProperty auction1Time = new SimpleIntegerProperty(auctions[0].time);
						IntegerProperty auction2Time = new SimpleIntegerProperty(auctions[1].time);
						IntegerProperty auction3Time = new SimpleIntegerProperty(auctions[2].time);
						IntegerProperty auction4Time = new SimpleIntegerProperty(auctions[3].time);
						IntegerProperty auction5Time = new SimpleIntegerProperty(auctions[4].time);
						itemTimer1.textProperty().bind(auction1Time.asString());
						itemTimer2.textProperty().bind(auction2Time.asString());
						itemTimer3.textProperty().bind(auction3Time.asString());
						itemTimer4.textProperty().bind(auction4Time.asString());
						itemTimer5.textProperty().bind(auction5Time.asString());
						timeline1 = new Timeline();
						timeline2 = new Timeline();
						timeline3 = new Timeline();
						timeline4 = new Timeline();
						timeline5 = new Timeline();
				        timeline1.getKeyFrames().add(
				                new KeyFrame(Duration.seconds(auctions[0].time+1),
				                new KeyValue(auction1Time, 0)));
				        timeline1.playFromStart();
				        timeline2.getKeyFrames().add(
				                new KeyFrame(Duration.seconds(auctions[1].time+1),
				                new KeyValue(auction2Time, 0)));
				        timeline2.playFromStart();
				        timeline3.getKeyFrames().add(
				                new KeyFrame(Duration.seconds(auctions[2].time+1),
				                new KeyValue(auction3Time, 0)));
				        timeline3.playFromStart();
				        timeline4.getKeyFrames().add(
				                new KeyFrame(Duration.seconds(auctions[3].time+1),
				                new KeyValue(auction4Time, 0)));
				        timeline4.playFromStart();
				        timeline5.getKeyFrames().add(
				                new KeyFrame(Duration.seconds(auctions[4].time+1),
				                new KeyValue(auction5Time, 0)));
				        timeline5.playFromStart();
						
						for(int i=0; i<auctions.length; i++) {
							if(auctions[i].sold == false)
								itemChooser.getItems().add(auctions[i].name);
							historyChooser.getItems().add(auctions[i].name);
						}
						itemChooser.setValue(auctions[0].name);
						
					}};
			Platform.runLater(initializer);
			}});
		thread.start();
	}
	
	public void timerEnded(Item item) {
			Thread thread = new Thread (new Runnable() {		
				@Override
				public void run() {
					Runnable initializer = new Runnable () {
						public void run() {
							for(int i=0; i<auctions.length; i++) {
								if(auctions[i].equals(item)) {
									if(i == 0) {
										itemTimer1.textProperty().unbind();
										itemTimer1.setText("CLOSED");
									}
									else if(i == 1) {
										itemTimer2.textProperty().unbind();
										itemTimer2.setText("CLOSED");
									}
									else if(i == 2) {
										itemTimer3.textProperty().unbind();
										itemTimer3.setText("CLOSED");
									}
									else if(i == 3) {
										itemTimer4.textProperty().unbind();
										itemTimer4.setText("CLOSED");
									}
									else if(i == 4) {
										itemTimer5.textProperty().unbind();
										itemTimer5.setText("CLOSED");
									}
								}
							}
							if(item.sold == false) {
								if(item.highestBidder != null) {
									itemSold(item.name, item.highestBidder, item.bidPrice);
									serverConsole.setText(serverConsole.getText() + item.highestBidder + 
									" won the auction for " + item.name + " with a bid of $" + item.bidPrice + "! \n");
								}
								else {
									serverConsole.setText(serverConsole.getText() + "Nobody won the auction for " + item.name + ". \n");
									itemChooser.getItems().remove(item.name);
									try {
										itemChooser.setValue(itemChooser.getItems().get(0));}
									catch(IndexOutOfBoundsException e) {
										itemChooser.setDisable(true);
									}
								}
							}
						}};
						Platform.runLater(initializer);
				}});
			thread.start();
		}
	
	public void updateAuctions() {
		Thread thread = new Thread (new Runnable() {		
			@Override
			public void run() {
				Runnable initializer = new Runnable () {
					public void run() {
						DecimalFormat df = new DecimalFormat("#.00");
						bidPrice1.setText("$" + String.valueOf(df.format(auctions[0].bidPrice)));
						bidPrice2.setText("$" + String.valueOf(df.format(auctions[1].bidPrice)));
						bidPrice3.setText("$" + String.valueOf(df.format(auctions[2].bidPrice)));
						bidPrice4.setText("$" + String.valueOf(df.format(auctions[3].bidPrice)));
						bidPrice5.setText("$" + String.valueOf(df.format(auctions[4].bidPrice)));
					}};
			Platform.runLater(initializer);
			}});
		thread.start();
	}
	
	public void bid(javafx.event.ActionEvent e) {
		String itemname = itemChooser.getValue();
		String bid = userBid.getText();
		if(bidIsValid(itemname, bid)) {
			toServer.println("bid: "+ username + "+" + itemname + "+" + bid);
			toServer.flush();
		}else {
			Thread thread = new Thread (new Runnable() {		
				@Override
				public void run() {
					Runnable initializer = new Runnable () {
						public void run() {
							serverConsole.setText(serverConsole.getText() + "Error, bid too low for item: " + itemname + "\n");
						}};
				Platform.runLater(initializer);
				}});
			thread.start();
		}
			
	}
	
	private boolean bidIsValid(String itemname, String userBid) {
		Double bid = Double.valueOf(userBid);
		for(int i=0; i<auctions.length; i++) {
			if(auctions[i].name.equals(itemname)) {
				if(bid <= auctions[i].bidPrice)
					return false;
			}
		}
		return true;
	}

	public void quit(javafx.event.ActionEvent e) {
		toServer.println("quit: " + username);
		toServer.flush();
	}
	
	public void exit() {
		try{
			toServer.close();
			fromServer.close();
			socket.close();
		}
		catch(IOException e2) {
			System.out.println("Exception occured while closing socket");
		}
		System.exit(0);
	}
	
	public void logIn(javafx.event.ActionEvent e) {
		String username = userName.getText();
		toServer.println("username: " + username);
		toServer.flush();
	}

	public void viewHistory(javafx.event.ActionEvent e) {
		Item item;
		String itemName = historyChooser.getValue();
		for(int i=0; i<auctions.length; i++) {
			if(auctions[i].name.equals(itemName)) {
				item = auctions[i];
				showHistory(item);
			}
		}
		
	}

	public void showHistory(Item item) {
		Thread thread = new Thread (new Runnable() {		
			@Override
			public void run() {
				Runnable initializer = new Runnable () {
					public void run() {
						historyField.clear();
						Iterator<String> iterator = item.history.iterator();
						while(iterator.hasNext()) {
							historyField.setText(historyField.getText() + iterator.next());
						}
					}};
			Platform.runLater(initializer);
			}});
		thread.start();
		
	}

	public void updateAuctions(String itemName, String highBidder, Double bid) {
		for(int i=0; i<auctions.length; i++) {
			if(auctions[i].name.equals(itemName)) {
				auctions[i].history.add(highBidder + " bids $" + df.format(bid) + " for " + itemName + "\n");
				auctions[i].bidPrice = bid;
				auctions[i].highestBidder = highBidder;
			}
		}
		updateAuctions();
	}

	public void loginSuccess() {
		username = userName.getText();
		Thread thread = new Thread (new Runnable() {		
			@Override
			public void run() {
				Runnable initializer = new Runnable () {
					public void run() {
						history.setDisable(false);
						bidButton.setDisable(false);
						itemChooser.setDisable(false);
						historyChooser.setDisable(false);
						userBid.setDisable(false);
						userName.setDisable(true);
						logIn.setDisable(true);
						serverConsole.setText(serverConsole.getText() + "Welcome " + username + "!\n");
					}};
					Platform.runLater(initializer);
			}});
		thread.start();
	}

	public void itemSold(String itemName, String winner, Double bid) {
		Thread thread = new Thread (new Runnable() {		
			@Override
			public void run() {
				Runnable initializer = new Runnable () {
					public void run() {
						int sold = -1;
						for(int i=0; i<auctions.length; i++) {
							if(auctions[i].name.equals(itemName)) {
								sold = i;
								auctions[i].history.add(winner + " bought " + itemName + " for $" + df.format(bid) + "!\n");
								auctions[i].sold = true;
								auctions[i].highestBidder = winner;
							}
						}
						if(sold != -1) {
							if(sold == 0) {
								winner1.setVisible(true);
								winner1.setText("WINNER: " + auctions[0].highestBidder);
								itemSold1.setVisible(true);
								itemTimer1.textProperty().unbind();
								itemTimer1.setText("CLOSED");
							}
							if(sold == 1) {
								winner2.setVisible(true);
								winner2.setText("WINNER: " + auctions[1].highestBidder);
								itemSold2.setVisible(true);
								itemTimer2.textProperty().unbind();
								itemTimer2.setText("CLOSED");
							}
							if(sold == 2) {
								winner3.setVisible(true);
								winner3.setText("WINNER: " + auctions[2].highestBidder);
								itemSold3.setVisible(true);
								itemTimer3.textProperty().unbind();
								itemTimer3.setText("CLOSED");
							}
							if(sold == 3) {
								winner4.setVisible(true);
								winner4.setText("WINNER: " + auctions[3].highestBidder);
								itemSold4.setVisible(true);
								itemTimer4.textProperty().unbind();
								itemTimer4.setText("CLOSED");
							}
							if(sold == 4) {
								winner5.setVisible(true);
								winner5.setText("WINNER: " + auctions[4].highestBidder);
								itemSold5.setVisible(true);
								itemTimer5.textProperty().unbind();
								itemTimer5.setText("CLOSED");
							}
						}
						itemChooser.getItems().remove(itemName);
						try {
							itemChooser.setValue(itemChooser.getItems().get(0));}
						catch(IndexOutOfBoundsException e) {
							itemChooser.setDisable(true);
							bidButton.setDisable(true);
						}
					}};
			Platform.runLater(initializer);
			}});
		thread.start();
		
		
	}
}



