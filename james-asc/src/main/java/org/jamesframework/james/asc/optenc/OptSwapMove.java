

package org.jamesframework.james.asc.optenc;

import org.jamesframework.core.search.neigh.Move;


public class OptSwapMove implements Move<CoreSubsetSolution> {

    private final int addI;
    private final int delI;

    public OptSwapMove(int addI, int delI) {
        this.addI = addI;
        this.delI = delI;
    }

    public int getDelI() {
        return delI;
    }

    public int getAddI() {
        return addI;
    }
    
    @Override
    public void apply(CoreSubsetSolution solution) {
        solution.swap(addI, delI);
    }

    @Override
    public void undo(CoreSubsetSolution solution) {
        apply(solution);
    }

}
