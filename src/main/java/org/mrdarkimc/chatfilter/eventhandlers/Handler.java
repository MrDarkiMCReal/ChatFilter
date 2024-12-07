package org.mrdarkimc.chatfilter.eventhandlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.mrdarkimc.chatfilter.ChatFilter;
import org.mrdarkimc.chatfilter.Log;

import java.util.*;
import java.util.regex.Pattern;

public class Handler implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    void onchat(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().hasPermission(ChatFilter.permission))
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

//        StringBuilder builder = new StringBuilder();
//        String[] strs = original.split(" ");
//        for (int i = 0; i < strs.length; i++) {
//            for (Map.Entry<String, String> entry : ChatFilter.replaceableMap.entrySet()) {
//                if (strs[i].toLowerCase().contains(entry.getKey())){
//                    strs[i].toLowerCase().replaceAll("(?i)" + entry.getKey(), entry.getValue());
//                }
//            }
//            //strs[i] = ChatFilter.replaceableMap.getOrDefault(strs[i].toLowerCase(), strs[i]);
//            builder.append(strs[i]);
//            builder.append(" ");
//        }
//        e.setMessage(builder.toString());
//        if (e.getMessage().equals(saveOriginal)) {
//            Log.send(e.getPlayer(), e.getPlayer().getLocation(), saveOriginal, Log.Logtype.COMMAND);
//        }

    }

    @EventHandler
    void onSign(SignChangeEvent e) {
        String spliterator = "gg2SPLITERATOR2gg";
        if (e.getPlayer().hasPermission(ChatFilter.permission)) {
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


        // Сохраняем оригинальные строки
//        String[] originalLine = e.getLines();
//
//        // Обрабатываем каждую строку в таблице
//        for (int i = 0; i < e.getLines().length; i++) {
//            StringBuilder builder = new StringBuilder();
//
//            // Разбиваем строку на слова
//            String[] strs = e.getLine(i).split(" ");
//
//            // Обрабатываем каждое слово
//            for (int i2 = 0; i2 < strs.length; i2++) {
//                // Заменяем слово, если оно есть в replaceableMap
//                for (Map.Entry<String, String> entry : ChatFilter.replaceableMap.entrySet()) {
//                    strs[i2] = strs[i2].toLowerCase().replaceAll("(?i)" + entry.getKey(), entry.getValue());
//                }
//                //strs[i2] = ChatFilter.replaceableMap.getOrDefault(strs[i2].toLowerCase(), strs[i2]);
//                builder.append(strs[i2]);
//                builder.append(" "); // Добавляем пробел после каждого слова
//            }
//
//            // Устанавливаем отредактированную строку обратно в таблицу
//            e.setLine(i, builder.toString().trim());
//        }

        // Проверка, были ли изменения в строках
//        if (!Arrays.equals(originalLine, e.getLines())) {
//            Log.send(e.getPlayer(), e.getBlock().getLocation(), Arrays.toString(originalLine), Log.Logtype.SIGN);
//        }


//
//
//
//            for (Map.Entry<String, String> stringStringEntry : ChatFilter.replaceableMap.entrySet()) {
//                String cachedline = e.getLine(i);
//                String updatedline =  e.getLine(i).replaceAll("(?i)"+stringStringEntry.getKey(),stringStringEntry.getValue());
//                e.setLine(i,updatedline);
//                if (!cachedline.equals(updatedline)){
//                    Log.send(e.getPlayer(), e.getBlock().getLocation(), cachedline, Log.Logtype.SIGN);
//                }
//            }
//        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().hasPermission(ChatFilter.permission)) {
            return;
        }
        String message = e.getMessage();
        String untouchmessage = e.getMessage();

        for (Map.Entry<String, String> stringStringEntry : ChatFilter.replaceableMap.entrySet()) {
            String regex = stringStringEntry.getKey();
            String replacement = stringStringEntry.getValue();
            message = Pattern.compile(regex,Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE).matcher(message).replaceAll(replacement);

//            if (!message.toLowerCase().matches(".*\\b" + stringStringEntry.getKey().toLowerCase() + "\\b.*")) {
//                continue;
//            }


        }
        e.setMessage(message);
//        StringBuilder builder = new StringBuilder();
//        String[] strs = message.split(" ");
//        for (int i = 0; i < strs.length; i++) {
//            for (Map.Entry<String, String> entry : ChatFilter.replaceableMap.entrySet()) {
//                if (strs[i].toLowerCase().contains(entry.getKey())){
//                    strs[i].toLowerCase().replaceAll("(?i)" + entry.getKey(), entry.getValue());
//                }
//            }
//            //strs[i] = ChatFilter.replaceableMap.getOrDefault(strs[i].toLowerCase(), strs[i]);
//            builder.append(strs[i]);
//            builder.append(" ");
//        }
//        e.setMessage(builder.toString());
        if (!message.equals(untouchmessage)) {
            Log.send(e.getPlayer(), e.getPlayer().getLocation(), untouchmessage, Log.Logtype.CHAT);
        }
        }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        if (event.getPlayer().hasPermission(ChatFilter.permission)) {
            return;
        }
        //Bukkit.getLogger().info("Triggering");
        Player player = event.getPlayer();
        BookMeta untouchMeta = event.getNewBookMeta();
        BookMeta bookMeta = (BookMeta) event.getNewBookMeta();

        if (bookMeta == null || !bookMeta.hasPages()) {
            return;
        }

        List<String> pages = new ArrayList<>(bookMeta.getPages()); // Создаем новый список, чтобы избежать изменения неизменяемого списка
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

