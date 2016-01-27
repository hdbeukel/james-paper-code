package org.jamesframework.james.asc.optenc;

import java.util.Arrays;
import org.jamesframework.core.problems.sol.Solution;

public class CoreSubsetSolution extends Solution {

    private final int[] sel;
    private final int[] unsel;

    public CoreSubsetSolution(int[] sel, int[] unsel) {
        this.sel = sel;
        this.unsel = unsel;
    }

    public int[] getSel() {
        return sel;
    }

    public int[] getUnsel() {
        return unsel;
    }

    public void swap(int addI, int delI) {
        int tmp = sel[delI];
        sel[delI] = unsel[addI];
        unsel[addI] = tmp;
    }

    @Override
    public Solution copy() {
        return new CoreSubsetSolution(
                Arrays.copyOf(sel, sel.length),
                Arrays.copyOf(unsel, unsel.length)
        );
    }

    @Override
    public boolean equals(Object other) {
        // not needed here so consider all solutions different
        return false;
    }

    @Override
    public int hashCode() {
        // not needed here so map all solutions to same hash code
        return -1;
    }
    
    @Override
    public String toString(){
        return Arrays.toString(sel);
    }

}
