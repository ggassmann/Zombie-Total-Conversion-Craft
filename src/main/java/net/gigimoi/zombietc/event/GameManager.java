package net.gigimoi.zombietc.event;

import com.google.gson.Gson;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import javafx.scene.text.TextAlignment;
import net.gigimoi.zombietc.EntityZZombie;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.TextRenderHelper;
import net.gigimoi.zombietc.net.MessageAddNode;
import net.gigimoi.zombietc.net.MessageAddNodeConnection;
import net.gigimoi.zombietc.net.MessageSetWave;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.gigimoi.zombietc.pathfinding.MCNode;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;

import javax.vecmath.Vector3f;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by gigimoi on 7/14/2014.
 */
public class GameManager {
    private static class GameData {
        int zombiesToSpawn = 0;
        public int wave = 0;
        int zombiesAlive = 0;
        int timeToNextWave = 0;

        int nextWaveZombies = 0;
        int currentWaveMaxZombies = 0;
        List<MCNode> nodes;
        List<BlockNode.MCNodePair> nodeConnections;
        public GameData() {
        }
        public GameData(GameManager manager) {
            zombiesAlive = manager.zombiesAlive;
            wave = manager.wave;
            zombiesToSpawn = manager.zombiesToSpawn;
            timeToNextWave = manager.timeToNextWave;
            nextWaveZombies = manager.nextWaveZombies;
            currentWaveMaxZombies = manager.currentWaveMaxZombies;
            nodes = BlockNode.nodes;
            nodeConnections = BlockNode.nodeConnections;
        }
    }
    public static ArrayList<Vector3f> spawnPositions = new ArrayList<Vector3f>();
    public static ArrayList<World> worldsSpawnedTo = new ArrayList<World>();
    int zombiesToSpawn = 0;
    public int wave = 0;
    int zombiesAlive = 0;
    int timeToNextWave = 0;

    int nextWaveZombies = 0;
    int currentWaveMaxZombies = 0;

