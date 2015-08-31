package me.grumpyrice;

import com.google.common.collect.Multiset;
import me.grumpyrice.commands.HeadsShopCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class main extends JavaPlugin {

    private Map<String, Double> players = new HashMap<String, Double>();
    private Integer invSize;

    public Inventory inv = Bukkit.createInventory(null, 9 * invSize, "Heads Shop");

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        players = loadHashMap();
        loadValues();
        getCommand("headsshop").setExecutor(new HeadsShopCommand(this));
    }

    public void loadValues(){
        invSize = this.getConfig().getInt("inventory-size");
        setupInventory(inv);
    }

    private void setupInventory(Inventory inv){
        for(String player : players.keySet()){
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner(player);
            head.setItemMeta(meta);
            inv.addItem(head);
        }
    }

    private HashMap<String, Double> loadHashMap(){
        if(!this.getConfig().contains("heads-to-purchase") || !this.getConfig().isConfigurationSection("heads-to-purchase"))
            this.getConfig().createSection("heads-to-purchase");
        ConfigurationSection sec = this.getConfig().getConfigurationSection("heads-to-purchase");

        HashMap<String, Double> heads = new HashMap<String, Double>();

        for(String key : sec.getKeys(false)){
            if(sec.isDouble(key))
                heads.put(key, sec.getDouble(key));
        }
        this.saveConfig();
        return heads;
    }
}
