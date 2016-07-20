
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.problem.Problem;
import java.util.BitSet;

public class CoreSubsetSolutionWithDelta extends CoreSubsetSolution {

    // previous solution and move applied to obtain this solution
    private final CoreSubsetSolution predecessor;
    private final SwapMove move;
    
    public CoreSubsetSolutionWithDelta(Problem problem, int size, int coreSize) {
        this(problem, size, coreSize, null);
    }

    public CoreSubsetSolutionWithDelta(Problem problem, int size, int coreSize, BitSet selected) {
        this(problem, size, coreSize, selected, null, null);
    }

    public CoreSubsetSolutionWithDelta(Problem problem, int size, int coreSize, BitSet selected,
                                       CoreSubsetSolution predecessor, SwapMove move) {
        super(problem, size, coreSize, selected);
        this.predecessor = predecessor;
        this.move = move;
    }
    
    public CoreSubsetSolutionWithDelta(CoreSubsetSolution sol){
        this(sol, null, null);
    }
    
    public CoreSubsetSolutionWithDelta(CoreSubsetSolution sol, CoreSubsetSolution pred, SwapMove move){
        this(sol.getProblem(), sol.getSize(), sol.getCoreSize(), sol.getSelected(), pred, move);
    }
    
    @Override
    public CoreSubsetSolutionWithDelta createRandom() {
        return new CoreSubsetSolutionWithDelta(super.createRandom());
    }
    
    @Override
    public CoreSubsetSolutionWithDelta getRandomNeighbour() {
        // get random neighbour
        CoreSubsetSolution neighbour = super.getRandomNeighbour();
        // attach current solution and applied move
        return new CoreSubsetSolutionWithDelta(neighbour, this, getMovement());
    }

    public CoreSubsetSolution getPredecessor() {
        return predecessor;
    }

    public SwapMove getAppliedMove() {
        return move;
    }
    
}
