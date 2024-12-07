package org.mrdarkimc.chatfilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.SatanicLib.SatanicLib;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicLib.configsetups.Configs;
import org.mrdarkimc.chatfilter.eventhandlers.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChatFilter extends JavaPlugin implements Listener {
    public static Map<String,String> replaceableMap = new HashMap<>();
    public static List<String> commandlist = new ArrayList<>();
    public static Configs config;
    public static Configs log;
    public static String permission = "satanicfilter.ignore";
    public static ChatFilter instance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        SatanicLib.setupLib(this);
        instance = this;
        getServer().getPluginManager().registerEvents(new Handler(),this);
        getServer().getPluginCommand("sfilter").setExecutor(new Command());
        config = Configs.Defaults.setupConfig();
        log = new Configs("log");
        Utils.startUp("SatanicFilter Premium");
        deserealize();

    }
    public static void deserealize(){
        commandlist = config.get().getStringList("triggercommands");
        for (String blacklist : config.get().getStringList("blacklist")) {
            String[] strs = blacklist.split("\\|");
            replaceableMap.put(strs[0],strs[1]);
        }
        Bukkit.getLogger().info(ChatColor.YELLOW + "[SatanicFilter] Успешно зарегистрировано: " + commandlist.size() + " команд.");
        Bukkit.getLogger().info(ChatColor.YELLOW + "[SatanicFilter] Успешно зарегистрировано: " + replaceableMap.size() + " ругательств.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
