package tokyo.peya.plugins.areapvp.command;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;
import org.bukkit.potion.*;
import tokyo.peya.plugins.areapvp.*;
import tokyo.peya.plugins.areapvp.api.events.*;
import tokyo.peya.plugins.areapvp.events.*;
import tokyo.peya.plugins.areapvp.play.*;

public class Spawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "エラー！このコマンドはプレイヤーからのみ実行できます。");
            return true;
        }

        Player player = (Player) sender;
        if (player.hasMetadata("x-hitted"))
        {
            sender.sendMessage(ChatColor.RED + "エラー！戦闘中は/respawnできません！");
            return true;
        }

        if (player.hasMetadata("x-spawn"))
        {
            sender.sendMessage(ChatColor.RED + "エラー！/respawnは10秒に1回可能です！");
            return true;
        }
        if (player.getLocation().getY() >= AreaPvP.spawnloc)
        {
            player.sendMessage(ChatColor.RED + "あなたは現在スポーンポイント範囲内にいます！");
            return true;
        }

        PlayerSpawnEvent event = EventDispatcher.dispatch(new PlayerSpawnEvent());
        if (event.isCancelled())
        {
            if (event.getCancelMessage() != null)
                sender.sendMessage(event.getCancelMessage());
            else
                sender.sendMessage(ChatColor.RED + "この操作は取り消されました。");

            return true;
        }
        player.teleport(player.getWorld().getSpawnLocation());
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.ABSORPTION);
        player.removePotionEffect(PotionEffectType.SPEED);
        InventoryUtils.onRespawn(player);
        player.setMetadata("x-spawn", new FixedMetadataValue(AreaPvP.getPlugin(), 10));

        return true;
    }
}
