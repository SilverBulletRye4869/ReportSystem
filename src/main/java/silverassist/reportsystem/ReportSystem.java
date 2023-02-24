package silverassist.reportsystem;

import net.dv8tion.jda.api.JDA;
import org.bukkit.plugin.java.JavaPlugin;
import silverassist.reportsystem.reportSystem.MainSystem;

import java.util.logging.Logger;

public final class ReportSystem extends JavaPlugin {
    private static JDA jda = null;
    private static Discord discord = null;
    private static ReportSystem plugin;
    private static MainSystem MAIN_SYSTEM;
    private static Logger log;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();

        log = getLogger();
        discord = new Discord(this);
        jda = discord.startBot();
        plugin = this;
        MAIN_SYSTEM = new MainSystem(this,discord);
        new Command("report", MAIN_SYSTEM);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(jda!=null)jda.shutdownNow();
    }

    public static JavaPlugin getInstance(){return plugin;}
    public static Discord getDiscord(){return discord;}
    public static JDA getJDA(){return jda;}
    public static MainSystem getMainSystem(){return MAIN_SYSTEM;}
    public static Logger getLog(){return log;}




}
