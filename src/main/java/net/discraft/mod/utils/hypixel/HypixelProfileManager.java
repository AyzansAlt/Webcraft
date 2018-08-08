package net.discraft.mod.utils.hypixel;

import com.google.gson.JsonObject;
import net.discraft.mod.Discraft;
import net.discraft.mod.utils.EnumHypixelRank;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;

import java.util.HashMap;
import java.util.UUID;

public class HypixelProfileManager {

    public static HashMap<String, HypixelProfile> hypixelProfiles = new HashMap<String, HypixelProfile>();

    public static HypixelProfile getProfileFromUUID(String givenUUID){

        if(hypixelProfiles.containsKey(givenUUID)){
            return hypixelProfiles.get(givenUUID);
        } else if(Discraft.getInstance().hypixelVariables.apiUUID != null) {

            if(Discraft.getInstance().hypixelVariables.timeOut <= 0) {

                Discraft.getInstance().hypixelVariables.timeOut = 10;

                new Thread() {

                    public void run() {

                        HypixelAPI.getInstance().setApiKey(Discraft.getInstance().hypixelVariables.apiUUID);
                        Request request = RequestBuilder.newBuilder(RequestType.PLAYER)
                                .addParam(RequestParam.PLAYER_BY_UUID, UUID.fromString(givenUUID))
                                .createRequest();
                        HypixelAPI.getInstance().getAsync(request, (Callback<PlayerReply>) (failCause, result) -> {

                            if (failCause != null) {

                                failCause.printStackTrace();

                            } else {

                                HypixelProfile hypixelProfile = new HypixelProfile();

                                if(result.getPlayer() != null) {

                                    if(result.getPlayer().get("_id") != null) {
                                    hypixelProfile.hypixelProfileID = result.getPlayer().get("_id").getAsString();
                                    }

                                    if(result.getPlayer().get("playername") != null) {
                                    hypixelProfile.hypixelUsername = result.getPlayer().get("playername").getAsString();
                                    }

                                    if(result.getPlayer().get("networkExp") != null) {
                                    hypixelProfile.networkEXP = result.getPlayer().get("networkExp").getAsDouble();
                                    }

                                    if(result.getPlayer().get("lastLogin") != null) {
                                    hypixelProfile.lastLogin = result.getPlayer().get("lastLogin").getAsLong();
                                    }

                                    if(result.getPlayer().get("firstLogin") != null) {
                                        hypixelProfile.firstLogin = result.getPlayer().get("firstLogin").getAsLong();
                                    }

                                    if(result.getPlayer().get("karma") != null) {
                                        hypixelProfile.karma = result.getPlayer().get("karma").getAsLong();
                                    }

                                    if(result.getPlayer().get("displayname") != null) {
                                        hypixelProfile.hypixelDisplayName = result.getPlayer().get("displayname").getAsString();
                                    }

                                    if(result.getPlayer().getAsJsonObject("settings") != null) {
                                        if(result.getPlayer().getAsJsonObject("settings").get("allowFriendRequests") != null) {
                                            hypixelProfile.hypixelProfileSettings.allowFriendRequests = result.getPlayer().getAsJsonObject("settings").get("allowFriendRequests").getAsBoolean();
                                        }
                                        if(result.getPlayer().getAsJsonObject("settings").get("legacyCompass") != null) {
                                            hypixelProfile.hypixelProfileSettings.legacyCompass = result.getPlayer().getAsJsonObject("settings").get("legacyCompass").getAsBoolean();
                                        }
                                        if(result.getPlayer().getAsJsonObject("settings").get("allowGuildRequests") != null) {
                                            hypixelProfile.hypixelProfileSettings.allowGuildRequests = result.getPlayer().getAsJsonObject("settings").get("allowGuildRequests").getAsBoolean();
                                        }
                                    }
                                } else {
                                    hypixelProfile.isBot = true;
                                    hypixelProfile.hypixelProfileID = "BOT";
                                    hypixelProfile.hypixelUsername = "BOT";
                                }

                                hypixelProfiles.put(givenUUID, hypixelProfile);
                                Discraft.getInstance().getLogger().printLine("Discraft","Cached Hypixel Profile for " + hypixelProfile.hypixelUsername + "!");

                            }
                            HypixelAPI.getInstance().finish();
                        });
                    }

                }.start();
            }

            return null;

        }

        return null;

    }

    public static void clearCache() {
        hypixelProfiles.clear();
    }
}
