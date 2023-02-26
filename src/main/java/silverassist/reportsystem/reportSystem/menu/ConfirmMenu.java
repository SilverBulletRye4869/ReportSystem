package silverassist.reportsystem.reportSystem.menu;

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
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.Util;
import silverassist.reportsystem.reportSystem.MainSystem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmMenu {
    private static final ItemStack CANCEL_BG = Util.createItem(Material.RED_STAINED_GLASS_PANE,"§c§lキャンセル");
     private static final ItemStack CONFIRM_BG = Util.createItem(Material.LIME_STAINED_GLASS_PANE, "§a§l送信");

    private final JavaPlugin plugin;
    private final MainSystem MAIN_SYSTEM;
    private final Player P;
    private final String[] REPORT_MESSAGES;


    public ConfirmMenu(JavaPlugin plugin, MainSystem mainSystem,Player p, String[] messages){
        this.plugin = plugin;
        this.MAIN_SYSTEM = mainSystem;
        this.P =p;
        REPORT_MESSAGES = messages;
        plugin.getServer().getPluginManager().registerEvents(new listener(),plugin);
    }

    public void open(){
        Inventory inv = Bukkit.createInventory(P,27, Util.PREFIX+"§d§lレポート最終確認");
        Util.invFill(inv);
        for(int i = 10;i<12;i++)inv.setItem(i,CANCEL_BG);
        for(int i = 15;i<17;i++)inv.setItem(i,CANCEL_BG);
        List<String> mainTxt = Arrays.asList(Arrays.copyOfRange(REPORT_MESSAGES,2,REPORT_MESSAGES.length)).stream()
                .map(e -> "§r§f"+e)
                .collect(Collectors.toList());
        inv.setItem(13,Util.createItem(Material.PAPER,"§e§l件名: §f§l"+REPORT_MESSAGES[1],mainTxt));

        P.openInventory(inv);
    }




    private class listener implements Listener {
        @EventHandler
        public void onInvenotryClick(InventoryClickEvent e){
            if(!e.getWhoClicked().equals(P))return;
            if(e.getCurrentItem()==null || !e.getClickedInventory().getType().equals(InventoryType.CHEST))return;
            e.setCancelled(true);
            int slot = e.getSlot();

            if(slot>9 && slot<12)P.closeInventory();
            else if(slot>14&&slot<17){
                boolean result = MAIN_SYSTEM.report(P,REPORT_MESSAGES);
                if(result)Util.sendPrefixMessage(P,"§a§lレポートの送信に成功しました。");
                else{
                    Util.sendPrefixMessage(P,"§c§lレポートの送信に失敗しました。");
                    Util.sendPrefixMessage(P,"§d§l繰り返し失敗する場合はお近くの運営にお尋ねください。");
                }
                P.closeInventory();
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent e){
            if(!e.getPlayer().equals(P))return;
            HandlerList.unregisterAll(this);
        }
    }
}
