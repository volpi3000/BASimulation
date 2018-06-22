package main;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import simulation.Metric;
import simulation.SpotMetric;

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
    public static void writeSpotMetrics(ArrayList<SpotMetric> spotMetrics) throws Exception {

        String csvFile = "SpotMetrics.csv";
        FileWriter writer = new FileWriter(csvFile);
        
        //Header
        CSVUtils.writeLine(writer, Arrays.asList("Time","TotalAvailable", "TotalAppAvailable"));
        for(SpotMetric m:spotMetrics)
        {
        	CSVUtils.writeLine(writer, Arrays.asList(m.getTimestamp()+"",m.getTotalAvailable()+"",m.getTotalAppAvailable()+""));
        }
        


        writer.flush();
        writer.close();

    }

}