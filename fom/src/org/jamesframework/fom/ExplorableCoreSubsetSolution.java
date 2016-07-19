
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.problem.Problem;
import es.us.lsi.isa.fomfw.solution.ExplorableSolution;
import es.us.lsi.isa.fomfw.solution.Movement;


public class ExplorableCoreSubsetSolution extends CoreSubsetSolution implements ExplorableSolution {

    public ExplorableCoreSubsetSolution(Problem problem, boolean[] selected, int coreSize) {
        super(problem, selected, coreSize);
    }

    @Override
    public ExplorableSolution getNeighbour() {
        throw new UnsupportedOperationException("Not used."); 
    }

    @Override
    public Movement getMovement() {
        throw new UnsupportedOperationException("Not used."); 
    }

    @Override
    public long neighboursToExplore() {
        throw new UnsupportedOperationException("Not used."); 
    }

    @Override
    public ExplorableSolution getRandomNeighbour() {
        return null; // TODO
    }

    @Override
    public void resetNeighbourhood() {
        throw new UnsupportedOperationException("Not used."); 
    }

}
