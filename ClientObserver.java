package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends ObjectOutputStream implements Observer{
	OutputStream out;
	
	public ClientObserver(OutputStream out) throws IOException {
		this.out = out;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
	}

}
