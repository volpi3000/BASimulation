package main;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NameAlreadyBoundException;

import main.Enums.oType;
import main.Enums.search;
import manhattan.Manhattan;
import navigation.Navigation;
import simulation.Metric;
import simulation.Simulation;

public class Main {

	static Manhattan matrix;

	public static void setup() {
		int entrances = 10;
		int roadlenght = 300;
		int spotsperroad = 20;
		int appParkingSpots = 2;
		double spawnMultiplikator = 4.0;
		int metersPerSecond = 6;
		int totalRuntime = 86400;
		int parkingDurationMin = 1200;
		int parkingDurationMax = 10800;
		double percentAppUser = 0.00;
		

		System.out.println("Creating Map ...");
		try {
			matrix = new Manhattan(entrances, roadlenght, spotsperroad, appParkingSpots);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Map Created");

		// read CSV
		Map<String, String> spawn = CSVReader.readMap();

		Navigation nav = new Navigation(matrix);
		nav.preCache(matrix);
		Simulation sim = new Simulation(matrix, nav, metersPerSecond, spawn, spawnMultiplikator, totalRuntime,
				parkingDurationMin, parkingDurationMax, percentAppUser);
		sim.run();
		ArrayList<Metric> metrics = sim.getMetrics();

		try {
			CSVWriter.writeMetrics(metrics);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			CSVWriter.writeSpotMetrics(sim.getSpotMetrics());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printMetrics(metrics, matrix);

	}

	private static void printMetrics(ArrayList<Metric> metrics, Manhattan matrix) {
		int totalCars = 0;
		int measuredCars = 0;
		int averageDistanceTraveled = 0;
		int averageTravelTime = 0;
		int averageParkingduration = 0;
		int carsFailed = 0;
		int totalAppSpots = matrix.getTotalAppSpots();
		int totalAppCars = 0;
		int appUsersbecameNormal = 0;
		int totalSpots = matrix.getTotalSpots();

		for (Metric m : metrics) {
			totalCars++;

			if (m.isCarFailed()) {
				carsFailed++;
			} else {
				averageDistanceTraveled += m.getDistanceTravelled();
				averageTravelTime += (m.getTravelEndTime() - m.getCreationTime());
				measuredCars++;
				if(m.getMode()==search.APP)
				{
					totalAppCars++;
				}
				if(m.isFailedApp())
				{
					appUsersbecameNormal++;
				}
			}

		}

		double avgDT = (averageDistanceTraveled * 1.0) / (measuredCars * 1.0);
		double avgTT = (averageTravelTime * 1.0) / (measuredCars * 1.0);
		System.out.println("Metrics:");
		System.out.println("Total Cars created: " + totalCars);
		System.out.println("Total AppCars created: " + totalAppCars);
		System.out.println("Total Cars failed: " + carsFailed);
		System.out.println("Total AppCars became normal: " + appUsersbecameNormal);
		System.out.println("Total Parkingspots: " + totalSpots);
		System.out.println("Total App Parkingapots: " + totalAppSpots);
		System.out.println("Total Normal Parkingsspots: " + (totalSpots-totalAppSpots));
		System.out.println("Average Distance Travelled: " + avgDT);
		System.out.println("Average Time Travelled: " + avgTT);

	}

	public static void main(String[] args) {
		setup();
		// printMap();

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
