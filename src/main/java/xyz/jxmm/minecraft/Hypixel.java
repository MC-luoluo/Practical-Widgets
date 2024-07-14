package xyz.jxmm.minecraft;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import xyz.jxmm.minecraft.bb.BuildBattle;
import xyz.jxmm.minecraft.arcade.Arcade;
import xyz.jxmm.minecraft.bw.BedWars;
import xyz.jxmm.minecraft.duels.Duels;
import xyz.jxmm.minecraft.guild.Guild;
import xyz.jxmm.minecraft.guild.Tool;
import xyz.jxmm.minecraft.mm.MurderMystery;
import xyz.jxmm.minecraft.player.Online;
import xyz.jxmm.minecraft.player.Player;
import xyz.jxmm.minecraft.sw.SkyWars;
import xyz.jxmm.minecraft.tnt.TNTGames;
import xyz.jxmm.minecraft.tourney.tournament;
import xyz.jxmm.minecraft.fish.Fish;
import xyz.jxmm.perm.Determine;

import static xyz.jxmm.minecraft.HypURLConnect.hypixelURLConnect;


public class Hypixel {

    public static void hypixel(String msg, Long sender, Group group) {
        //信息部分
        String handle = msg.toLowerCase().substring(3);
        MessageChain at = MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]");
        MessageChainBuilder chain = new MessageChainBuilder().append(at);

        //数据部分
        StringBuilder ID = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        JsonObject json;

        String type = "";
        if (handle.endsWith(" all")) {
            type = "all";
            handle = handle.replaceAll(" members", "");
        }

