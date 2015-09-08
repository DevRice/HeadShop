package me.grumpyrice;

import me.grumpyrice.commands.HeadShopCommand;
import me.grumpyrice.listeners.InventoryListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadShopPlugin extends JavaPlugin {

    public Economy econ = null;

    public Map<String, Integer> players = new HashMap<String, Integer>();

    public String invName = "";

    public Integer invSize = null;

    private static final String CONFIG_NAME = "heads.yml";
    YamlConfiguration conf = new YamlConfiguration();

    public Inventory inv = null;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        players = loadHashMap();
        invName = loadName();
        invSize = loadSize();
        getCommand("headshop").setExecutor(new HeadShopCommand(this));
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public String loadName(){
        File datafolder = getDataFolder();

        if (!datafolder.exists()) {
            datafolder.mkdirs();
        }

        File f = new File(datafolder, CONFIG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Unable to create " + CONFIG_NAME);
                return "Head Shop!";
            }
        }


        conf.options().pathSeparator('.');
        conf.options().header(new StringBuilder().append("Heads configuration!").append(System.getProperty("line.separator")).toString());

        try {
            conf.load(f);
        } catch (Exception e) {
            getLogger().severe("==================== " + getDescription().getName() + " ====================");
            getLogger().severe("Unable to load " + CONFIG_NAME);
            getLogger().severe("Check your config for formatting issues!");
            getLogger().severe("Error: " + e.getMessage());
            getLogger().severe("====================================================");
            return "Head Shop!";
        }
        if(!conf.contains("inventory-name") || conf.getString("inventory-name").isEmpty()) {
            conf.set("inventory-name", "Head Shop!");
        }

        String invName = "";
        if(conf.isString("inventory-name"))
            invName = conf.getString("inventory-name");

        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invName;
    }

    public Integer loadSize(){
        File datafolder = getDataFolder();

        if (!datafolder.exists()) {
            datafolder.mkdirs();
        }

        File f = new File(datafolder, CONFIG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Unable to create " + CONFIG_NAME);
                return 3;
            }
        }


        conf.options().pathSeparator('.');
        conf.options().header(new StringBuilder().append("Heads configuration!").append(System.getProperty("line.separator")).toString());

        try {
            conf.load(f);
        } catch (Exception e) {
            getLogger().severe("==================== " + getDescription().getName() + " ====================");
            getLogger().severe("Unable to load " + CONFIG_NAME);
            getLogger().severe("Check your config for formatting issues!");
            getLogger().severe("Error: " + e.getMessage());
            getLogger().severe("====================================================");
            return 3;
        }
        if(!conf.contains("inventory-size") || conf.getInt("inventory-size") > 6 || conf.getInt("inventory-size") < 0) {
            conf.set("inventory-size", 3);
        }

        Integer invSize = null;
         if(conf.isInt("inventory-size"))
             invSize = conf.getInt("inventory-size");

        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invSize;
    }



    public void setupInventory(Inventory inv, CommandSender bal){
        int counter = 0;
        for(String player : players.keySet()){
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner(player);
            meta.setDisplayName(player + "'s skull");
            List<String> lore = new ArrayList<String>();
            String defaultLore = "�9Cost: $" + players.get(player);
            if(players.get(player).equals(0)){
                defaultLore = "�9�oFREE";
            }
            lore.add(defaultLore);
            meta.setLore(lore);
            head.setItemMeta(meta);
            inv.setItem(counter, head);
            counter++;
        }
        ItemStack balance = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = balance.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Current balance:");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.BLUE + "$" + econ.getBalance(((Player) bal)));
        meta.setLore(lore);
        balance.setItemMeta(meta);
        inv.setItem(invSize*9 -1, balance);
    }

    public HashMap<String, Integer> loadHashMap(){
        File datafolder = getDataFolder();

        if (!datafolder.exists()) {
            datafolder.mkdirs();
        }

        File f = new File(datafolder, CONFIG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Unable to create " + CONFIG_NAME);
                return null;
            }
        }


        conf.options().pathSeparator('.');
        conf.options().header(new StringBuilder().append("Heads configuration!").append(System.getProperty("line.separator")).toString());

        try {
            conf.load(f);
        } catch (Exception e) {
            getLogger().severe("==================== " + getDescription().getName() + " ====================");
            getLogger().severe("Unable to load " + CONFIG_NAME);
            getLogger().severe("Check your config for formatting issues!");
            getLogger().severe("Error: " + e.getMessage());
            getLogger().severe("====================================================");
            return null;
        }
        if(!conf.contains("heads-to-purchase") || !conf.isConfigurationSection("heads-to-purchase"))
            conf.createSection("heads-to-purchase");
        ConfigurationSection sec = conf.getConfigurationSection("heads-to-purchase");

        HashMap<String, Integer> heads = new HashMap<String, Integer>();

        for(String key : sec.getKeys(false)){
            if(sec.isInt(key))
                heads.put(key, sec.getInt(key));
        }
        try{
            conf.save(f);
        } catch (IOException e){
            e.printStackTrace();
        }
        return heads;
    }

    public String color(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}