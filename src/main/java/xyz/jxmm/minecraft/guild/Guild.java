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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static xyz.jxmm.minecraft.MJURLConnect.moJangURLConnect;
import static xyz.jxmm.minecraft.guild.GuildDetermine.determine;

public class Guild {

    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static void common(String msg, Long sender, Group group, MessageChainBuilder chain) {
        JsonObject json;
        String id = null;
        String type = "";

        if (msg.endsWith(" members")) {
            type = "members";
            msg = msg.replaceAll(" members", "");
        }

        if (msg.startsWith("player ")) { //玩家ID
            msg = msg.replaceAll("player ", "");
            id = msg;
            json = new Gson().fromJson(Tool.main(msg, group, chain, "player"), JsonObject.class);
        } else if (msg.startsWith("name ")) { //公会name
            msg = msg.replaceAll("name ", "");
            json = new Gson().fromJson(Tool.guild(msg, "name"), JsonObject.class);
        } else if (msg.startsWith("id ")) { //公会name
            msg = msg.replaceAll("id ", "");
            json = new Gson().fromJson(Tool.guild(msg, "id"), JsonObject.class);
        } else { //默认 玩家ID
            id = msg;
            json = new Gson().fromJson(Tool.main(msg, group, chain, "player"), JsonObject.class);
        }

        guild(json, sender, group, id, type);

    }

    public static void guild(JsonObject json, Long sender, Group group, String id, String type) {
        MessageChainBuilder chain = new MessageChainBuilder();
        MessageChainBuilder achievementChain = new MessageChainBuilder();
        MessageChainBuilder membersChain = new MessageChainBuilder();
        MessageChainBuilder membersList = new MessageChainBuilder();


        JsonArray members;
        JsonObject achievements = new JsonObject();

        if (json.get("guild").isJsonNull()) {
            chain.append(MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]")).append(new PlainText(" 玩家未加入公会(player) 或 不存在此公会(name)"));
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
                if (members.get(i).getAsJsonObject().get("rank").getAsString().equals("Guild Master")) {
                    chain.append(new PlainText(moJangURLConnect(members.get(i).getAsJsonObject().get("uuid").getAsString(), "uuid")));
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

            if (id != null) {
                //membersChain.append(new PlainText("name 查询无玩家信息!"));
                //} else {
                String uuid = moJangURLConnect(id, "name");

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

            if (Objects.equals(type, "members")) {
                membersList.append(new PlainText("成员列表: "));
                for (int x = 0; x < members.size(); x++) {
                    membersList.append(new PlainText("\n\n成员名称: "));
                    membersList.append(new PlainText(moJangURLConnect(members.get(x).getAsJsonObject().get("uuid").getAsString(), "uuid")));
                    membersList.append(new PlainText("\nrank: "));
                    membersList.append(new PlainText(members.get(x).getAsJsonObject().get("rank").getAsString()));
                    membersList.append(new PlainText("\n加入时间: "));
                    instant = Instant.ofEpochMilli(members.get(x).getAsJsonObject().get("joined").getAsLong());
                    LocalDateTime localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    membersList.append(new PlainText(String.valueOf(localDate)));
                    if (members.get(x).getAsJsonObject().has("questParticipation")) {
                        membersList.append(new PlainText("\n完成任务: "));
                        membersList.append(new PlainText(String.valueOf(members.get(x).getAsJsonObject().get("questParticipation").getAsInt())));
                    }
                }
            }

            ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
            builder.add(group.getBot().getId(), group.getBot().getNick(), chain.build());
            builder.add(group.getBot().getId(), group.getBot().getNick(), achievementChain.build());
            if (!membersChain.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), membersChain.build());
            }
            if (!membersList.isEmpty()) {
                builder.add(group.getBot().getId(), group.getBot().getNick(), membersList.build());
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
