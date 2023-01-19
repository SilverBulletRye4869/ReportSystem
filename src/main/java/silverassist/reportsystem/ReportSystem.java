package silverassist.reportsystem;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.command.Report;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;

public final class ReportSystem extends JavaPlugin {
    private static JDA jda = null;
    private static Discord discord = null;
    private static ReportSystem plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();

        discord = new Discord(this);
        jda = discord.startBot();
        plugin = this;

        new Report("report", discord);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(jda!=null)jda.shutdownNow();
    }

    public static JavaPlugin getInstance(){return plugin;}
    public static JDA getJDA(){return jda;}




}
