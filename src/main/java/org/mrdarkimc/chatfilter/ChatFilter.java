package org.mrdarkimc.chatfilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.SatanicLib.SatanicLib;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicLib.configsetups.Configs;
import org.mrdarkimc.chatfilter.eventhandlers.Handler;

import java.util.*;

public final class ChatFilter extends JavaPlugin implements Listener {
    public static Map<String,String> replaceableMap = new HashMap<>();
    public static List<String> commandlist = new ArrayList<>();
    public static Configs config;
    public static Configs chattags;
    public static Configs log;
    public static String ignorePermission = "satanicfilter.ignore";
    public static String skipPlayTimePermission = "satanicfilter.playtimebypass";
    public static String muteCommand;
    public static ChatFilter instance;
    public static List<String> banlist = new ArrayList<>();
    public static char[] ignoreChars;


    @Override
    public void onEnable() {
        // Plugin startup logic
        SatanicLib.setupLib(this);
        instance = this;
        getServer().getPluginManager().registerEvents(new Handler(),this);
        getServer().getPluginCommand("sfilter").setExecutor(new Command());
        config = Configs.Defaults.setupConfig();
        chattags = Configs.Defaults.setupChatTags();
        log = new Configs("log");
        Utils.startUp("SatanicFilter Premium");
        deserealize();

    }
    public static void deserealize(){
        commandlist = config.get().getStringList("triggercommands");
        replaceableMap.clear();
        for (String blacklist : config.get().getStringList("blacklist")) {
            String[] strs = blacklist.split("\\|");
            replaceableMap.put(strs[0],strs[1]);
        }
        banlist = config.get().getStringList("banlist");
        ignoreChars = config.get().getString("ignoreChar").toCharArray();
        Bukkit.getLogger().info(ChatColor.YELLOW + "[SatanicFilter] Успешно зарегистрировано: " + commandlist.size() + " команд.");
        Bukkit.getLogger().info(ChatColor.YELLOW + "[SatanicFilter] Успешно зарегистрировано: " + replaceableMap.size() + " ругательств.");
        muteCommand = config.get().getString("mutecommand");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
