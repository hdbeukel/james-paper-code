package org.jamesframework.opt4j;

import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.optimizer.MaxIterations;
import org.opt4j.core.optimizer.OptimizerModule;
import org.opt4j.operators.neighbor.Neighbor;

/**
 * Adaptation of simulated annealing module from Opt4j.
 */
public class HillClimberModule extends OptimizerModule {

    @Info("The number of iterations.")
    @MaxIterations
    protected int iterations = 100000;

    /**
     * Returns the number of iterations.
     *
     * @see #setIterations
     * @return the number of iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Sets the number of iterations.
     *
     * @see #getIterations
     * @param iterations the number of iterations
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public void config() {
        bindIterativeOptimizer(HillClimber.class);
        bind(Neighbor.class).to(CoreNeighbor.class);
    }
    
}
