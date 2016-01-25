
package org.jamesframework.eva2;

import eva2.optimization.individuals.AbstractEAIndividual;
import eva2.optimization.individuals.InterfaceDataTypeBinary;
import eva2.optimization.population.Population;
import eva2.problems.AbstractProblemBinary;
import java.util.BitSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jamesframework.core.util.SetUtilities;

public class CoreSelectionProblem extends AbstractProblemBinary {

    // random generator
    private static final Random RNG = new Random();
    
    // size of the dataset (number of individuals)
    private final int n;
    // size of the core collection
    private final int coreSize;
    // set of indices assigned to items in bitset
    private final Set<Integer> indices;
    
    // distance matrix
    private final double[][] dist;
    
    public CoreSelectionProblem(int n, int coreSize, double[][] dist) {
        this.n = n;
        this.coreSize = coreSize;
        this.dist = dist;
        indices = IntStream.range(0, n).boxed().collect(Collectors.toSet());
    }
    
    @Override
    public double[] evaluate(BitSet t) {

        int s = t.cardinality();
        int numDist = s * (s - 1) / 2;
        double sumDist = 0.0;
        int[] sel = t.stream().toArray();
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s; j++) {
                sumDist += dist[sel[i]][sel[j]];
            }
        }
        
        // maximize average distance
        return new double[]{-sumDist / numDist};
        
    }

    @Override
    public int getProblemDimension() {
        return n;
    }

    @Override
    public Object clone() {
        CoreSelectionProblem copy = new CoreSelectionProblem(n, coreSize, dist);
        copy.cloneObjects(this);
        return copy;
    }

    @Override
    public void initializePopulation(Population population) {
        
        super.initializePopulation(population);
        
        // modify solutions to match desired core size
        for (AbstractEAIndividual ind : population) {
            InterfaceDataTypeBinary indBinary = (InterfaceDataTypeBinary) ind;
            BitSet b = indBinary.getBinaryData();
            // generate random subset of indices to set
            Set<Integer> subset = SetUtilities.getRandomSubset(indices, coreSize, RNG);
            // modify bits in bitset
            b.clear();
            subset.forEach(i -> b.set(i));
            // set modified genotype
            indBinary.setBinaryGenotype(b);
        }
        
    }
    
    
    
}
