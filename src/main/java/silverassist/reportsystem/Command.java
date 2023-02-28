package silverassist.reportsystem;

import net.dv8tion.jda.api.JDA;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import silverassist.reportsystem.reportSystem.InputByChat;
import silverassist.reportsystem.reportSystem.MainSystem;


import java.util.List;


public class Command implements CommandExecutor {
    private final JavaPlugin plugin;
    private final JDA jda;
    private final MainSystem MAIN_SYSTEM;

    public Command(String label, MainSystem mainSystem){
        this.plugin = ReportSystem.getInstance();
        this.jda = ReportSystem.getJDA();
        this.MAIN_SYSTEM = mainSystem;

        PluginCommand command =  plugin.getCommand(label);
        command.setExecutor(this);
        command.setTabCompleter(new Tab());

    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("プレイヤーのみ実行可能です");
            return false;
        }

        Player p = (Player)sender;

        if(InputByChat.isReporting.test(p))return true;
        if(args.length <3){
            MAIN_SYSTEM.getHomeMenu().open(p);
            return true;
        };

        boolean result = MAIN_SYSTEM.report(p,args);
        if(!result)Util.sendPrefixMessage(p,"§cレポートの送信に失敗しました。レポートタイプ等があっているかを再度確認し実行してください");

        return true;
    }

    private class Tab implements TabCompleter{

        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            switch (args.length) {
                case 1:
                    return List.of("bug",  "violation", "else");
                case 2:
                    return List.of("<件名>");
                case 3:
                    return List.of("<本文(空白で改行)>");
            }
            return null;
        }
    }
}
