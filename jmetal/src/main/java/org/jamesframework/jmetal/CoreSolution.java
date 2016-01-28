

package org.jamesframework.jmetal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;


public class CoreSolution extends AbstractGenericSolution<Integer, CoreSelectionProblem> {

    private final int[] unsel;
    
    public CoreSolution(CoreSelectionProblem problem, List<Integer> sel, int[] unsel) {
        super(problem);
        for (int i = 0; i < sel.size(); i++) {
            setVariableValue(i, sel.get(i));
        }
        this.unsel = unsel;
    }

    @Override
    public String getVariableValueString(int index) {
        return getVariableValue(index).toString() ;
    }

    @Override
    public Solution<Integer> copy() {
        List<Integer> sel = new ArrayList<>();
        for (int i = 0; i < getNumberOfVariables(); i++) {
            sel.add(getVariableValue(i));
        }
        CoreSolution copy = new CoreSolution(problem, sel, Arrays.copyOf(unsel, unsel.length));
        copy.setObjective(0, getObjective(0));
        return copy;
    }
    
    public int[] getUnselectedIDs() {
        return unsel;
    }

}
