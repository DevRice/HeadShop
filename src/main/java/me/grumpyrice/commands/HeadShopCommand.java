package me.grumpyrice.commands;

import me.grumpyrice.HeadShopPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadShopCommand implements CommandExecutor {

    HeadShopPlugin plugin;

    public HeadShopCommand(HeadShopPlugin plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length < 1){
            if(plugin.players.isEmpty()){
                sender.sendMessage(ChatColor.RED + "There are no heads for sale!");
                return true;
            }
            plugin.inv = Bukkit.createInventory(null, 9*plugin.invSize, plugin.color(plugin.invName));
            plugin.setupInventory(plugin.inv, sender);
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
                sender.sendMessage("§aYou have successfully reloaded the configuration for HeadShop!");
                return true;
            }
        }
        else{
            sender.sendMessage("§c/headshop");
        }
        return true;
    }
}
