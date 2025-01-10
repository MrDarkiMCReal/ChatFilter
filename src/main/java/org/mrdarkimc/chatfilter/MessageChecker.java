package org.mrdarkimc.chatfilter;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.mrdarkimc.SatanicLib.TagBuilderGetter;
import org.mrdarkimc.SatanicLib.messages.KeyedMessage;
import org.mrdarkimc.chatfilter.eventhandlers.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageChecker {
    private static final Map<Player, String> cachedMessage = new HashMap<>();

    public enum MessageStatus {
        BAD, GOOD, ALREADY_BLOCKED
    }
    public static void removeFromCache(Player player){
        cachedMessage.remove(player);
    }

    public static boolean checkAlreadyBlockedMessage(Player player) {
        return cachedMessage.containsKey(player);
    }
    public static boolean havePlayedEnough(Player player){
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE) > ChatFilter.config.get().getInt("newbiePlayTimeInTicks");
    }
    public static void passMessage(Player player){
        if (!cachedMessage.containsKey(player)){
            new KeyedMessage(null,"messages.player_left",Map.of("{player}",player.getName())).sendToPlayersWithPermission("satanicfilter.chatmoderator");
            return;
        }
        String message = new String(cachedMessage.get(player));
        cachedMessage.remove(player);
        Handler.passedPlayers.add(player);
        player.chat(message);
    }

    public static MessageStatus checkUnicode(Player sender, String message) {
        String regex = "[^a-zA-Zа-яёА-ЯЁ0-9\\s!@#$`~%^&*()_\\-+=\\[\\]{}|;:'\",.<>?/]"; //todo добавил ~ `
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            if (checkAlreadyBlockedMessage(sender))
                return MessageStatus.ALREADY_BLOCKED;
            cachedMessage.put(sender, message);
            return MessageStatus.BAD;
        }
        return MessageStatus.GOOD;
    }
    public static MessageStatus checkBanWords(Player player, String message){
        String combined = message.replace("точка",".");
        for (char ch : ChatFilter.ignoreChars) {
            combined = combined.replace(String.valueOf(ch),"");
        }
        for (String string : ChatFilter.banlist) {
            if (combined.toLowerCase().contains(string.toLowerCase())){
                if (checkAlreadyBlockedMessage(player))
                    return MessageStatus.ALREADY_BLOCKED;
                cachedMessage.put(player,message);
                return MessageStatus.BAD;
            }
        }
        return MessageStatus.GOOD;
    }
    public static void checkPlayer(Player player){
        if (cachedMessage.containsKey(player)){
            String text = cachedMessage.get(player);
            alertMods(player,text);
        }
    }

    public static void alertMods(Player player, String message) {
//        if (cachedMessage.containsKey(player)) {
//            player.sendMessage("Вы еще раз написали подозрительное сообщение. Ожидайте проверки первого сообщения.");
//            return;
//        }
        TextComponent msgToMods = TagBuilderGetter.get(player, "messageToModerators", Map.of("{player}", player.getName(), "{message}", message));
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> p.hasPermission("satanicfilter.chatmoderator"))
                .forEach(adm -> {
                    adm.spigot().sendMessage(msgToMods);
                    adm.playSound(adm.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1,2);
                });
        cachedMessage.put(player, message);
    }
}
