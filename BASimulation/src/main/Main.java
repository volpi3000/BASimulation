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
	
	static Settings se = null;

	public static ArrayList<Metric> setup() {
		int entrances = 10;
		int roadlenght = 200;
		int spotsperroad = 15;
		int appParkingSpots = 5;
		double spawnMultiplikator = 6;
		int metersPerSecond = 6;
		int totalRuntime = 86400;
		int parkingDurationMin = 1200;
		int parkingDurationMax = 7200;
		double percentAppUser = 33.00;
		
		se = new Settings(entrances,roadlenght,spotsperroad,appParkingSpots,spawnMultiplikator,metersPerSecond,totalRuntime,parkingDurationMin,parkingDurationMax,percentAppUser);

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
			//CSVWriter.writeSpotMetrics(sim.getSpotMetrics());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return metrics;
	}

	private static String printMetrics(ArrayList<Metric> metrics) {
		
		String out ="";
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
		int averageSearchDistance = 0;
		int sD = 0;

		for (Metric m : metrics) {
			totalCars++;
			
			if (m.isCarFailed()) {
				carsFailed++;
			} else {
				averageDistanceTraveled += m.getDistanceTravelled();
				averageTravelTime += (m.getTravelEndTime() - m.getCreationTime());
				averageSearchDistance+=m.getDistanceSearchingTravelled();
				measuredCars++;
				if(sD<m.getDistanceSearchingTravelled())
				{
					sD=m.getDistanceSearchingTravelled();
				}
				if(m.getMode()==search.APP)
				{
					totalAppCars++;
				}
				if(m.isFailedApp())
				{
					appUsersbecameNormal++;
					totalAppCars++;
				}
			}

		}

		double avgDT = (averageDistanceTraveled * 1.0) / (measuredCars * 1.0);
		double avgTT = (averageTravelTime * 1.0) / (measuredCars * 1.0);
		double avgSD = (averageSearchDistance *1.0)/(measuredCars * 1.0);
		out += ("Metrics:"+"\n");
		out +=("Total Cars created: " + totalCars+"\n");
		out +=("Total AppCars created: " + totalAppCars+"\n");
		out +=("Total Cars failed: " + carsFailed+"\n");
		out +=("Total AppCars became normal: " + appUsersbecameNormal+"\n");
		out +=("Total Parkingspots: " + totalSpots+"\n");
		out +=("Total App Parkingapots: " + totalAppSpots+"\n");
		out +=("Total Normal Parkingsspots: " + (totalSpots-totalAppSpots)+"\n");
		out +=("Average Distance Travelled: " + avgDT+"\n");
		out +=("Average Search Distance Travelled: " + avgSD+"\n");
		out +=("Average Time Travelled: " + avgTT+"\n");
		out +=("Highest Search distance: " + sD+"\n");
		return out;
	}

	public static void main(String[] args) {
		
		int runs = 1;
		ArrayList<Metric>[] metrics = new ArrayList[runs];
		String[] extra = new String[runs];
		for(int i= 0; i<runs;i++)
		{
		metrics[i]=setup();
		extra[i]=printMetrics(metrics[i]);
		System.out.println(extra[i]);
		}
		//ExcelWriter.writeMetricsSheet(metrics,se,extra);
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
