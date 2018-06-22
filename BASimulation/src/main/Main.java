package main;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

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
		int roadlenght = 300;
		int spotsperroad = 25;
		double spawnMultiplikator = 4.0;
		int metersPerSecond = 6;
		int totalRuntime = 86400; 
		int parkingDurationMin = 1200;
		int parkingDurationMax = 10800;
		
		System.out.println("Creating Map ...");
		try {
			matrix = new Manhattan(entrances, roadlenght, spotsperroad);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Map Created");

		//read CSV
		Map<String, String> spawn = CSVReader.readMap();
		
		Navigation nav = new Navigation(matrix);
		nav.preCache(matrix);
		Simulation sim = new Simulation(matrix, nav, metersPerSecond, spawn,spawnMultiplikator, totalRuntime,parkingDurationMin,parkingDurationMax);
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
		int totalParkingSpots = 0;
		int totalAppUsers=0;
		int appUsersbecameNormal=0;
		int totalSpots = matrix.getTotalSpots();
		
		for(Metric m: metrics)
		{
			totalCars++;
			
			if(m.isCarFailed())
			{
				carsFailed++;
			}
			else
			{
				averageDistanceTraveled += m.getDistanceTravelled();
				averageTravelTime +=(m.getTravelEndTime()-m.getCreationTime());
				measuredCars++;
			}
			
		}
		
		double avgDT= (averageDistanceTraveled*1.0)/(measuredCars*1.0);
		double avgTT= (averageTravelTime*1.0)/(measuredCars*1.0);
		System.out.println("Metrics:");
		System.out.println("Total Cars created: "+ totalCars);
		System.out.println("Total Cars failed: " + carsFailed);
		System.out.println("Toal Parkingspots: "+ totalSpots);
		System.out.println("Average Distance Travelled: "+ avgDT);
		System.out.println("Average Time Travelled: "+ avgTT);
		
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
