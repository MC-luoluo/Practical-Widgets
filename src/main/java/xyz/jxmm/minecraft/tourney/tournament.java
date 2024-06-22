package xyz.jxmm.minecraft.tourney;

import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class tournament {
    public static void tourney(JsonObject json, Long sender, Group group) {
        MessageChain at = MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]");
        MessageChainBuilder chain = new MessageChainBuilder().append(at);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        if (json.get("player").isJsonObject()) {
            JsonObject playerJson = json.get("player").getAsJsonObject();
            if (playerJson.has("tourney")) {
                JsonObject tourneyJson = playerJson.get("tourney").getAsJsonObject();
                chain.append(new PlainText("\n" + Nick.nick(playerJson) + " ")); //玩家名称前缀
                chain.append(new PlainText(playerJson.get("displayname").getAsString()));
                chain.append(new PlainText(" | 锦标赛数据: "));
                if (tourneyJson.has("first_join_lobby")) {
                    chain.append(new PlainText("\n首次进入大厅: "));
                    chain.append(new PlainText(simpleDateFormat.format(new Date(tourneyJson.get("first_join_lobby").getAsLong()))));
                }
                if (tourneyJson.has("total_tributes")) {
                    chain.append(new PlainText("\n战魂: "));
                    chain.append(new PlainText(String.valueOf(tourneyJson.get("total_tributes").getAsInt())));
                }
                if (tourneyJson.has("sw_normal_doubles_0")) {
                    chain.append(new PlainText("\n\n24年6月空岛战争: "));
                    JsonObject current = tourneyJson.get("sw_normal_doubles_0").getAsJsonObject();
                    if (current.has("games_played")) {
                        chain.append(new PlainText("\n游玩场次: "));
                        chain.append(new PlainText(String.valueOf(current.get("games_played").getAsInt())));
                    }
                    if (current.has("playtime")) {
                        chain.append(new PlainText(" | 游玩时间: "));
                        chain.append(new PlainText(current.get("playtime").getAsInt() / 60 + "h" + current.get("playtime").getAsInt() % 60 + "m"));
                    }
                    if (current.has("tributes_earned")) {
                        chain.append(new PlainText(" | 获得战魂: "));
                        chain.append(new PlainText(String.valueOf(current.get("tributes_earned").getAsInt())));
                    }
                    if (current.has("claimed_ranking_reward")){
                        chain.append(new PlainText("\n奖励领取时间: "));
                        chain.append(new PlainText(simpleDateFormat.format(new Date(tourneyJson.get("claimed_ranking_reward").getAsLong()))));
                    }
                }
            } else chain.append(new PlainText("该玩家的锦标赛数据为空"));

            group.sendMessage(chain.build());

        }
    }
}
