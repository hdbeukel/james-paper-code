
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.problem.Problem;
import es.us.lsi.isa.fomfw.solution.AbstractSolution;
import es.us.lsi.isa.fomfw.solution.ExplorableSolution;
import es.us.lsi.isa.fomfw.solution.Movement;
import es.us.lsi.isa.fomfw.util.FOMConfig;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CoreSubsetSolution extends AbstractSolution implements ExplorableSolution {

    private final int size, coreSize;
    private final BitSet selected;
    
    public CoreSubsetSolution(Problem problem, int size, int coreSize) {
        this(problem, size, coreSize, null);
    }
    
    public CoreSubsetSolution(Problem problem, int size, int coreSize, BitSet selected){
        super(problem);
        this.size = size;
        this.coreSize = coreSize;
        this.selected = selected;
    }

    @Override
    public CoreSubsetSolution createRandom() {

        BitSet select = new BitSet(size);
        List<Integer> indices = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(indices);
        for(int i = 0; i < coreSize; i++){
            select.set(indices.get(i));
        }
        
        return new CoreSubsetSolution(problem, size, coreSize, select);
        
    }
    
    public BitSet getSelected(){
        return selected;
    }
    
    public int getSize(){
        return size;
    }
    
    public int getCoreSize(){
        return coreSize;
    }
    
    @Override
    public CoreSubsetSolution getRandomNeighbour() {
        
        // swap one selected and unselected item
        List<Integer> sel = new ArrayList<>();
        List<Integer> unsel = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (selected.get(i)) {
                sel.add(i);
            } else {
                unsel.add(i);
            }
        }
        
        Random rng = FOMConfig.getRandomNumberGenerator();
        int del = sel.get(rng.nextInt(sel.size()));
        int add = unsel.get(rng.nextInt(unsel.size()));
        
        BitSet newSelected = (BitSet) selected.clone();
        newSelected.flip(add);
        newSelected.flip(del);
                
        return new CoreSubsetSolution(problem, size, coreSize, newSelected);

    }
    
    @Override
    public Movement getMovement() {
        throw new UnsupportedOperationException("Not used here.");
    }

    @Override
    public ExplorableSolution getNeighbour() {
        throw new UnsupportedOperationException("Not used here.");
    }

    @Override
    public long neighboursToExplore() {
        throw new UnsupportedOperationException("Not used here.");
    }

    @Override
    public void resetNeighbourhood() { /* do nothing */ }

}
