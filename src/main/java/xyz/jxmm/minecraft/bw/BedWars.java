package xyz.jxmm.minecraft.bw;

import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.DecimalFormat;

import static xyz.jxmm.minecraft.bw.BedWarsDetermine.*;

public class BedWars {
    //起床战争解析
    public static void bw(JsonObject json, Long sender, Group group) {
        MessageChain at = MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]");
        MessageChainBuilder chain = new MessageChainBuilder().append(at);
        JsonObject bwJson;
        JsonObject playerJson;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (json.get("player").isJsonObject()) {
            playerJson = json.get("player").getAsJsonObject();

            chain.append(new PlainText("\n" + Nick.nick(playerJson) + " ")); //玩家名称前缀
            chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
            chain.append(new PlainText(" | 起床战争数据:"));

            if (json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("Bedwars")) {
                bwJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject();

                if (games_played_bedwars(bwJson)) {
                    chain.append(new PlainText("\n游戏场次: "));
                    chain.append(new PlainText(String.valueOf(bwJson.get("games_played_bedwars").getAsInt())));
                    chain.append(new PlainText(" | 等级: "));
                    chain.append(new PlainText(String.valueOf(json.get("player").getAsJsonObject().get("achievements").getAsJsonObject().get("bedwars_level").getAsInt())));
                    if (bwJson.has("winstreak")) {
                        chain.append(new PlainText(" | 连胜: "));
                        chain.append(new PlainText(String.valueOf(bwJson.get("winstreak").getAsInt())));
                    }
                }

                chain.append(new PlainText("\n胜场: "));
                if (bwJson.has("wins_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 败场: "));
                if (bwJson.has("losses_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("losses_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | WLR: "));
                if (bwJson.has("wins_bedwars") && bwJson.has("losses_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("wins_bedwars").getAsInt() /
                                    (float) bwJson.get("losses_bedwars").getAsInt())));
                } else if (bwJson.has("wins_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("wins_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n摧毁床: "));
                if (bwJson.has("beds_broken_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 被摧毁床: "));
                if (bwJson.has("beds_lost_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("beds_lost_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | BLR: "));
                if (bwJson.has("beds_broken_bedwars") && bwJson.has("beds_lost_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("beds_broken_bedwars").getAsInt() /
                                    (float) bwJson.get("beds_lost_bedwars").getAsInt())));
                } else if (bwJson.has("beds_broken_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("beds_broken_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n击杀: "));
                if (bwJson.has("kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 死亡: "));
                if (bwJson.has("deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | KDR: "));
                if (bwJson.has("kills_bedwars") && bwJson.has("deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("kills_bedwars").getAsInt() /
                                    (float) bwJson.get("deaths_bedwars").getAsInt())));
                } else if (bwJson.has("kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                chain.append(new PlainText("\n最终击杀: "));
                if (bwJson.has("final_kills_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | 最终死亡: "));
                if (bwJson.has("final_deaths_bedwars")) {
                    chain.append(new PlainText(String.valueOf(bwJson.get("final_deaths_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));
                chain.append(new PlainText(" | FKDR："));
                if (bwJson.has("final_kills_bedwars") && bwJson.has("final_deaths_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) bwJson.get("final_kills_bedwars").getAsInt() /
                                    (float) bwJson.get("final_deaths_bedwars").getAsInt())));
                } else if (bwJson.has("final_kills_bedwars")) {
                    chain.append(new PlainText(decimalFormat.format(bwJson.get("final_kills_bedwars").getAsInt())));
                } else chain.append(new PlainText("0"));

                int k = 0;
                int d = 0;
                if (bwJson.has("kills_bedwars") || bwJson.has("final_kills_bedwars") ||
                        bwJson.has("deaths_bedwars") || bwJson.has("final_deaths_bedwars")) {
                    if (bwJson.has("kills_bedwars")) {
                        k += bwJson.get("kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("final_kills_bedwars")) {
                        k += bwJson.get("final_kills_bedwars").getAsInt();
                    }
                    if (bwJson.has("deaths_bedwars")) {
                        d += bwJson.get("deaths_bedwars").getAsInt();
                    }
                    if (bwJson.has("final_deaths_bedwars")) {
                        d += bwJson.get("final_deaths_bedwars").getAsInt();
                    }
                    chain.append(new PlainText("\n总击杀: "));
                    chain.append(new PlainText(String.valueOf(k)));
                    chain.append(new PlainText(" | 总死亡: "));
                    chain.append(new PlainText(String.valueOf(d)));
                    chain.append(new PlainText(" | 总KD: "));
                    if (d != 0) {
                        chain.append(new PlainText(decimalFormat.format((double) k / d)));
                    } else chain.append(new PlainText(decimalFormat.format(k)));
                } else chain.append(new PlainText("0"));

            } else {
                chain.append(new PlainText("该玩家的起床战争数据为空"));
            }
        } else if (json.get("player").isJsonNull()) {
            chain.append(new PlainText("<playerID> 不存在"));
        }

        group.sendMessage(chain.build());

    }
}
