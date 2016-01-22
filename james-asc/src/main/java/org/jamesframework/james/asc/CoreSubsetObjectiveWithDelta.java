package org.jamesframework.james.asc;

import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.problems.objectives.evaluations.SimpleEvaluation;
import org.jamesframework.core.search.neigh.Move;
import org.jamesframework.core.subset.SubsetSolution;
import org.jamesframework.core.subset.neigh.moves.SwapMove;

public class CoreSubsetObjectiveWithDelta extends CoreSubsetObjective {

    @Override
    public Evaluation evaluate(Move move, SubsetSolution curSolution, Evaluation curEvaluation, CoreSubsetData data) {
        
        // cast move
        SwapMove swapMove = (SwapMove) move;

        // get current evaluation
        double curEval = curEvaluation.getValue();
        // undo average to get sum of distances
        int numSelected = curSolution.getNumSelectedIDs();
        int numDistances = numSelected * (numSelected - 1) / 2;
        double sumDist = curEval * numDistances;

        // get added and removed ID
        int added = swapMove.getAddedID();
        int removed = swapMove.getDeletedID();
        
        // update distance sum
        sumDist += curSolution.getSelectedIDs()
                              .stream()
                              .mapToDouble(id -> data.getDistance(added, id) - data.getDistance(removed, id))
                              .sum();
        // correction term
        sumDist -= data.getDistance(added, removed);

        // return new evaluation
        double newEval = sumDist / numDistances;
        return SimpleEvaluation.WITH_VALUE(newEval);

    }

}
