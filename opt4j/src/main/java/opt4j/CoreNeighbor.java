
package opt4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.opt4j.core.genotype.BooleanGenotype;
import org.opt4j.operators.neighbor.Neighbor;

public class CoreNeighbor implements Neighbor<BooleanGenotype> {

    private static final Random RNG = new Random();
    
    @Override
    public void neighbor(BooleanGenotype genotype) {
        
        System.out.println("HELLO");
        
        // swap a selected (true) and unselected (false) item
        List<Integer> sel = new ArrayList<>();
        List<Integer> unsel = new ArrayList<>();
        for (int i = 0; i < genotype.size(); i++) {
            if (genotype.get(i)) {
                sel.add(i);
            } else {
                unsel.add(i);
            }
        }
        
        int add = unsel.get(RNG.nextInt(unsel.size()));
        int del = sel.get(RNG.nextInt(sel.size()));
        
        genotype.set(add, true);
        genotype.set(del, false);
        
    }

}
