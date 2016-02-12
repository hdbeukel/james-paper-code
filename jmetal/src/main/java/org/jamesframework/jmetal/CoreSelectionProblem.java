

package org.jamesframework.jmetal;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jamesframework.core.util.SetUtilities;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;


public class CoreSelectionProblem extends AbstractBinaryProblem {

    private static final Random RG = new Random();
    
    private final int n;
    private final int s;
    private final double[][] dist;
    private final Set<Integer> ids;
    
    public CoreSelectionProblem(double[][] dist, int coreSize) {
        
        this.dist = dist;
        this.n = dist.length;
        this.s = coreSize;
        
        setNumberOfVariables(1);
        setNumberOfObjectives(1);
        
        ids = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        
    }
    
    public int getCoreSize() {
        return s;
    }
    
    public int getDatasetSize() {
        return n;
    }
    
    public Set<Integer> getIDs() {
        return ids;
    }

    @Override
    public void evaluate(BinarySolution sol) {
        
        // compute average pairwise distance
        int numDist = s * (s - 1) / 2;
        double sumDist = 0.0;
        int[] sel = sol.getVariableValue(0).stream().toArray();
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s; j++) {
                sumDist += dist[sel[i]][sel[j]];
            }
        }
        
        // maximize average distance
        sol.setObjective(0, -sumDist / numDist);
        
    }

    @Override
    public BinarySolution createSolution() {
        
        // random subset of IDs to select
        Set<Integer> sel = SetUtilities.getRandomSubset(ids, s, RG);
        // convert to binary solution
        BinarySolution sol = new DefaultBinarySolution(this);
        BinarySet bitset = new BinarySet(n);
        for (int id : ids) {
            if (sel.contains(id)) {
                bitset.set(id);
            }
        }
        sol.setVariableValue(0, bitset);
        
        return sol;
        
    }

    @Override
    protected int getBitsPerVariable(int index) {
        return n;
    }
        
}
