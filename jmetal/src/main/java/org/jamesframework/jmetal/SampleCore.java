
package org.jamesframework.jmetal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jamesframework.james.asc.FileReader;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder.GeneticAlgorithmVariant;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.localsearch.BasicLocalSearch;
import org.uma.jmetal.util.AlgorithmRunner;

public class SampleCore {

    /**
     * 0: file
     * 1: size
     * 2: steps
     * 3: GA emulation (true) or direct local search operator (false)
     */
    public static void main(String[] args) {

        // parse arguments
        String file = args[0];
        int coreSize = Integer.parseInt(args[1]);
        int steps = Integer.parseInt(args[2]);
        boolean ga = Boolean.parseBoolean(args[3]);
        
        // read distance matrix
        double[][] dist = FileReader.read(file).getDistanceMatrix();
        // create problem
        CoreSelectionProblem problem = new CoreSelectionProblem(dist, coreSize);

        // init operators
        CrossoverOperator<CoreSolution> crossover = new CopyFirstParentCrossover<>();
        MutationOperator<CoreSolution> mutation = new SubsetSwapMutation();
        SelectionOperator<List<CoreSolution>, CoreSolution> selection = new SelectFirstParent<>();
        
        CoreSolution sol;
        long time;
        if (ga) {
            
            System.err.println("INFO: GA Emulation");
        
            // create algorithm
            Algorithm<CoreSolution> algo = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
                    .setPopulationSize(1)
                    .setMaxEvaluations(steps)
                    .setSelectionOperator(selection)
                    .setVariant(GeneticAlgorithmVariant.STEADY_STATE)
                    .build();

            // run search
            AlgorithmRunner runner = new AlgorithmRunner.Executor(algo).execute();

            // get results
            sol = algo.getResult();
            time = runner.getComputingTime();
            
        } else {
            
            System.err.println("INFO: Direct local search");
            
            // direct local search
            LocalSearchOperator<CoreSolution> localSearch = new BasicLocalSearch<>(
                    steps,
                    mutation,
                    Comparator.comparing(s -> s.getObjective(0)),
                    problem
            );
            
            // run algo
            long start = System.currentTimeMillis();
            sol = localSearch.execute(problem.createSolution());
            long stop = System.currentTimeMillis();
            
            time = stop - start;
            
        }
        
        // output results
        List<Integer> selectedIds = new ArrayList<>();
        for (int i = 0; i < sol.getNumberOfVariables(); i++) {
            selectedIds.add(sol.getVariableValue(i));
        }
        System.out.println("Best solution: " + selectedIds);
        System.out.println("Best score: " + -sol.getObjective(0));
        System.out.println("Execution time (ms): " + time);

    }

}
