package org.mrdarkimc.chatfilter.web;

import org.mrdarkimc.chatfilter.ChatFilter;

public class DataPacket {
    String nickname;
    String message;
    String server_name;

    public DataPacket(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
        this.server_name = ChatFilter.config.get().getString("servername");
    }
}
