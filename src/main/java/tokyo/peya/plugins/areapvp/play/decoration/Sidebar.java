package tokyo.peya.plugins.areapvp.play.decoration;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;
import tokyo.peya.plugins.areapvp.*;
import tokyo.peya.plugins.areapvp.play.*;
import tokyo.peya.plugins.areapvp.player.*;

import java.math.*;
import java.text.*;
import java.util.*;

public class Sidebar
{
    public static PacketPlayOutScoreboardScore getScore(Scoreboard board, ScoreboardObjective obj, String str, int c)
    {
        ScoreboardScore scoreboardScore = new ScoreboardScore(board, obj, str);
        scoreboardScore.setScore(c);
        return new PacketPlayOutScoreboardScore(scoreboardScore);
    }

    public static void send(Player player, Packet<?> p)
    {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
    }

    public static void sendBoard(Player player)
    {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

        if (!InfoContainer.isInitialize(player))
            return;
        PlayerInfo info = InfoContainer.getInfo(player);

        final Scoreboard b = new Scoreboard();

        final ScoreboardObjective o = b.registerObjective("peyang", IScoreboardCriteria.b);
        o.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "THE HYPIXEL PIT");

        send(player, new PacketPlayOutScoreboardObjective(o, 1));
        send(player, new PacketPlayOutScoreboardObjective(o, 0));

        send(player, new PacketPlayOutScoreboardDisplayObjective(1, o));

        send(player, getScore(b, o, ChatColor.GRAY + format.format(new Date()), info.prestige == 0 ? 10: 11));
        send(player, getScore(b, o, ChatColor.WHITE.toString(), info.prestige == 0 ? 9: 10));
        if (info.prestige != 0)
            send(player, getScore(b, o, ChatColor.WHITE + "Prestige: " + ChatColor.YELLOW +
                    PlayerInfo.arabicToRoman(info.prestige), 9));
        send(player, getScore(b, o, ChatColor.WHITE + "Level: " + PlayerInfo.getPrefix(info.level, info.prestige), 8));

        long exp = Exp.getExp(info.level + (info.level == 119 ? 0: 1), info.prestige) - info.exp;

        send(player, getScore(b, o, ChatColor.WHITE + (info.level != 120 ? "Needed": "") + " XP: " +
                ChatColor.AQUA + (info.level != 120 ? String.format("%,d", exp): "MAXED!"), 7));

        send(player, getScore(b, o, ChatColor.ITALIC.toString(), 6));

        BigDecimal decimal = BigDecimal.valueOf(AreaPvP.economy.getBalance(player));

        String decimalOf;

        if (decimal.compareTo(new BigDecimal(1000)) <= 0)
            decimalOf = String.valueOf(decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        else
            decimalOf = String.format("%,d", decimal.setScale(1, BigDecimal.ROUND_HALF_UP).longValue());

        send(player, getScore(b, o, ChatColor.WHITE + "Gold: " + ChatColor.GOLD + decimalOf + "g", 5));

        send(player, getScore(b, o, ChatColor.YELLOW.toString(), 4));
        if (!player.hasMetadata("x-hitted"))
            send(player, getScore(b, o, ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Idling", 3));
        else
        {
            Integer hitted = null;
            for (MetadataValue hitter : player.getMetadata("x-hitted"))
                if (hitter.getOwningPlugin().getName().equals(AreaPvP.getPlugin().getName()))
                    hitted = hitter.asInt();
            if (hitted == null)
                send(player, getScore(b, o, ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Idling", 3));
            else
            {
                if (hitted > 5)
                    send(player, getScore(b, o, ChatColor.WHITE + "Status: " + ChatColor.RED + "Fighting", 3));
                else
                    send(player, getScore(b, o, ChatColor.WHITE + "Status: " + ChatColor.RED + "Fighting" + ChatColor.GRAY +
                            "(" + hitted + ChatColor.GRAY + ")", 3));
            }
        }

        long streak = Kill.getStreak(player.getUniqueId());

        if (streak == 0)
            send(player, getScore(b, o, ChatColor.BLUE.toString(), 2));
        else
            send(player, getScore(b, o, "Streak: " + ChatColor.GREEN + streak, 2));
        send(player, getScore(b, o, ChatColor.BLACK.toString(), 1));

        player.setLevel(info.level);

        BigDecimal r = new BigDecimal(Exp.getExp(info.level, info.prestige) == 0 ? info.exp: Exp.getExp(info.level, info.prestige));

        BigDecimal rb = new BigDecimal(info.exp == 0 ? 1: info.exp);
        if (r.compareTo(new BigDecimal(0)) != 0)
            player.setExp(Math.min(rb.divide(r, BigDecimal.ROUND_DOWN).floatValue(), 1.0f));
        else
            player.setExp(rb.floatValue());

        info = InfoContainer.getInfoAllowNick(player);

        player.setPlayerListName(PlayerInfo.getPrefix(info.level, info.prestige) + ChatColor.GRAY + " " + player.getName());
        player.setDisplayName(PlayerInfo.getPrefix(info.level, info.prestige) + ChatColor.GRAY + " " + player.getName());

    }


}
