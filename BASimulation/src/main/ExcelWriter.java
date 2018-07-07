package main;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import simulation.Metric;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ExcelWriter {

	public static void writeMetricsSheet(ArrayList<Metric>[] metrics, Settings se, String[] extra) {
		
		ZonedDateTime now = ZonedDateTime.now(); 
		String pattern        = "yyyy-MM-dd-HH-mm-ss";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);

		String output = dtf.format(now);
		
		
		String filename = "LukasBachelorSimulation-"+output+".xlsx";
		XSSFWorkbook workbook = new XSSFWorkbook();
		// sheets
		int counter = 0;
		for (ArrayList<Metric> temp : metrics) {
			counter++;
			XSSFSheet sheet = workbook.createSheet("Run " + counter);
			int cols = 6;
			int rows = temp.size() + 2;
			Object[][] datatypes = new Object[rows][cols];

			datatypes[0] = new Object[] { "Creation Time", "Travel Endtime", "Search Distance", "Total Distance",
					"Car Type", "Car Failed" };
			int r = 1;
			for (Metric m : temp) {
				datatypes[r] = new Object[] { m.getCreationTime(), m.getTravelEndTime(),
						m.getDistanceSearchingTravelled(), m.getDistanceTravelled(), m.getMode().name(), boolToString(m.isCarFailed()) };
				r++;
			}
		
			//add extra
			datatypes[r] = new Object[] { "Info: ",extra[counter-1],"","","","" };
	

			int rowNum = 0;
			System.out.println("Creating sheet" + counter+1);

			for (Object[] datatype : datatypes) {
				Row row = sheet.createRow(rowNum++);
				int colNum = 0;
				for (Object field : datatype) {
					Cell cell = row.createCell(colNum++);
					if (field instanceof String) {
						cell.setCellValue((String) field);
					} else if (field instanceof Integer) {
						cell.setCellValue((Integer) field);
					}
				}
			}
		
		}
		//create Settings sheet
				XSSFSheet sheet = workbook.createSheet("Settings");
				int cols = 2;
				int rows = 10;
				Object[][] datatypes = new Object[rows][cols];
				datatypes[0] = new Object[] { "Number of Entrances", se.getEntrances() };
				datatypes[1] = new Object[] { "Road length",se.getRoadlenght()  };
				datatypes[2] = new Object[] { "Meters per Second",se.getMetersPerSecond()  };
				datatypes[3] = new Object[] { "Parking spots per road", se.getSpotsperroad() };
				datatypes[4] = new Object[] { "Spawn multiplicator", (int) se.getSpawnMultiplikator() };
				datatypes[5] = new Object[] { "Parking duration min", se.getParkingDurationMin() };
				datatypes[6] = new Object[] { "Parking duration max", se.parkingDurationMax };
				datatypes[7] = new Object[] { "Percent of on-demand parkers", (int) se.percentAppUser };
				datatypes[8] = new Object[] { "On-demand spots per road ", se.getAppParkingSpots() };
				datatypes[9] = new Object[] { "Total runtime", se.getTotalRuntime()  };
				
				int rowNum = 0;
				System.out.println("Creating sheet" + (counter+1));

				for (Object[] datatype : datatypes) {
					Row row = sheet.createRow(rowNum++);
					int colNum = 0;
					for (Object field : datatype) {
						Cell cell = row.createCell(colNum++);
						if (field instanceof String) {
							cell.setCellValue((String) field);
						} else if (field instanceof Integer) {
							cell.setCellValue((Integer) field);
						}
					}
				}
		
		// Write to file
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");

	}
	private static String boolToString(boolean x)
	{
		if(x==true)
		{
			return "true";
		}
		
		return "false";
	}

}
