
package org.jamesframework.opt4j;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jamesframework.core.util.SetUtilities;
import org.opt4j.core.genotype.BooleanGenotype;
import org.opt4j.core.problem.Creator;
import org.opt4j.core.start.Constant;

public class CoreCreator implements Creator<BooleanGenotype> {

    private static final Random RNG = new Random();
    
    private final int n;
    private final int coreSize;

    public CoreCreator(@Constant(value = "n") int n, @Constant(value = "coreSize") int coreSize) {
        this.n = n;
        this.coreSize = coreSize;
    }
    
    @Override
    public BooleanGenotype create() {
        BooleanGenotype geno = new BooleanGenotype();
        Set<Integer> all = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        Set<Integer> sel = SetUtilities.getRandomSubset(all, coreSize, RNG);
        for (int i = 0; i < n; i++) {
            geno.add(sel.contains(i));
        }
        return geno;
    }
    
}
