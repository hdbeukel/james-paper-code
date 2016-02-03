
package org.jamesframework.opt4j;

import org.opt4j.operators.neighbor.NeighborModule;


public class CoreNeighborModule extends NeighborModule {

    @Override
    protected void config() {
        addOperator(CoreNeighbor.class);        
    }

}
