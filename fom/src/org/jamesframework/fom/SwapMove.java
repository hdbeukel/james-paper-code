

package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.solution.Movement;
import java.util.BitSet;

public class SwapMove extends Movement {

    private final int add, del;

    public SwapMove(int add, int del) {
        this.add = add;
        this.del = del;
    }

    public int getAdd() {
        return add;
    }

    public int getDel() {
        return del;
    }
    
    public void apply(CoreSubsetSolution sol){
        BitSet sel = sol.getSelected();
        sel.flip(add);
        sel.flip(del);
    }
    
}
