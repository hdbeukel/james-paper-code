package org.jamesframework.opt4j;

import org.opt4j.core.genotype.BooleanGenotype;
import org.opt4j.core.problem.Decoder;

public class CoreDecoder implements Decoder<BooleanGenotype, CoreSolution> {

    @Override
    public CoreSolution decode(BooleanGenotype geno) {

        CoreSolution sol = new CoreSolution();
        for (int i = 0; i < geno.size(); i++) {
            if (geno.get(i)) {
                sol.add(i);
            }
        }
        
        return sol;

    }

}
