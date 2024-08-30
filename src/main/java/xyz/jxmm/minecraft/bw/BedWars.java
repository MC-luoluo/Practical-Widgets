package xyz.jxmm.minecraft.bw;

import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.DecimalFormat;
import java.util.Objects;

public class BedWars {
    //起床战争解析
    public static void bw(JsonObject json, Long sender, Group group, String type) {
        MessageChainBuilder chain = new MessageChainBuilder();
        MessageChainBuilder eight_one = new MessageChainBuilder();
        MessageChainBuilder eight_two = new MessageChainBuilder();
        MessageChainBuilder four_three = new MessageChainBuilder();
        MessageChainBuilder four_four = new MessageChainBuilder();
        MessageChainBuilder two_four = new MessageChainBuilder();
        /*梦幻
        MessageChainBuilder rush = new MessageChainBuilder();
        MessageChainBuilder ultimate = new MessageChainBuilder();
        MessageChainBuilder lucky = new MessageChainBuilder();
        MessageChainBuilder voidless = new MessageChainBuilder();
        MessageChainBuilder armed = new MessageChainBuilder();
        MessageChainBuilder swap = new MessageChainBuilder();
        MessageChainBuilder castle = new MessageChainBuilder();*/
        if (!Objects.equals(type, "all")) {
            chain.append(MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]")).append(new PlainText(" \n"));
        }
        JsonObject playerJson = json.get("player").getAsJsonObject();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (playerJson.has("stats") && json.get("player").isJsonObject()) {

            chain.append(new PlainText(Nick.nick(playerJson) + " ")); //玩家名称前缀
            chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
            chain.append(new PlainText(" | 起床战争数据:"));

            if (json.get("player").getAsJsonObject().get("stats").getAsJsonObject().has("Bedwars")) {
                JsonObject bwJson = json.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("Bedwars").getAsJsonObject();

                if (bwJson.has("games_played_bedwars") && bwJson.has("Experience")) {
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
                }


                //Solo
                if (Objects.equals(type, "all")) {
                    eight_one.append(new PlainText("Solo"));
                    if (bwJson.has("eight_one_games_played_bedwars")) {
                        eight_one.append(new PlainText(" | 场次: "));
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_games_played_bedwars").getAsInt())));
                    }
                    eight_one.append(new PlainText("\n胜场: "));
                    if (bwJson.has("eight_one_wins_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_wins_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | 败场: "));
                    if (bwJson.has("eight_one_losses_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_losses_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | WLR: "));
                    if (bwJson.has("eight_one_wins_bedwars") && bwJson.has("eight_one_losses_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_one_wins_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_one_losses_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_one_wins_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_wins_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));

                    eight_one.append(new PlainText("\n摧毁床: "));
                    if (bwJson.has("eight_one_beds_broken_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_beds_broken_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | 被摧毁床: "));
                    if (bwJson.has("eight_one_beds_lost_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_beds_lost_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | BLR: "));
                    if (bwJson.has("eight_one_beds_broken_bedwars") && bwJson.has("eight_one_beds_lost_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_one_beds_broken_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_one_beds_lost_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_one_beds_broken_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_beds_broken_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));

                    eight_one.append(new PlainText("\n击杀: "));
                    if (bwJson.has("eight_one_kills_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_kills_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | 死亡: "));
                    if (bwJson.has("eight_one_deaths_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_deaths_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | KDR: "));
                    if (bwJson.has("eight_one_kills_bedwars") && bwJson.has("eight_one_deaths_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_one_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_one_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_one_kills_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_kills_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));

                    eight_one.append(new PlainText("\n最终击杀: "));
                    if (bwJson.has("eight_one_final_kills_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_final_kills_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | 最终死亡: "));
                    if (bwJson.has("eight_one_final_deaths_bedwars")) {
                        eight_one.append(new PlainText(String.valueOf(bwJson.get("eight_one_final_deaths_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));
                    eight_one.append(new PlainText(" | FKDR："));
                    if (bwJson.has("eight_one_final_kills_bedwars") && bwJson.has("eight_one_final_deaths_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_one_final_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_one_final_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_one_final_kills_bedwars")) {
                        eight_one.append(new PlainText(decimalFormat.format(bwJson.get("eight_one_final_kills_bedwars").getAsInt())));
                    } else eight_one.append(new PlainText("0"));

                    k = 0;
                    d = 0;
                    if (bwJson.has("eight_one_kills_bedwars") || bwJson.has("eight_one_final_kills_bedwars") ||
                            bwJson.has("eight_one_deaths_bedwars") || bwJson.has("eight_one_final_deaths_bedwars")) {
                        if (bwJson.has("eight_one_kills_bedwars")) {
                            k += bwJson.get("eight_one_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("eight_one_final_kills_bedwars")) {
                            k += bwJson.get("eight_one_final_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("eight_one_deaths_bedwars")) {
                            d += bwJson.get("eight_one_deaths_bedwars").getAsInt();
                        }
                        if (bwJson.has("eight_one_final_deaths_bedwars")) {
                            d += bwJson.get("eight_one_final_deaths_bedwars").getAsInt();
                        }
                        eight_one.append(new PlainText("\n总击杀: "));
                        eight_one.append(new PlainText(String.valueOf(k)));
                        eight_one.append(new PlainText(" | 总死亡: "));
                        eight_one.append(new PlainText(String.valueOf(d)));
                        eight_one.append(new PlainText(" | 总KD: "));
                        if (d != 0) {
                            eight_one.append(new PlainText(decimalFormat.format((double) k / d)));
                        } else eight_one.append(new PlainText(decimalFormat.format(k)));
                    }
                }


                //双倍
                if (Objects.equals(type, "all")) {
                    eight_two.append(new PlainText("Double"));
                    if (bwJson.has("eight_two_games_played_bedwars")) {
                        eight_two.append(new PlainText(" | 场次: "));
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_games_played_bedwars").getAsInt())));
                    }
                    eight_two.append(new PlainText("\n胜场: "));
                    if (bwJson.has("eight_two_wins_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_wins_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | 败场: "));
                    if (bwJson.has("eight_two_losses_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_losses_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | WLR: "));
                    if (bwJson.has("eight_two_wins_bedwars") && bwJson.has("eight_two_losses_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_two_wins_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_two_losses_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_two_wins_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_wins_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));

                    eight_two.append(new PlainText("\n摧毁床: "));
                    if (bwJson.has("eight_two_beds_broken_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_beds_broken_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | 被摧毁床: "));
                    if (bwJson.has("eight_two_beds_lost_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_beds_lost_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | BLR: "));
                    if (bwJson.has("eight_two_beds_broken_bedwars") && bwJson.has("eight_two_beds_lost_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_two_beds_broken_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_two_beds_lost_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_two_beds_broken_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_beds_broken_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));

                    eight_two.append(new PlainText("\n击杀: "));
                    if (bwJson.has("eight_two_kills_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_kills_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | 死亡: "));
                    if (bwJson.has("eight_two_deaths_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_deaths_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | KDR: "));
                    if (bwJson.has("eight_two_kills_bedwars") && bwJson.has("eight_two_deaths_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_two_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_two_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_two_kills_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_kills_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));

                    eight_two.append(new PlainText("\n最终击杀: "));
                    if (bwJson.has("eight_two_final_kills_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_final_kills_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | 最终死亡: "));
                    if (bwJson.has("eight_two_final_deaths_bedwars")) {
                        eight_two.append(new PlainText(String.valueOf(bwJson.get("eight_two_final_deaths_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));
                    eight_two.append(new PlainText(" | FKDR："));
                    if (bwJson.has("eight_two_final_kills_bedwars") && bwJson.has("eight_two_final_deaths_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("eight_two_final_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("eight_two_final_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("eight_two_final_kills_bedwars")) {
                        eight_two.append(new PlainText(decimalFormat.format(bwJson.get("eight_two_final_kills_bedwars").getAsInt())));
                    } else eight_two.append(new PlainText("0"));

                    k = 0;
                    d = 0;
                    if (bwJson.has("eight_two_kills_bedwars") || bwJson.has("eight_two_final_kills_bedwars") ||
                            bwJson.has("eight_two_deaths_bedwars") || bwJson.has("eight_two_final_deaths_bedwars")) {
                        if (bwJson.has("eight_two_kills_bedwars")) {
                            k += bwJson.get("eight_two_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("eight_two_final_kills_bedwars")) {
                            k += bwJson.get("eight_two_final_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("eight_two_deaths_bedwars")) {
                            d += bwJson.get("eight_two_deaths_bedwars").getAsInt();
                        }
                        if (bwJson.has("eight_two_final_deaths_bedwars")) {
                            d += bwJson.get("eight_two_final_deaths_bedwars").getAsInt();
                        }
                        eight_two.append(new PlainText("\n总击杀: "));
                        eight_two.append(new PlainText(String.valueOf(k)));
                        eight_two.append(new PlainText(" | 总死亡: "));
                        eight_two.append(new PlainText(String.valueOf(d)));
                        eight_two.append(new PlainText(" | 总KD: "));
                        if (d != 0) {
                            eight_two.append(new PlainText(decimalFormat.format((double) k / d)));
                        } else eight_two.append(new PlainText(decimalFormat.format(k)));
                    }
                }


                //3s
                if (Objects.equals(type, "all")) {
                    four_three.append(new PlainText("3v3v3v3"));
                    if (bwJson.has("four_three_games_played_bedwars")) {
                        four_three.append(new PlainText(" | 场次: "));
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_games_played_bedwars").getAsInt())));
                    }
                    four_three.append(new PlainText("\n胜场: "));
                    if (bwJson.has("four_three_wins_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_wins_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | 败场: "));
                    if (bwJson.has("four_three_losses_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_losses_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | WLR: "));
                    if (bwJson.has("four_three_wins_bedwars") && bwJson.has("four_three_losses_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_three_wins_bedwars").getAsInt() /
                                        (float) bwJson.get("four_three_losses_bedwars").getAsInt())));
                    } else if (bwJson.has("four_three_wins_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(bwJson.get("four_three_wins_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));

                    four_three.append(new PlainText("\n摧毁床: "));
                    if (bwJson.has("four_three_beds_broken_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_beds_broken_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | 被摧毁床: "));
                    if (bwJson.has("four_three_beds_lost_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_beds_lost_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | BLR: "));
                    if (bwJson.has("four_three_beds_broken_bedwars") && bwJson.has("four_three_beds_lost_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_three_beds_broken_bedwars").getAsInt() /
                                        (float) bwJson.get("four_three_beds_lost_bedwars").getAsInt())));
                    } else if (bwJson.has("four_three_beds_broken_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(bwJson.get("four_three_beds_broken_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));

                    four_three.append(new PlainText("\n击杀: "));
                    if (bwJson.has("four_three_kills_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_kills_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | 死亡: "));
                    if (bwJson.has("four_three_deaths_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_deaths_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | KDR: "));
                    if (bwJson.has("four_three_kills_bedwars") && bwJson.has("four_three_deaths_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_three_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("four_three_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("four_three_kills_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(bwJson.get("four_three_kills_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));

                    four_three.append(new PlainText("\n最终击杀: "));
                    if (bwJson.has("four_three_final_kills_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_final_kills_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | 最终死亡: "));
                    if (bwJson.has("four_three_final_deaths_bedwars")) {
                        four_three.append(new PlainText(String.valueOf(bwJson.get("four_three_final_deaths_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));
                    four_three.append(new PlainText(" | FKDR："));
                    if (bwJson.has("four_three_final_kills_bedwars") && bwJson.has("four_three_final_deaths_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_three_final_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("four_three_final_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("four_three_final_kills_bedwars")) {
                        four_three.append(new PlainText(decimalFormat.format(bwJson.get("four_three_final_kills_bedwars").getAsInt())));
                    } else four_three.append(new PlainText("0"));

                    k = 0;
                    d = 0;
                    if (bwJson.has("four_three_kills_bedwars") || bwJson.has("four_three_final_kills_bedwars") ||
                            bwJson.has("four_three_deaths_bedwars") || bwJson.has("four_three_final_deaths_bedwars")) {
                        if (bwJson.has("four_three_kills_bedwars")) {
                            k += bwJson.get("four_three_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("four_three_final_kills_bedwars")) {
                            k += bwJson.get("four_three_final_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("four_three_deaths_bedwars")) {
                            d += bwJson.get("four_three_deaths_bedwars").getAsInt();
                        }
                        if (bwJson.has("four_three_final_deaths_bedwars")) {
                            d += bwJson.get("four_three_final_deaths_bedwars").getAsInt();
                        }
                        four_three.append(new PlainText("\n总击杀: "));
                        four_three.append(new PlainText(String.valueOf(k)));
                        four_three.append(new PlainText(" | 总死亡: "));
                        four_three.append(new PlainText(String.valueOf(d)));
                        four_three.append(new PlainText(" | 总KD: "));
                        if (d != 0) {
                            four_three.append(new PlainText(decimalFormat.format((double) k / d)));
                        } else four_three.append(new PlainText(decimalFormat.format(k)));
                    }
                }


                //4s
                if (Objects.equals(type, "all")) {
                    four_four.append(new PlainText("4v4v4v4"));
                    if (bwJson.has("four_four_games_played_bedwars")) {
                        four_four.append(new PlainText(" | 场次: "));
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_games_played_bedwars").getAsInt())));
                    }
                    four_four.append(new PlainText("\n胜场: "));
                    if (bwJson.has("four_four_wins_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_wins_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | 败场: "));
                    if (bwJson.has("four_four_losses_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_losses_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | WLR: "));
                    if (bwJson.has("four_four_wins_bedwars") && bwJson.has("four_four_losses_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_four_wins_bedwars").getAsInt() /
                                        (float) bwJson.get("four_four_losses_bedwars").getAsInt())));
                    } else if (bwJson.has("four_four_wins_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(bwJson.get("four_four_wins_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));

                    four_four.append(new PlainText("\n摧毁床: "));
                    if (bwJson.has("four_four_beds_broken_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_beds_broken_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | 被摧毁床: "));
                    if (bwJson.has("four_four_beds_lost_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_beds_lost_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | BLR: "));
                    if (bwJson.has("four_four_beds_broken_bedwars") && bwJson.has("four_four_beds_lost_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_four_beds_broken_bedwars").getAsInt() /
                                        (float) bwJson.get("four_four_beds_lost_bedwars").getAsInt())));
                    } else if (bwJson.has("four_four_beds_broken_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(bwJson.get("four_four_beds_broken_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));

                    four_four.append(new PlainText("\n击杀: "));
                    if (bwJson.has("four_four_kills_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_kills_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | 死亡: "));
                    if (bwJson.has("four_four_deaths_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_deaths_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | KDR: "));
                    if (bwJson.has("four_four_kills_bedwars") && bwJson.has("four_four_deaths_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_four_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("four_four_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("four_four_kills_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(bwJson.get("four_four_kills_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));

                    four_four.append(new PlainText("\n最终击杀: "));
                    if (bwJson.has("four_four_final_kills_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_final_kills_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | 最终死亡: "));
                    if (bwJson.has("four_four_final_deaths_bedwars")) {
                        four_four.append(new PlainText(String.valueOf(bwJson.get("four_four_final_deaths_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));
                    four_four.append(new PlainText(" | FKDR："));
                    if (bwJson.has("four_four_final_kills_bedwars") && bwJson.has("four_four_final_deaths_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("four_four_final_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("four_four_final_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("four_four_final_kills_bedwars")) {
                        four_four.append(new PlainText(decimalFormat.format(bwJson.get("four_four_final_kills_bedwars").getAsInt())));
                    } else four_four.append(new PlainText("0"));

                    k = 0;
                    d = 0;
                    if (bwJson.has("four_four_kills_bedwars") || bwJson.has("four_four_final_kills_bedwars") ||
                            bwJson.has("four_four_deaths_bedwars") || bwJson.has("four_four_final_deaths_bedwars")) {
                        if (bwJson.has("four_four_kills_bedwars")) {
                            k += bwJson.get("four_four_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("four_four_final_kills_bedwars")) {
                            k += bwJson.get("four_four_final_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("four_four_deaths_bedwars")) {
                            d += bwJson.get("four_four_deaths_bedwars").getAsInt();
                        }
                        if (bwJson.has("four_four_final_deaths_bedwars")) {
                            d += bwJson.get("four_four_final_deaths_bedwars").getAsInt();
                        }
                        four_four.append(new PlainText("\n总击杀: "));
                        four_four.append(new PlainText(String.valueOf(k)));
                        four_four.append(new PlainText(" | 总死亡: "));
                        four_four.append(new PlainText(String.valueOf(d)));
                        four_four.append(new PlainText(" | 总KD: "));
                        if (d != 0) {
                            four_four.append(new PlainText(decimalFormat.format((double) k / d)));
                        } else four_four.append(new PlainText(decimalFormat.format(k)));
                    }
                }


                //4v4
                if (Objects.equals(type, "all")) {
                    two_four.append(new PlainText("4v4"));
                    if (bwJson.has("two_four_games_played_bedwars")) {
                        two_four.append(new PlainText(" | 场次: "));
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_games_played_bedwars").getAsInt())));
                    }
                    two_four.append(new PlainText("\n胜场: "));
                    if (bwJson.has("two_four_wins_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_wins_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | 败场: "));
                    if (bwJson.has("two_four_losses_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_losses_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | WLR: "));
                    if (bwJson.has("two_four_wins_bedwars") && bwJson.has("two_four_losses_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("two_four_wins_bedwars").getAsInt() /
                                        (float) bwJson.get("two_four_losses_bedwars").getAsInt())));
                    } else if (bwJson.has("two_four_wins_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(bwJson.get("two_four_wins_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));

                    two_four.append(new PlainText("\n摧毁床: "));
                    if (bwJson.has("two_four_beds_broken_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_beds_broken_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | 被摧毁床: "));
                    if (bwJson.has("two_four_beds_lost_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_beds_lost_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | BLR: "));
                    if (bwJson.has("two_four_beds_broken_bedwars") && bwJson.has("two_four_beds_lost_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("two_four_beds_broken_bedwars").getAsInt() /
                                        (float) bwJson.get("two_four_beds_lost_bedwars").getAsInt())));
                    } else if (bwJson.has("two_four_beds_broken_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(bwJson.get("two_four_beds_broken_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));

                    two_four.append(new PlainText("\n击杀: "));
                    if (bwJson.has("two_four_kills_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_kills_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | 死亡: "));
                    if (bwJson.has("two_four_deaths_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_deaths_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | KDR: "));
                    if (bwJson.has("two_four_kills_bedwars") && bwJson.has("two_four_deaths_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("two_four_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("two_four_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("two_four_kills_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(bwJson.get("two_four_kills_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));

                    two_four.append(new PlainText("\n最终击杀: "));
                    if (bwJson.has("two_four_final_kills_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_final_kills_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | 最终死亡: "));
                    if (bwJson.has("two_four_final_deaths_bedwars")) {
                        two_four.append(new PlainText(String.valueOf(bwJson.get("two_four_final_deaths_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));
                    two_four.append(new PlainText(" | FKDR："));
                    if (bwJson.has("two_four_final_kills_bedwars") && bwJson.has("two_four_final_deaths_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(
                                (float) bwJson.get("two_four_final_kills_bedwars").getAsInt() /
                                        (float) bwJson.get("two_four_final_deaths_bedwars").getAsInt())));
                    } else if (bwJson.has("two_four_final_kills_bedwars")) {
                        two_four.append(new PlainText(decimalFormat.format(bwJson.get("two_four_final_kills_bedwars").getAsInt())));
                    } else two_four.append(new PlainText("0"));

                    k = 0;
                    d = 0;
                    if (bwJson.has("two_four_kills_bedwars") || bwJson.has("two_four_final_kills_bedwars") ||
                            bwJson.has("two_four_deaths_bedwars") || bwJson.has("two_four_final_deaths_bedwars")) {
                        if (bwJson.has("two_four_kills_bedwars")) {
                            k += bwJson.get("two_four_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("two_four_final_kills_bedwars")) {
                            k += bwJson.get("two_four_final_kills_bedwars").getAsInt();
                        }
                        if (bwJson.has("two_four_deaths_bedwars")) {
                            d += bwJson.get("two_four_deaths_bedwars").getAsInt();
                        }
                        if (bwJson.has("two_four_final_deaths_bedwars")) {
                            d += bwJson.get("two_four_final_deaths_bedwars").getAsInt();
                        }
                        two_four.append(new PlainText("\n总击杀: "));
                        two_four.append(new PlainText(String.valueOf(k)));
                        two_four.append(new PlainText(" | 总死亡: "));
                        two_four.append(new PlainText(String.valueOf(d)));
                        two_four.append(new PlainText(" | 总KD: "));
                        if (d != 0) {
                            two_four.append(new PlainText(decimalFormat.format((double) k / d)));
                        } else two_four.append(new PlainText(decimalFormat.format(k)));
                    }
                }

                if (Objects.equals(type, "all")) {
                    ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
                    builder.add(group.getBot().getId(), group.getBot().getNick(), chain.build());
                    builder.add(group.getBot().getId(), group.getBot().getNick(), eight_one.build());
                    builder.add(group.getBot().getId(), group.getBot().getNick(), eight_two.build());
                    builder.add(group.getBot().getId(), group.getBot().getNick(), four_three.build());
                    builder.add(group.getBot().getId(), group.getBot().getNick(), four_four.build());
                    builder.add(group.getBot().getId(), group.getBot().getNick(), two_four.build());
                    group.sendMessage(builder.build());
                }else group.sendMessage(chain.build());

            } else {
                chain.append(new PlainText("该玩家的起床战争数据为空"));
                group.sendMessage(chain.build());
            }
        } else if (json.get("player").isJsonNull()) {
            chain.append(new PlainText("<playerID> 不存在"));
            group.sendMessage(chain.build());
        }


    }
}