    private Random _r = new Random();

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(event.side == Side.SERVER) {
            if(ZombieTC.editorModeManager.enabled) {
                wave = 0;
                zombiesAlive = 0;
                timeToNextWave = 0;
                nextWaveZombies = 10;
                currentWaveMaxZombies = 0;
                zombiesToSpawn = 0;
                return;
            }
            if(shouldSpawn() && spawnPositions.size() > 0) {
                int i = _r.nextInt(spawnPositions.size());
                EntityZZombie zombie = new EntityZZombie(worldsSpawnedTo.get(i));
                zombie.setPosition(spawnPositions.get(i).x, spawnPositions.get(i).y, spawnPositions.get(i).z);
                worldsSpawnedTo.get(i).spawnEntityInWorld(zombie);
                zombiesAlive++;
                zombiesToSpawn--;
            }
            spawnPositions = new ArrayList<Vector3f>();
            worldsSpawnedTo = new ArrayList<World>();
            if(zombiesAlive == 0 && zombiesToSpawn == 0 && timeToNextWave == 0) {
                wave++;
                timeToNextWave = 60 * 6;
                nextWaveZombies = currentWaveMaxZombies + 10;
                ZombieTC.network.sendToAll(new MessageSetWave(wave));
            }
            timeToNextWave = Math.max(0, timeToNextWave - 1);
            if(timeToNextWave == 1) {
                currentWaveMaxZombies = nextWaveZombies;
                zombiesToSpawn = currentWaveMaxZombies;
            }
        }
    }

    private boolean shouldSpawn() {
        if(zombiesToSpawn == 0) {
            return false;
        }
        if(wave < 3 && _r.nextInt(60) != 0) {
            return false;
        }
        if(wave >= 3 && wave < 5 && _r.nextInt(50) != 0) {
            return false;
        }
        if(wave >= 5 && wave < 8 && _r.nextInt(40) != 0) {
            return false;
        }
        if(wave >= 8 && wave < 13 && _r.nextInt(30) != 0) {
            return false;
        }
        if(wave >= 13 && wave < 20 && _r.nextInt(20) != 0) {
            return false;
        }
        return true;
    }

    public String getSaveFilePath(World world) {
        return (MinecraftServer.getServer().isDedicatedServer() ? MinecraftServer.getServer().getFolderName() : ("saves/" + world.getSaveHandler().getWorldDirectoryName())) + "/zombietc.json";
    }
    public boolean getSaveFileExists(World world) {
        String path = getSaveFilePath(world);
        return new File(path).exists();
    }
    public String getSaveFile(World world) {
        String location = getSaveFilePath(world);
        File f = new File(location);
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return location;
    }
    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) throws FileNotFoundException {
        if(event.world.getSaveHandler().getWorldDirectoryName().equals("none")) {
            return;
        }
        if(!getSaveFileExists(event.world)) {
            wave = 0;
            currentWaveMaxZombies = 0;
            nextWaveZombies = 10;
            timeToNextWave = 0;
            zombiesToSpawn = 0;
            zombiesAlive = 0;
            BlockNode.nodes = new ArrayList<MCNode>();
            BlockNode.nodeConnections = new ArrayList<BlockNode.MCNodePair>();
            return;
        }
        Gson gson = new Gson();
        Scanner in = new Scanner(new FileReader(getSaveFile(event.world)));
        String fileData = "";
        while (in.hasNext()) { fileData += in.next(); }

        GameData saveData = gson.fromJson(fileData, GameData.class);
        zombiesAlive = saveData.zombiesAlive;
        currentWaveMaxZombies = saveData.currentWaveMaxZombies;
        timeToNextWave = saveData.timeToNextWave;
        wave = saveData.wave;
        zombiesAlive = saveData.zombiesAlive;
        zombiesToSpawn = saveData.zombiesToSpawn;
        BlockNode.nodes = saveData.nodes;
        BlockNode.nodeConnections = saveData.nodeConnections;
        for(int i = 0; i < BlockNode.nodes.size(); i++) { //Fixes duplicate node entries
            MCNode node = BlockNode.nodes.get(i);
            for(int j = 0; j < BlockNode.nodeConnections.size(); j++) {
                BlockNode.MCNodePair link = BlockNode.nodeConnections.get(j);
                if(link.n1.position.distanceTo(node.position) < 0.001) {
                    link.n1 = node;
                }
                if(link.n2.position.distanceTo(node.position) < 0.001) {
                    link.n2 = node;
                }
            }
        }
    }
    @SubscribeEvent
    public void onSave(WorldEvent.Save event) throws FileNotFoundException {
        Gson gson = new Gson();
        PrintWriter writer = new PrintWriter(getSaveFile(event.world));
        writer.print(gson.toJson(new GameData(this)));
        writer.flush();
        writer.close();
    }
    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.player.getEntityWorld().isRemote && !MinecraftServer.getServer().isSinglePlayer()) {
            ZombieTC.network.sendTo(new MessageSetWave(wave), (EntityPlayerMP) event.player);
            for(int i = 0; i < BlockNode.nodes.size(); i++) {
                ZombieTC.network.sendTo(new MessageAddNode(
                        (int)BlockNode.nodes.get(i).position.xCoord,
                        (int)BlockNode.nodes.get(i).position.yCoord,
                        (int)BlockNode.nodes.get(i).position.zCoord
                    ), (EntityPlayerMP)event.player
                );
            }
            for(int i = 0; i < BlockNode.nodeConnections.size(); i++) {
                ZombieTC.network.sendTo(new MessageAddNodeConnection(
                        Vec3.createVectorHelper(
                                (int)BlockNode.nodeConnections.get(i).n1.position.xCoord,
                                (int)BlockNode.nodeConnections.get(i).n1.position.yCoord,
                                (int)BlockNode.nodeConnections.get(i).n1.position.zCoord),
                        Vec3.createVectorHelper(
                                (int)BlockNode.nodeConnections.get(i).n2.position.xCoord,
                                (int)BlockNode.nodeConnections.get(i).n2.position.yCoord,
                                (int)BlockNode.nodeConnections.get(i).n2.position.zCoord
                        )
                ), (EntityPlayerMP)event.player);
            }
        }
    }
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if(event.entityLiving.getClass() == EntityZZombie.class && !event.entity.worldObj.isRemote) {
            zombiesAlive--;
        }
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
        if(event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            if(ZombieTC.editorModeManager.enabled) {
                TextRenderHelper.drawString("Editor mode enabled", 2, 2, TextAlignment.LEFT);
            }
            TextRenderHelper.drawString("Wave: " + wave, 2, (int) (event.resolution.getScaledHeight()) - 10, TextAlignment.LEFT);
            TextRenderHelper.drawString("Zombies Left: " + (zombiesToSpawn + zombiesAlive), 2, (int) (event.resolution.getScaledHeight()) - 20, TextAlignment.LEFT);
            ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if(heldItem != null && heldItem.getItem().getClass() == ItemWeapon.class) {
                ((ItemWeapon)heldItem.getItem()).drawUIFor(heldItem, event);
            }
        }
    }
}