package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.TextAlignment;
import net.gigimoi.zombietc.helpers.TextRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class ScoreboardManager {
    public List<String> scoreboardNames = new ArrayList<String>();
    public List<Integer> scoreboardScores = new ArrayList<Integer>();
    @SubscribeEvent
    public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        for(int i = 0; i < scoreboardNames.size(); i++) {
            if(scoreboardNames.get(i).equals(event.player.getCommandSenderName())) {
                scoreboardNames.remove(i);
                scoreboardScores.remove(i);
            }
        }
    }
    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        for(int i = 0; i < scoreboardNames.size(); i++) {
            if(scoreboardNames.get(i).equals(event.player.getCommandSenderName())) {
                scoreboardNames.remove(i);
                scoreboardScores.remove(i);
            }
        }
        scoreboardNames.add(event.player.getCommandSenderName());
        scoreboardScores.add(100);
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
        if(ZombieTC.editorModeManager.enabled) {
            return;
        }
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            int yOff = 2;
            TextRenderHelper.drawString("Scoreboard", 2, yOff, TextAlignment.Left);
            yOff += 6;
            TextRenderHelper.drawString("----------", 2, yOff, TextAlignment.Left);
            for(int i = 0; i < scoreboardNames.size(); i++) {
                yOff += 10;
                TextRenderHelper.drawString(scoreboardNames.get(i) + ": " + scoreboardScores.get(i), 2, yOff, TextAlignment.Left);
            }
        }
    }
}
