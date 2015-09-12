package me.grumpyrice.commands;

import me.grumpyrice.HeadShopPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class HeadShopCommand implements CommandExecutor {

    HeadShopPlugin plugin;

    public HeadShopCommand(HeadShopPlugin plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length < 1){
            if(!(sender instanceof Player)){
                sender.sendMessage("Unfortunately, you cannot view the head GUI.");
                return true;
            }
            if(plugin.players.isEmpty()){
                sender.sendMessage(ChatColor.RED + "There are no heads for sale!");
                return true;
            }
            plugin.inv = Bukkit.createInventory(null, 9*plugin.invSize, plugin.invName);
            setupInventory(plugin.inv, sender);
            ((Player) sender).openInventory(plugin.inv);
            return true;
        }
        else if(args.length > 2){
            sender.sendMessage("§cToo many arguments!");
            sender.sendMessage("§c/headshop");
            return true;
        }
        else if(args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("headshop.reload")) {
                sender.sendMessage("§cYou cannot reload the configuration for HeadShop.");
                return true;
            } else {
                plugin.players = plugin.loadHashMap();
                plugin.invName = plugin.loadName();
                plugin.invSize = plugin.loadSize();
                plugin.inv = null;
                sender.sendMessage("§aYou have successfully reloaded the configuration for HeadShop!");
                return true;
            }
        }
        else{
            sender.sendMessage("§c/headshop");
        }
        return true;
    }

    private void setupInventory(Inventory inv, CommandSender bal){
        int counter = 0;
        for(String player : plugin.players.keySet()){
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner(player);
            meta.setDisplayName(player + "'s skull");
            List<String> lore = new ArrayList<String>();
            String defaultLore = "§9Cost: $" + plugin.players.get(player);
            if(plugin.players.get(player).equals(0)){
                defaultLore = "§9§oFree!";
            }
            lore.add(defaultLore);
            meta.setLore(lore);
            head.setItemMeta(meta);
            inv.setItem(counter, head);
            counter++;
        }
        ItemStack item = new ItemStack(Material.BOOK, 1);

        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        meta.setDisplayName(ChatColor.GREEN + "Current balance:");
        lore.add(ChatColor.BLUE+"$"+plugin.econ.getBalance(((Player) bal)));
        meta.setLore(lore);
        item.setItemMeta(meta);

        inv.setItem(plugin.invSize*9 -1, item);
    }
}
