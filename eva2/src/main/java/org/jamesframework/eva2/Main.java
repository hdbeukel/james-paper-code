package org.jamesframework.eva2;

import eva2.OptimizerFactory;
import eva2.optimization.OptimizationParameters;
import eva2.optimization.operator.terminators.MaximumTimeTerminator;
import eva2.optimization.strategies.HillClimbing;
import java.util.BitSet;
import java.util.Random;

public class Main {

    private static final Random RG = new Random();
    
    public static void main(String[] args) {

        int n = 1283;
        int coreSize = 500;
        
        double[][] dist = generateRandomDistanceMatrix(n);
        CoreSelectionProblem problem = new CoreSelectionProblem(n, coreSize, dist);

        OptimizationParameters optParams = OptimizerFactory.hillClimbing(problem, 1);
        MaximumTimeTerminator sc = new MaximumTimeTerminator();
        sc.setMaximumTime(30);
        //GenerationTerminator sc = new GenerationTerminator(1000);
        optParams.setTerminator(sc);
        HillClimbing hc = (HillClimbing) optParams.getOptimizer();
        hc.setMutationOperator(new SubsetSwapMutation(n));

        BitSet sol = OptimizerFactory.optimizeToBinary(optParams, "tmp");
        System.out.println(OptimizerFactory.terminatedBecause() + "\nFound solution: ");
        for (int i = 0; i < problem.getProblemDimension(); i++) {
            if(sol.get(i)){
                System.out.print(i + " ");
            }
        }
        System.out.println();
        System.out.println("Value: " + problem.evaluate(sol)[0]);
        System.out.println("Number of evaluations: " + OptimizerFactory.lastEvalsPerformed());

    }
    
    private static double[][] generateRandomDistanceMatrix(int n){
        
        double[][] dist = new double[n][n];
        
        for(int i=0; i<n; i++){
            for(int j=i+1; j<n; j++){
                double d = RG.nextDouble();
                dist[i][j] = d;
                dist[j][i] = d;
            }
        }
        
        return dist;
        
    }

}
