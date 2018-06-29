package main;

public class Settings {
	final int entrances;
	final int roadlenght;
	final int spotsperroad;
	final int appParkingSpots;
	final double spawnMultiplikator;	
	final int metersPerSecond;
	final int totalRuntime;
	final int parkingDurationMin;
	final int parkingDurationMax;
	final double percentAppUser;
	
	

	



	public Settings(int entrances, int roadlenght, int spotsperroad, int appParkingSpots, double spawnMultiplikator,
			int metersPerSecond, int totalRuntime, int parkingDurationMin, int parkingDurationMax,
			double percentAppUser) {
		super();
		this.entrances = entrances;
		this.roadlenght = roadlenght;
		this.spotsperroad = spotsperroad;
		this.appParkingSpots = appParkingSpots;
		this.spawnMultiplikator = spawnMultiplikator;
		this.metersPerSecond = metersPerSecond;
		this.totalRuntime = totalRuntime;
		this.parkingDurationMin = parkingDurationMin;
		this.parkingDurationMax = parkingDurationMax;
		this.percentAppUser = percentAppUser;
	}







	public int getEntrances() {
		return entrances;
	}







	public int getRoadlenght() {
		return roadlenght;
	}







	public int getSpotsperroad() {
		return spotsperroad;
	}







	public int getAppParkingSpots() {
		return appParkingSpots;
	}







	public double getSpawnMultiplikator() {
		return spawnMultiplikator;
	}







	public int getMetersPerSecond() {
		return metersPerSecond;
	}







	public int getTotalRuntime() {
		return totalRuntime;
	}







	public int getParkingDurationMin() {
		return parkingDurationMin;
	}







	public int getParkingDurationMax() {
		return parkingDurationMax;
	}







	public double getPercentAppUser() {
		return percentAppUser;
	}
	
	

}
