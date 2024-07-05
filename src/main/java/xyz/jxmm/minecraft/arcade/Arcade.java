package xyz.jxmm.minecraft.arcade;

import static xyz.jxmm.minecraft.arcade.ArcadeDetermine.*;

import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.DecimalFormat;

public class Arcade {
    public static void arc(JsonObject json, Long sender, Group group) {
        MessageChainBuilder chain = new MessageChainBuilder();
        JsonObject playerJson;
        JsonObject acdJson;
        JsonObject achievements;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (json.get("player").isJsonObject()) {
            playerJson = json.get("player").getAsJsonObject();
            achievements = playerJson.get("achievements").getAsJsonObject();

            if (playerJson.has("stats") && playerJson.get("stats").getAsJsonObject().has("Arcade")) {
                acdJson = playerJson.get("stats").getAsJsonObject().get("Arcade").getAsJsonObject();

                chain.append(new PlainText(Nick.nick(playerJson))); //玩家名称前缀
                chain.append(new PlainText(json.get("player").getAsJsonObject().get("displayname").getAsString()));
                chain.append(new PlainText(" | 街机游戏 数据如下："));

                //硬币
                chain.append(new PlainText("\n街机硬币: "));
                if (coins(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("coins").getAsInt())));
                } else chain.append(new PlainText("null"));

                //总胜利数
                chain.append(new PlainText(" | 总胜利数: "));
                if (arcade_arcade_winner(achievements)) {
                    chain.append(new PlainText(String.valueOf(achievements.get("arcade_arcade_winner").getAsInt())));
                } else chain.append(new PlainText("null"));

                //心跳水立方
                if (acdJson.has("dropper")) {
                    chain.append(new PlainText("\n心跳水立方: "));
                    JsonObject dropper = acdJson.get("dropper").getAsJsonObject();

                    chain.append(new PlainText("\n    最佳时间: "));
                    if (fastest_game(dropper)) {
                        double fg = dropper.get("fastest_game").getAsDouble() / 1000;
                        chain.append(new PlainText(fg >= 60 ?
                                ((int) fg / 60) + ":" + String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000)) :
                                String.format("%02d", (int) fg % 60) + ":" + String.format("%03d", Math.round((fg - (int) fg) * 1000))
                        ));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    游玩次数: "));
                    if (game_played(dropper)) {
                        chain.append(new PlainText(String.valueOf(dropper.get("games_played").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 胜场: "));
                    if (wins_dropper(dropper)) {
                        chain.append(new PlainText(String.valueOf(dropper.get("wins").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 无暇胜利: "));
                    if (flawless_games(dropper)) {
                        chain.append(new PlainText(String.valueOf(dropper.get("flawless_games").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    完成游戏: "));
                    if (games_finished(dropper)) {
                        chain.append(new PlainText(String.valueOf(dropper.get("games_finished").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 完成地图: "));
                    if (maps_completed(dropper)) {
                        chain.append(new PlainText(String.valueOf(dropper.get("maps_completed").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 死亡数: "));
                    if (fails_dropper(dropper)) {
                        chain.append(new PlainText(String.valueOf(dropper.get("fails").getAsInt())));
                    } else chain.append(new PlainText("null"));


                }

                //派对游戏
                chain.append(new PlainText("\n派对游戏: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_party(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_party").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 总胜利回合数: "));
                if (round_wins_party(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("round_wins_party").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 总计获得星星: "));
                if (total_stars_party(acdJson)) {
                    chain.append(new PlainText(acdJson.get("total_stars_party").getAsInt() + "⭐"));
                } else chain.append(new PlainText("null"));

                //行尸走肉
                chain.append(new PlainText("\n行尸走肉: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_dayone(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_dayone").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 击杀: "));
                if (kills_dayone(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kills_dayone").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 爆头数: "));
                if (headshots_dayone(acdJson)) {
                    chain.append(new PlainText(acdJson.get("headshots_dayone").getAsInt() + "💀"));
                } else chain.append(new PlainText("null"));

                //赏金猎人
                /*
                chain.append(new PlainText("\n赏金猎人: "));
                chain.append(new PlainText("null"));

                 */

                //Capture The Wool
                /*
                chain.append(new PlainText("\n捕捉羊毛: "));
                chain.append(new PlainText("null"));

                 */

                //进击的苦力怕
                chain.append(new PlainText("\n进击的苦力怕: "));
                chain.append(new PlainText("\n    最大波次: "));
                if (crreper_Attack(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("max_wave").getAsInt())));
                } else chain.append(new PlainText("null"));

                //龙之战
                /*
                chain.append(new PlainText("\n龙之战胜场: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 击杀: "));
                chain.append(new PlainText("null"));
                 */

                //末影掘战
                /*
                chain.append(new PlainText("\n末影掘战胜场: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 破坏方块数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 总计激活加成数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 激活大型射击加成数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 激活三重射击加成数: "));
                chain.append(new PlainText("null"));
                 */

                //农场躲猫猫
                chain.append(new PlainText("\n农场躲猫猫: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_farm_hunt(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 作为动物胜场: "));
                if (animal_wins_farm_hunt(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("animal_wins_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 作为猎人胜场: "));
                if (hunter_wins_farm_hunt(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hunter_wins_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    击杀: "));
                if (kills_farm_hunt(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kills_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 作为动物击杀数: "));
                if (animal_kills_farm_hunt(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("animal_kills_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 作为猎人击杀: "));
                if (hunter_kills_farm_hunt(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hunter_kills_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                /*
                chain.append(new PlainText("\n    弓箭击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 作为动物弓箭击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 作为猎人弓箭击杀: "));
                chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    嘲讽使用次数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 安全的嘲讽使用次数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 较危险的嘲讽使用次数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 危险的嘲讽使用次数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 烟花嘲讽使用次数: "));
                chain.append(new PlainText("null"));
                */

                chain.append(new PlainText("\n    便便收集数: "));
                if (poop_collected_farm_hunt(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("poop_collected_farm_hunt").getAsInt())));
                } else chain.append(new PlainText("null"));

                //足球
                chain.append(new PlainText("\n足球: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_soccer(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    得分: "));
                if (goals_soccer(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("goals_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    踢出数: "));
                if (powerkicks_soccer(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("powerkicks_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 传球数: "));
                if (kicks_soccer(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kicks_soccer").getAsInt())));
                } else chain.append(new PlainText("null"));

                //星际战争
                /* chain.append(new PlainText("\n星际战争胜场:  "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 死亡数: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 帝国军击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 反抗军击杀: "));
                chain.append(new PlainText("null"));
                chain.append(new PlainText(" | 射击次数: "));
                chain.append(new PlainText("null"));
                 */

                //躲猫猫
                chain.append(new PlainText("\n道具躲猫猫: "));
                chain.append(new PlainText("\n    躲藏者胜场: "));
                if (hider_wins_hide_and_seek(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hider_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 寻找者胜场: "));
                if (seeker_wins_hide_and_seek(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("seeker_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n派对躲猫猫: "));
                chain.append(new PlainText("\n    躲藏者胜场: "));
                if (party_pooper_hider_wins_hide_and_seek(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("party_pooper_hider_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 寻找者胜场: "));
                if (party_pooper_seeker_wins_hide_and_seek(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("party_pooper_seeker_wins_hide_and_seek").getAsInt())));
                } else chain.append(new PlainText("null"));

                //人体打印机
                chain.append(new PlainText("\n人体打印机: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_hole_in_the_wall(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_hole_in_the_wall").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 游玩回合数: "));
                if (rounds_hole_in_the_wall(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("rounds_hole_in_the_wall").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    最佳资格赛得分: "));
                if (hitw_record_q(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hitw_record_q").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 最佳决赛得分: "));
                if (hitw_record_f(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("hitw_record_f").getAsInt())));
                } else chain.append(new PlainText("null"));


                //我说你做
                chain.append(new PlainText("\n我说你做: "));
                chain.append(new PlainText("\n    我说你做胜场: "));
                if (wins_simon_says(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    我说你做分数: "));
                if (rounds_simon_says(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("rounds_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 最高分: "));
                if (top_score_simon_says(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("top_score_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    我说你做回合胜利数: "));
                if (round_wins_simon_says(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("round_wins_simon_says").getAsInt())));
                } else chain.append(new PlainText("null"));

                //迷你战墙
                chain.append(new PlainText("\n迷你战墙: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    最终击杀: "));
                if (final_kills_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("final_kills_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 击杀: "));
                if (kills_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kills_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 死亡数: "));
                if (deaths_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | KD: "));
                if (kills_mini_walls(acdJson) && deaths_mini_walls(acdJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) acdJson.get("kills_mini_walls").getAsInt() /
                                    (float) acdJson.get("deaths_mini_walls").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    箭矢命中数: "));
                if (arrows_hit_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("arrows_hit_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 箭矢射击数: "));
                if (arrows_shot_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("arrows_shot_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 命中率: "));
                if (arrows_hit_mini_walls(acdJson) && arrows_shot_mini_walls(acdJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) acdJson.get("arrows_hit_mini_walls").getAsInt() /
                                    (float) acdJson.get("arrows_shot_mini_walls").getAsInt()
                    )));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText("\n    凋零击杀: "));
                if (wither_kills_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wither_kills_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 凋零伤害量: "));
                if (wither_damage_mini_walls(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wither_damage_mini_walls").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 平均伤害: "));
                if (wither_kills_mini_walls(acdJson) && wither_damage_mini_walls(acdJson)) {
                    chain.append(new PlainText(decimalFormat.format(
                            (float) acdJson.get("wither_damage_mini_walls").getAsInt() /
                                    (float) acdJson.get("wither_kills_mini_walls").getAsInt()
                    )));
                }


                //像素画家
                chain.append(new PlainText("\n像素画家: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_pixel_painters(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_pixel_painters").getAsInt())));
                } else chain.append(new PlainText("null"));


                //Pixel party
                chain.append(new PlainText("\n像素派对: "));
                if (pixel_party(acdJson)) {
                    JsonObject pixel_party = acdJson.get("pixel_party").getAsJsonObject();

                    chain.append(new PlainText("\n    游戏场次: "));
                    if (games_played(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("games_played").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 完成回合数: "));
                    if (rounds_completed(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    胜场: "));
                    if (wins_pixel_party(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("wins").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    经典模式胜场: "));
                    if (wins_normal(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("wins_normal").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 经典模式回合完成数: "));
                    if (rounds_completed_normal(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed_normal").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    狂热模式胜场: "));
                    if (wins_hyper(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("wins_hyper").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText(" | 狂热模式回合完成数: "));
                    if (rounds_completed_hyper(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(pixel_party.get("rounds_completed_hyper").getAsInt())));
                    } else chain.append(new PlainText("null"));

                    chain.append(new PlainText("\n    加成收集数: "));
                    if (power_ups_collected(pixel_party) && power_ups_collected_normal(pixel_party) && power_ups_collected_hyper(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(
                                pixel_party.get("power_ups_collected").getAsInt()
                                        + pixel_party.get("power_ups_collected_normal").getAsInt()
                                        + pixel_party.get("power_ups_collected_hyper").getAsInt())));

                    } else if (power_ups_collected(pixel_party) && power_ups_collected_normal(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(
                                pixel_party.get("power_ups_collected").getAsInt()
                                        + pixel_party.get("power_ups_collected_normal").getAsInt())));

                    } else if (power_ups_collected(pixel_party) && power_ups_collected_hyper(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(
                                pixel_party.get("power_ups_collected").getAsInt()
                                        + pixel_party.get("power_ups_collected_hyper").getAsInt())));
                    } else if (power_ups_collected_normal(pixel_party) && power_ups_collected_hyper(pixel_party)) {
                        chain.append(new PlainText(String.valueOf(
                                pixel_party.get("power_ups_collected_normal").getAsInt()
                                        + pixel_party.get("power_ups_collected_hyper").getAsInt())));
                    } else chain.append(new PlainText("null"));


                } else chain.append(new PlainText("null"));


                //乱棍之战
                chain.append(new PlainText("\n乱棍之战: "));
                chain.append(new PlainText("\n    胜场: "));
                if (wins_throw_out(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_throw_out").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 击杀: "));
                if (kills_throw_out(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("kills_throw_out").getAsInt())));
                } else chain.append(new PlainText("null"));


                //僵尸末日
                chain.append(new PlainText("\n僵尸末日"));

                chain.append(new PlainText("\n    总生存回合: "));
                if (total_rounds_survived_zombies(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (best_round_zombies(acdJson)) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies").getAsInt())));
                }

                if (wins_zombies(acdJson)) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies").getAsInt())));
                }

                chain.append(new PlainText("\n  最速记录 10回合: "));
                if (acdJson.has("fastest_time_10_zombies")) {
                    int t = acdJson.get("fastest_time_10_zombies").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (zombie_kills_zombies(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (players_revived_zombies(acdJson)) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (times_knocked_down_zombies(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deaths_zombies(acdJson)) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (windows_repaired_zombies(acdJson)) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (doors_opened_zombies(acdJson)) {
                    chain.append(new PlainText(" | 开门: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies").getAsInt())));
                }

                //僵尸末日穷途末路
                chain.append(new PlainText("\n\n穷途末路: "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (deter(acdJson, "total_rounds_survived_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "best_round_zombies_deadend")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_deadend").getAsInt())));
                }

                if (deter(acdJson, "wins_zombies_deadend")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_deadend").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_deadend_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_deadend_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_deadend_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_deadend_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_deadend_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_deadend_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (deter(acdJson, "zombie_kills_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "players_revived_zombies_deadend")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_deadend").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (deter(acdJson, "times_knocked_down_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "deaths_zombies_deadend")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_deadend").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (deter(acdJson, "windows_repaired_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 开门: "));
                if (deter(acdJson, "doors_opened_zombies_deadend")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_deadend").getAsInt())));
                } else chain.append(new PlainText("null"));


                //僵尸末日坏血之宫
                chain.append(new PlainText("\n\n坏血之宫: "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (deter(acdJson, "total_rounds_survived_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "best_round_zombies_badblood")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_badblood").getAsInt())));
                }

                if (deter(acdJson, "wins_zombies_badblood")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_badblood").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_badblood_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_badblood_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_badblood_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_badblood_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_badblood_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_badblood_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (deter(acdJson, "zombie_kills_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "players_revived_zombies_badblood")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_badblood").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (deter(acdJson, "times_knocked_down_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "deaths_zombies_badblood")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_badblood").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (deter(acdJson, "windows_repaired_zombies_badblood")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_badblood").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "doors_opened_zombies_badblood")) {
                    chain.append(new PlainText(" | 开门: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_badblood").getAsInt())));
                }


                //僵尸末日外星游乐园
                chain.append(new PlainText("\n\n外星游乐园: "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (deter(acdJson, "total_rounds_survived_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "best_round_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_alienarcadium").getAsInt())));
                }

                if (deter(acdJson, "wins_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_alienarcadium_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_alienarcadium_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_alienarcadium_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_alienarcadium_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_alienarcadium_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_alienarcadium_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (deter(acdJson, "zombie_kills_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "players_revived_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (deter(acdJson, "times_knocked_down_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "deaths_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (deter(acdJson, "windows_repaired_zombies_alienarcadium")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_alienarcadium").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "doors_opened_zombies_alienarcadium")) {
                    chain.append(new PlainText(" | 开门: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_alienarcadium").getAsInt())));
                }

                chain.append(new PlainText("\n\n监狱(译名未定): "));

                chain.append(new PlainText("\n    总生存回合: "));
                if (deter(acdJson, "total_rounds_survived_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("total_rounds_survived_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "best_round_zombies_prison")) {
                    chain.append(new PlainText(" | 最佳回合: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("best_round_zombies_prison").getAsInt())));
                }

                if (deter(acdJson, "wins_zombies_prison")) {
                    chain.append(new PlainText(" | 胜场: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("wins_zombies_prison").getAsInt())));
                }

                chain.append(new PlainText("\n  普通最速 10回合: "));
                if (acdJson.has("fastest_time_10_zombies_prison_normal")) {
                    int t = acdJson.get("fastest_time_10_zombies_prison_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                } else chain.append(new PlainText("null"));
                if (acdJson.has("fastest_time_20_zombies_prison_normal")) {
                    chain.append(new PlainText(" | 20回合: "));
                    int t = acdJson.get("fastest_time_20_zombies_prison_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }
                if (acdJson.has("fastest_time_30_zombies_prison_normal")) {
                    chain.append(new PlainText(" | 30回合: "));
                    int t = acdJson.get("fastest_time_30_zombies_prison_normal").getAsInt();
                    chain.append(new PlainText(String.format("%02d", t / 60) + ":" + String.format("%02d", t % 60)));
                }

                chain.append(new PlainText("\n    击杀僵尸: "));
                if (deter(acdJson, "zombie_kills_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("zombie_kills_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "players_revived_zombies_prison")) {
                    chain.append(new PlainText(" | 救援玩家: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("players_revived_zombies_prison").getAsInt())));
                }

                chain.append(new PlainText("\n    被击倒: "));
                if (deter(acdJson, "times_knocked_down_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("times_knocked_down_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                if (deter(acdJson, "deaths_zombies_prison")) {
                    chain.append(new PlainText(" | 死亡: "));
                    chain.append(new PlainText(String.valueOf(acdJson.get("deaths_zombies_prison").getAsInt())));
                }

                chain.append(new PlainText("\n    修复窗户: "));
                if (deter(acdJson, "windows_repaired_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("windows_repaired_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));

                chain.append(new PlainText(" | 开门: "));
                if (deter(acdJson, "doors_opened_zombies_prison")) {
                    chain.append(new PlainText(String.valueOf(acdJson.get("doors_opened_zombies_prison").getAsInt())));
                } else chain.append(new PlainText("null"));


                ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
                builder.add(group.getBot().getId(), group.getBot().getNick(), chain.build());
                group.sendMessage(builder.build());

            } else {
                chain.append(MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]"));
                chain.append(new PlainText(" 无法获取" + json.get("player").getAsJsonObject().get("displayname").getAsString() + "的街机游戏数据"));
                group.sendMessage(chain.build());
            }


        }


    }
}
