package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class ChatClient extends Application { 
	// I/O streams 
	DataOutputStream toServer = null; 
	DataInputStream fromServer = null;


	@Override
	public void start(Stage primaryStage) { 
		// Panel p to hold the label and text field 
		BorderPane paneForTextField = new BorderPane(); 
		paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
		paneForTextField.setStyle("-fx-border-color: green"); 
		paneForTextField.setLeft(new Label("Enter a radius: ")); 

		TextField tf = new TextField(); 
		tf.setAlignment(Pos.BOTTOM_RIGHT); 
		paneForTextField.setCenter(tf); 

		BorderPane mainPane = new BorderPane(); 
		// Text area to display contents 
		TextArea ta = new TextArea(); 
		mainPane.setCenter(new ScrollPane(ta)); 
		mainPane.setTop(paneForTextField); 


		// Create a scene and place it in the stage 
		Scene scene = new Scene(mainPane, 450, 200); 
		primaryStage.setTitle("Client"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 
		tf.setOnAction(e -> { 
			try {
				String message = tf.getText();
				toServer.writeUTF(message);
				toServer.flush();
				String messageBack = fromServer.readUTF();
				ta.appendText("Server: " + messageBack + ".");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		try { 
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 6969); 
			fromServer = new DataInputStream(socket.getInputStream()); 
			toServer = new DataOutputStream(socket.getOutputStream()); 
		} 
		catch (IOException ex) { 
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
