package silverassist.reportsystem;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Discord {
    private final JavaPlugin plugin;
    private final HashMap<String,TextChannel> channelData = new HashMap<>();
    private JDA jda;

    public Discord(JavaPlugin plugin){
        this.plugin = plugin;
    }

    JDA startBot(){
        FileConfiguration config = plugin.getConfig();
        String token = config.getString("bot_token");
        if(token == null){
            System.err.println("[ReportPlugin] Tokenを記入してください");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return null;
        }
        try {
            jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES)
                    .setRawEventsEnabled(true)
                    .setActivity(Activity.playing("CONAN!"))
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return null;
        }

        return jda;
    }

    public TextChannel getChanelByName(String id){
        if(!channelData.containsKey(id)){
            if(!reloadChannel(id))return null;
        }
        return channelData.get(id);

    }
    private boolean reloadChannel(String type){
        FileConfiguration config = plugin.getConfig();
        String id = config.getString("channel_"+type);
        if(id==null)return false;
        channelData.put(type,jda.getTextChannelById(id));
        return true;
    }
}


