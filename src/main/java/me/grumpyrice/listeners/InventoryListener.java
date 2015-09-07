package me.grumpyrice.listeners;

import me.grumpyrice.main;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class InventoryListener implements Listener {

    main plugin;

    public InventoryListener(main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void onHeadsShopInteract(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemMeta meta = e.getCurrentItem().getItemMeta();
        String item = meta.getDisplayName();

        if(!inv.getName().equals("Head Shop!")) {return;}

        if(!plugin.players.isEmpty()){
            for(String key : plugin.players.keySet()){
                if(!item.equals(key+"'s skull")) {return;}
                if(plugin.econ.getBalance(p) < plugin.players.get(key)) {
                    e.setCancelled(true);
                    p.closeInventory();
                    p.sendMessage(ChatColor.RED + "Insufficient funds.");
                    return;
                }
                EconomyResponse r = plugin.econ.withdrawPlayer(p.getName(), plugin.players.get(key));
                if(!r.transactionSuccess()) {
                    e.setCancelled(true);
                    p.closeInventory();
                    p.sendMessage(ChatColor.RED + "Failed transaction.");
                    return;
                }
                e.setCancelled(true);
                p.getInventory().addItem(e.getCurrentItem());
                p.updateInventory();
                p.closeInventory();
                p.sendMessage(ChatColor.GREEN + "You have been given " + key + "'s skull and $" + plugin.players
                .get(key) +" has been removed from your balance.");
                break;
            }
        }
        else{
            p.sendMessage(ChatColor.RED + "There are currently no heads for sale.");
            return;
        }
    }
}
