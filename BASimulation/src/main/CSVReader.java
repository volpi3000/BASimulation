package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {

    

        
    

	public static Map<String, String> readMap() {
		
		String csvFile = "LukasAutoSpline.csv";
        String line = "";
        String cvsSplitBy = ",";
        
        Map<String, String> spawn = new HashMap<String, String>();
        

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	//Skip first line
        	br.readLine();

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                spawn.put(country[0], country[1]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return spawn;

	}

}