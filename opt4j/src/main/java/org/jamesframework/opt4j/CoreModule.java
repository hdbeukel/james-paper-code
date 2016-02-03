

package org.jamesframework.opt4j;

import org.opt4j.core.problem.ProblemModule;
import org.opt4j.core.start.Constant;
import org.opt4j.operators.neighbor.Neighbor;


public class CoreModule extends ProblemModule {
    
    @Override
    protected void config() {
        bindProblem(CoreCreator.class, CoreDecoder.class, CoreEvaluator.class);
        bind(Neighbor.class).to(CoreNeighbor.class);
    }

}
