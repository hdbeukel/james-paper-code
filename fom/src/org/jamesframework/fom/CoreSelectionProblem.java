
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.problem.UniversalProblem;
import es.us.lsi.isa.fomfw.solution.Solution;
import java.util.ArrayList;
import java.util.List;

public class CoreSelectionProblem extends UniversalProblem {

    private final double[][] dist;
    
    public CoreSelectionProblem(double[][] dist) {
        this.dist = dist;
    }
    
    @Override
    public double fitness(Solution sol) {
        
        // cast to core subset solution
        CoreSubsetSolution coreSel = (CoreSubsetSolution) sol;
        
        // extract indices of selected items
        boolean[] selected = coreSel.getSelected();
        List<Integer> core = new ArrayList<>();
        for(int i = 0; i < coreSel.getSize(); i++){
            if(selected[i]){
                core.add(i);
            }
        }
        
        // compute average pairwise distance
        int n = core.size();
        int numDist = n * (n - 1) / 2;
        double sumDist = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sumDist += dist[core.get(i)][core.get(j)];
            }
        }
        double avgDist = sumDist/numDist;
        
        return -avgDist; // maximize
        
    }

}
