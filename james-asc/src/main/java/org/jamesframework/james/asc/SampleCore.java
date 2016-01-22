
package org.jamesframework.james.asc;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import org.jamesframework.core.search.algo.RandomDescent;
import org.jamesframework.core.search.neigh.Neighbourhood;
import org.jamesframework.core.search.stopcriteria.MaxRuntime;
import org.jamesframework.core.subset.SubsetProblem;
import org.jamesframework.core.subset.SubsetSolution;
import org.jamesframework.core.subset.neigh.SingleSwapNeighbourhood;

public class SampleCore {

    /**
     * 0: file
     * 1: size
     * 2: runtime (sec)
     * 3: enable delta evaluation (boolean)
     */
    public static void main(String[] args) throws FileNotFoundException {

        String file = args[0];
        
        // initialize data
        CoreSubsetData data = FileReader.read(file);
        
        // create objective
        boolean delta = Boolean.parseBoolean(args[3]);
        CoreSubsetObjective obj = delta ? new CoreSubsetObjectiveWithDelta() : new CoreSubsetObjective();
        
        // specify desired subset size
        int size = Integer.parseInt(args[1]);
        // finalize problem
        SubsetProblem<CoreSubsetData> problem = new SubsetProblem<>(data, obj, size);

        // create search
        Neighbourhood<SubsetSolution> neigh = new SingleSwapNeighbourhood();
        RandomDescent<SubsetSolution> search = new RandomDescent<>(problem, neigh);
        int seconds = Integer.parseInt(args[2]);
        search.addStopCriterion(new MaxRuntime(seconds, TimeUnit.SECONDS));
        
        // run
        search.start();
        System.out.println("Best solution: " + search.getBestSolution().getSelectedIDs());
        System.out.println("Best score: " + search.getBestSolutionEvaluation());
        System.out.println("Performed steps: " + search.getSteps());
        search.dispose();

    }

}
