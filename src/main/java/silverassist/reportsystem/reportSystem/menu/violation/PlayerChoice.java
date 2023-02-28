package silverassist.reportsystem.reportSystem.menu.violation;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.Util;
import silverassist.reportsystem.reportSystem.MainSystem;

import java.util.stream.Collectors;

public class PlayerChoice {
    private final JavaPlugin plugin;
    private final MainSystem MAIN_SYSTEM;
    private final Player P;
    private final Player[] PLAYERS;

    private int page;

    public PlayerChoice(JavaPlugin plugin,MainSystem mainSystem, Player p){
        this.plugin = plugin;
        this.MAIN_SYSTEM = mainSystem;
        this.P = p;
        p.closeInventory();
        plugin.getServer().getPluginManager().registerEvents(new listener(),plugin);
        PLAYERS = Bukkit.getOnlinePlayers().stream()/*.filter(g->!g.equals(P))*/.collect(Collectors.toList()).toArray(new Player[Bukkit.getOnlinePlayers().size()]);
    }

    public void open(){open(0);}
    public void open(int page){
        this.page = page;
        Inventory inv = Bukkit.createInventory(P,54, Util.PREFIX+"§d§lプレイヤーを選択してください。");
        for(int i = 45*page;i<Math.min(45*(page+1),PLAYERS.length);i++)inv.setItem(i%45,Util.getHead(PLAYERS[i]));
        for(int i = 45;i<54;i++)inv.setItem(i,Util.GUI_BG);
        if(page>0)inv.setItem(45,Util.createItem(Material.RED_STAINED_GLASS_PANE,"§c§l前へ"));
        if(page < (PLAYERS.length-1)/45)inv.setItem(53,Util.createItem(Material.LIME_STAINED_GLASS_PANE,"§a§l次へ"));
        unregisterCancel = false;
        Util.delayInvOpen(P,inv);

    }

    private boolean unregisterCancel = false;
    private class listener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent e){
            if(!e.getWhoClicked().equals(P) || e.getCurrentItem()==null || !e.getClickedInventory().getType().equals(InventoryType.CHEST))return;
            int slot = e.getSlot();
            ItemStack item = e.getInventory().getItem(slot);
            switch (item.getType()){
                case PLAYER_HEAD:
                    Player target = ((SkullMeta)item.getItemMeta()).getOwningPlayer().getPlayer();
                    new ReasonChoice(plugin,MAIN_SYSTEM,P,target).open();
                    break;
                case RED_STAINED_GLASS_PANE:
                    unregisterCancel=true;
                    open(--page);
                    break;
                case LIME_STAINED_GLASS_PANE:
                    unregisterCancel = true;
                    open(++page);
                    break;
            }

        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent e){
            if(!e.getPlayer().equals(P) || unregisterCancel)return;
            HandlerList.unregisterAll(this);
        }
    }
}
