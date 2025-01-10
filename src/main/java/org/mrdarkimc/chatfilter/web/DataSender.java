package org.mrdarkimc.chatfilter.web;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.chatfilter.ChatFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataSender {
    public static void sendData(DataPacket data){
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
                    Bukkit.getLogger().info(" ");
                    Bukkit.getLogger().info("Статус код: " + responseCode);
                    Bukkit.getLogger().info("Пакет: ");
                    Bukkit.getLogger().info("nickname: " + data.nickname);
                    Bukkit.getLogger().info("message: " + data.message);
                    Bukkit.getLogger().info("server_name: " + data.server_name);
                    Bukkit.getLogger().info(" ");

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
