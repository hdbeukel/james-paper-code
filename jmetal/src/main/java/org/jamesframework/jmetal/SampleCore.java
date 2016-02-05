
package org.jamesframework.jmetal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jamesframework.james.asc.FileReader;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder.GeneticAlgorithmVariant;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.localsearch.BasicLocalSearch;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;

public class SampleCore {

    /**
     * 0: file
     * 1: size
     * 2: steps
     */
    public static void main(String[] args) {

        run(args);
        System.gc();

    }
    
    private static void run(String[] args) {
        
        // parse arguments
        String file = args[0];
        int coreSize = Integer.parseInt(args[1]);
        int steps = Integer.parseInt(args[2]);
        
        // read distance matrix
        double[][] dist = FileReader.read(file).getDistanceMatrix();
        // create problem
        CoreSelectionProblem problem = new CoreSelectionProblem(dist, coreSize);

        // init mutation
        MutationOperator<BinarySolution> mutation = new SubsetSwapMutation();
                    
        // create search
        LocalSearchOperator<BinarySolution> localSearch = new BasicLocalSearch<>(
                steps,
                mutation,
                Comparator.comparing(s -> s.getObjective(0)),
                problem
        );

        // run algo
        long start = System.currentTimeMillis();
        BinarySolution sol = localSearch.execute(problem.createSolution());
        long stop = System.currentTimeMillis();
        long time = stop - start;
            
        
        // output results
        Set<Integer> selectedIds = sol.getVariableValue(0).stream().boxed().collect(Collectors.toSet());
        System.out.println("Best solution: " + selectedIds);
        System.out.println("Best score: " + -sol.getObjective(0));
        System.out.println("runtime (ms): " + time);
        
    }

}