//        for (String page : pages) {
//            String linesInPageToString = page.replaceAll("\n",spliteratorForPages);
//            pageReverseBuilder.append(page);
//            pageReverseBuilder.append(spliterator);
//        }



//        for (int i = 0; i < pages.size(); i++) {
//            String[] linesTogether = pages.get(i).split("\n");
//            StringBuilder togetherBuilder = new StringBuilder();
//
//            for (int i1 = 0; i1 < linesTogether.length; i1++) { //пробегаем по каждой строке
//                StringBuilder builder = new StringBuilder();
//                String[] strs = linesTogether[i].split(" ");
//                for (int i3 = 0; i3 < strs.length; i3++) {
//                    for (Map.Entry<String, String> entry : ChatFilter.replaceableMap.entrySet()) {
//                        strs[i3] = strs[i3].toLowerCase().replaceAll("(?i)" + entry.getKey(), entry.getValue());
//                    }
//                    //strs[i3] = ChatFilter.replaceableMap.getOrDefault(strs[i3].toLowerCase(), strs[i3]);
//                    builder.append(strs[i3]);
//                    builder.append(" ");
//                }
//                togetherBuilder.append(builder);
//                togetherBuilder.append("\n");
//            }
//            pages.set(i, togetherBuilder.toString());
//        }
        bookMeta.setPages(pages); // Устанавливаем отфильтрованные страницы
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

//            StringBuilder builder = new StringBuilder();
//            String[] strs = pages.get(i).split(" ");
//            for (int i3 = 0; i3 < strs.length; i3++) {
//                strs[i3] = ChatFilter.replaceableMap.getOrDefault(strs[i3].toLowerCase(),strs[i3]);
//                builder.append(strs[i3]);
//                builder.append(" ");
//            }
//            pages.set(i,builder.toString());
//
//            for (Map.Entry<String, String> entry : ChatFilter.replaceableMap.entrySet()) {
//                String pageText = pages.get(i);
//                Bukkit.getLogger().info("Original text: " + pageText);
//               String filteredText = pageText.replaceAll("(?i)" + entry.getKey(), entry.getValue());
//                Bukkit.getLogger().info("Filtered text: " + filteredText);
//                pages.set(i, filteredText); // Теперь можно безопасно изменять элементы
//            }
//        }
//
//        bookMeta.setPages(pages); // Устанавливаем отфильтрованные страницы
//        event.setNewBookMeta(bookMeta);
//

//    }

}
