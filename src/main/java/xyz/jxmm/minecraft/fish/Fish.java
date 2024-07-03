package xyz.jxmm.minecraft.fish;

import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.DecimalFormat;

public class Fish {
    public static void fish(JsonObject json, Long sender, Group group) {
        MessageChain at = MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]");
        MessageChainBuilder chain = new MessageChainBuilder().append(at);
        /*
        MessageChainBuilder all = new MessageChainBuilder();
        MessageChainBuilder water = new MessageChainBuilder();
        MessageChainBuilder lava = new MessageChainBuilder();
        MessageChainBuilder ice = new MessageChainBuilder();*/

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        //判断是否存在fishing数据
        if (json.get("player").getAsJsonObject().has("stats") &&
                json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("MainLobby") &&
                json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("MainLobby").getAsJsonObject().has("fishing")) {
            JsonObject playerJson = json.get("player").getAsJsonObject();
            JsonObject fishJson = playerJson.get("stats").getAsJsonObject().get("MainLobby").getAsJsonObject().get("fishing").getAsJsonObject();

            chain.append(new PlainText(Nick.nick(playerJson) + " "));
            chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
            chain.append(new PlainText(" | 大厅钓鱼数据:"));
            if (fishJson.has("stats") && fishJson.get("stats").getAsJsonObject().has("permanent")) {
                JsonObject permanent = fishJson.get("stats").getAsJsonObject().get("permanent").getAsJsonObject();
                chain.append(new PlainText("\n钓到鱼: "));
                int fish = 0;
                if (permanent.has("water") && permanent.get("water").getAsJsonObject().has("fish")) {
                    fish += permanent.get("water").getAsJsonObject().get("fish").getAsInt();
                }
                if (permanent.has("ice") && permanent.get("ice").getAsJsonObject().has("fish")) {
                    fish += permanent.get("ice").getAsJsonObject().get("fish").getAsInt();
                }
                if (permanent.has("lava") && permanent.get("lava").getAsJsonObject().has("fish")) {
                    fish += permanent.get("lava").getAsJsonObject().get("fish").getAsInt();
                }
                chain.append(new PlainText(String.valueOf(fish)));
                chain.append(new PlainText("\n钓到宝藏: "));
                int treasure = 0;
                if (permanent.has("water") && permanent.get("water").getAsJsonObject().has("treasure")) {
                    treasure += permanent.get("water").getAsJsonObject().get("treasure").getAsInt();
                }
                if (permanent.has("ice") && permanent.get("ice").getAsJsonObject().has("treasure")) {
                    treasure += permanent.get("ice").getAsJsonObject().get("treasure").getAsInt();
                }
                if (permanent.has("lava") && permanent.get("lava").getAsJsonObject().has("treasure")) {
                    treasure += permanent.get("lava").getAsJsonObject().get("treasure").getAsInt();
                }
                chain.append(new PlainText(String.valueOf(treasure)));
                chain.append(new PlainText("\n钓到垃圾: "));
                int junk = 0;
                if (permanent.has("water") && permanent.get("water").getAsJsonObject().has("junk")) {
                    junk += permanent.get("water").getAsJsonObject().get("junk").getAsInt();
                }
                if (permanent.has("ice") && permanent.get("ice").getAsJsonObject().has("junk")) {
                    junk += permanent.get("ice").getAsJsonObject().get("junk").getAsInt();
                }
                if (permanent.has("lava") && permanent.get("lava").getAsJsonObject().has("junk")) {
                    junk += permanent.get("lava").getAsJsonObject().get("junk").getAsInt();
                }
                chain.append(new PlainText(String.valueOf(junk)));
            }
        } else {
            chain.append(MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]")).append(new PlainText(" 该玩家的钓鱼数据为空"));
            //group.sendMessage(chain.build());
        }
        group.sendMessage(chain.build());
    }
}
