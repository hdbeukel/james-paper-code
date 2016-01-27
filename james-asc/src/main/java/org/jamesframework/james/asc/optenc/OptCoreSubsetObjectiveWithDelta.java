package org.jamesframework.james.asc.optenc;

import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.problems.objectives.evaluations.SimpleEvaluation;
import org.jamesframework.core.search.neigh.Move;
import org.jamesframework.james.asc.CoreSubsetData;

public class OptCoreSubsetObjectiveWithDelta extends OptCoreSubsetObjective {

    @Override
    public Evaluation evaluate(Move move, CoreSubsetSolution curSolution,
                               Evaluation curEvaluation, CoreSubsetData data) {
        
        // cast move
        OptSwapMove swapMove = (OptSwapMove) move;

        // get current evaluation
        double curEval = curEvaluation.getValue();
        // undo average to get sum of distances
        int numSelected = curSolution.getSel().length;
        int numDistances = numSelected * (numSelected - 1) / 2;
        double sumDist = curEval * numDistances;

        // get added and removed ID
        int added = curSolution.getUnsel()[swapMove.getAddI()];
        int removed = curSolution.getSel()[swapMove.getDelI()];
        
        // update distance sum
        for (int id : curSolution.getSel()) {
            sumDist += data.getDistance(added, id) - data.getDistance(removed, id);
        }
        // correction term
        sumDist -= data.getDistance(added, removed);

        // return new evaluation
        double newEval = sumDist / numDistances;
        return SimpleEvaluation.WITH_VALUE(newEval);

    }

}
