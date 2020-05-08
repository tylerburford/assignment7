package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
	
	// I/O streams 
	public PrintWriter toServer; 
	public BufferedReader fromServer;
	public Gson gson;
	
	//JavaFX
	Stage stage;
	Scene scene;
	FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientFX.fxml"));
	@FXML Button bidButton;
	@FXML Button quit;
	@FXML TextField userBid;
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
	@FXML ChoiceBox<String> itemChooser;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	 
	@Override
	public void start(Stage primaryStage) throws Exception { 
		loader.setController(this);
		Parent root = loader.load();
		stage = primaryStage; 
        scene = new Scene(root, 800, 500); 
        primaryStage.setTitle("Auction House");
        primaryStage.setScene(scene);
        primaryStage.show();
		try { 
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 6969); 
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
						bidPrice1.setText("$" + String.valueOf(auctions[0].bidPrice));
						buyPrice1.setText("$" + String.valueOf(auctions[0].sellPrice));
						
						auction2.setText(auctions[1].name);
						description2.setText(auctions[1].description);
						bidPrice2.setText("$" + String.valueOf(auctions[1].bidPrice));
						buyPrice2.setText("$" + String.valueOf(auctions[1].sellPrice));
						
						auction3.setText(auctions[2].name);
						description3.setText(auctions[2].description);
						bidPrice3.setText("$" + String.valueOf(auctions[2].bidPrice));
						buyPrice3.setText("$" + String.valueOf(auctions[2].sellPrice));
						
						auction4.setText(auctions[3].name);
						description4.setText(auctions[3].description);
						bidPrice4.setText("$" + String.valueOf(auctions[3].bidPrice));
						buyPrice4.setText("$" + String.valueOf(auctions[3].sellPrice));
						
						auction5.setText(auctions[4].name);		
						description5.setText(auctions[4].description);
						bidPrice5.setText("$" + String.valueOf(auctions[4].bidPrice));
						buyPrice5.setText("$" + String.valueOf(auctions[4].sellPrice));
						
						for(int i=0; i<auctions.length; i++) {
							itemChooser.getItems().add(auctions[i].name);
						}
					}};
			Platform.runLater(initializer);
			}});
		thread.start();
	}
	
		
	
	public void bidEntered(javafx.event.ActionEvent e) {
		
	}
	public void bid(javafx.event.ActionEvent e) {
		String name = itemChooser.getValue();
		String bid = userBid.getText();
		toServer.println(name + "+" + bid);
		toServer.flush();
		System.out.println(name + "+" + bid);
	}
	
	public void quit(javafx.event.ActionEvent e) {
		System.exit(0);
	}
}



