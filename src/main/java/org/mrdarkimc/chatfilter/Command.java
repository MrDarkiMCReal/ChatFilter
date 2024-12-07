package org.mrdarkimc.chatfilter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (strings.length == 1 && strings[0].equals("reload")){
            if (!commandSender.hasPermission("satanicfilter.ignore"))
                return true;
            ChatFilter.deserealize();
            commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Команды и ключевые слова обновлены");
        }
        return true;
    }
}
