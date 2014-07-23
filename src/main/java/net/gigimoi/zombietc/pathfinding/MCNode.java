package net.gigimoi.zombietc.pathfinding;

import com.stackframe.pathfinder.Node;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class MCNode implements Node<MCNode> {
    public Point3 position;

    public transient List<MCNode> linksTo;
                                           //GameManager should have a
                                           //method to reset all links.
                                           //Reset links on editor mode toggle and join
    public MCNode() {
        this.linksTo = new ArrayList();
    }

    public MCNode(Vec3 pos) {
        this();
        this.position = Point3.fromVec3(pos);
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
        return linksTo;
    }
}
