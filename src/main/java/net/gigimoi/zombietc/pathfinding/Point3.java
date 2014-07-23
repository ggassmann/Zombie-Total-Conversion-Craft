package net.gigimoi.zombietc.pathfinding;

import net.minecraft.util.Vec3;

import java.util.List;

/**
 * Created by gigimoi on 7/22/2014.
 */
public class Point3 {
    public int xCoord;
    public int yCoord;
    public int zCoord;

    public Point3(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }
    public double distanceTo(Point3 pos) {
        return toVec3().distanceTo(pos.toVec3());
    }
    public static Point3 fromVec3(Vec3 vec) {
        return new Point3((int)vec.xCoord, (int)vec.yCoord, (int)vec.zCoord);
    }

    public Vec3 toVec3() {
        return Vec3.createVectorHelper(xCoord, yCoord, zCoord);
    }
}
