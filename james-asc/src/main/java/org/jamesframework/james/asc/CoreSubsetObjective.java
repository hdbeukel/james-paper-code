package org.jamesframework.james.asc;

import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.problems.objectives.evaluations.SimpleEvaluation;
import org.jamesframework.core.subset.SubsetSolution;

public class CoreSubsetObjective implements Objective<SubsetSolution, CoreSubsetData> {

    @Override
    public Evaluation evaluate(SubsetSolution solution, CoreSubsetData data) {
        int n = solution.getNumSelectedIDs();
        int numDist = n * (n - 1) / 2;
        double sumDist = 0.0;
        Integer[] sel = new Integer[n];
        solution.getSelectedIDs().toArray(sel);
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
