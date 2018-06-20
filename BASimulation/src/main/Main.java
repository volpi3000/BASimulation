package main;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.naming.NameAlreadyBoundException;

import main.Enums.oType;
import manhattan.Manhattan;
import navigation.Navigation;
import simulation.Metric;
import simulation.Simulation;

public class Main {

	static Manhattan matrix;

	public static void setup() {
		int entrances = 10;
		int roadlenght = 100;
		int spotsperroad = 30;
		int carsPerSecond = 1;
		int metersPerSecond = 2;
		int totalRuntime = 86400; 
		int parkingDurationMin = 1800;
		int parkingDurationMax = 18000;

		try {
			matrix = new Manhattan(entrances, roadlenght, spotsperroad);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Navigation nav = new Navigation(matrix);
		nav.preCache(matrix);
		Simulation sim = new Simulation(matrix, nav, metersPerSecond, carsPerSecond, totalRuntime,parkingDurationMin,parkingDurationMax);
		sim.run();
		ArrayList<Metric> metrics = sim.getMetrics();
		for(Metric item:metrics)
		{
			System.out.println(item.getDistanceTravelled());
		}

	}

	public static void main(String[] args) {
		setup();
		//printMap();

	}

	static void printMap() {
		PrintStream out = null;
		try {
			out = new PrintStream(System.out, true, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int y = 0; y < matrix.map[0].length; y++) {
			for (int x = 0; x < matrix.map[0].length; x++) {
				switch (matrix.map[x][y].getType()) {
				case ENTRANCE:
					out.print("E");
					break;
				case STREET:
					out.print("X");
					break;
				case DUMMY:
					out.print("\u2588");
					break;
				case INTERSECTION:
					out.print("0");
					break;
				case PARKINGSPOT:
					out.print("P");
					break;
				default:
					break;

				}
			}
			System.out.print("\n");
		}
	}

}
