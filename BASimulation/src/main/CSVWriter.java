package main;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import simulation.Metric;

public class CSVWriter {

    public static void writeMetrics(ArrayList<Metric> metrics) throws Exception {

        String csvFile = "metrics.csv";
        FileWriter writer = new FileWriter(csvFile);
        
        //Header
        CSVUtils.writeLine(writer, Arrays.asList("Creation Time", "Travel Endtime", "Search Distance", "Total Distance","Car Failed"));
        for(Metric m:metrics)
        {
        	CSVUtils.writeLine(writer, Arrays.asList(m.getCreationTime()+"", m.getTravelEndTime()+"",m.getDistanceSearchingTravelled()+"", m.getDistanceTravelled()+"",m.isCarFailed()+""));
        }
        


        writer.flush();
        writer.close();

    }

}