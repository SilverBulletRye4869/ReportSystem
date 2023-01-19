package silverassist.reportsystem.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import silverassist.reportsystem.Discord;
import silverassist.reportsystem.ReportSystem;

import java.awt.*;
import java.util.List;
import java.util.StringJoiner;

import static silverassist.reportsystem.Util.*;


public class Report implements CommandExecutor {
    private final JavaPlugin plugin;
    private final JDA jda;
    private final Discord discord;

    public Report(String label, Discord discord){
        this.plugin = ReportSystem.getInstance();
        this.jda = ReportSystem.getJDA();
        this.discord= discord;

        PluginCommand command =  plugin.getCommand(label);
        command.setExecutor(this);
        command.setTabCompleter(new Tab());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length <3)return true;
        if(!(sender instanceof Player)){
            sender.sendMessage("プレイヤーのみ実行可能です");
            return false;
        }

        Player p = (Player)sender;
        TextChannel channel;
        Color color;
        switch (args[0]){
            case "バグ":
            case "bug":
                channel = discord.getChanelByName("bug");
                color = Color.RED;
                break;
            case "違反行為":
            case "violation":
                channel = discord.getChanelByName("violation");
                color = Color.YELLOW;
                break;
            case "その他":
            case "else":
                channel = discord.getChanelByName("else");
                color = Color.GREEN;
                break;
            default:
                sendPrefixMessage(p,"§c§lレポートの種類が存在しません");
                return true;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail("https://minotar.net/avatar/"+p.getUniqueId());
        eb.setColor(color);
        StringJoiner sj = new StringJoiner("\n");
        for(int i = 2;i<args.length;i++)sj.add(args[i]);
        eb.addField(args[1],sj.toString(),false);
        eb.setFooter("sended by "+p.getName()+" ("+p.getUniqueId()+")");
        channel.sendMessage(eb.build()).queue();

        return true;
    }

    private class Tab implements TabCompleter{

        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            switch (args.length) {
                case 1:
                    return List.of("バグ", "bug", "違反行為", "violation", "その他", "else");
                case 2:
                    return List.of("<件名>");
                case 3:
                    return List.of("<本文(空白で改行)>");
            }
            return null;
        }
    }
}
