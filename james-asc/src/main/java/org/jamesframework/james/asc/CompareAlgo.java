/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jamesframework.james.asc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.ParallelTempering;
import org.jamesframework.core.search.algo.RandomDescent;
import org.jamesframework.core.search.neigh.Neighbourhood;
import org.jamesframework.core.search.stopcriteria.MaxRuntime;
import org.jamesframework.core.subset.SubsetProblem;
import org.jamesframework.core.subset.SubsetSolution;
import org.jamesframework.core.subset.neigh.SingleSwapNeighbourhood;
import org.jamesframework.ext.analysis.Analysis;
import org.jamesframework.ext.analysis.AnalysisResults;
import org.jamesframework.ext.analysis.JsonConverter;

public class CompareAlgo {

    /**
     * 0: selection ratio
     * 1: runs
     * 2: runtime (sec)
     * 3 - n: files
     */
    public static void main(String[] args) throws IOException {
        
        double ratio = Double.parseDouble(args[0]);
        int runs = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);
        List<String> files = new ArrayList<>();
        for(int f = 3; f < args.length; f++){
            files.add(args[f]);
        }
        
        // read data sets
        List<CoreSubsetData> datasets = files.stream()
                                             .map(FileReader::read)
                                             .collect(Collectors.toList());
        
        // create objective
        CoreSubsetObjective obj = new CoreSubsetObjective();
        
        // initialize analysis
        Analysis<SubsetSolution> analysis = new Analysis<>();
        
        // add problems
        for(int d = 0; d < datasets.size(); d++){
            // get dataset
            CoreSubsetData dataset = datasets.get(d);
            // set size
            int size = (int) Math.round(ratio * dataset.getIDs().size());
            // create problem
            SubsetProblem<CoreSubsetData> problem = new SubsetProblem<>(obj, dataset, size);
            // add to analysis
            String datasetID = "dataset-" + (d+1);
            analysis.addProblem(datasetID, problem);
        }
        
        // initialize neighbourhood
        Neighbourhood<SubsetSolution> neigh = new SingleSwapNeighbourhood();
        
        // add random descent
        analysis.addSearch("Random Descent", problem -> {
            Search<SubsetSolution> rd = new RandomDescent<>(problem, neigh);
            rd.addStopCriterion(new MaxRuntime(time, TimeUnit.SECONDS));
            return rd;
        });
        
        // add parallel tempering
        analysis.addSearch("Parallel Tempering", problem -> {
            double minTemp = 1e-8;
            double maxTemp = 1e-4;
            int numReplicas = 10;
            Search<SubsetSolution> pt = new ParallelTempering<>(problem, neigh, numReplicas, minTemp, maxTemp);
            pt.addStopCriterion(new MaxRuntime(time, TimeUnit.SECONDS));
            return pt;
        });
        
        // set number of runs
        analysis.setNumRuns(runs);
        
        // run analysis
        AnalysisResults<SubsetSolution> results = analysis.run();
        
        // write to json file
        results.writeJSON("comparison.json", JsonConverter.SUBSET_SOLUTION);
        
    }
    
}
