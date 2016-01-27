

package org.jamesframework.jmetal;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jamesframework.core.util.SetUtilities;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;


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
        
        setNumberOfVariables(n);
        setNumberOfObjectives(1);
        
        ids = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        
    }
    
    @Override
    protected int getBitsPerVariable(int i) {
        return 1;
    }

    @Override
    public void evaluate(BinarySolution sol) {

        // create array of selected item IDs
        int[] sel = new int[s];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (sol.getVariableValue(i).get(0)) {
                sel[k] = i;
                k++;
            }
        }
        
        // compute average pairwise distance
        int numDist = s * (s - 1) / 2;
        double sumDist = 0.0;
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
        
        // create solution
        BinarySolution sol = new DefaultBinarySolution(this);
        for (int i = 0; i < n; i++) {
            if (sel.contains(i)) {
                sol.getVariableValue(i).set(0);
            } else {
                sol.getVariableValue(i).clear(0);
            }
        }
        
        return sol;
        
    }
        
}
