package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

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
    private Item[] auctions;
    
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
			System.out.println(auctions);
		} catch (FileNotFoundException e) {
			System.out.println("File not found! D:");
		}
	}

	private void SetupNetworking() {
        try {
        	System.out.println("Server Started");
            ServerSocket ss = new ServerSocket(6969);
            while (true) {
                Socket clientSocket = ss.accept();
                ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
                Thread t = new Thread(new ClientHandler(clientSocket, writer));
                t.start();
                addObserver((Observer) writer);
                System.out.println("got a connection");
            }
        } catch (IOException e) {}
    }

    class ClientHandler implements Runnable {
        private  ObjectInputStream reader;
        private  ClientObserver writer; // See Canvas. Extends ObjectOutputStream, implements Observer
        Socket clientSocket;

        public ClientHandler(Socket clientSocket, ClientObserver writer) {
			this.writer = writer;
			this.clientSocket = clientSocket;
        }

        public void run() {
        	try {
				DataInputStream inputFromClient = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(clientSocket.getOutputStream());
				while(true) {
					String message = "placeholder";
					//System.out.println("message recieved: " + message);
					//outputToClient.writeUTF("Message recieved: " + message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
