
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.metaheuristic.localsearch.simulatedannealing.GeometricCooler;
import es.us.lsi.isa.fomfw.metaheuristic.localsearch.simulatedannealing.SA;
import es.us.lsi.isa.fomfw.metaheuristic.terminator.NIterationsTerminator;
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
        CoreSelectionProblem problem = new CoreSelectionProblem(dist);

        // create initial solution
        CoreSubsetSolution initSol = new CoreSubsetSolution(problem, dist.length, coreSize).createRandom();
                    
        // create search
        StochasticHillClimber search = new StochasticHillClimber();
        search.setTerminator(new NIterationsTerminator(steps));

        // run algo
        long start = System.currentTimeMillis();
        CoreSubsetSolution sol = (CoreSubsetSolution) search.optimize(initSol);
        long stop = System.currentTimeMillis();
        long time = stop - start;
            
        
        // output results
        Set<Integer> selectedIds = sol.getSelected().stream().boxed().collect(Collectors.toSet());
        System.out.println("Best solution: " + selectedIds);
        System.out.println("Best score: " + -sol.getFitness());
        System.out.println("runtime (ms): " + time);
        
    }

}
