package org.jamesframework.opt4j;

import org.opt4j.core.genotype.BooleanGenotype;
import org.opt4j.core.problem.Decoder;

public class CoreDecoder implements Decoder<BooleanGenotype, CoreSubset> {

    @Override
    public CoreSubset decode(BooleanGenotype geno) {

        CoreSubset subset = new CoreSubset();
        for (int i = 0; i < geno.size(); i++) {
            if (geno.get(i)) {
                subset.add(i);
            }
        }
        return subset;

    }

}
