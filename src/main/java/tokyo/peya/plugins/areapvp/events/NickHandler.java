package tokyo.peya.plugins.areapvp.events;

import net.dev.eazynick.api.*;
import org.bukkit.*;
import org.bukkit.event.*;
import tokyo.peya.plugins.areapvp.*;
import tokyo.peya.plugins.areapvp.player.*;

public class NickHandler implements Listener
{
    @EventHandler
    public void onNick(PlayerNickEvent e)
    {
        if (InfoContainer.isNicked(e.getPlayer()))
            return;
        InfoContainer.nick(e.getPlayer());
        AreaPvP.refreshScoreBoard(e.getPlayer());
        e.getPlayer().sendMessage(ChatColor.YELLOW + "あなたはニックネームを使用しているため、表示されるレベルがランダム化されています！");
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onUnnick(PlayerUnnickEvent e)
    {
        InfoContainer.unnick(e.getPlayer());
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        AreaPvP.refreshScoreBoard(e.getPlayer());
    }
}
