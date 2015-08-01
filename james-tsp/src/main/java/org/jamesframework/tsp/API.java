/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jamesframework.tsp;

import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.problems.Problem;
import org.jamesframework.core.search.algo.MetropolisSearch;
import org.jamesframework.core.search.algo.ParallelTempering;
import org.jamesframework.core.search.algo.RandomDescent;
import org.jamesframework.examples.tsp.TSP2OptNeighbourhood;
import org.jamesframework.examples.tsp.TSPData;
import org.jamesframework.examples.tsp.TSPSolution;

public class API {

    private static final API API = new API();

    private API(){}

    public static API getAPI(){
        return API;
    }

    public MetropolisSearch<TSPSolution> getMetropolis(Problem<TSPSolution> problem, double temp){
        TSPData data = (TSPData) ((GenericProblem) problem).getData();
        double avgNNDist = computeAvgNearestNeighbourDistance(data);
        double scaledTemp = temp * avgNNDist;
        MetropolisSearch<TSPSolution> ms = new MetropolisSearch<>(problem, new TSP2OptNeighbourhood(), scaledTemp);
        return ms;
    }

    public ParallelTempering<TSPSolution> getParallelTempering(Problem<TSPSolution> problem,
                                                               double minTemp, double maxTemp,
                                                               int numReplicas){
        TSPData data = (TSPData) ((GenericProblem) problem).getData();
        double avgNNDist = computeAvgNearestNeighbourDistance(data);
        double scaledMinTemp = minTemp * avgNNDist;
        double scaledMaxTemp = maxTemp * avgNNDist;
        System.err.println("Creating parallel tempering search:");
        System.err.println(" - Min. temp: " + minTemp);
        System.err.println(" - Max. temp: " + maxTemp);
        System.err.println(" - Scaled min. temp: " + scaledMinTemp);
        System.err.println(" - Scaled max. temp: " + scaledMaxTemp);
        System.err.println(" - Average nearest neighbour distance: " + avgNNDist);
        ParallelTempering<TSPSolution> pt = new ParallelTempering<>(problem, new TSP2OptNeighbourhood(),
                                                                    numReplicas, scaledMinTemp, scaledMaxTemp);
        return pt;
    }

    public RandomDescent<TSPSolution> getRandomDescent(Problem<TSPSolution> problem){
        return new RandomDescent<>(problem, new TSP2OptNeighbourhood());
    }

    // compute average distance from city to nearest other city
    // (only non-identical neighbours, i.e. with distance > 0, are considered)
    private double computeAvgNearestNeighbourDistance(TSPData data){
        int n = data.getNumCities();
        double sum = 0.0;
        for(int i=0; i<n; i++){
            double min = Double.MAX_VALUE;
            for(int j=0; j<n; j++) {
                double dist = data.getDistance(i, j);
                if(dist > 0.0 && dist < min){
                    min = dist;
                }
            }
            sum += min;
        }
        return sum/n;
    }
}
