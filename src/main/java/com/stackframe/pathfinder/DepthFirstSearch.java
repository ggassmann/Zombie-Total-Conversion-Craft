// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package com.stackframe.pathfinder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An implementation of the Depth First Search path finding algorithm.
 *
 * @author Gene McCulley
 */
public class DepthFirstSearch<T extends Node<T>> extends AbstractPathFinder<T> {

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
            for (T neighbor : state.node.neighbors()) {
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
        return "Depth First";
    }

}