
package org.jamesframework.fom;
import es.us.lsi.isa.fomfw.solution.*;
import es.us.lsi.isa.fomfw.metaheuristic.localsearch.*;


public class StochasticHillClimber extends LocalSearch {

    protected Solution aplica(ExplorableSolution sol) 
    {
        ExplorableSolution candidata;
        current=sol;
        optimum=sol;
        double diferencia=0;
        niterations=0;
        current.resetNeighbourhood();
        terminator.reset();
        observer.resetObserver();
        // Main Loop
        while(!terminator.terminationCriteria(optimum, current,this) && !stop)
        {
            candidata=current.getRandomNeighbour();
            diferencia= candidata.getFitness() - current.getFitness();
            if(diferencia < 0)
            {
                current = candidata;
                if(current.getFitness() < optimum.getFitness())
                    optimum=current;
            }
            niterations++;
            observer.notice();
        }
        return optimum;
    }
    
}
