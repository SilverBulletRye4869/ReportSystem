package silverassist.reportsystem.reportSystem.menu.violation;

import com.sun.tools.javac.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.Util;
import silverassist.reportsystem.reportSystem.InputByChat;
import silverassist.reportsystem.reportSystem.MainSystem;

import java.util.List;

public class ReasonChoice {
    private final JavaPlugin plugin;
    private final MainSystem MAIN_SYSTEM;
    private final Player P;
    private final Player TARGET;

    public ReasonChoice(JavaPlugin plugin, MainSystem mainSystem, Player p, Player target){
        this.plugin = plugin;
        this.MAIN_SYSTEM = mainSystem;
        this.P = p;
        this.TARGET = target;
        plugin.getServer().getPluginManager().registerEvents(new listener(),plugin);
    }

    public void open(){
        Inventory inv = Bukkit.createInventory(P,9,"§d§l理由選択");
        inv.setItem(0,Util.createItem(Material.DIAMOND_BOOTS,"§c§lスピードハック"));
        inv.setItem(1,Util.createItem(Material.FEATHER,"§c§l不当な空中浮遊"));
        inv.setItem(2,Util.createItem(Material.NETHERITE_SWORD,"§c§l戦闘チート", List.of("§fオートエイム等")));
        inv.setItem(3,Util.createItem(Material.TNT,"§c§l荒らし"));
        inv.setItem(4,Util.createItem(Material.PLAYER_HEAD,"§c§l不適切なスキン"));
        inv.setItem(5,Util.createItem(Material.BOOK,"§c§l暴言･スパム等",List.of("§f煽り等の不適切な発言を含む")));
        inv.setItem(6,Util.createItem(Material.COMPARATOR,"§c§lマクロ･自動化"));
        inv.setItem(7,Util.createItem(Material.GUNPOWDER,"§c§lバグの悪用"));
        inv.setItem(8, Util.createItem(Material.NAME_TAG,"§6§lカスタム理由",List.of("§e詳細理由の入力が可能です")));
        P.openInventory(inv);
    }

    private class listener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent e){
            if(!e.getWhoClicked().equals(P) || e.getCurrentItem() == null || !e.getClickedInventory().getType().equals(InventoryType.CHEST))return;
            int slot = e.getSlot();
            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            if(slot==8)new InputByChat(plugin,MAIN_SYSTEM,P,"violation");
            else MAIN_SYSTEM.report(P,new String[]{"violation",name.substring(3),"違反者: "+TARGET.getName(),"違反者座標: "+TARGET.getLocation()});
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent e){
            if(!e.getPlayer().equals(P))return;
            HandlerList.unregisterAll(this);
        }
    }
}