
package org.jamesframework.jmetal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jamesframework.james.asc.FileReader;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder.GeneticAlgorithmVariant;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;

public class SampleCore {

    /**
     * 0: file
     * 1: size
     * 2: steps
     */
    public static void main(String[] args) {

        // parse arguments
        String file = args[0];
        int coreSize = Integer.parseInt(args[1]);
        int steps = Integer.parseInt(args[2]);
        
        // read distance matrix
        double[][] dist = FileReader.read(file).getDistanceMatrix();
        // create problem
        CoreSelectionProblem problem = new CoreSelectionProblem(dist, coreSize);

        // init operators
        CrossoverOperator<BinarySolution> crossover = new CopyFirstParentCrossover<>();
        MutationOperator<BinarySolution> mutation = new SubsetSwapMutation();
        SelectionOperator<List<BinarySolution>, BinarySolution> selection = new SelectFirstParent<>();
        
        // create algorithm
        Algorithm<BinarySolution> algo = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
                .setPopulationSize(1)
                .setMaxEvaluations(steps)
                .setSelectionOperator(selection)
                .setVariant(GeneticAlgorithmVariant.STEADY_STATE)
                .build();
        
        // run search
        AlgorithmRunner runner = new AlgorithmRunner.Executor(algo).execute();
        
        // output results
        BinarySolution sol = algo.getResult();
        Set<Integer> selectedIds = new HashSet<>();
        for (int i = 0; i < sol.getNumberOfVariables(); i++) {
            if (sol.getVariableValue(i).get(0)) {
                selectedIds.add(i);
            }
        }
        System.out.println("Best solution: " + selectedIds);
        System.out.println("Best score: " + -sol.getObjective(0));
        System.out.println("Execution time (ms): " + runner.getComputingTime());

    }

}
