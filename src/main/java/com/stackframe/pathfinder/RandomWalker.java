// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package com.stackframe.pathfinder;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * An implementation of the Depth First Search path finding algorithm.
 *
 * @author Gene McCulley
 */
public class RandomWalker<T extends Node<T>> extends AbstractPathFinder<T> {

    private final Random rng = new Random();

    public List<T> findPath(Collection<T> graph, T start, Collection<T> goals) {
        canceled = false;
        return search(new NodeState<T>(start, null), new HashSet<T>(), goals);
    }

    private List<T> search(final NodeState<T> state, final Set<T> visited, Collection<T> goals) {
        if (canceled) {
            return null;
        }

        visited.add(state.node);
        fireConsidered(new PathEvent<T>(this) {

            public List<T> getPath() {
                return state.makePath();
            }

        });
        if (goals.contains(state.node)) {
            return state.makePath();
        } else {
            List<T> neighbors = Lists.newLinkedList(state.node.neighbors());
            while (!neighbors.isEmpty()) {
                T neighbor = neighbors.remove(rng.nextInt(neighbors.size()));
                if (!visited.contains(neighbor)) {
                    List<T> path = search(new NodeState<T>(neighbor, state), visited, goals);
                    if (path != null) {
                        return path;
                    }
                }
            }
        }

        return null;
    }

    public String name() {
        return "Random Walker";
    }

}