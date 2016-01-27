

package org.jamesframework.jmetal;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;


public class CoreSolution extends AbstractGenericSolution<Integer, CoreSelectionProblem> {

    private final List<Integer> unsel;
    
    public CoreSolution(CoreSelectionProblem problem, List<Integer> sel, List<Integer> unsel) {
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
        CoreSolution copy = new CoreSolution(problem, sel, new ArrayList<>(unsel));
        copy.setObjective(0, getObjective(0));
        return copy;
    }
    
    public List<Integer> getUnselectedIDs() {
        return unsel;
    }

}
