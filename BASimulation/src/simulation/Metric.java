package simulation;

import main.Enums.search;

public class Metric {
	
	final int creationTime;
	int distanceTravelled = 0;
	final search mode;
	int timeExit;
	
	public Metric(int creationTime, search mode) {
		this.creationTime = creationTime;
		this.mode = mode;
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

	public int getTimeExit() {
		return timeExit;
	}

	public void setTimeExit(int timeExit) {
		this.timeExit = timeExit;
	}
	
	
}
