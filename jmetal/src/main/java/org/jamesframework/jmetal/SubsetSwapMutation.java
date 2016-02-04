

package org.jamesframework.jmetal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;


public class SubsetSwapMutation implements MutationOperator<BinarySolution> {

    private static final Random RG = new Random();
    
    @Override
    public BinarySolution execute(BinarySolution sol) {
                        
        // swap one selected and unselected item
        List<Integer> sel = new ArrayList<>();
        List<Integer> unsel = new ArrayList<>();
        BinarySet b = sol.getVariableValue(0);
        for (int i = 0; i < b.getBinarySetLength(); i++) {
            if (b.get(i)) {
                sel.add(i);
            } else {
                unsel.add(i);
            }
        }
        
        int del = sel.get(RG.nextInt(sel.size()));
        int add = unsel.get(RG.nextInt(unsel.size()));
        
        b.flip(add);
        b.flip(del);
        
        sol.setVariableValue(0, b);
        
        return sol;
        
    }

}
