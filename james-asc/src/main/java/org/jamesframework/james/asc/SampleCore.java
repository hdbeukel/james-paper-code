
package org.jamesframework.james.asc;

import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.RandomDescent;
import org.jamesframework.core.search.neigh.Neighbourhood;
import org.jamesframework.core.search.stopcriteria.MaxSteps;
import org.jamesframework.core.subset.SubsetProblem;
import org.jamesframework.core.subset.SubsetSolution;
import org.jamesframework.core.subset.neigh.SingleSwapNeighbourhood;
import org.jamesframework.james.asc.optenc.CoreSubsetProblem;
import org.jamesframework.james.asc.optenc.CoreSubsetSolution;
import org.jamesframework.james.asc.optenc.OptCoreSubsetObjective;
import org.jamesframework.james.asc.optenc.OptCoreSubsetObjectiveWithDelta;
import org.jamesframework.james.asc.optenc.OptSingleSwapNeighbourhood;

public class SampleCore {

    /**
     * 0: file
     * 1: size
     * 2: steps
     * 3: enable delta evaluation (boolean)
     * 4: use optimized encoding (boolean)
     */
    public static void main(String[] args) {

        run(args);
        System.gc();
        
    }
    
    private static void run(String[] args) {
        
        // parse arguments
        String file = args[0];
        int size = Integer.parseInt(args[1]);
        int steps = Integer.parseInt(args[2]);
        boolean delta = Boolean.parseBoolean(args[3]);
        boolean optEncoding = Boolean.parseBoolean(args[4]);
        
        // initialize data
        CoreSubsetData data = FileReader.read(file);
        
        if (delta) {
            System.err.println("INFO: Delta evaluation activated");
        } else {
            System.err.println("INFO: Delta evaluation disabled");
        }
        
        Search<?> search;
        
        if (!optEncoding) {

            System.err.println("INFO: Standard subset solution encoding");            
            
            // create objective
            CoreSubsetObjective obj = delta ? new CoreSubsetObjectiveWithDelta() : new CoreSubsetObjective();

            // finalize problem
            SubsetProblem<CoreSubsetData> problem = new SubsetProblem<>(data, obj, size);

            // create search
            Neighbourhood<SubsetSolution> neigh = new SingleSwapNeighbourhood();
            search = new RandomDescent<>(problem, neigh);
            
        } else {
            
            System.err.println("INFO: Optimized encoding");
            
            // create objective
            OptCoreSubsetObjective obj = delta ? new OptCoreSubsetObjectiveWithDelta() : new OptCoreSubsetObjective();

            // finalize problem
            CoreSubsetProblem problem = new CoreSubsetProblem(data, size, obj);

            // create search
            Neighbourhood<CoreSubsetSolution> neigh = new OptSingleSwapNeighbourhood();
            search = new RandomDescent<>(problem, neigh);
            
        }

        // run
        search.addStopCriterion(new MaxSteps(steps));
        search.start();
        System.out.println("Best solution: " + search.getBestSolution());
        System.out.println("Best score: " + search.getBestSolutionEvaluation());
        System.out.println("Performed steps: " + search.getSteps());
        System.out.println("Runtime (ms): " + search.getRuntime());
        search.dispose();
        
    }

}
