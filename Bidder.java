package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
	
	// I/O streams 
	public PrintWriter toServer; 
	public BufferedReader fromServer;
	public Gson gson;
	
	//JavaFX
	Stage stage;
	Scene scene;
	FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientFX.fxml"));
	@FXML Button button;
	@FXML Button quit;
	@FXML TextField tf;
	@FXML TextArea ta;
	
	public static void main(String[] args) {
		launch(args);
	}
	 
	@Override
	public void start(Stage primaryStage) throws Exception { 
		Parent root = loader.load();
		stage = primaryStage; 
        scene = new Scene(root, 1361, 826); 
        primaryStage.setTitle("Testing FXML");
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
		System.out.println("asfds");
	}
	
	
		
	
	public void buttonPressed(javafx.event.ActionEvent e) {
	}
	public void TextEntered(javafx.event.ActionEvent e) {
	
	}
	
	public void quit(javafx.event.ActionEvent e) {
		System.exit(0);
	}
}



