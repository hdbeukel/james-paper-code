/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jamesframework.tsp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import mjson.Json;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.MetropolisSearch;
import org.jamesframework.core.search.stopcriteria.MaxRuntime;
import org.jamesframework.core.search.stopcriteria.StopCriterion;
import org.jamesframework.examples.tsp.TSP;
import org.jamesframework.examples.tsp.TSP2OptNeighbourhood;
import org.jamesframework.examples.tsp.TSPData;
import org.jamesframework.examples.tsp.TSPFileReader;
import org.jamesframework.examples.tsp.TSPObjective;
import org.jamesframework.examples.tsp.TSPSolution;
import org.jamesframework.ext.analysis.Analysis;
import org.jamesframework.ext.analysis.AnalysisResults;

/**
 * Fine tune temperature range for parallel tempering search.
 * 
 * @author <a href="mailto:herman.debeukelaer@ugent.be">Herman De Beukelaer</a>
 */
public class ParallelTemperingTuner {

    public static void main(String[] args) {
        // parse arguments
        if(args.length < 6){
            System.err.println("Usage: java -cp james-tsp.jar "
                    + "org.jamesframework.tsp.ParallelTemperingTuner "
                    + "<runs> <runtime> <mintemp> <maxtemp> <numsearches> "
                    + "[<inputfile>]+");
            System.exit(1);
        }
        int runs = Integer.parseInt(args[0]);
        int timeLimit = Integer.parseInt(args[1]);
        double minTemp = Double.parseDouble(args[2]);
        double maxTemp = Double.parseDouble(args[3]);
        int n = Integer.parseInt(args[4]);
        List<String> filePaths = new ArrayList<>();
        for(int i=5; i<args.length; i++){
            filePaths.add(args[i]);
        }
        run(filePaths, runs, timeLimit, minTemp, maxTemp, n);
    }
    
    private static void run(List<String> filePaths, int runs, int timeLimit,
                            double minTemp, double maxTemp, int numSearches){
        
        // read datasets
        System.out.println("# PARSING INPUT");
        TSPFileReader reader = new TSPFileReader();
        List<TSPData> datasets = new ArrayList<>();
        for(String filePath : filePaths){
            System.out.println("Reading file: " + filePath);
            try {
                TSPData data = reader.read(filePath);
                datasets.add(data);
            } catch (FileNotFoundException ex) {
                System.err.println("Failed to read file: " + filePath);
                System.exit(2);
            }
        }
        
        // create objective
        TSPObjective obj = new TSPObjective();
        
        // initialize analysis object
        Analysis<TSPSolution> analysis = new Analysis<>();
        
        // ADD PROBLEMS (ONE PER DATA SET)
        System.out.println("# ADDING PROBLEMS TO ANALYSIS");
        
        for(int d = 0; d < datasets.size(); d++){
            // create problem
            TSPData data = datasets.get(d);
            GenericProblem<TSPSolution, TSPData> problem = new GenericProblem<>(data, obj, TSP.RANDOM_SOLUTION_GENERATOR);
            // set problem ID to file name (without directories and without extensions)
            String path = filePaths.get(d);
            String filename = new File(path).getName();
            String id = filename.substring(0, filename.indexOf("."));
            System.out.println("Add problem \"" + id + "\" (read from: " + path + ")");
            analysis.addProblem(id, problem);
        }
        
        // ADD SEARCHES
        System.out.println("# ADDING SEARCHES TO ANALYSIS");

        // create stop criterion
        StopCriterion stopCrit = new MaxRuntime(timeLimit, TimeUnit.MINUTES);
        
        double tempDelta = (maxTemp - minTemp)/(numSearches - 1);
        DecimalFormat df = new DecimalFormat("#.################", DecimalFormatSymbols.getInstance(Locale.US));
        for(int s = 0; s < numSearches; s++){
            // compute temperature
            double temp = minTemp + s * tempDelta;
            // add Metropolis search
            System.out.format("Add Metropolis search (temp: %s)\n", df.format(temp));
            String id = "MS-" + (s+1);
            analysis.addSearch(id, problem -> {
                TSPData data = (TSPData) ((GenericProblem) problem).getData();
                double avgNNDist = computeAvgNearestNeighbourDistance(data);
                double scaledTemp = temp * avgNNDist;
                Search<TSPSolution> ms = new MetropolisSearch<>(problem, new TSP2OptNeighbourhood(), scaledTemp);
                ms.addStopCriterion(stopCrit);
                return ms;
            });
        }
        // set number of search runs
        analysis.setNumRuns(runs);
                
        // start loader
        Timer loaderTimer = new Timer();
        TimerTask loaderTask = new TimerTask() {
            private char[] loader = new char[40];
            private int l = 6;
            private int p = 0;

            @Override
            public void run() {
                printLoader(loader);
                p = (p+1)%loader.length;
                updateLoader(loader, p, l);
            }
        };
        loaderTimer.schedule(loaderTask, 0, 100);
        
        // run analysis
        System.out.format("# RUNNING ANALYSIS (runs per search: %d)\n", runs);
        
        AnalysisResults<TSPSolution> results = analysis.run();
        
        // stop loader
        loaderTask.cancel();
        loaderTimer.cancel();
        System.out.println("# Done!");
        
        // write to JSON
        System.out.println("# WRITING JSON FILE");
        String jsonFile = "parallel-tempering-tuner.json";
        try {
            results.writeJSON(jsonFile, sol -> Json.array(sol.getCities().toArray()));
        } catch (IOException ex) {
            System.err.println("Failed to write JSON file: " + jsonFile);
            System.exit(3);
        }
        System.out.println("# Wrote \"" + jsonFile + "\"");
        
    }
    
    private static void updateLoader(char[] loader, int start, int loaderLength){
        Arrays.fill(loader, ' ');
        for(int t=0; t<loaderLength; t++){
            int pos = (start+t) % (loader.length);
            loader[pos] = '-';
        }
    }
    
    private static void printLoader(char[] loader){
        for(char c : loader){
            System.out.print(c);
        }
        System.out.print("\r");
    }
    
    // compute average distance from city to nearest other city
    private static double computeAvgNearestNeighbourDistance(TSPData data){
        int n = data.getNumCities();
        double sum = 0.0;
        for(int i=0; i<n; i++){
            double min = Double.MAX_VALUE;
            for(int j=0; j<n; j++) {
                if(j != i){
                    double dist = data.getDistance(i, j);
                    if(dist < min){
                        min = dist;
                    }
                }
            }
            sum += min;
        }
        return sum/n;
    }
    
}
