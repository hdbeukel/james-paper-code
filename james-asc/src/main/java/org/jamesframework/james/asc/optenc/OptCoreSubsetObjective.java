
package org.jamesframework.james.asc.optenc;

import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.problems.objectives.evaluations.SimpleEvaluation;
import org.jamesframework.james.asc.CoreSubsetData;


public class OptCoreSubsetObjective implements Objective<CoreSubsetSolution, CoreSubsetData> {

    @Override
    public Evaluation evaluate(CoreSubsetSolution solution, CoreSubsetData data) {
        
        int n = solution.getSel().length;
        int numDist = n * (n - 1) / 2;
        double sumDist = 0.0;
        int[] sel = solution.getSel();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sumDist += data.getDistance(sel[i], sel[j]);
            }
        }
        return new SimpleEvaluation(sumDist / numDist);
        
    }

    @Override
    public boolean isMinimizing() {
        return false;
    }

}