        if (handle.startsWith(" bw ")) {
            ID.append(handle.replaceAll(" bw ", ""));//得到玩家ID

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                BedWars.bw(json, sender, group);
            }

        } else if (handle.startsWith(" sw ")) {
            ID.append(handle.replaceAll(" sw ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                SkyWars.sw(json, sender, group);
            }


        } else if (handle.startsWith(" player ")) {
            ID.append(handle.replaceAll(" player ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                JsonObject guild = new Gson().fromJson(Tool.main(ID.toString(), group, chain, "player"), JsonObject.class);
                JsonObject online = new Gson().fromJson(Online.main(ID.toString(), group, chain), JsonObject.class);

                if (error(json, chain, group) && error(guild, chain, group) && error(online, chain, group)) {
                    Player.player(json, guild, online, sender, group);
                }
            }


        } else if (handle.startsWith(" arc")) {
            ID.append(handle.replaceAll(" arcade ", "").replaceAll(" arc ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                Arcade.arc(json, sender, group);
            }

        } else if (handle.startsWith(" mm ")) {
            ID.append(handle.replaceAll(" mm ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                MurderMystery.mm(json, sender, group, type);
            }

        } else if (handle.startsWith(" tnt ")) {
            ID.append(handle.replaceAll(" tnt ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                TNTGames.tnt(json, sender, group);
            }

        } else if (handle.startsWith(" duels ")) {
            ID.append(handle.replaceAll(" duels ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                Duels.duels(json, sender, group);
            }

        } else if (handle.startsWith(" tourney ")) {
            ID.append(handle.replaceAll(" tourney ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                tournament.tourney(json, sender, group);
            }

        } else if (handle.startsWith(" bb ")) {
            ID.append(handle.replaceAll(" bb ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);
            JsonObject leaders = new Gson().fromJson(Leaders.leaders(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                BuildBattle.bb(json, leaders, sender, group);
            }

        } else if (handle.startsWith(" guild ")) {
            ID.append(handle.replaceAll(" guild ", ""));
            Guild.common(ID.toString(), sender, group, chain);

        } else if (handle.startsWith(" fish ")) {
            ID.append(handle.replaceAll(" fish ", ""));

            stringBuilder.append(analysis(ID.toString(), group, chain));//将玩家信息写入stringBuilder
            json = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            if (!stringBuilder.toString().isEmpty()) {
                Fish.fish(json, sender, group);
            }

        } else {
            chain.append(new PlainText("指令不完整, 缺少关键字或关键字错误"));
            chain.append(new PlainText("\n" + xyz.jxmm.PracticalWidgets.perfix()));
            chain.append(new PlainText("hyp <type> <playerID>"));
            chain.append(new PlainText("\n<type>包含如下关键字:" +
                    "\nplayer 玩家系列数据" +
                    "\nbw 起床战争" +
                    "\nsw 空岛战争" +
                    "\nmm 密室杀手" +
                    "\narc 街机游戏" +
                    "\ntnt 掘战游戏" +
                    "\nduels 决斗游戏" +
                    "\nbb 建筑大师" +
                    "\nguild 公会数据" +
                    "\ntourney 锦标赛数据" +
                    "\nfish 大厅钓鱼"));
            chain.append(new PlainText("\n<playerID> ID & 16进制uuid & 带连接符16进制uuid"));
            /*
                <tnt>掘战游戏
                <ww>羊毛战争
             */
            group.sendMessage(chain.build());
        }


    }


    public static Boolean error(JsonObject json, MessageChainBuilder chain, Group group) {
        return Error.err(json, chain, group);
    }

    public static String analysis(String ID, Group group, MessageChainBuilder chain) {
        StringBuilder uuid = new StringBuilder();
        if (ID.length() < 32) {
            uuid.append(MJURLConnect.moJangURLConnect(ID, "name"));
            return mojangErr(uuid.toString(), group, chain);
        } else if (ID.length() == 32) {
            return mojangErr(ID, group, chain);
        } else if (ID.length() == 36) {
            return mojangErr(ID, group, chain);
        } else {
            chain.append(new PlainText("<playerID> 错误"));
            group.sendMessage(chain.build());
            return "";
        }


    }

    public static String mojangErr(String uuid, Group group, MessageChainBuilder chain) {
        switch (uuid) {
            case "":
                chain.append(new PlainText("NULL\n未知错误, 从MJ官方无法得到任何信息"));
                group.sendMessage(chain.build());
                return "";
            case "Connection timed out":
                chain.append(new PlainText("连接超时, 这可能是因为玩家不存在, 请检查您输入的玩家ID是否正确\n或者请检查您的网络状况"));
                group.sendMessage(chain.build());
                return "";
            case "FileNotFound":
                chain.append(new PlainText("玩家不存在, 请检查输入的玩家ID是否正确"));
                group.sendMessage(chain.build());
                return "";
            case "IO":
                chain.append(new PlainText("<playerID> 格式错误, 请输入正确的玩家ID"));
                group.sendMessage(chain.build());
                return "";
            case "reset":
                chain.append(new PlainText("链接已重置, 请稍后再试"));
                group.sendMessage(chain.build());
                return "";
            case "PKIX":
                chain.append(new PlainText("DNS劫持, 通常这是运营商问题, 请稍后再试"));
                group.sendMessage(chain.build());
                return "";
            default:
                //从hypixel官方API得到用户数据
                String stringBuilder = hypixelURLConnect(uuid);

                if (stringBuilder.equals("noHypixelAPI")) {
                    chain.append("请前往配置文件填写hypixelAPI后重试");

                    System.out.println("以下出现的报错如果是 NullPointerException, 这是正常现象，因为您未填写HypixelAPI, 如果不是请联系作者");
                    System.out.println("如果您在配置文件未找到HypixelAPI填写位置,请重启mirai后检查配置文件, 如果还是没有请联系作者");
                    group.sendMessage(chain.build());
                    return "";
                } else {
                    return stringBuilder;
                }
        }
    }

    public static void perm(String msg, Long sender, Group group) {
        if (Determine.main(sender, group, "hyp")) {
            hypixel(msg, sender, group);
        }
    }

}
