package net.gigimoi.zombietc.util.pathfinding;

import com.stackframe.pathfinder.Node;
import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.tile.TileNode;
import net.gigimoi.zombietc.util.Point3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class MCNode implements Node<MCNode> {
    public Point3 position;

    public transient ArrayList<MCNode> linksTo;

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
        if (isDisabled()) {
            return new ArrayList<MCNode>();
        }
        ArrayList<MCNode> links = (ArrayList<MCNode>) linksTo.clone();
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).isDisabled()) {
                links.remove(i);
            }
        }

        return links;
    }

    public boolean isDisabled() {
        World world = ZombieTC.proxy.getWorld(Side.CLIENT);
        if(world == null) {
            return true;
        }
        TileEntity tileRaw = world.getTileEntity(position.xCoord, position.yCoord, position.zCoord);
        TileNode tile = (TileNode) tileRaw;
        return tile == null || tile.deactivated;
    }
}
