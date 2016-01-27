

package org.jamesframework.jmetal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.BinarySolution;


public class SubsetSwapMutation implements MutationOperator<BinarySolution> {

    private static final Random RG = new Random();
    
    @Override
    public BinarySolution execute(BinarySolution source) {
                
        int n = source.getNumberOfVariables();
        
        // swap one selected and unselected item
        List<Integer> sel = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (source.getVariableValue(i).get(0)) {
                sel.add(i);
            }
        }
        List<Integer> unsel = IntStream.range(0, n).boxed()
                                                   .filter(i -> !sel.contains(i))
                                                   .collect(Collectors.toList());
        int add = unsel.get(RG.nextInt(unsel.size()));
        int del = sel.get(RG.nextInt(sel.size()));
        
        source.getVariableValue(del).clear(0);
        source.getVariableValue(add).set(1);
        
        return source;
        
    }

}
