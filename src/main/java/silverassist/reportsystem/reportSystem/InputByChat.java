package silverassist.reportsystem.reportSystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.Util;
import silverassist.reportsystem.reportSystem.menu.ConfirmMenu;
import silverassist.reportsystem.reportSystem.menu.TypeChoice;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class InputByChat implements Listener {
    private static final Set<Player> REPORTING = new HashSet<>();
    public static final Predicate<Player> isReporting = p -> REPORTING.contains(p);

    private final JavaPlugin plugin;
    private final MainSystem MAIN_SYSTEM;
    private final Player P;
    private final String REPORT_TYPE;

    public InputByChat(JavaPlugin plugin,MainSystem mainSystem, Player p,String type){
        this.plugin = plugin;
        this.MAIN_SYSTEM = mainSystem;
        this.P = p;
        this.REPORT_TYPE = type;
        Util.sendPrefixMessage(p,"§a報告内容を以下の形式でチャットで入力してください(\"cancel\"でキャンセル)");
        Util.sendPrefixMessage(p,"§e<件名> <本文(空白で改行)>");
        Util.sendPrefixMessage(p,"§d・5分間応答がない場合は自動キャンセルされます。");
        Util.sendPrefixMessage(p,"§d・このチャットは全体に送信されることはありません");
        p.closeInventory();
        REPORTING.add(P);
        plugin.getServer().getPluginManager().registerEvents(this,plugin);

        Bukkit.getScheduler().runTaskLater(plugin,()->{
            Util.sendPrefixMessage(P,"§c§l一定時間レポートの送信が無かったためキャンセルされました。");
            this.close();
        },20*300);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){
        if(!P.equals(e.getPlayer()))return;
        e.setCancelled(true);
        if(e.getMessage().equals("cancel")){
            this.close();
            Util.sendPrefixMessage(P,"§c§lキャンセルしました");
            return;
        }
        String[] msgs = e.getMessage().split(" ");
        if(msgs.length < 2 || msgs[0].length() == 0){
            Util.sendPrefixMessage(P,"§c§l件名と本文を正しく入力してください");
            return;
        }
        new ConfirmMenu(plugin,MAIN_SYSTEM,P,msgs).open();
        this.close();
        return;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(!P.equals(e.getPlayer()))return;
        this.close();
    }

    private void close(){
        HandlerList.unregisterAll(this);
        REPORTING.remove(P);
    }

}
