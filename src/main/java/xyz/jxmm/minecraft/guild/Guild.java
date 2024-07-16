package xyz.jxmm.minecraft.guild;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.Nick;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static xyz.jxmm.minecraft.MJURLConnect.moJangURLConnect;
import static xyz.jxmm.minecraft.guild.GuildDetermine.determine;

public class Guild {

    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static void common(String msg, Long sender, Group group, MessageChainBuilder chain, String type) {
        JsonObject json;
        String name = null;

        if (msg.endsWith(" members")) {
            type = "members";
            msg = msg.replaceAll(" members", "");
        }

        if (msg.startsWith("player ")) { //玩家ID
            msg = msg.replaceAll("player ", "");
            name = msg;
            json = new Gson().fromJson(Tool.main(msg, group, chain, "player"), JsonObject.class);
        } else if (msg.startsWith("name ")) { //公会name
            msg = msg.replaceAll("name ", "");
            json = new Gson().fromJson(Tool.guild(msg, "name"), JsonObject.class);
        } else if (msg.startsWith("id ")) { //公会name
            msg = msg.replaceAll("id ", "");
            json = new Gson().fromJson(Tool.guild(msg, "id"), JsonObject.class);
        } else { //默认 玩家ID
            name = msg;
            json = new Gson().fromJson(Tool.main(msg, group, chain, "player"), JsonObject.class);
        }

        guild(json, sender, group, name, type);

    }

