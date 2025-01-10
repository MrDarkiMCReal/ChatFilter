package org.mrdarkimc.chatfilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mrdarkimc.SatanicLib.messages.KeyedMessage;

import java.util.Map;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (strings.length >0){
            switch (strings[0]){
                case "reload":
                    if (!commandSender.hasPermission("satanicfilter.ignore"))
                        return true;
                    ChatFilter.config.reloadConfig();
                    ChatFilter.deserealize();
                    commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Команды и ключевые слова обновлены");
                    return true;
                case "pass":
                    if (!commandSender.hasPermission("satanicfilter.chatmoderator"))
                        return true;
                    if (strings.length < 2) {
                        commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Выберите игрока");
                        return true;
                    }
                    Player sus = Bukkit.getPlayer(strings[1]);
                    if (sus!=null) {
                        new KeyedMessage(null,"messages.player_passed", Map.of("{player}",sus.getName())).sendToPlayersWithPermission("satanicfilter.chatmoderator");
                        MessageChecker.passMessage(sus);
                        return true;
                    }
                    commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Игрок оффлайн");
            }
        }
        return true;
    }
}
