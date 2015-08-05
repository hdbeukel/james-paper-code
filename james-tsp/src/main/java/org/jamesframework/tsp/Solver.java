/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jamesframework.tsp;

import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.problems.Problem;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.stopcriteria.MaxRuntime;
import org.jamesframework.core.search.stopcriteria.StopCriterion;
import org.jamesframework.examples.tsp.TSP;
import org.jamesframework.examples.tsp.TSPData;
import org.jamesframework.examples.tsp.TSPFileReader;
import org.jamesframework.examples.tsp.TSPObjective;
import org.jamesframework.examples.tsp.TSPSolution;


public class Solver {

    public static void main(String[] args) {
        // parse arguments
        if(args.length < 3){
            System.err.println("Usage: java -cp james-tsp.jar "
                    + "org.jamesframework.tsp.Solver "
                    + "<runs> <runtime> <inputfile>");
            System.exit(1);
        }
        int runs = Integer.parseInt(args[0]);
        int timeLimit = Integer.parseInt(args[1]);
        String filePath = args[2];
        run(filePath, runs, timeLimit);
    }

    private static void run(String filePath, int runs, int timeLimit){

        try {

            // read input
            TSPFileReader reader = new TSPFileReader();
            TSPData data = reader.read(filePath);

            // create objective
            TSPObjective obj = new TSPObjective();

            // create problem
            Problem<TSPSolution> problem = new GenericProblem<>(data, obj, TSP.RANDOM_SOLUTION_GENERATOR);

            // create stop criterion
            StopCriterion sc = new MaxRuntime(timeLimit, TimeUnit.SECONDS);

            // write header
            System.out.println("repeat, rd, pt");

            // repeatedly apply random descent and parallel tempering
            for(int repeat = 0; repeat < runs; repeat++){

                // create random descent
                Search<TSPSolution> rd = API.getAPI().getRandomDescent(problem);
                rd.addStopCriterion(sc);

                // apply random descent
                rd.start();

                // store best value
                double rdBestValue = rd.getBestSolutionEvaluation().getValue();

                // dispose random descent
                rd.dispose();

                // create parallel tempering
                double minTemp = 1e-8;
                double maxTemp = 0.6;
                int numReplicas = 10;
                Search<TSPSolution> pt = API.getAPI().getParallelTempering(problem, minTemp, maxTemp, numReplicas);
                pt.addStopCriterion(sc);

                // apply parallel tempering
                pt.start();

                // store best value
                double ptBestValue = pt.getBestSolutionEvaluation().getValue();

                // dispose parallel tempering
                pt.dispose();

                // write repeat number and best values
                System.out.format(Locale.US, "%d, %d, %d\n", repeat, Math.round(rdBestValue), Math.round(ptBestValue));

            }

        } catch (FileNotFoundException ex) {
            System.err.println("Cannot read file: " + filePath);
            System.exit(2);
        }

    }

}