    public static void guild(JsonObject json, Long sender, Group group, String name, String type) {
        MessageChainBuilder chain = new MessageChainBuilder();
        MessageChainBuilder achievementChain = new MessageChainBuilder();
        MessageChainBuilder membersChain = new MessageChainBuilder();
        MessageChainBuilder preferredGames = new MessageChainBuilder();
        MessageChainBuilder membersList = new MessageChainBuilder();
        MessageChainBuilder membersList2 = new MessageChainBuilder();
        MessageChainBuilder gameExp = new MessageChainBuilder();


        JsonArray members;
        JsonObject achievements = new JsonObject();

        if (json.get("guild").isJsonNull()) {
            chain.append(MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]"));
            if (name != null) {
                chain.append(new PlainText(" 该玩家未加入公会"));
            } else {
                chain.append(new PlainText(" 不存在此公会"));
            }
            group.sendMessage(chain.build());
        } else {
            json = json.get("guild").getAsJsonObject();
            members = json.get("members").getAsJsonArray();
            if (json.has("achievements")) achievements = json.get("achievements").getAsJsonObject();

            chain.append(new PlainText("公会名称: "));
            chain.append(new PlainText(json.get("name").getAsString()));

            //标签 & 颜色
            if (json.has("tag")) {
                //chain.append(new PlainText("\n标签: "));
                chain.append(new PlainText(" [" + json.get("tag").getAsString() + "]"));
                if (json.has("tagColor")) {
                    chain.append(new PlainText("(" + Nick.color(json.get("tagColor").getAsString()) + ")"));
                }
            }

            chain.append(new PlainText("\n公会id: "));
            chain.append(new PlainText(json.get("_id").getAsString()));

            chain.append(new PlainText("\n会长: "));
            for (int i = 0; i < members.size(); i++) {
                if (members.get(i).getAsJsonObject().get("rank").getAsString().equals("Guild Master") || members.get(i).getAsJsonObject().get("rank").getAsString().equals("GUILDMASTER")) {
                    chain.append(new PlainText(moJangURLConnect(members.get(i).getAsJsonObject().get("uuid").getAsString(), "uuid")));
                    break;
                }
            }

            chain.append(new PlainText("\n成员数量: "));
            chain.append(new PlainText(members.size() + "/125"));

            chain.append(new PlainText("\n公会创建时间: "));
            long created = json.get("created").getAsLong();
            Instant instant = Instant.ofEpochMilli(created);
            LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            chain.append(new PlainText(localDateTime.toString()));

            //经验 & 等级
            int exp = json.get("exp").getAsInt();
            int[] expNeeded = {100000, 150000, 250000, 500000, 750000, 1000000, 1250000, 1500000, 2000000, 2500000, 2500000, 2500000, 2500000, 2500000, 3000000};

            chain.append(new PlainText("\n等级: "));
            int level = 0;
            for (int j : expNeeded) {
                if (exp >= j) {
                    exp -= j;
                    level++;
                } else break;
            }
            while (exp >= 3000000) {
                level++;
                exp -= 3000000;
            }
            chain.append(new PlainText(String.valueOf(level)));

            //chain.append(new PlainText("\n经验进度: "));

            int ex/* = json.get("exp").getAsInt()*/;
            //chain.append(new PlainText(formatExp(ex)));


            String target = "100K";
            exp = json.get("exp").getAsInt();
            int target1 = 100000;
            int index = 0;
            for (int j : expNeeded) {
                if (exp >= j) {
                    exp -= j;
                    target = Tool.exp(index);
                    target1 = expNeeded[index];
                    index++;
                } else break;
            }
            while (exp >= 3000000) {
                target = "3M";
                target1 = 3000000;
                exp -= 3000000;
            }
            chain.append(new PlainText(
                    " (" + decimalFormat.format((float) exp / (float) 1000000) +
                            "/" + target + " " +
                            decimalFormat.format((float) exp / (float) target1 * 100) +
                            "%)"));

            //描述
            if (json.has("description")) {
                chain.append(new PlainText("\n描述: "));
                try {
                    chain.append(new PlainText(json.get("description").getAsString()));
                } catch (UnsupportedOperationException e) {
                    chain.append(new PlainText("null"));
                }
            }


            //公会成就
            achievementChain.append(new PlainText("公会成就:\n"));

            achievementChain.append(new PlainText("最高同时在线: "));
            achievementChain.append(new PlainText(String.valueOf(achievements.get("ONLINE_PLAYERS").getAsInt())));

            achievementChain.append(new PlainText("\n每日最高经验: "));
            if (determine(achievements, "EXPERIENCE_KINGS")) {
                ex = achievements.get("EXPERIENCE_KINGS").getAsInt();
                achievementChain.append(new PlainText(formatExp(ex)));
            } else achievementChain.append(new PlainText("null"));

            achievementChain.append(new PlainText("\n公会胜场数: "));
            if (determine(achievements, "WINNERS")) {
                achievementChain.append(new PlainText(String.valueOf(achievements.get("WINNERS").getAsInt())));
            } else achievementChain.append(new PlainText("null"));


            achievementChain.append(new PlainText("\n公会每周经验: "));
            Set<String> set = new HashSet<>();
            int sum = 0;

            //总计
            for (int i = 0; i < members.size(); i++) {
                JsonObject expHistory = new JsonObject();
                if (determine(members.get(i).getAsJsonObject(), "expHistory")) {
                    expHistory = members.get(i).getAsJsonObject().get("expHistory").getAsJsonObject();
                }
                set = expHistory.keySet();
                for (String j : set) {
                    sum += expHistory.get(j).getAsInt();
                }
            }
            achievementChain.append(new PlainText(formatExp(sum)));
            int weekExp = sum; //周 平均每位成员经验

            //周每日
            for (String s : set) {
                achievementChain.append(new PlainText("\n    " + s + ": "));
                sum = 0;
                for (int i = 0; i < members.size(); i++) {
                    JsonObject expHistory;
                    if (determine(members.get(i).getAsJsonObject(), "expHistory")) {
                        expHistory = members.get(i).getAsJsonObject().get("expHistory").getAsJsonObject();
                    } else continue;
                    sum += expHistory.get(s).getAsInt();
                }
                achievementChain.append(new PlainText(formatExp(sum)));
            }


            achievementChain.append(new PlainText("\n平均每位成员经验:"));
            //今日
            achievementChain.append(new PlainText("\n今日: "));
            if (determine(achievements, "EXPERIENCE_KINGS")) {
                achievementChain.append(new PlainText(decimalFormat.format(
                        (float) achievements.get("EXPERIENCE_KINGS").getAsInt() /
                                (float) members.size())
                ));
            }
            //一周
            achievementChain.append(new PlainText(" | 一周: "));
            achievementChain.append(new PlainText(decimalFormat.format(
                    (float) weekExp /
                            (float) members.size())
            ));

            if (name != null) {
                //membersChain.append(new PlainText("name 查询无玩家信息!"));
                //} else {
                String uuid = moJangURLConnect(name, "name");

                membersChain.append(new PlainText("成员: "));
                membersChain.append(new PlainText(moJangURLConnect(uuid, "uuid")));


                for (int i = 0; i < members.size(); i++) {
                    JsonObject member = members.get(i).getAsJsonObject();
                    if (member.get("uuid").getAsString().equals(uuid)) {
                        //rank
                        membersChain.append(new PlainText("\n成员rank: "));
                        membersChain.append(member.get("rank").getAsString());

                        //加入时间
                        membersChain.append(new PlainText("\n加入时间: "));
                        instant = Instant.ofEpochMilli(member.get("joined").getAsLong());
                        LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                        membersChain.append(new PlainText(String.valueOf(localDate)));

                        //任务
                        if (determine(member, "questParticipation")) {
                            membersChain.append(new PlainText("\n完成任务: "));
                            membersChain.append(new PlainText(String.valueOf(member.get("questParticipation").getAsInt())));
                        }

                        //经验
                        if (determine(member, "expHistory")) {
                            membersChain.append(new PlainText("\n个人每周经验: "));
                            sum = 0;
                            JsonObject expHistory = member.get("expHistory").getAsJsonObject();
                            for (String s : set) {
                                sum += expHistory.get(s).getAsInt();
                            }
                            membersChain.append(new PlainText(formatExp(sum)));

                            for (String s : set) {
                                membersChain.append(new PlainText("\n    " + s + ": "));
                                membersChain.append(new PlainText(formatExp(expHistory.get(s).getAsInt())));

                                //排名
                                membersChain.append(new PlainText(" #"));
                                int e = expHistory.get(s).getAsInt();
                                int x = 1;
                                for (int m = 0; m < members.size(); m++) {
                                    if (e < members.get(m).getAsJsonObject().get("expHistory").getAsJsonObject().get(s).getAsInt()) {
                                        x++;
                                    }
                                }
                                membersChain.append(new PlainText(String.valueOf(x)));
                                membersChain.append(new PlainText("/"));
                                membersChain.append(new PlainText(String.valueOf(members.size())));
                            }
                        }
                        break;
                    }
                }


            }

            if (Objects.equals(type, "all") && json.has("preferredGames")) {
                preferredGames.append(new PlainText("公会主打游戏: \n"));
                preferredGames.append(new PlainText(String.valueOf(json.get("preferredGames").getAsJsonArray())));
            }

            if (Objects.equals(type, "all") && json.has("guildExpByGameType")) {
                JsonObject expType = json.get("guildExpByGameType").getAsJsonObject();
                gameExp.append(new PlainText("各游戏经验: "));
                if (expType.has("BEDWARS")) {
                    gameExp.append(new PlainText("\n起床战争: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("BEDWARS").getAsInt())));
                }
                if (expType.has("SKYWARS")) {
                    gameExp.append(new PlainText("\n空岛战争: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("SKYWARS").getAsInt())));
                }
                if (expType.has("DUELS")) {
                    gameExp.append(new PlainText("\n决斗游戏: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("DUELS").getAsInt())));
                }
                if (expType.has("ARCADE")) {
                    gameExp.append(new PlainText("\n街机游戏: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("ARCADE").getAsInt())));
                }
                if (expType.has("BUILD_BATTLE")) {
                    gameExp.append(new PlainText("\n建筑大师: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("BUILD_BATTLE").getAsInt())));
                }
                if (expType.has("MURDER_MYSTERY")) {
                    gameExp.append(new PlainText("\n密室杀手: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("MURDER_MYSTERY").getAsInt())));
                }
                if (expType.has("TNTGAMES")) {
                    gameExp.append(new PlainText("\n掘战游戏: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("TNTGAMES").getAsInt())));
                }
                if (expType.has("SURVIVAL_GAMES")) {
                    gameExp.append(new PlainText("\n饥饿游戏: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("SURVIVAL_GAMES").getAsInt())));
                }
                if (expType.has("PROTOTYPE")) {
                    gameExp.append(new PlainText("\n实验游戏: "));
                    gameExp.append(new PlainText(String.valueOf(expType.get("PROTOTYPE").getAsInt())));
                }
            }

            if (Objects.equals(type, "members") || Objects.equals(type, "all")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日-HH时mm分ss秒", Locale.CHINA);
                membersList.append(new PlainText("成员列表: "));
                for (int x = 0; x < members.size(); x++) {
                    if (x <= 60) {
                        membersList.append(new PlainText("\n\n玩家: "));
                        membersList.append(new PlainText(moJangURLConnect(members.get(x).getAsJsonObject().get("uuid").getAsString(), "uuid")));
                        membersList.append(new PlainText("\nrank: "));
                        membersList.append(new PlainText(members.get(x).getAsJsonObject().get("rank").getAsString()));
                        membersList.append(new PlainText("\n加入时间: "));
                        long t = members.get(x).getAsJsonObject().get("joined").getAsLong();
                        membersList.append(new PlainText(simpleDateFormat.format(new Date(t))));
                        if (members.get(x).getAsJsonObject().has("questParticipation")) {
                            membersList.append(new PlainText("\n完成任务: "));
                            membersList.append(new PlainText(String.valueOf(members.get(x).getAsJsonObject().get("questParticipation").getAsInt())));
                        }
                        membersList.append(new PlainText("\n周经验: "));
                        JsonObject expHistory = new JsonObject();
                        if (determine(members.get(x).getAsJsonObject(), "expHistory")) {
                            expHistory = members.get(x).getAsJsonObject().get("expHistory").getAsJsonObject();
                        }
                        set = expHistory.keySet();
                        sum = 0;
                        for (String j : set) {
                            sum += expHistory.get(j).getAsInt();
                        }
                        membersList.append(new PlainText(formatExp(sum)));
                    } else {
                        membersList2.append(new PlainText("\n\n玩家: "));
                        membersList2.append(new PlainText(moJangURLConnect(members.get(x).getAsJsonObject().get("uuid").getAsString(), "uuid")));
                        membersList2.append(new PlainText("\nrank: "));
                        membersList2.append(new PlainText(members.get(x).getAsJsonObject().get("rank").getAsString()));
                        membersList2.append(new PlainText("\n加入时间: "));
                        long t = members.get(x).getAsJsonObject().get("joined").getAsLong();
                        membersList2.append(new PlainText(simpleDateFormat.format(new Date(t))));
                        if (members.get(x).getAsJsonObject().has("questParticipation")) {
                            membersList2.append(new PlainText("\n完成任务: "));
                            membersList2.append(new PlainText(String.valueOf(members.get(x).getAsJsonObject().get("questParticipation").getAsInt())));
                        }
                        membersList2.append(new PlainText("\n周经验: "));
                        JsonObject expHistory = new JsonObject();
                        if (determine(members.get(x).getAsJsonObject(), "expHistory")) {
                            expHistory = members.get(x).getAsJsonObject().get("expHistory").getAsJsonObject();
                        }
                        set = expHistory.keySet();
                        sum = 0;
                        for (String j : set) {
                            sum += expHistory.get(j).getAsInt();
                        }
                        membersList2.append(new PlainText(formatExp(sum)));
                    }
                }
            }

            ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
            builder.add(group.getBot().getId(), group.getBot().getNick(), chain.build());
            builder.add(group.getBot().getId(), group.getBot().getNick(), achievementChain.build());
            if (!membersChain.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), membersChain.build());
            }
            if (!preferredGames.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), preferredGames.build());
            }
            if (!gameExp.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), gameExp.build());
            }
            if (!membersList.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), membersList.build());
            }
            if (!membersList2.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), membersList2.build());
            }
            group.sendMessage(builder.build());
        }

    }

    //格式化经验值
    public static String formatExp(int ex) {
        if (ex >= 100000 & ex < 1000000) {
            return decimalFormat.format((float) ex / 1000) + "K";
        } else if (ex > 1000000) {
            return decimalFormat.format((float) ex / 1000000) + "M";
        } else {
            return String.valueOf(ex);
        }
    }
}
