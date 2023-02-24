package silverassist.reportsystem.reportSystem;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.Discord;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class MainSystem {

    private static final Set<String> REPORT_TYPE = Set.of("bug","violation","else");

    private final JavaPlugin plugin;
    private final Discord DISCORD;
    private final Map<String, Color> color = Map.of("bug",Color.RED,"violation",Color.YELLOW,"else",Color.GREEN);

    private final InputByChat CHAT_INPUT_SYSTEM;


    public MainSystem(JavaPlugin plugin, Discord discord){
        this.plugin = plugin;
        this.DISCORD =discord;
        this.CHAT_INPUT_SYSTEM = new InputByChat(plugin,this);
    }

    public boolean report(Player p,String type,String[] args){
        if(!REPORT_TYPE.contains(type))return false;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail("https://minotar.net/avatar/"+p.getUniqueId());
        eb.setColor(color.get(type));
        StringJoiner sj = new StringJoiner("\n");
        for(int i = 2;i<args.length;i++)sj.add(args[i]);
        eb.addField(args[1],sj.toString(),false);
        eb.setFooter("sended by "+p.getName()+" ("+p.getUniqueId()+")");
        DISCORD.getChanelByName(type).sendMessage(eb.build()).queue();
        return true;
    }


    public InputByChat getChatInputSystem(){return CHAT_INPUT_SYSTEM;}
}
