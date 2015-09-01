package me.grumpyrice;

import me.grumpyrice.commands.HeadsShopCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class main extends JavaPlugin {

    public Map<String, Integer> players = new HashMap<String, Integer>();
    public Integer invSize = null;

    private static final String CONFIG_NAME = "heads.yml";
    YamlConfiguration conf = new YamlConfiguration();

    public Inventory inv = null;

    @Override
    public void onEnable() {
        players = loadHashMap();
        invSize = loadSize();
        getCommand("headsshop").setExecutor(new HeadsShopCommand(this));
        inv = Bukkit.createInventory(null, 9 * invSize, "Heads Shop!");
        setupInventory(inv);
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

    public void setupInventory(Inventory inv){
        for(String player : players.keySet()){
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner(player);
            head.setItemMeta(meta);
            inv.addItem(head);
        }
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

    public void save(Map<String,Integer> players, Integer invSize){

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
                return;
            }
        }

        YamlConfiguration conf = new YamlConfiguration();
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
            return;
        }

        conf.createSection("heads-to-purchase", players);
        conf.set("inventory-size", invSize);

        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
