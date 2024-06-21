package xyz.jxmm.minecraft.bw;

import com.google.gson.JsonObject;

public class BedWarsDetermine {
    public static Boolean games_played_bedwars(JsonObject bwJson){
        return bwJson.has("games_played_bedwars") && bwJson.has("Experience");
    }
}
