
package org.jamesframework.jmetal;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.Solution;

public class CopyFirstParentCrossover<S extends Solution<?>> implements CrossoverOperator<S> {

    @SuppressWarnings("unchecked")
    @Override
    public List<S> execute(List<S> source) {

        List<S> list = new ArrayList<>();
        list.add((S) source.get(0).copy());

        return list;

    }

    @Override
    public int getNumberOfParents() {
        return 1;
    }

}
