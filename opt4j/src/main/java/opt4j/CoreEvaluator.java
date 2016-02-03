
package opt4j;

import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class CoreEvaluator implements Evaluator<CoreSolution> {

    public static double[][] dist;
    
    @Override
    public Objectives evaluate(CoreSolution core) {
        
        int n = core.size();
        int numDist = n * (n - 1) / 2;
        double sumDist = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sumDist += dist[core.get(i)][core.get(j)];
            }
        }
        double avgDist = sumDist / numDist;
        
        Objectives obj = new Objectives();
        obj.add("distance", Sign.MAX, avgDist);
        return obj;
        
    }

}
