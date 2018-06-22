package simulation;

import main.Enums.search;

public class Metric {
	
	private final int creationTime;
	private int distanceTravelled = 0;
	private int distanceSearchingTravelled = 0;
	private int travelEndTime=0;
	private final search mode;
	private int timeExit;
	private boolean carFailed = false;
	
	
	public Metric(int creationTime, search mode) {
		this.creationTime = creationTime;
		this.mode = mode;
	}

	
	public int getCreationTime() {
		return creationTime;
	}


	public int getTravelEndTime() {
		return travelEndTime;
	}


	public void setTravelEndTime(int travelTime) {
		this.travelEndTime = travelTime;
	}


	public int getDistanceSearchingTravelled() {
		return distanceSearchingTravelled;
	}


	public void setDistanceSearchingTravelled(int distanceSearchingTravelled) {
		this.distanceSearchingTravelled = distanceSearchingTravelled;
	}


	public boolean isCarFailed() {
		return carFailed;
	}


	public void setCarFailed(boolean carFailed) {
		this.carFailed = carFailed;
	}


	public int getDistanceTravelled() {
		return distanceTravelled;
	}

	public void setDistanceTravelled(int distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	
	public void distancePlusOne()
	{
		distanceTravelled++;
	}
	public void distanceSearchingPlusOne()
	{
		distanceSearchingTravelled++;
	}

	public int getTimeExit() {
		return timeExit;
	}

	public void setTimeExit(int timeExit) {
		this.timeExit = timeExit;
	}
	
	
}
