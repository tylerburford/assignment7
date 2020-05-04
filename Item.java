package server;

public class Item {
	private String name;
	private String description;
	private double bidPrice;
	private double sellPrice;
	
	public Item(String name, String description, double bidPrice, double sellPrice) {
		this.setName(name);
		this.setDescription(description);
		this.setBidPrice(bidPrice);
		this.setSellPrice(sellPrice);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
}

