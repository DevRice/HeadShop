package me.grumpyrice.listeners;

import me.grumpyrice.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
public class InventoryListener implements Listener {

    main plugin;

    public InventoryListener(main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void onHeadsShopInteract(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        String item = e.getCurrentItem().getItemMeta().getDisplayName();

        if(!inv.getName().equals(plugin.inv.getName())) return;
    }
}
