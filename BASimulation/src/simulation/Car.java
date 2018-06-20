package simulation;

import java.util.LinkedList;

import main.Coordinate;
import main.Enums.direction;
import main.Enums.search;
import main.Enums.status;
import manhattan.Intersection;
import navigation.Vertex;

public class Car {

	Coordinate position;
	final Coordinate target;
	final search mode;
	final int parkingDuration;
	int parkingStart;
	Intersection currentTarget;
	status state;
	direction dir;
	Intersection nextLoc;
	LinkedList<Vertex> path;
	Intersection searchStart;
	boolean ignoreInit = true;
	final Metric data;

	public Car(Coordinate pos, search natural, Coordinate target, int parkingDuration, int creationTime, status state,
			direction dir, Intersection currentTarget, LinkedList<Vertex> path) {
		this.position = pos;
		this.state = state;
		this.mode = natural;
		this.parkingDuration = parkingDuration;
		this.data = new Metric(creationTime,mode);
		this.dir = dir;
		this.currentTarget = currentTarget;
		this.target = target;
		this.path = path;

	}
	
	
	public int getParkingDuration() {
		return parkingDuration;
	}


	public int getParkingStart() {
		return parkingStart;
	}


	public void setParkingStart(int parkingStart) {
		this.parkingStart = parkingStart;
	}


	public Metric getData() {
		return data;
	}


	public boolean isIgnoreInit() {
		return ignoreInit;
	}


	public void setIgnoreInit(boolean ignoreInit) {
		this.ignoreInit = ignoreInit;
	}


	public Coordinate getPosition() {
		return position;
	}

	public Intersection getSearchStart() {
		return searchStart;
	}

	public void setSearchStart(Intersection searchStart) {
		this.searchStart = searchStart;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public status getState() {
		return state;
	}

	public void setState(status state) {
		this.state = state;
	}

	public direction getDir() {
		return dir;
	}

	public void setDir(direction dir) {
		this.dir = dir;
	}

	public Intersection getNextLoc() {
		return nextLoc;
	}

	public void setNextLoc(Intersection nextLoc) {
		this.nextLoc = nextLoc;
	}

	public Coordinate getTarget() {
		return target;
	}

}
