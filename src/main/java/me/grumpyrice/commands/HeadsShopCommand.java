package me.grumpyrice.commands;

import me.grumpyrice.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadsShopCommand implements CommandExecutor {

    main plugin;

    public HeadsShopCommand(main plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length < 1){
            ((Player) sender).openInventory(plugin.inv);
            return true;
        }
        else if(args.length > 2){
            sender.sendMessage("§cToo many arguments!");
            sender.sendMessage("§c/headsshop");
            return true;
        }
        else if(args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("headsshop.reload")) {
                sender.sendMessage("§cYou cannot reload the configuration for HeadsShop.");
                return true;
            } else {
                plugin.players = plugin.loadHashMap();
                plugin.invSize = plugin.loadSize();
                plugin.inv = Bukkit.createInventory(null, 9 * plugin.invSize, "Heads Shop!");
                plugin.setupInventory(plugin.inv);
                plugin.save(plugin.players, plugin.invSize);
                sender.sendMessage("§aYou have successfully reloaded the configuration for HeadsShop.");
                return true;
            }
        }
        else{
            sender.sendMessage("§c/headsshop");
        }
        return true;
    }
}
