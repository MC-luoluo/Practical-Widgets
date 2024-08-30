package xyz.jxmm.minecraft;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.tools.URLConnect;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

public class Leaders {
    public static String leaders(){
        Properties properties = new Properties();
        File cfg = new File("./PracticalWidgets/config.properties");

        try {
            properties.load(new InputStreamReader(Files.newInputStream(cfg.toPath()), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String HypixelAPI = properties.getProperty("HypixelAPI");
        String connectURL = "https://api.hypixel.net/v2/leaderboards?key=" + HypixelAPI;
        return URLConnect.URLConnect(connectURL);
    }
}
