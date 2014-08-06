package net.gigimoi.zombietc.block;

import net.minecraft.init.Blocks;

/**
 * Created by gigimoi on 8/5/2014.
 */
public class BlockNodeDoor extends BlockNode {
    public static final BlockNodeDoor iron = new BlockNodeDoor();
    public BlockNodeDoor() {
        setBlockName("Node Door");
    }
    @Override
    public int getRenderType() {
        return Blocks.iron_door.getRenderType();
    }
    @Override
    public boolean renderAsNormalBlock() {
        return Blocks.iron_door.renderAsNormalBlock();
    }
}