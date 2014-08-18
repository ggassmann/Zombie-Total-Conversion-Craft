package net.gigimoi.zombietc.event;

import com.google.gson.Gson;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.api.ITileEntityPurchasable;
import net.gigimoi.zombietc.block.BlockNode;
import net.gigimoi.zombietc.client.ClientProxy;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.gigimoi.zombietc.net.MessageChangeEditorMode;
import net.gigimoi.zombietc.net.MessagePurchaseTile;
import net.gigimoi.zombietc.net.MessageRegeneratePathMap;
import net.gigimoi.zombietc.net.MessageSetWave;
import net.gigimoi.zombietc.net.map.MessageAddBarricade;
import net.gigimoi.zombietc.net.map.MessageAddNode;
import net.gigimoi.zombietc.net.map.MessageAddNodeConnection;
import net.gigimoi.zombietc.net.map.MessagePrepareStaticVariables;
import net.gigimoi.zombietc.tile.TileZTC;
import net.gigimoi.zombietc.util.IListenerZTC;
import net.gigimoi.zombietc.util.Point3;
import net.gigimoi.zombietc.util.pathfinding.MCNode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.input.Keyboard;

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
    public static ArrayList<Point3> blockBarricades = new ArrayList<Point3>();
    public static ArrayList<Vector3f> spawnPositions = new ArrayList<Vector3f>();
    public static ArrayList<World> worldsSpawnedTo = new ArrayList<World>();
    public boolean activating;
    public int wave = 0;
    public int zombiesToSpawn = 0;
    public int zombiesAlive = 0;
    int timeToNextWave = 0;
    int nextWaveZombies = 0;
    int currentWaveMaxZombies = 0;
    short refreshFoodbarsCooldown = 0;
    private Random _r = new Random();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(ZombieTC.proxy.getWorld(event.side) == null) {
            return;
        }
        ZombieTC.proxy.getWorld(event.side).setWorldTime(ZombieTC.editorModeManager.enabled ? 1000 : 15000);
        refreshFoodbarsCooldown++;
        if (refreshFoodbarsCooldown > 200) {
            refreshFoodbarsCooldown = 0;
            List playerEntities = ZombieTC.proxy.getWorld(event.side).playerEntities;
            for (int i = 0; i < playerEntities.size(); i++) {
                EntityPlayer player = (EntityPlayer) playerEntities.get(i);
                player.getFoodStats().addStats(100, 100);
            }
        }
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.START) {
            if (ZombieTC.editorModeManager.enabled) {
                wave = 0;
                zombiesAlive = 0;
                timeToNextWave = 0;
                nextWaveZombies = 10;
                currentWaveMaxZombies = 0;
                zombiesToSpawn = 0;
                return;
            }
            if (shouldSpawn() && spawnPositions.size() > 0) {
                int i = _r.nextInt(spawnPositions.size());
                EntityZZombie zombie = new EntityZZombie(worldsSpawnedTo.get(i));
                zombie.setPosition(spawnPositions.get(i).x, spawnPositions.get(i).y, spawnPositions.get(i).z);
                worldsSpawnedTo.get(i).spawnEntityInWorld(zombie);
                zombiesAlive++;
                zombiesToSpawn--;
            }
            spawnPositions = new ArrayList<Vector3f>();
            worldsSpawnedTo = new ArrayList<World>();
            if (zombiesAlive == 0 && zombiesToSpawn == 0 && timeToNextWave == 0) {
                wave++;
                timeToNextWave = 60 * 6;
                nextWaveZombies = currentWaveMaxZombies + 10;
                ZombieTC.network.sendToAll(new MessageSetWave(wave));
            }
            timeToNextWave = Math.max(0, timeToNextWave - 1);
            if (timeToNextWave == 1) {
                currentWaveMaxZombies = nextWaveZombies;
                zombiesToSpawn = currentWaveMaxZombies;
            }
        } else if (event.side == Side.CLIENT && ZombieTC.proxy.getWorld(Side.CLIENT) != null) {
            EntityPlayer player = ZombieTC.proxy.getPlayer();
            TileEntity tilePlayerOver = player.worldObj.getTileEntity((int) Math.round(player.posX - 0.5), (int) Math.round(player.posY - 0.5), (int) Math.round(player.posZ - 0.5));
            if (tilePlayerOver != null && ITileEntityPurchasable.class.isAssignableFrom(tilePlayerOver.getClass())) {
                ITileEntityPurchasable purchasable = (ITileEntityPurchasable) tilePlayerOver;
                if (purchasable.getEnabled()) {
                    ZombieTC.gameOverlayManager.setActivateMessage("Press [" + Keyboard.getKeyName(ClientProxy.activate.getKeyCode()) + "] to " + purchasable.getVerb() + ": " + purchasable.getPrice() + " vim");
                    if (activating && PlayerManager.ZombieTCPlayerProperties.get(player).vim >= purchasable.getPrice()) {
                        TileEntity tile = (TileEntity) purchasable;
                        ZombieTC.network.sendToServer(new MessagePurchaseTile(tile.xCoord, tile.yCoord, tile.zCoord, player));
                    }
                }
            }
        }
    }

    private boolean shouldSpawn() {
        if (zombiesToSpawn == 0) {
            return false;
        }
        if (wave < 3 && _r.nextInt(55) != 0) {
            return false;
        }
        if (wave >= 3 && wave < 5 && _r.nextInt(48) != 0) {
            return false;
        }
        if (wave >= 5 && wave < 8 && _r.nextInt(40) != 0) {
            return false;
        }
        if (wave >= 8 && wave < 13 && _r.nextInt(47) != 0) {
            return false;
        }
        if (wave >= 13 && wave < 20 && _r.nextInt(27) != 0) {
            return false;
        }
        return _r.nextInt(20) != 0;
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
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return location;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) throws FileNotFoundException {
        if (event.world.getSaveHandler().getWorldDirectoryName().equals("none")) {
            return;
        }
        if (!getSaveFileExists(event.world)) {
            wave = 0;
            currentWaveMaxZombies = 0;
            nextWaveZombies = 10;
            timeToNextWave = 0;
            zombiesToSpawn = 0;
            zombiesAlive = 0;
            BlockNode.nodes = new ArrayList<MCNode>();
            BlockNode.nodeConnections = new ArrayList<BlockNode.MCNodePair>();
            blockBarricades = new ArrayList<Point3>();
            return;
        }
        Gson gson = new Gson();
        Scanner in = new Scanner(new FileReader(getSaveFile(event.world)));
        String fileData = "";
        while (in.hasNext()) {
            fileData += in.next();
        }

        GameData saveData = gson.fromJson(fileData, GameData.class);
        zombiesAlive = saveData.zombiesAlive;
        currentWaveMaxZombies = saveData.currentWaveMaxZombies;
        timeToNextWave = saveData.timeToNextWave;
        wave = saveData.wave;
        zombiesAlive = saveData.zombiesAlive;
        zombiesToSpawn = saveData.zombiesToSpawn;
        BlockNode.nodes = saveData.nodes;
        BlockNode.nodeConnections = saveData.nodeConnections;
        blockBarricades = saveData.blockBarricades;
        if (blockBarricades == null) {
            blockBarricades = new ArrayList<Point3>();
        }
        for (int i = 0; i < BlockNode.nodes.size(); i++) { //Fixes duplicate node entries
            MCNode node = BlockNode.nodes.get(i);
            for (int j = 0; j < BlockNode.nodeConnections.size(); j++) {
                BlockNode.MCNodePair link = BlockNode.nodeConnections.get(j);
                if (link.n1.position.distanceTo(node.position) < 0.001) {
                    link.n1 = node;
                }
                if (link.n2.position.distanceTo(node.position) < 0.001) {
                    link.n2 = node;
                }
            }
        }
        regeneratePathMap();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onSave(WorldEvent.Save event) throws FileNotFoundException {
        Gson gson = new Gson();
        PrintWriter writer = new PrintWriter(getSaveFile(event.world));
        writer.print(gson.toJson(new GameData(this)));
        writer.flush();
        writer.close();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!MinecraftServer.getServer().isSinglePlayer()) {
            ZombieTC.network.sendTo(new MessagePrepareStaticVariables(), (EntityPlayerMP) event.player);
            ZombieTC.network.sendTo(new MessageSetWave(wave), (EntityPlayerMP) event.player);
            for (int i = 0; i < BlockNode.nodes.size(); i++) {
                ZombieTC.network.sendTo(new MessageAddNode(
                                (int) BlockNode.nodes.get(i).position.xCoord,
                                (int) BlockNode.nodes.get(i).position.yCoord,
                                (int) BlockNode.nodes.get(i).position.zCoord
                        ), (EntityPlayerMP) event.player
                );
            }
            for (int i = 0; i < BlockNode.nodeConnections.size(); i++) {
                ZombieTC.network.sendTo(new MessageAddNodeConnection(
                        Vec3.createVectorHelper(
                                (int) BlockNode.nodeConnections.get(i).n1.position.xCoord,
                                (int) BlockNode.nodeConnections.get(i).n1.position.yCoord,
                                (int) BlockNode.nodeConnections.get(i).n1.position.zCoord),
                        Vec3.createVectorHelper(
                                (int) BlockNode.nodeConnections.get(i).n2.position.xCoord,
                                (int) BlockNode.nodeConnections.get(i).n2.position.yCoord,
                                (int) BlockNode.nodeConnections.get(i).n2.position.zCoord
                        )
                ), (EntityPlayerMP) event.player);
            }
            for (int i = 0; i < blockBarricades.size(); i++) {
                ZombieTC.network.sendTo(new MessageAddBarricade(
                        (int) blockBarricades.get(i).xCoord,
                        (int) blockBarricades.get(i).yCoord,
                        (int) blockBarricades.get(i).zCoord
                ), (EntityPlayerMP) event.player);
            }
            ZombieTC.network.sendTo(new MessageRegeneratePathMap(), (EntityPlayerMP) event.player);
            if (ZombieTC.editorModeManager.enabled) {
                ZombieTC.network.sendTo(new MessageChangeEditorMode(), (EntityPlayerMP) event.player);
            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.entityLiving.getClass() == EntityZZombie.class && !event.entity.worldObj.isRemote) {
            zombiesAlive--;
        }
    }

    public void regeneratePathMap() {
        for (int i = 0; i < BlockNode.nodes.size(); i++) {
            MCNode node = BlockNode.nodes.get(i);
            node.linksTo = new ArrayList<MCNode>();
            for (int j = 0; j < BlockNode.nodeConnections.size(); j++) {
                BlockNode.MCNodePair pair = BlockNode.nodeConnections.get(j);
                if (pair.n1.position.distanceTo(node.position) < 0.01) {
                    node.linksTo.add(pair.n2);
                }
                if (pair.n2.position.distanceTo(node.position) < 0.01) {
                    node.linksTo.add(pair.n1);
                }
            }
        }
    }
    private List<IListenerZTC> listeners = new ArrayList<IListenerZTC>();
    public void invokeEvent(String event) {
        for(int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onEvent(event);
        }
    }

    public void registerListener(IListenerZTC listenerZTC) {
        listeners.add(listenerZTC);
    }
    public void unregisterListener(IListenerZTC listenerZTC) {
        listeners.remove(listenerZTC);
    }

    public boolean isRegisteredListener(TileZTC tileZTC) {
        return listeners.contains(tileZTC);
    }

    private static class GameData {
        public int wave = 0;
        int zombiesToSpawn = 0;
        int zombiesAlive = 0;
        int timeToNextWave = 0;

        int nextWaveZombies = 0;
        int currentWaveMaxZombies = 0;
        List<MCNode> nodes;
        List<BlockNode.MCNodePair> nodeConnections;
        ArrayList<Point3> blockBarricades;
        List<String> scoreboardNames;
        List<Integer> scoreboardScores;

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
            blockBarricades = manager.blockBarricades;
        }
    }
}
