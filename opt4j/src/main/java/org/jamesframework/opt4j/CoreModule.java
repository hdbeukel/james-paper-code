

package org.jamesframework.opt4j;

import org.opt4j.core.problem.ProblemModule;
import org.opt4j.core.start.Constant;


public class CoreModule extends ProblemModule {

    @Constant(value = "n")
    protected int n;
    
    @Constant(value = "coreSize")
    protected int coreSize;
    
    @Constant(value = "dist")
    protected double[][] dist;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public double[][] getDist() {
        return dist;
    }

    public void setDist(double[][] dist) {
        this.dist = dist;
    }
    
    @Override
    protected void config() {
        bindProblem(CoreCreator.class, CoreDecoder.class, CoreEvaluator.class);
    }

}
