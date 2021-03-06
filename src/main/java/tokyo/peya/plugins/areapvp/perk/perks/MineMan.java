package tokyo.peya.plugins.areapvp.perk.perks;

import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import tokyo.peya.plugins.areapvp.*;
import tokyo.peya.plugins.areapvp.perk.*;

import java.util.*;

public class MineMan implements IPerkEntry
{

    public static ItemStack getPickaxe()
    {
        ItemStack stack = new ItemStack(Material.DIAMOND_PICKAXE);
        stack.addEnchantment(Enchantment.DIG_SPEED, 4);
        stack = Items.noDrop(stack);
        stack = Items.setPerk(stack);
        stack = Items.cantEnderChest(stack);
        return Items.setUnbreakable(stack);
    }

    @Override
    public ItemStack getItem()
    {
        return Items.cantEnderChest(Items.setPerk(Items.setUnbreakable(Items.noDrop(new ItemStack(Material.COBBLESTONE)))));
    }

    @Override
    public List<String> getShopLore()
    {
        return Arrays.asList(ChatColor.GRAY + "丸石24個と" + ChatColor.AQUA + "ダイヤモンドのピッケル" + ChatColor.GREEN + "を",
                ChatColor.GRAY + "スポーンします。", "", ChatColor.GRAY + "プレイヤーを倒した時、" + ChatColor.WHITE + "丸石を3個与えます。"
        );
    }

    @Override
    public String getName()
    {
        return "mineMan";
    }

    @Override
    public int getNeedPrestige()
    {
        return 0;
    }

    @Override
    public int getNeedGold()
    {
        return 3000;
    }

    @Override
    public int getNeedLevel()
    {
        return 30;
    }

    @Override
    public void onRemove(Player player)
    {
        player.getInventory().remove(Material.COBBLESTONE);
        player.getInventory().remove(getPickaxe());
    }

    @Override
    public void onBuy(Player player)
    {
        player.getInventory().addItem(Items.cantEnderChest(Items.noDrop(new ItemStack(Material.COBBLESTONE, 24))));

        player.getInventory().addItem(getPickaxe());
    }

    @Override
    public void onWork(Player player)
    {
        player.getInventory().addItem(Items.cantEnderChest(Items.noDrop(new ItemStack(Material.COBBLESTONE, 3))));
    }
}
