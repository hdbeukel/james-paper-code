

package org.jamesframework.jmetal;

import java.util.List;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;

public class SelectFirstParent<S extends Solution<?>> implements SelectionOperator<List<S>, S> {

    @Override
    public S execute(List<S> source) {
        return source.get(0);
    }

}
