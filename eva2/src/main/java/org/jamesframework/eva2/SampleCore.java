
package org.jamesframework.eva2;

import eva2.OptimizerFactory;
import eva2.optimization.OptimizationParameters;
import eva2.optimization.operator.terminators.GenerationTerminator;
import eva2.optimization.strategies.HillClimbing;
import java.util.BitSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.jamesframework.james.asc.FileReader;

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
        int n = dist.length;
        // create problem
        CoreSelectionProblem problem = new CoreSelectionProblem(n, coreSize, dist);

        // create search
        OptimizationParameters optParams = OptimizerFactory.hillClimbing(problem, 1);
        GenerationTerminator sc = new GenerationTerminator(steps);
        optParams.setTerminator(sc);
        HillClimbing hc = (HillClimbing) optParams.getOptimizer();
        hc.setMutationOperator(new SubsetSwapMutation(n));

        // run search
        long start = System.currentTimeMillis();
        BitSet sol = OptimizerFactory.optimizeToBinary(optParams, "eva2.out");
        long stop = System.currentTimeMillis();
        long time = stop - start;
        
        // output results
        Set<Integer> selectedIds = sol.stream().boxed().collect(Collectors.toSet());
        System.out.println("Best solution: " + selectedIds);
        System.out.println("Best score: " + -problem.evaluate(sol)[0]);
        System.out.println("Performed evaluations: " + OptimizerFactory.lastEvalsPerformed());
        System.out.println("Runtime: " + time);

    }

}
