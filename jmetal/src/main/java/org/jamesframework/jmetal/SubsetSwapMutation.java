

package org.jamesframework.jmetal;

import java.util.Random;
import org.uma.jmetal.operator.MutationOperator;


public class SubsetSwapMutation implements MutationOperator<CoreSolution> {

    private static final Random RG = new Random();
    
    @Override
    public CoreSolution execute(CoreSolution sol) {
                
        int s = sol.getNumberOfVariables();
        
        // swap one selected and unselected item
        
        int delI = RG.nextInt(s);
        int addI = RG.nextInt(sol.getUnselectedIDs().length);
        
        int del = sol.getVariableValue(delI);
        int add = sol.getUnselectedIDs()[addI];
        
        sol.setVariableValue(delI, add);
        sol.getUnselectedIDs()[addI] = del;
        
        return sol;
        
    }

}
