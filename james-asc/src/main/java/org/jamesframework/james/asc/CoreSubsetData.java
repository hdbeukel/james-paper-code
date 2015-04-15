/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamesframework.james.asc;

import java.util.HashSet;
import java.util.Set;
import org.jamesframework.core.problems.datatypes.IntegerIdentifiedData;

public class CoreSubsetData implements IntegerIdentifiedData {

    private double[][] dist;
    private Set<Integer> ids;

    public CoreSubsetData(double[][] dist) {
        this.dist = dist;
        ids = new HashSet<>();
        for (int id = 0; id < dist.length; id++) {
            ids.add(id);
        }
    }

    public double getDistance(int id1, int id2) {
        return dist[id1][id2];
    }

    @Override
    public Set<Integer> getIDs() {
        return ids;
    }

}
