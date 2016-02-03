
package opt4j;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jamesframework.core.util.SetUtilities;
import org.opt4j.core.genotype.BooleanGenotype;
import org.opt4j.core.problem.Creator;

public class CoreCreator implements Creator<BooleanGenotype> {

    private static final Random RNG = new Random();
    
    public static int n;
    public static int coreSize;
    
    public static Set<Integer> all;

    public CoreCreator() {
        all = IntStream.range(0, n).boxed().collect(Collectors.toSet());
    }
    
    @Override
    public BooleanGenotype create() {
        Set<Integer> sel = SetUtilities.getRandomSubset(all, coreSize, RNG);
        
        BooleanGenotype geno = new BooleanGenotype();
        for (int i = 0; i < all.size(); i++) {
            geno.add(sel.contains(i));
        }
        
        return geno;
    }
    
}
