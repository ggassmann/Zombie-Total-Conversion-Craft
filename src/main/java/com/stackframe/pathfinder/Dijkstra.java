// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package com.stackframe.pathfinder;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An implementation of Dijkstra's algorithm for path finding.
 *
 * @author Gene McCulley
 */
public class Dijkstra<T extends Node<T>> extends AbstractPathFinder<T> {

    private class State extends NodeState<T> implements Comparable<State> {

        private double dist;

        private State(T node, State parent, double dist) {
            super(node, parent);
            this.dist = dist;
        }

        public int compareTo(State other) {
            return (int)(dist - other.dist);
        }

    }

    public List<T> findPath(Collection<T> graph, T start, Collection<T> goals) {
        canceled = false;
        final Map<T, State> states = new HashMap<T, State>();
        final Set<T> Q = new HashSet<T>(graph);

        for (T n : graph) {
            states.put(n, new State(n, null, Double.POSITIVE_INFINITY));
        }

        states.get(start).dist = 0;
        Predicate<Map.Entry<T, State>> notVisited = new Predicate<Map.Entry<T, State>>() {

            public boolean apply(Entry<T, State> t) {
                return Q.contains(t.getKey());
            }

        };
        Ordering<Map.Entry<T, State>> orderByEntryValue = Utilities.orderByEntryValue();
        while (!(Q.isEmpty() || canceled)) {
            Collection<Map.Entry<T, State>> inQ = Collections2.filter(states.entrySet(), notVisited);
            final Map.Entry<T, State> uEntry = orderByEntryValue.min(inQ);
            if (uEntry.getValue().dist == Double.POSITIVE_INFINITY) {
                break;
            }

            final T u = uEntry.getKey();
            final State state = uEntry.getValue();
            fireConsidered(new PathEvent<T>(this) {

                @Override
                public List<T> getPath() {
                    return state.makePath();
                }

            });
            Q.remove(u);
            if (goals.contains(u)) {
                return state.makePath();
            }

            for (T v : u.neighbors()) {
                double alt = state.dist + u.traverseCost(v);
                State vState = states.get(v);
                if (alt < vState.dist) {
                    vState.dist = alt;
                    vState.previous = state;
                }
            }
        }

        return null;
    }

    public String name() {
        return "Dijkstra";
    }

}