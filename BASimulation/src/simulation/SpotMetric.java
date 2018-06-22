package simulation;

public class SpotMetric {
	
	private final int timestamp;
	private final int totalAvailable;
	private final int totalAppAvailable;
	
	public SpotMetric(int timestamp, int totalAvailable, int totalAppAvailable) {
		super();
		this.timestamp = timestamp;
		this.totalAvailable = totalAvailable;
		this.totalAppAvailable = totalAppAvailable;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public int getTotalAvailable() {
		return totalAvailable;
	}
	public int getTotalAppAvailable() {
		return totalAppAvailable;
	}
	
	

}
