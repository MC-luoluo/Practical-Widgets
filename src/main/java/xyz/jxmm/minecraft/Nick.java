package xyz.jxmm.minecraft;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Nick {

    @NotNull
    public static String nick(JsonObject json) {
        if (json.has("rank") && !Objects.equals(json.get("rank").getAsString(), "NORMAL")) {
            String r1 = json.get("rank").getAsString();
            switch (r1) {
                case "YOUTUBER":
                    return "[YOUTUBE]";
                case "ADMIN":
                    return "[ADMIN]";
                case "GAME_MASTER":
                    return "[GM]";
                default:
                    return "[" + r1 + "]";
            }
        } else if (json.has("newPackageRank")) {
            String rank = json.get("newPackageRank").getAsString();
            boolean rankPlus = json.has("monthlyPackageRank");
            switch (rank) {

                case "MVP_PLUS":
                    String n1 = "";
                    String n2 = "";

                    if (json.has("rankPlusColor")) {  //++颜色
                        n1 = color(json.get("rankPlusColor").getAsString());
                        if (n1.isEmpty()) {
                            System.out.println(json.get("rankPlusColor").getAsString());
                        }
                    }
                    if (json.has("monthlyRankColor")) {  //rank颜色
                        n2 = color(json.get("monthlyRankColor").getAsString());
                        if (n2.isEmpty()) {
                            System.out.println(json.get("monthlyRankColor").getAsString());
                        }
                    }

                    if (rankPlus && json.get("monthlyPackageRank").getAsString().equals("SUPERSTAR")) {
                        return "[" + n2 + "MVP" + n1 + "++]";
                    } else {
                        return "[MVP" + n1 + "+]";
                    }
                case "MVP":
                    return "[MVP]";
                case "VIP_PLUS":
                    return "[VIP+]";
                case "VIP":
                    return "[VIP]";
            }
        }
        return "";
    }

    public static String color(String str) {
        Map<String, String> map = new HashMap<>();
        map.put("RED", "红");
        map.put("GOLD", "金");
        map.put("GREEN", "绿");
        map.put("YELLOW", "黄");
        map.put("LIGHT_PURPLE", "淡紫");
        map.put("WHITE", "白");
        map.put("BLUE", "蓝");
        map.put("AQUA", "青");

        map.put("DARK_GREEN", "深绿");
        map.put("DARK_RED", "深红");
        map.put("DARK_AQUA", "深水绿");
        map.put("DARK_PURPLE", "暗紫");
        map.put("深灰", "");
        map.put("BLACK", "黑");
        map.put("DARK_BLUE", "深蓝");

        return map.getOrDefault(str, "");
    }
}
