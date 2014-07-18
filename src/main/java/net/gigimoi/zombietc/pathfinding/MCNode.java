package net.gigimoi.zombietc.pathfinding;

import com.stackframe.pathfinder.Node;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class MCNode implements Node<MCNode> {
    public Vec3 position;

    public MCNode(Vec3 pos) {
        this.position = pos;
    }

    @Override
    public double pathCostEstimate(MCNode goal) {
        return position.distanceTo(goal.position);
    }

    @Override
    public double traverseCost(MCNode dest) {
        return 1;
    }

    @Override
    public Iterable<MCNode> neighbors() {
        List<MCNode> neighbors = new ArrayList<MCNode>();
        for(int i = 0; i < BlockNode.nodeConnections.size(); i++) {
            BlockNode.MCNodePair link = BlockNode.nodeConnections.get(i);
            if(link.n1.position.distanceTo(position) < 0.001) {
                neighbors.add(link.n2);
            }
            if(link.n2.position.distanceTo(position) < 0.001) {
                neighbors.add(link.n1);
            }
        }
        return neighbors;
    }
}
