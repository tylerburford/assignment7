package server;

public class Item {
	String highestBidder;
	Boolean sold;
	String name;
	String description;
	Double bidPrice;
	Double sellPrice;
	Integer time;
	
	public Item(String name, String description, double bidPrice, double sellPrice) {
		this.sold = false;
		this.name = name;
		this.description = description;
		this.bidPrice = bidPrice;
		this.sellPrice = sellPrice;
	}
}

