package me.grumpyrice;

import me.grumpyrice.commands.HeadShopCommand;
import me.grumpyrice.listeners.InventoryListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeadShopPlugin extends JavaPlugin {

    public Economy econ = null;

    public Map<String, Integer> players = new HashMap<String, Integer>();

    public String invName = "";

    public Integer invSize = null;

    public Inventory inv = null;

    private static final String CONFIG_NAME = "heads.yml";
    YamlConfiguration conf = new YamlConfiguration();

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
        inv = Bukkit.createInventory(null, 9 * invSize, invName);
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

        File f = new File(datafolder, CONFIG_NAME);

        if(!loadSuccess(datafolder, f)){
            return null;
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

        File f = new File(datafolder, CONFIG_NAME);

        if(!loadSuccess(datafolder, f)){
            return null;
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

    public HashMap<String, Integer> loadHashMap(){
        File datafolder = getDataFolder();

        File f = new File(datafolder, CONFIG_NAME);

        if(!loadSuccess(datafolder, f)){
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

    private boolean loadSuccess(File datafolder, File f){
        if (!datafolder.exists()) {
            datafolder.mkdirs();
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Unable to create " + CONFIG_NAME);
                return false;
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
            return false;
        }
        return true;
    }

}
