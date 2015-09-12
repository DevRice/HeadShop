package me.grumpyrice.listeners;

import me.grumpyrice.HeadShopPlugin;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("deprecation")
public class InventoryListener implements Listener {

    HeadShopPlugin plugin;

    public InventoryListener(HeadShopPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void onHeadsShopInteract(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemStack head = e.getCurrentItem();

        if(ChatColor.stripColor(inv.getName()) != plugin.invName){
            return;
        }
        e.setCancelled(true);

        if(head == null || head.getType() == Material.AIR || !head.hasItemMeta()){
            return;
        }
        ItemMeta meta = head.getItemMeta();
        String item = meta.getDisplayName();
        String key = item.replace("'s skull", "");

        if(!(head.getType() == Material.SKULL_ITEM)){
            return;
        }
        p.closeInventory();

        if(!plugin.players.containsKey(key)){
            p.sendMessage(ChatColor.RED + "Something went wrong!");
            return;
        }
        if(plugin.econ.getBalance(p) < plugin.players.get(key)) {
            p.sendMessage(ChatColor.RED + "Insufficient funds.");
            return;
        }
        EconomyResponse r = plugin.econ.withdrawPlayer(p.getName(), plugin.players.get(key));
        if(!r.transactionSuccess()) {
            p.sendMessage(ChatColor.RED + "Failed transaction.");
            return;
        }
        giveHead(p, key);
        p.sendMessage(ChatColor.GREEN + "You have been given " + key + "'s skull and $" + plugin.players
                .get(key) + " has been removed from your balance.");
        return;

    }

    private void giveHead(Player p, String name){
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(name);
        meta.setDisplayName(name + "'s skull");
        meta.setLore(null);
        head.setItemMeta(meta);
        p.getInventory().addItem(head);
        p.updateInventory();
    }
}
