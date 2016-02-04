

package org.jamesframework.jmetal.optenc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jamesframework.core.util.SetUtilities;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;


public class CoreSelectionProblem extends AbstractGenericProblem<CoreSolution> {

    private static final Random RG = new Random();
    
    private final int n;
    private final int s;
    private final double[][] dist;
    private final Set<Integer> ids;
    
    public CoreSelectionProblem(double[][] dist, int coreSize) {
        
        this.dist = dist;
        this.n = dist.length;
        this.s = coreSize;
        
        setNumberOfVariables(s);
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
    public void evaluate(CoreSolution sol) {
        
        // compute average pairwise distance
        int numDist = s * (s - 1) / 2;
        double sumDist = 0.0;
        int id1, id2;
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s; j++) {
                id1 = sol.getVariableValue(i);
                id2 = sol.getVariableValue(j);
                sumDist += dist[id1][id2];
            }
        }
        
        // maximize average distance
        sol.setObjective(0, -sumDist / numDist);
        
    }

    @Override
    public CoreSolution createSolution() {
        
        // random subset of IDs to select
        List<Integer> sel = new ArrayList<>();
        SetUtilities.getRandomSubset(ids, s, RG, sel);
        
        // unselected IDs
        Set<Integer> unsel = new HashSet<>(ids);
        unsel.removeAll(sel);
        int[] unselA = new int[unsel.size()];
        int i = 0;
        for (int id : unsel) {
            unselA[i++] = id;
        }
        
        // create solution
        CoreSolution sol = new CoreSolution(this, sel, unselA);
        
        return sol;
        
    }
        
}
