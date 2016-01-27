
package org.jamesframework.eva2;

import eva2.optimization.individuals.AbstractEAIndividual;
import eva2.optimization.individuals.InterfaceDataTypeBinary;
import eva2.optimization.operator.mutation.InterfaceMutation;
import eva2.optimization.population.Population;
import eva2.problems.InterfaceOptimizationProblem;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SubsetSwapMutation implements InterfaceMutation {

    private static final Random RG = new Random();
    private final int n;

    public SubsetSwapMutation(int n) {
        this.n = n;
    }
    
    @Override
    public void initialize(AbstractEAIndividual individual, InterfaceOptimizationProblem opt) {}

    @Override
    public void mutate(AbstractEAIndividual individual) {
    
        InterfaceDataTypeBinary bInd = (InterfaceDataTypeBinary) individual;
        BitSet b = bInd.getBinaryData();
        
        // swap one selected and unselected item
        List<Integer> sel = b.stream().boxed().collect(Collectors.toList());
        List<Integer> unsel = IntStream.range(0, n).boxed()
                                                          .filter(i -> !sel.contains(i))
                                                          .collect(Collectors.toList());
        int add = unsel.get(RG.nextInt(unsel.size()));
        int del = sel.get(RG.nextInt(sel.size()));
        
        b.flip(add);
        b.flip(del);
        
        bInd.setBinaryGenotype(b);
        
    }

    @Override
    public void crossoverOnStrategyParameters(AbstractEAIndividual indy1, Population partners) {}

    @Override
    public String getStringRepresentation() {
        return "Subset swap mutation";
    }
    
    @Override
    public Object clone() {
        return new SubsetSwapMutation(n);
    }

}
