
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.problem.Problem;
import es.us.lsi.isa.fomfw.solution.AbstractSolution;
import es.us.lsi.isa.fomfw.solution.Solution;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CoreSubsetSolution extends AbstractSolution {

    private final int size, coreSize;
    private final boolean[] selected;
    
    public CoreSubsetSolution(Problem problem, int size, int coreSize) {
        super(problem);
        this.size = size;
        this.coreSize = coreSize;
        selected = null;
    }
    
    public CoreSubsetSolution(Problem problem, boolean[] selected, int coreSize){
        super(problem);
        this.size = selected.length;
        this.coreSize = coreSize;
        this.selected = selected;
    }

    @Override
    public Solution createRandom() {

        boolean[] select = new boolean[size];
        List<Integer> indices = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(indices);
        for(int i = 0; i < coreSize; i++){
            select[indices.get(i)] = true;
        }
        
        return new ExplorableCoreSubsetSolution(problem, select, coreSize);
        
    }
    
    public boolean[] getSelected(){
        return selected;
    }
    
    public int getSize(){
        return size;
    }
    
    public int getCoreSize(){
        return coreSize;
    }

}
