package silverassist.reportsystem.reportSystem.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.Util;
import silverassist.reportsystem.reportSystem.InputByChat;
import silverassist.reportsystem.reportSystem.MainSystem;

import java.util.HashSet;
import java.util.Set;

public class TypeChoice {
    private final JavaPlugin plugin;
    private final MainSystem MAIN_SYSTEM;
    private final Set<Player> openingThis = new HashSet<>();

    public TypeChoice(JavaPlugin plugin, MainSystem mainSystem){
        this.plugin = plugin;
        this.MAIN_SYSTEM = mainSystem;
        plugin.getServer().getPluginManager().registerEvents(new listener(),plugin);
    }

    public void open(Player p){
        Inventory inv = Bukkit.createInventory(p,9, Util.PREFIX+"§d§lレポートタイプ選択");
        Util.invFill(inv);
        inv.setItem(1,Util.createItem(Material.RED_STAINED_GLASS_PANE,"§c§lバグ報告"));
        inv.setItem(1,Util.createItem(Material.YELLOW_STAINED_GLASS_PANE,"§c§l違反報告"));
        inv.setItem(1,Util.createItem(Material.LIME_STAINED_GLASS_PANE,"§c§lその他"));
        openingThis.add(p);
        Util.delayInvOpen(p,inv);
    }

    private class listener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent e){
            Player p = (Player) e.getWhoClicked();
            if(!openingThis.contains(p) || e.getCurrentItem() ==null || !e.getInventory().getType().equals(InventoryType.CHEST))return;
            switch (e.getSlot()){
                case 1:
                    new InputByChat(plugin,MAIN_SYSTEM,p,"bug");
                    break;
                case 5:

                    break;

                case 9:
                    new InputByChat(plugin,MAIN_SYSTEM,p,"else");
                    break;
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent e){
            Player p = (Player) e.getPlayer();
            if(!openingThis.contains(p))return;
            openingThis.remove(p);

        }
    }
}
