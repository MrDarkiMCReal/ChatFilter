package org.mrdarkimc.chatfilter.web;

import org.bukkit.Bukkit;
import org.mrdarkimc.chatfilter.ChatFilter;
import org.mrdarkimc.chatfilter.web.interfaces.BasePacket;

public class BadMessagePacket implements BasePacket {
    String ID;
    String nickname;
    String message;
    String server_name;

    public BadMessagePacket(String id, String nickname, String message) {
        this.ID = id;
        this.nickname = nickname;
        this.message = message;
        this.server_name = ChatFilter.config.get().getString("servername");
    }

    @Override
    public void log(int statuscode) {
        Bukkit.getLogger().info(" ");
        Bukkit.getLogger().info("Статус код: " + statuscode);
        Bukkit.getLogger().info("Пакет: ");
        Bukkit.getLogger().info("nickname: " + nickname);
        Bukkit.getLogger().info("message: " + message);
        Bukkit.getLogger().info("server_name: " + server_name);
        Bukkit.getLogger().info(" ");
    }
}
