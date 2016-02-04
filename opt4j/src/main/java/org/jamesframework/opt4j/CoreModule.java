

package org.jamesframework.opt4j;

import org.opt4j.core.problem.ProblemModule;


public class CoreModule extends ProblemModule {
    
    @Override
    protected void config() {
        bindProblem(CoreCreator.class, CoreDecoder.class, CoreEvaluator.class);
    }

}
