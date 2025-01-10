package org.mrdarkimc.chatfilter.eventhandlers;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.mrdarkimc.SatanicLib.TagBuilderGetter;
import org.mrdarkimc.SatanicLib.messages.KeyedMessage;
import org.mrdarkimc.SatanicLib.messages.Message;
import org.mrdarkimc.chatfilter.ChatFilter;
import org.mrdarkimc.chatfilter.Log;
import org.mrdarkimc.chatfilter.MessageChecker;
import org.mrdarkimc.chatfilter.web.DataPacket;
import org.mrdarkimc.chatfilter.web.DataSender;

import java.util.*;
import java.util.regex.Pattern;

public class Handler implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    void onchat(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().hasPermission(ChatFilter.ignorePermission))
            return;
        String original = e.getMessage();
        String saveOriginal = e.getMessage();
        boolean doreturn = true;
        for (String allowedCmmand : ChatFilter.commandlist) {
            if (original.startsWith(allowedCmmand)) {
                doreturn = false;
                break;
            }
        }
        if (doreturn)
            return;

        for (Map.Entry<String, String> stringStringEntry : ChatFilter.replaceableMap.entrySet()) {
            String regex = stringStringEntry.getKey();
            String replacement = stringStringEntry.getValue();
            original = Pattern.compile(regex,Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE).matcher(original).replaceAll(replacement);
        }
        e.setMessage(original);
        if (!e.getMessage().equals(saveOriginal)) {
            Log.send(e.getPlayer(), e.getPlayer().getLocation(), saveOriginal, Log.Logtype.COMMAND);
        }

    }

    @EventHandler
    void onSign(SignChangeEvent e) {
        String spliterator = "gg2SPLITERATOR2gg";
        if (e.getPlayer().hasPermission(ChatFilter.ignorePermission)) {
            return;
        }

        String[] savedArray = e.getLines();

        StringBuilder lineReverseBuilder = new StringBuilder();
        for (String line : e.getLines()) {
            lineReverseBuilder.append(line);
            lineReverseBuilder.append(spliterator);
        }
        String updatedlines = lineReverseBuilder.toString();
        for (Map.Entry<String, String> stringStringEntry : ChatFilter.replaceableMap.entrySet()) {
            String regex = stringStringEntry.getKey();
            String replacement = stringStringEntry.getValue();
            updatedlines = Pattern.compile(regex,Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE).matcher(updatedlines).replaceAll(replacement);
        }
        String[] strings = Arrays.stream(updatedlines.split(spliterator)).toArray(String[]::new);
        for (int i = 0; i < Arrays.stream(updatedlines.split(spliterator)).toArray().length; i++) {
            e.setLine(i,strings[i]);
        }
        if (!Arrays.equals(e.getLines(), savedArray)){
            Log.send(e.getPlayer(), e.getBlock().getLocation(), Arrays.toString(savedArray), Log.Logtype.SIGN);
        }

    }
    public static Set<Player> passedPlayers = new HashSet<>();
    @EventHandler(priority = EventPriority.LOWEST)
    void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission(ChatFilter.ignorePermission)) {
            return;
        }
        String message = e.getMessage();
        String untouchmessage = e.getMessage();
        if (player.hasPermission(ChatFilter.skipPlayTimePermission) || MessageChecker.havePlayedEnough(player)){

        }else {
            e.setCancelled(true);
            TextComponent comp = TagBuilderGetter.get(null,"playtimeToAdmins",Map.of("{player}", player.getName(), "{message}", message));
            Bukkit.getOnlinePlayers().stream()
                    .filter(player1 -> player1.hasPermission("satanicfilter.chatmoderator"))
                    .forEach((p) -> {
                        p.spigot().sendMessage(comp);
                        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1,2);
                    });
            new KeyedMessage(player,"messages.playtime",Map.of("{player}", player.getName())).send();
            return;
        }

        if (passedPlayers.contains(player)){
            passedPlayers.remove(player);
        }else {
            switch (MessageChecker.checkUnicode(player, untouchmessage)) { //проверка на юникод
                case BAD:
                    new KeyedMessage(player,"messages.banmessage",null).send();
                    e.setCancelled(true);
                    MessageChecker.alertMods(player, message);
                    DataSender.sendData(new DataPacket(player.getName(),message));
                    return;
                case ALREADY_BLOCKED:
                    e.setCancelled(true);
                    new KeyedMessage(player,"messages.already_banmessage",null).send();
                    return;
                case GOOD:
                    break;
            }
            switch (MessageChecker.checkBanWords(player, message)) {
                case BAD:
                    new KeyedMessage(player,"messages.banmessage",null).send();
                    e.setCancelled(true);
                    MessageChecker.alertMods(player, message);
                    DataSender.sendData(new DataPacket(player.getName(),message));
                    return;
                case ALREADY_BLOCKED:
                    e.setCancelled(true);
                    new KeyedMessage(player,"messages.already_banmessage",null).send();
                    return;
                case GOOD:
                    break;
            }
        }







        for (Map.Entry<String, String> stringStringEntry : ChatFilter.replaceableMap.entrySet()) {
            String regex = stringStringEntry.getKey();
            String replacement = stringStringEntry.getValue();
            message = Pattern.compile(regex,Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE).matcher(message).replaceAll(replacement);
        }
        e.setMessage(message);
        if (!message.equals(untouchmessage)) {
            Log.send(e.getPlayer(), e.getPlayer().getLocation(), untouchmessage, Log.Logtype.CHAT);
        }
        }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        if (event.getPlayer().hasPermission(ChatFilter.ignorePermission)) {
            return;
        }
        Player player = event.getPlayer();
        BookMeta untouchMeta = event.getNewBookMeta();
        BookMeta bookMeta = (BookMeta) event.getNewBookMeta();

        if (bookMeta == null || !bookMeta.hasPages()) {
            return;
        }

        List<String> pages = new ArrayList<>(bookMeta.getPages());
        //1 страница в книге = 1 элемент в списке
        String spliteratorForPages = "ggSPLITERATORgg";
        String spliteratorForLines = "gg2SPLITERATOR2gg";
        StringBuilder pageReverseBuilder = new StringBuilder();
        for (String page : pages) {
            pageReverseBuilder.append(page);
            pageReverseBuilder.append(spliteratorForPages);
        }
        String pagesString = pageReverseBuilder.toString();
        String finalText = pagesString.replaceAll("\n",spliteratorForLines);
        for (Map.Entry<String, String> stringStringEntry : ChatFilter.replaceableMap.entrySet()) {
            String regex = stringStringEntry.getKey();
            String replacement = stringStringEntry.getValue();
            finalText = Pattern.compile(regex,Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE).matcher(finalText).replaceAll(replacement);
        }
        pages = Arrays.stream(finalText.replaceAll(spliteratorForLines,"\n").split(spliteratorForPages)).toList();

        bookMeta.setPages(pages);
        event.setNewBookMeta(bookMeta);
        if (!bookMeta.equals(untouchMeta)) {
            Log.send(player, player.getLocation(), bookMeta.getAsString(), Log.Logtype.BOOK);
            Bukkit.getOperators()
                    .stream()
                    .filter(OfflinePlayer::isOnline)
                    .map(OfflinePlayer::getPlayer)
                    .filter(Objects::nonNull)
                    .forEach(admin -> {
                        admin.sendMessage(ChatColor.RED + "[SatanicFilter]" + ChatColor.GRAY + " ТОЛЬКО ДЛЯ ОПЕРАТОРОВ: ");
                        admin.sendMessage(ChatColor.RED + "[SatanicFilter]" + ChatColor.GRAY + " Игрок " + player.getName() + " использовал ругательства в книге");
                    });
        }
    }
    @EventHandler
    void quit(PlayerQuitEvent e){
        MessageChecker.removeFromCache(e.getPlayer());
    }


}
