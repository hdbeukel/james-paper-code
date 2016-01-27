

package org.jamesframework.james.asc.optenc;

import java.util.List;
import java.util.Random;
import org.jamesframework.core.search.neigh.Move;
import org.jamesframework.core.search.neigh.Neighbourhood;


public class OptSingleSwapNeighbourhood implements Neighbourhood<CoreSubsetSolution> {

    @Override
    public Move<? super CoreSubsetSolution> getRandomMove(CoreSubsetSolution solution, Random rnd) {
        
        int addI = rnd.nextInt(solution.getUnsel().length);
        int delI = rnd.nextInt(solution.getSel().length);
        
        return new OptSwapMove(addI, delI);
        
    }

    @Override
    public List<? extends Move<? super CoreSubsetSolution>> getAllMoves(CoreSubsetSolution solution) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
