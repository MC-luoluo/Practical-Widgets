package xyz.jxmm.minecraft.mm;

import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.DecimalFormat;

import static xyz.jxmm.minecraft.mm.MurderMysteryDetermine.*;

public class MurderMystery {

    public static void mm(JsonObject json, Long sender, Group group, String type) {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject playerJson;
        JsonObject mmJson;
        //JsonObject quests;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (json.get("player").isJsonObject()) {
            playerJson = json.get("player").getAsJsonObject();

            if (playerJson.has("stats") && playerJson.get("stats").getAsJsonObject().has("MurderMystery")) {
                mmJson = playerJson.get("stats").getAsJsonObject().get("MurderMystery").getAsJsonObject();

                chain.append(new PlainText("\n" + Nick.nick(playerJson) + " "));
                chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
                chain.append(new PlainText(" | 密室杀手 数据如下:"));

                //硬币
                chain.append(new PlainText("\n硬币: "));
                if (coins(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("coins").getAsInt())));
                } else chain.append(new PlainText("null"));

                //捡起金锭
                chain.append(new PlainText(" | 捡起金锭: "));
                if (coins_pickedup(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("coins_pickedup").getAsInt())));
                } else chain.append(new PlainText("null"));

                //游戏次数
                chain.append(new PlainText("\n游戏次数: "));
                if (games(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (wins(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (games(mmJson) && wins(mmJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins").getAsInt() /
                                    (mmJson.get("games").getAsInt() -
                                            mmJson.get("wins").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //侦探胜场
                if (detective_wins(mmJson) && games(mmJson)) {
                    chain.append(new PlainText("\n侦探胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("detective_wins").getAsInt())));
                    if (mmJson.has("quickest_detective_win_time_seconds")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_detective_win_time_seconds").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //杀手胜场
                if (murderer_wins(mmJson) && games(mmJson)) {
                    chain.append(new PlainText("\n杀手胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("murderer_wins").getAsInt())));
                    if (mmJson.has("quickest_murderer_win_time_seconds")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_murderer_win_time_seconds").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //英雄胜场
                if (was_hero(mmJson) && games(mmJson)) {
                    chain.append(new PlainText("\n英雄胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("was_hero").getAsInt())));
                }

                //总击杀
                chain.append(new PlainText("\n最终击杀: "));
                if (kills(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总死亡
                chain.append(new PlainText(" | 最终死亡: "));
                if (deaths(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (kills(mmJson) && deaths(mmJson)) {
                    chain.append(new PlainText(" | FKDR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills").getAsInt() /
                                    (float) mmJson.get("deaths").getAsInt()
                    )));
                }

                //总弓箭击杀
                chain.append(new PlainText("\n弓箭击杀: "));
                if (bow_kills(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (knife_kills(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总飞刀击杀
                chain.append(new PlainText("\n飞刀击杀: "));
                if (thrown_knife_kills(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (trap_kills(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills").getAsInt())));
                } else chain.append(new PlainText("null"));

                //周胜场
                /*不能正确获取
                chain.append(new PlainText("\n周胜场: "));
                if (quests(playerJson)){
                    quests = playerJson.get("quests").getAsJsonObject();
                    if (mm_weekly_wins(quests)){
                        JsonObject mm_weekly_wins = quests.get("mm_weekly_wins").getAsJsonObject();
                        if (active(mm_weekly_wins)){
                            JsonObject active = mm_weekly_wins.get("active").getAsJsonObject();
                            if (objectives(active)){
                                JsonObject objectives = active.get("objectives").getAsJsonObject();
                                if (weekly_win(objectives)){
                                    chain.append(new PlainText(String.valueOf(objectives.get("mm_weekly_win").getAsInt())));
                                } else chain.append(new PlainText("null"));
                            } else chain.append(new PlainText("null"));
                        } else chain.append(new PlainText("null"));
                    } else chain.append(new PlainText("null"));
                } else chain.append(new PlainText("null"));

                //周击杀
                chain.append(new PlainText(" | 周击杀: "));
                if (quests(playerJson)){
                    quests = playerJson.get("quests").getAsJsonObject();
                    if (mm_weekly_murderer_kills(quests)){
                        JsonObject mm_weekly_murderer_kills = quests.get("mm_weekly_murderer_kills").getAsJsonObject();
                        if (active(mm_weekly_murderer_kills)){
                            JsonObject active = mm_weekly_murderer_kills.get("active").getAsJsonObject();
                            if (objectives(active)){
                                JsonObject objectives = active.get("objectives").getAsJsonObject();
                                if (weekly_kill(objectives)){
                                    chain.append(new PlainText(String.valueOf(objectives.get("mm_weekly_kills_as_murderer").getAsInt())));
                                } else chain.append(new PlainText("null"));
                            } else chain.append(new PlainText("null"));
                        } else chain.append(new PlainText("null"));
                    } else chain.append(new PlainText("null"));
                } else chain.append(new PlainText("null"));*/


                //经典模式
                chain.append(new PlainText("\n\n经典模式: "));

                //场次
                chain.append(new PlainText("\n  游戏次数: "));
                if (games_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场"));
                if (wins_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (games_MURDER_CLASSIC(mmJson) && wins_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_CLASSIC").getAsInt() /
                                    (mmJson.get("games_MURDER_CLASSIC").getAsInt() -
                                            mmJson.get("wins_MURDER_CLASSIC").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //侦探胜场
                if (detective_wins_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText("\n  侦探胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("detective_wins_MURDER_CLASSIC").getAsInt())));
                    if (mmJson.has("quickest_detective_win_time_seconds_MURDER_CLASSIC")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_detective_win_time_seconds_MURDER_CLASSIC").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //杀手胜场
                if (murderer_wins_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText("\n  杀手胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("murderer_wins_MURDER_CLASSIC").getAsInt())));
                    if (mmJson.has("quickest_murderer_win_time_seconds_MURDER_CLASSIC")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_murderer_win_time_seconds_MURDER_CLASSIC").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //英雄胜场
                if (was_hero_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText("\n  英雄胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("was_hero_MURDER_CLASSIC").getAsInt())));
                }

                //经典模式击杀
                chain.append(new PlainText("\n  击杀: "));
                if (kills_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //经典模式死亡
                chain.append(new PlainText(" | 死亡: "));
                if (deaths_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //经典模式KD
                chain.append(new PlainText(" | KD: "));
                if (kills_MURDER_CLASSIC(mmJson) && deaths_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_CLASSIC").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_CLASSIC").getAsInt()
                    )));
                }

                //弓箭击杀
                chain.append(new PlainText("\n  弓箭击杀: "));
                if (bow_kills_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (knife_kills_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //飞刀击杀
                chain.append(new PlainText(" \n  飞刀击杀: "));
                if (thrown_knife_kills_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));

                //陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (trap_kills_MURDER_CLASSIC(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills_MURDER_CLASSIC").getAsInt())));
                } else chain.append(new PlainText("null"));


                //双倍模式
                chain.append(new PlainText("\n\n双倍模式: "));

                //场次
                chain.append(new PlainText("\n  游戏次数: "));
                if (games_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (wins_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (games_MURDER_DOUBLE_UP(mmJson) && wins_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_DOUBLE_UP").getAsInt() /
                                    (mmJson.get("games_MURDER_DOUBLE_UP").getAsInt() -
                                            mmJson.get("wins_MURDER_DOUBLE_UP").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //侦探胜场
                if (detective_wins_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText("\n  侦探胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("detective_wins_MURDER_DOUBLE_UP").getAsInt())));
                    if (mmJson.has("quickest_detective_win_time_seconds_MURDER_DOUBLE_UP")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_detective_win_time_seconds_MURDER_DOUBLE_UP").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //杀手胜场
                if (murderer_wins_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText("\n  杀手胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("murderer_wins_MURDER_DOUBLE_UP").getAsInt())));
                    if (mmJson.has("quickest_murderer_win_time_seconds_MURDER_DOUBLE_UP")) {
                        chain.append(new PlainText(" | 最快胜利: "));
                        int t = mmJson.get("quickest_murderer_win_time_seconds_MURDER_DOUBLE_UP").getAsInt();
                        chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                    }
                }

                //英雄胜场
                if (was_hero_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText("\n  英雄胜场: "));
                    chain.append(new PlainText(String.valueOf(mmJson.get("was_hero_MURDER_DOUBLE_UP").getAsInt())));
                }

                //双倍模式击杀
                chain.append(new PlainText("\n  击杀: "));
                if (kills_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //双倍模式死亡
                chain.append(new PlainText(" | 死亡: "));
                if (deaths_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //双倍模式KD
                chain.append(new PlainText(" | KD: "));
                if (kills_MURDER_DOUBLE_UP(mmJson) && deaths_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_DOUBLE_UP").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_DOUBLE_UP").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                //弓箭击杀
                chain.append(new PlainText("\n  弓箭击杀: "));
                if (bow_kills_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (knife_kills_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //飞刀击杀
                chain.append(new PlainText("\n  飞刀击杀: "));
                if (thrown_knife_kills_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));

                //陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (trap_kills_MURDER_DOUBLE_UP(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills_MURDER_DOUBLE_UP").getAsInt())));
                } else chain.append(new PlainText("null"));


                //感染模式
                chain.append(new PlainText("\n\n感染模式: "));

                chain.append(new PlainText("\n  总生存时间: "));
                if (mmJson.has("total_time_survived_seconds")) {
                    int t = mmJson.get("total_time_survived_seconds").getAsInt();
                    if (t >= 86400) {
                        chain.append(new PlainText(t / 86400 + "天" +
                                String.format("%02d", t % 86400 / 3600) + ":" +
                                String.format("%02d", t % 3600 / 60) + ":" +
                                String.format("%02d", t % 60)));
                    } else {
                        chain.append(new PlainText(String.format("%02d", t % 86400 / 3600) + ":" +
                                String.format("%02d", t % 3600 / 60) + ":" +
                                String.format("%02d", t % 60)));
                    }
                } else chain.append(new PlainText("null"));
                if (mmJson.has("longest_time_as_survivor_seconds")) {
                    chain.append(new PlainText(" | 幸存者最长生存时间: "));
                    int t = mmJson.get("longest_time_as_survivor_seconds").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                //场次
                chain.append(new PlainText("\n  场次: "));
                if (games_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (wins_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (games_MURDER_INFECTION(mmJson) && wins_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(" | WLR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_INFECTION").getAsInt() /
                                    (mmJson.get("games_MURDER_INFECTION").getAsInt() -
                                            mmJson.get("wins_MURDER_INFECTION").getAsInt())
                    )));
                }

                //击杀
                chain.append(new PlainText("\n  最终击杀: "));
                if (kills_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //死亡
                chain.append(new PlainText(" | 最终死亡: "));
                if (deaths_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //KD
                if (kills_MURDER_INFECTION(mmJson) && deaths_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(" | FKDR: "));
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_INFECTION").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_INFECTION").getAsInt()
                    )));
                }

                //幸存者击杀
                chain.append(new PlainText("\n  幸存者击杀: "));
                if (kills_as_survivor_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_as_survivor_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));

                //感染者击杀
                chain.append(new PlainText(" | 感染者击杀: "));
                if (kills_as_infected_MURDER_INFECTION(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_as_infected_MURDER_INFECTION").getAsInt())));
                } else chain.append(new PlainText("null"));


                //刺客模式
                chain.append(new PlainText("\n\n刺客模式: "));

                //场次
                chain.append(new PlainText("\n  场次: "));
                if (games_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("games_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //胜场
                chain.append(new PlainText(" | 胜场: "));
                if (wins_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("wins_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | WLR: "));
                if (games_MURDER_ASSASSINS(mmJson) && wins_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("wins_MURDER_ASSASSINS").getAsInt() /
                                    (mmJson.get("games_MURDER_ASSASSINS").getAsInt() -
                                            mmJson.get("wins_MURDER_ASSASSINS").getAsInt())
                    )));
                } else chain.append(new PlainText("null"));

                //刺客击杀
                chain.append(new PlainText("\n  击杀: "));
                if (kills_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //刺客死亡
                chain.append(new PlainText(" | 死亡: "));
                if (deaths_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("deaths_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //刺客KD
                chain.append(new PlainText(" | KD: "));
                if (kills_MURDER_ASSASSINS(mmJson) && deaths_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) mmJson.get("kills_MURDER_ASSASSINS").getAsInt() /
                                    (float) mmJson.get("deaths_MURDER_ASSASSINS").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                //弓箭击杀
                chain.append(new PlainText("\n  弓箭击杀: "));
                if (bow_kills_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("bow_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //匕首击杀
                chain.append(new PlainText(" | 匕首击杀: "));
                if (knife_kills_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("knife_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //飞刀击杀
                chain.append(new PlainText("\n  飞刀击杀: "));
                if (thrown_knife_kills_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("thrown_knife_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));

                //陷阱击杀
                chain.append(new PlainText(" | 陷阱击杀: "));
                if (trap_kills_MURDER_ASSASSINS(mmJson)) {
                    chain.append(new PlainText(String.valueOf(mmJson.get("trap_kills_MURDER_ASSASSINS").getAsInt())));
                } else chain.append(new PlainText("null"));


                ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
                builder.add(group.getBot().getId(), group.getBot().getNick(), chain.build());
                group.sendMessage(builder.build());

            } else {
                chain.append(MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]")).append(new PlainText(" 该玩家的密室杀手数据为空"));
                group.sendMessage(chain.build());

            }

        }
    }
}
