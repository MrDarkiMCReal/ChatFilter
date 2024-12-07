package org.mrdarkimc.chatfilter;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Log {
    public enum Logtype {
        SIGN("[ТАБЛИЧКА]"),
        COMMAND("[КОМАНДА]"),
        CHAT("[ЧАТ]"),
        BOOK("[КНИГА]");

        // Поле для хранения строки
        private final String nameForLog;

        // Конструктор для инициализации значения
        Logtype(String nameForLog) {
            this.nameForLog = nameForLog;
        }

        // Метод для получения значения строки
        public String getNameForLog() {
            return nameForLog;
        }
    }
    public static void send(Player player, Location location, String textToAdd, Logtype type){
        String playername = player.getName();
        String world = location.getWorld().getName();
        String x = String.valueOf(location.getX());
        String y = String.valueOf(location.getY());
        String z = String.valueOf(location.getZ());

        new BukkitRunnable(){

            @Override
            public void run() {

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("[dd.MM.yyyy HH:mm]");
                String time = sdf.format(now);
                StringBuilder builder = new StringBuilder();
                builder.append(time);
                builder.append(" ");
                builder.append(type.getNameForLog());
                builder.append(" ");
                builder.append(playername);
                if (type.equals(Logtype.SIGN)){
                    builder.append(" ");
                    builder.append("world:");
                    builder.append(world);
                    builder.append(" ");
                    builder.append("X:");
                    builder.append(x);
                    builder.append(" ");
                    builder.append("Y:");
                    builder.append(y);
                    builder.append(" ");
                    builder.append("Z:");
                    builder.append(z);
                }
                builder.append(" : ");
                builder.append(textToAdd);
                FileConfiguration log = ChatFilter.log.get();
                List<String> stringlist = log.getStringList("trace");
                stringlist.add(builder.toString());
                log.set("trace",stringlist);
                ChatFilter.log.saveConfig();
            }
        }.runTaskAsynchronously(ChatFilter.instance);

    }
}
