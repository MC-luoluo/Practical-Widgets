package xyz.jxmm.minecraft.tnt;

import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.DecimalFormat;

public class TNTGames {
    public static void tnt(JsonObject json, Long sender, Group group) {
        MessageChainBuilder chain = new MessageChainBuilder();

        if (json.get("player").isJsonObject()) {
            JsonObject playerJson = json.get("player").getAsJsonObject();

            if (playerJson.get("stats").getAsJsonObject().has("TNTGames")) {
                JsonObject tntJson = playerJson.get("stats").getAsJsonObject().get("TNTGames").getAsJsonObject();
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                chain.append(new PlainText("\n" + Nick.nick(playerJson) + " "));
                chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
                chain.append(new PlainText(" | TNT Games 数据如下:"));

                chain.append(new PlainText("\n胜场: "));
                if (tntJson.has("wins")) {
                    chain.append(new PlainText(String.valueOf(tntJson.get("wins").getAsInt())));
                } else {
                    chain.append(new PlainText("null"));
                }
                chain.append(new PlainText(" | 连胜: "));
                if (tntJson.has("winstreak")) {
                    chain.append(new PlainText(String.valueOf(tntJson.get("winstreak").getAsInt())));
                } else {
                    chain.append(new PlainText("null"));
                }
                if (tntJson.has("coins")) {
                    chain.append(new PlainText("\n硬币: "));
                    chain.append(new PlainText(String.valueOf(tntJson.get("coins").getAsInt())));
                }


                MessageChainBuilder tntag = new MessageChainBuilder();
                tntag.append(new PlainText("TNT Tag: "));

                tntag.append(new PlainText("\n胜场: "));
                if (tntJson.has("wins_tntag")) {
                    tntag.append(new PlainText(String.valueOf(tntJson.get("wins_tntag").getAsInt())));
                    tntag.append(new PlainText(" | WLR: "));
                    tntag.append(new PlainText(decimalFormat.format(
                            (double) tntJson.get("wins_tntag").getAsInt() /
                                    (double) tntJson.get("deaths_tntag").getAsInt())));
                } else tntag.append(new PlainText("null"));

                tntag.append(new PlainText("\n击杀: "));
                if (tntJson.has("kills_tntag")) {
                    tntag.append(new PlainText(String.valueOf(tntJson.get("kills_tntag").getAsInt())));
                } else tntag.append(new PlainText("null"));
                tntag.append(new PlainText(" | 死亡:"));
                if (tntJson.has("deaths_tntag")) {
                    tntag.append(new PlainText(String.valueOf(tntJson.get("deaths_tntag").getAsInt())));
                } else tntag.append(new PlainText("null"));
                if (tntJson.has("kills_tntag") && tntJson.has("deaths_tntag")) {
                    tntag.append(new PlainText(" | KDR:"));
                    tntag.append(new PlainText(decimalFormat.format(
                            (double) tntJson.get("kills_tntag").getAsInt() /
                                    (double) tntJson.get("deaths_tntag").getAsInt())));
                }


                MessageChainBuilder tntrun = new MessageChainBuilder();
                tntrun.append(new PlainText("TNT Run: "));

                if (tntJson.has("record_tntrun")) {
                    tntrun.append(new PlainText("\n最佳时间(秒): "));
                    tntrun.append(new PlainText(String.valueOf(tntJson.get("record_tntrun").getAsInt())));
                }

                tntrun.append(new PlainText("\n胜场: "));
                if (tntJson.has("wins_tntrun")) {
                    tntrun.append(new PlainText(String.valueOf(tntJson.get("wins_tntrun").getAsInt())));
                } else tntrun.append(new PlainText("null"));
                tntrun.append(new PlainText(" | 死亡: "));
                if (tntJson.has("deaths_tntrun")) {
                    tntrun.append(new PlainText(String.valueOf(tntJson.get("deaths_tntrun").getAsInt())));
                } else tntrun.append(new PlainText("null"));
                if (tntJson.has("wins_tntrun") && tntJson.has("deaths_tntrun")) {
                    tntrun.append(new PlainText(" | WLR: "));
                    tntrun.append(new PlainText(decimalFormat.format(
                            (double) tntJson.get("wins_tntrun").getAsInt() /
                                    (double) tntJson.get("deaths_tntrun").getAsInt())));
                }


                MessageChainBuilder pvprun = new MessageChainBuilder();
                pvprun.append(new PlainText("PVP TNT Run: "));

                if (tntJson.has("record_pvprun")) {
                    pvprun.append(new PlainText("\n最佳时间(秒): "));
                    pvprun.append(new PlainText(String.valueOf(tntJson.get("record_pvprun").getAsInt())));
                }

                pvprun.append(new PlainText("\n胜场: "));
                if (tntJson.has("wins_pvprun")) {
                    pvprun.append(new PlainText(String.valueOf(tntJson.get("wins_pvprun").getAsInt())));
                    if (tntJson.has("deaths_pvprun")) {
                        pvprun.append(new PlainText(" | WLR: "));
                        pvprun.append(new PlainText(decimalFormat.format(
                                (double) tntJson.get("wins_pvprun").getAsInt() /
                                        (double) tntJson.get("deaths_pvprun").getAsInt())));
                    }
                } else pvprun.append(new PlainText("null"));

                pvprun.append(new PlainText("\n击杀: "));
                if (tntJson.has("kills_pvprun")) {
                    pvprun.append(new PlainText(String.valueOf(tntJson.get("kills_pvprun").getAsInt())));
                } else pvprun.append(new PlainText("null"));
                pvprun.append(new PlainText(" | 死亡: "));
                if (tntJson.has("deaths_pvprun")) {
                    pvprun.append(new PlainText(String.valueOf(tntJson.get("deaths_pvprun").getAsInt())));
                } else pvprun.append(new PlainText("null"));
                if (tntJson.has("kills_pvprun") && tntJson.has("deaths_pvprun")) {
                    pvprun.append(new PlainText(" | KDR: "));
                    pvprun.append(new PlainText(decimalFormat.format(
                            (double) tntJson.get("kills_pvprun").getAsInt() /
                                    (double) tntJson.get("deaths_pvprun").getAsInt())));
                }


                MessageChainBuilder bowspleef = new MessageChainBuilder();
                bowspleef.append(new PlainText("Bow Spleef: "));
                bowspleef.append(new PlainText("\n胜场: "));
                if (tntJson.has("wins_bowspleef")) {
                    bowspleef.append(new PlainText(String.valueOf(tntJson.get("wins_bowspleef").getAsInt())));
                } else bowspleef.append(new PlainText("null"));
                bowspleef.append(new PlainText(" | 死亡: "));
                if (tntJson.has("deaths_bowspleef")) {
                    bowspleef.append(new PlainText(String.valueOf(tntJson.get("deaths_bowspleef").getAsInt())));
                } else bowspleef.append(new PlainText("null"));
                if (tntJson.has("wins_bowspleef") && tntJson.has("deaths_bowspleef")) {
                    bowspleef.append(new PlainText(" | WLR: "));
                    bowspleef.append(new PlainText(decimalFormat.format(
                            (double) tntJson.get("wins_bowspleef").getAsInt() /
                                    (double) tntJson.get("deaths_bowspleef").getAsInt())));
                }
                if (tntJson.has("tags_bowspleef")) {
                    bowspleef.append(new PlainText("\n射箭数: "));
                    bowspleef.append(new PlainText(String.valueOf(tntJson.get("tags_bowspleef").getAsInt())));
                }

                ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
                builder.add(group.getBot().getId(), group.getBot().getNick(), chain.build());
                builder.add(group.getBot().getId(), group.getBot().getNick(), tntag.build());
                builder.add(group.getBot().getId(), group.getBot().getNick(), tntrun.build());
                builder.add(group.getBot().getId(), group.getBot().getNick(), pvprun.build());
                builder.add(group.getBot().getId(), group.getBot().getNick(), bowspleef.build());
                group.sendMessage(builder.build());
            } else {
                chain.append(MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]")).append(new PlainText(" 该玩家的TNT游戏数据为空"));
                group.sendMessage(chain.build());
            }
        }
    }
}
