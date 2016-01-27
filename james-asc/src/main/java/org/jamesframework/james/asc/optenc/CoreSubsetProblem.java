

package org.jamesframework.james.asc.optenc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.util.SetUtilities;
import org.jamesframework.james.asc.CoreSubsetData;


public class CoreSubsetProblem extends GenericProblem<CoreSubsetSolution, CoreSubsetData> {

    public CoreSubsetProblem(CoreSubsetData data, int coreSize,
                             Objective<? super CoreSubsetSolution, ? super CoreSubsetData> objective) {
        super(data, objective, (r, d) -> {
            
            int i;
            
            Set<Integer> sel = SetUtilities.getRandomSubset(d.getIDs(), coreSize, r);
            int[] selA = new int[sel.size()];
            i = 0;
            for (int id : sel) {
                selA[i++] = id;
            }
            
            List<Integer> unsel = new ArrayList<>(d.getIDs());
            unsel.removeAll(sel);
            int[] unselA = new int[unsel.size()];
            i = 0;
            for (int id : unsel) {
                unselA[i++] = id;
            }
            
            return new CoreSubsetSolution(selA, unselA);
        });
    }

}
