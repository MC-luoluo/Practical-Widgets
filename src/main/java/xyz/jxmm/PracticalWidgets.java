package xyz.jxmm;

import xyz.jxmm.data.*;
import xyz.jxmm.data.Object;
import xyz.jxmm.tools.*;
import xyz.jxmm.music.*;
import xyz.jxmm.config.Main;
import xyz.jxmm.NewObject.*;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.io.IOException;

public final class PracticalWidgets extends JavaPlugin {
    public static final PracticalWidgets INSTANCE = new PracticalWidgets();

    private PracticalWidgets() {
        super(new JvmPluginDescriptionBuilder("xyz.jxmm.Practical_Widgets", "0.3.2")
                .name("一点小功能")
                .author("靖暄")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("一点小功能  已加载!");//插件加载提示
        getLogger().info("当前功能: 舔狗日记, 今日人品, 今日人品排行榜, 点歌, new对象, Hyp信息查询");

        try {//数据库相关
            Data.main();
            JrrpTop.main();
            TimerTask.timerTask();
            Main.main();
            Object.main();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        EventChannel<Event> eventChannel = GlobalEventChannel.INSTANCE.parentScope(this);
        eventChannel.subscribeAlways(GroupMessageEvent.class, g -> {
            String msg = g.getMessage().contentToString();
            Long sender = g.getSender().getId();
            String userName = g.getSenderName();
            Group group = g.getGroup();

            if(msg.equals("/update")){
                MainExample.update(group);
            } else if (msg.equals("/reset jrrptop")) {
                xyz.jxmm.jrrp.ResetJrrpTop.reWrite(group);
            } else if (JrrpMap.JrrpMap(msg)){
                try {
                    xyz.jxmm.jrrp.Main.main(sender, userName, group);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (msg.equals("/注册")) {
                Register.main(sender, userName, group);
            } else if (msg.equals("/舔狗日记")) {
                xyz.jxmm.dog.Main.main(sender,userName, group);
            } else if (JrrpMap.JrrpTopMap(msg)) {
                xyz.jxmm.jrrp.jrrpTop.jrrpTop(group,sender);
            } else if(msg.startsWith("/点歌")){
                xyz.jxmm.music.Main.main(msg,group,sender);
            } else if(msg.startsWith("/new对象")){
                NewObject.newObject(sender, group);
            } else if (msg.startsWith("/hyp ")){
                xyz.jxmm.Minecraft.Hypixel.hypixel(msg,sender,group);
            }
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("一点小功能  已卸载!");
    }
}