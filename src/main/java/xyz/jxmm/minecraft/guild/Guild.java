package xyz.jxmm.minecraft.guild;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
        if (msg.endsWith(" games")) {
            type = "games";
            msg = msg.replaceAll(" games", "");
        }
        if (msg.endsWith(" ranks")) {
            type = "ranks";
            msg = msg.replaceAll(" ranks", "");
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
        MessageChainBuilder membersList3 = new MessageChainBuilder();
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
            long exp = json.get("exp").getAsLong();
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

            //经验进度
            int xpLevel = Math.min(level, 14);
            chain.append(new PlainText(" (" + formatExp(exp) +
                    "/" + Tool.exp(xpLevel) + " " +
                    decimalFormat.format((float) exp / expNeeded[xpLevel] * 100) + "%)"
            ));

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
                achievementChain.append(new PlainText(formatExp(achievements.get("EXPERIENCE_KINGS").getAsInt())));
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


            Iterator<String> iterator = set.iterator();
            String date = iterator.next();
            int daySum = 0;
            for (int i = 0; i < members.size(); i++) {
                JsonObject expHistory;
                if (determine(members.get(i).getAsJsonObject(), "expHistory")) {
                    expHistory = members.get(i).getAsJsonObject().get("expHistory").getAsJsonObject();
                } else continue;
                daySum += expHistory.get(date).getAsInt();
            }
            achievementChain.append(new PlainText("\n平均每位成员经验:"));
            achievementChain.append(new PlainText("\n今日: "));
            achievementChain.append(new PlainText(formatExp((float) daySum / members.size())));

            //一周
            achievementChain.append(new PlainText(" | 一周: "));
            achievementChain.append(new PlainText(formatExp((float) weekExp / members.size())));

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

            boolean game = Objects.equals(type, "games") || Objects.equals(type, "all");
            if (game && json.has("preferredGames")) {
                preferredGames.append(new PlainText("公会主打游戏: \n"));
                //preferredGames.append(new PlainText(String.valueOf(json.get("preferredGames").getAsJsonArray())));
                JsonArray perGames = json.get("preferredGames").getAsJsonArray();
                for (JsonElement s : perGames) {
                    preferredGames.append(new PlainText(" " + s));
                }
            }

            if (game && json.has("guildExpByGameType")) {
                JsonObject expType = json.get("guildExpByGameType").getAsJsonObject();
                gameExp.append(new PlainText("各游戏经验: "));
                if (expType.has("BEDWARS")) {
                    gameExp.append(new PlainText("\n起床战争: "));
                    gameExp.append(new PlainText(formatExp(expType.get("BEDWARS").getAsFloat())));
                }
                if (expType.has("SKYWARS")) {
                    gameExp.append(new PlainText("\n空岛战争: "));
                    gameExp.append(new PlainText(formatExp(expType.get("SKYWARS").getAsFloat())));
                }
                if (expType.has("DUELS")) {
                    gameExp.append(new PlainText("\n决斗游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("DUELS").getAsFloat())));
                }
                if (expType.has("ARCADE")) {
                    gameExp.append(new PlainText("\n街机游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("ARCADE").getAsFloat())));
                }
                if (expType.has("BUILD_BATTLE")) {
                    gameExp.append(new PlainText("\n建筑大师: "));
                    gameExp.append(new PlainText(formatExp(expType.get("BUILD_BATTLE").getAsFloat())));
                }
                if (expType.has("MURDER_MYSTERY")) {
                    gameExp.append(new PlainText("\n密室杀手: "));
                    gameExp.append(new PlainText(formatExp(expType.get("MURDER_MYSTERY").getAsFloat())));
                }
                if (expType.has("TNTGAMES")) {
                    gameExp.append(new PlainText("\n掘战游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("TNTGAMES").getAsFloat())));
                }
                if (expType.has("PROTOTYPE")) {
                    gameExp.append(new PlainText("\n实验游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("PROTOTYPE").getAsFloat())));
                }
                if (expType.has("HOUSING")) {
                    gameExp.append(new PlainText("\n家园: "));
                    gameExp.append(new PlainText(formatExp(expType.get("HOUSING").getAsFloat())));
                }
                if (expType.has("SURVIVAL_GAMES")) {
                    gameExp.append(new PlainText("\n闪电饥饿游戏: "));
                    gameExp.append(new PlainText(formatExp(expType.get("SURVIVAL_GAMES").getAsFloat())));
                }
                if (expType.has("WOOL_GAMES")) {
                    gameExp.append(new PlainText("\n羊毛战争: "));
                    gameExp.append(new PlainText(formatExp(expType.get("WOOL_GAMES").getAsFloat())));
                }
                if (expType.has("PIT")) {
                    gameExp.append(new PlainText("\n天坑乱斗: "));
                    gameExp.append(new PlainText(formatExp(expType.get("PIT").getAsFloat())));
                }
                if (expType.has("BATTLEGROUND")) {
                    gameExp.append(new PlainText("\n战争领主: "));
                    gameExp.append(new PlainText(formatExp(expType.get("BATTLEGROUND").getAsFloat())));
                }
                if (expType.has("SUPER_SMASH")) {
                    gameExp.append(new PlainText("\n星碎英雄: "));
                    gameExp.append(new PlainText(formatExp(expType.get("SUPER_SMASH").getAsFloat())));
                }
                if (expType.has("MCGO")) {
                    gameExp.append(new PlainText("\n警匪大战: "));
                    gameExp.append(new PlainText(formatExp(expType.get("MCGO").getAsFloat())));
                }
                if (expType.has("WALLS3")) {
                    gameExp.append(new PlainText("\n超级战墙: "));
                    gameExp.append(new PlainText(formatExp(expType.get("WALLS3").getAsFloat())));
                }
                if (expType.has("UHC")) {
                    gameExp.append(new PlainText("\nUHC: "));
                    gameExp.append(new PlainText(formatExp(expType.get("UHC").getAsFloat())));
                }
                if (expType.has("QUAKECRAFT")
                        || expType.has("ARENA")
                        || expType.has("WALLS")
                        || expType.has("VAMPIREZ")
                        || expType.has("GINGERBREAD")
                        || expType.has("PAINTBALL")) {
                    gameExp.append(new PlainText("\n经典游戏: "));
                    float legacySum = 0.0f;
                    if (expType.has("QUAKECRAFT")) legacySum += expType.get("QUAKECRAFT").getAsFloat();
                    if (expType.has("ARENA")) legacySum += expType.get("ARENA").getAsFloat();
                    if (expType.has("WALLS")) legacySum += expType.get("WALLS").getAsFloat();
                    if (expType.has("VAMPIREZ")) legacySum += expType.get("VAMPIREZ").getAsFloat();
                    if (expType.has("GINGERBREAD")) legacySum += expType.get("GINGERBREAD").getAsFloat();
                    if (expType.has("PAINTBALL")) legacySum += expType.get("PAINTBALL").getAsFloat();
                    gameExp.append(new PlainText(formatExp(legacySum)));
                }
            }

            if (Objects.equals(type, "members") || Objects.equals(type, "all")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日-HH时mm分ss秒", Locale.CHINA);
                membersList.append(new PlainText("成员列表: "));
                for (int x = 0; x < members.size(); x++) {
                    if (x <= 42) {
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
                    } else if (x <= 84) {
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
                    }else {
                        membersList3.append(new PlainText("\n\n玩家: "));
                        membersList3.append(new PlainText(moJangURLConnect(members.get(x).getAsJsonObject().get("uuid").getAsString(), "uuid")));
                        membersList3.append(new PlainText("\nrank: "));
                        membersList3.append(new PlainText(members.get(x).getAsJsonObject().get("rank").getAsString()));
                        membersList3.append(new PlainText("\n加入时间: "));
                        long t = members.get(x).getAsJsonObject().get("joined").getAsLong();
                        membersList3.append(new PlainText(simpleDateFormat.format(new Date(t))));
                        if (members.get(x).getAsJsonObject().has("questParticipation")) {
                            membersList3.append(new PlainText("\n完成任务: "));
                            membersList3.append(new PlainText(String.valueOf(members.get(x).getAsJsonObject().get("questParticipation").getAsInt())));
                        }
                        membersList3.append(new PlainText("\n周经验: "));
                        JsonObject expHistory = new JsonObject();
                        if (determine(members.get(x).getAsJsonObject(), "expHistory")) {
                            expHistory = members.get(x).getAsJsonObject().get("expHistory").getAsJsonObject();
                        }
                        set = expHistory.keySet();
                        sum = 0;
                        for (String j : set) {
                            sum += expHistory.get(j).getAsInt();
                        }
                        membersList3.append(new PlainText(formatExp(sum)));
                    }
                }
            }

            ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
            //
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
            if (!membersList3.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), membersList3.build());
            }
            group.sendMessage(builder.build());
        }

    }

    //格式化经验值
    public static String formatExp(float ex) {
        if (ex >= 100000 & ex < 1000000) {
            return decimalFormat.format(ex / 1000) + "K";
        } else if (ex > 1000000 & ex < 1000000000) {
            return decimalFormat.format(ex / 1000000) + "M";
        } else if (ex > 1000000000) {
            return decimalFormat.format(ex / 1000000000) + "B";
        } else {
            return String.valueOf((int) ex);
        }
    }
}
