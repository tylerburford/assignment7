package server;

public class Item {
	String highestBidder;
	Boolean sold;
	String name;
	String description;
	double bidPrice;
	double sellPrice;
	
	public Item(String name, String description, double bidPrice, double sellPrice) {
		this.name = name;
		this.description = description;
		this.bidPrice = bidPrice;
		this.sellPrice = sellPrice;
	}
}

