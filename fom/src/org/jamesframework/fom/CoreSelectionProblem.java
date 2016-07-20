
package org.jamesframework.fom;

import es.us.lsi.isa.fomfw.problem.UniversalProblem;
import es.us.lsi.isa.fomfw.solution.Solution;

public class CoreSelectionProblem extends UniversalProblem {

    private final double[][] dist;
    
    public CoreSelectionProblem(double[][] dist) {
        this.dist = dist;
    }
    
    @Override
    public double fitness(Solution sol) {
                
        // cast to core subset solution
        CoreSubsetSolution coreSol = (CoreSubsetSolution) sol;
        
        // compute average pairwise distance
        double sumDist = 0.0;
        int[] sel = coreSol.getSelected().stream().toArray();
        int s = sel.length;
        int numDist = s * (s - 1) / 2;
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s; j++) {
                sumDist += dist[sel[i]][sel[j]];
            }
        }
        
        // maximize average distance
        double avgDist = sumDist/numDist;
        return -avgDist;
        
    }
    
    public double getDistance(int i, int j){
        return dist[i][j];
    }

}
