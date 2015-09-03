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

        if(!inv.getName().equals("Heads Shop!")) {return;}

        if(!plugin.players.isEmpty()){
            for(String key : plugin.players.keySet()){
                EconomyResponse r = plugin.econ.withdrawPlayer(p.getName(), plugin.players.get(p.getName()));
                if(!item.equals(key+"'s Head")) {return;}
                if(!r.transactionSuccess() || plugin.econ.getBalance(p.getName()) < plugin.players.get(p.getName())) {
                    e.setCancelled(true);
                    p.closeInventory();
                    p.sendMessage(ChatColor.RED + "You cannot purchase that head.");
                    return;
                }
                e.setCancelled(true);
                p.getInventory().addItem(e.getCurrentItem());
                p.updateInventory();
                p.closeInventory();
                break;
            }
        }
    }
}
