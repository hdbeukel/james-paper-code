
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.solution.Solution;

public class CoreSelectionProblemWithDelta extends CoreSelectionProblem {
    
    public CoreSelectionProblemWithDelta(double[][] dist) {
        super(dist);
    }
    
    @Override
    public double fitness(Solution sol) {
        
        // cast to core subset solution with delta
        CoreSubsetSolutionWithDelta coreSol = (CoreSubsetSolutionWithDelta) sol;
        
        if(coreSol.getPredecessor() == null){
            // full evaluation
            return super.fitness(sol);
        } else {
            
            // delta evaluation
            
            // get predecessor and applied move
            CoreSubsetSolution prevSolution = coreSol.getPredecessor();
            SwapMove move = coreSol.getAppliedMove();
            // get fitness of predecessor
            double prevFitness = prevSolution.getFitness();
            // undo average to get sum of distances
            int numSelected = prevSolution.getCoreSize();
            int numDistances = numSelected * (numSelected - 1) / 2;
            double sumDist = prevFitness * numDistances;

            // get added and removed ID
            int added = move.getAdd();
            int removed = move.getDel();

            // update distance sum
            sumDist += prevSolution.getSelected()
                                   .stream()
                                   .mapToDouble(id -> getDistance(added, id) - getDistance(removed, id))
                                   .sum();
            // correction term
            sumDist -= getDistance(added, removed);

            // return new evaluation
            double newFitness = sumDist/numDistances;
            return -newFitness;
            
        }
        
    }

}
