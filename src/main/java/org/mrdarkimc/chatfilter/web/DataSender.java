package org.mrdarkimc.chatfilter.web;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.chatfilter.ChatFilter;
import org.mrdarkimc.chatfilter.web.interfaces.BasePacket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataSender {
    public static <T extends BasePacket> void sendData(T data){
        new BukkitRunnable(){

            @Override
            public void run() {
                String urlString = ChatFilter.config.get().getString("URL");
                HttpURLConnection connection = null;

                try {
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    Gson gson = new Gson();
                    String jsonData = gson.toJson(data);


                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int responseCode = connection.getResponseCode();
                    data.log(responseCode);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.runTaskAsynchronously(ChatFilter.instance);
    }
}
