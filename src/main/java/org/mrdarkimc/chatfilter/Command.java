package org.mrdarkimc.chatfilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mrdarkimc.SatanicLib.messages.KeyedMessage;
import org.mrdarkimc.chatfilter.web.BadMessagePacket;
import org.mrdarkimc.chatfilter.web.DataSender;

import java.util.Map;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (strings.length >0){
            switch (strings[0]){
                case "reload":
                    if (!(commandSender.hasPermission("satanicfilter.chatmoderator") || commandSender instanceof ConsoleCommandSender))
                        return true;
                    ChatFilter.config.reloadConfig();
                    ChatFilter.deserealize();
                    commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Команды и ключевые слова обновлены");
                    return true;
                case "pass":
                    if (!(commandSender.hasPermission("satanicfilter.chatmoderator") || commandSender instanceof ConsoleCommandSender || commandSender instanceof RemoteConsoleCommandSender))
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
                    return true;
                case "mute":
                    if (!(commandSender.hasPermission("satanicfilter.chatmoderator") || commandSender instanceof ConsoleCommandSender || commandSender instanceof RemoteConsoleCommandSender))
                        return true;
                    if (strings.length < 2) {
                        commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Выберите игрока");
                        return true;
                    }
                    Player sus2 = Bukkit.getPlayer(strings[1]);
                    if (sus2!=null) {
                        new KeyedMessage(null,"messages.player_muted", Map.of("{player}",strings[1])).sendToPlayersWithPermission("satanicfilter.chatmoderator");
                        MessageChecker.removeFromCache(sus2);
                        StringBuilder builder = new StringBuilder();
                        for (int i = 3; i < strings.length; i++) {
                            builder.append(strings[i]);
                            builder.append(" ");
                        }
                        // /sfilter mute MrDarkiMC 123
                        Bukkit.getServer().dispatchCommand(commandSender,ChatFilter.muteCommand
                                .replace("{player}",strings[1])
                                .replace("{args}",builder.toString()));
                        return true;
                    }
                    commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Игрок оффлайн");
                    return true;
                case "ban":
                    if (!(commandSender.hasPermission("satanicfilter.chatmoderator") || commandSender instanceof ConsoleCommandSender || commandSender instanceof RemoteConsoleCommandSender))
                        return true;
                    if (strings.length < 2) {
                        commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Выберите игрока");
                        return true;
                    }
                    Player sus3 = Bukkit.getPlayer(strings[1]);
                    if (sus3!=null) {
                        new KeyedMessage(null,"messages.player_banned", Map.of("{player}",sus3.getName())).sendToPlayersWithPermission("satanicfilter.chatmoderator");
                        MessageChecker.removeFromCache(sus3);
                        StringBuilder builder = new StringBuilder();
                        for (int i = 3; i < strings.length; i++) {
                            builder.append(strings[i]);
                            builder.append(" ");
                        }
                        // /sfilter mute MrDarkiMC 123
                        Bukkit.getServer().dispatchCommand(commandSender,ChatFilter.muteCommand
                                .replace("{player}",sus3.getName())
                                .replace("{args}",builder.toString()));
                        DataSender.sendData(new BadMessagePacket("2",sus3.getName(),));
                        return true;
                    }
                    commandSender.sendMessage(ChatColor.YELLOW + "[SatanicFilter] Игрок оффлайн");
                    return true;
            }
        }
        return true;
    }
}
