package net.discraft.mod.module.hypixel.utils;

import net.discraft.mod.Discraft;
import net.discraft.mod.module.hypixel.Module_Hypixel;
import net.discraft.mod.module.hypixel.api.HypixelAPI;
import net.discraft.mod.module.hypixel.api.reply.PlayerReply;
import net.discraft.mod.module.hypixel.api.request.Request;
import net.discraft.mod.module.hypixel.api.request.RequestBuilder;
import net.discraft.mod.module.hypixel.api.request.RequestParam;
import net.discraft.mod.module.hypixel.api.request.RequestType;
import net.discraft.mod.module.hypixel.api.util.Callback;
import net.discraft.mod.module.hypixel.utils.profileobjects.HypixelProfile;

import java.util.HashMap;
import java.util.UUID;

public class HypixelProfileManager {

    public Module_Hypixel hypixelModule;
    public HashMap<String, HypixelProfile> hypixelProfiles = new HashMap<String, HypixelProfile>();

    public HypixelProfileManager(Module_Hypixel givenModule) {
        this.hypixelModule = givenModule;
    }

    public HypixelProfile getProfileFromUUID(String givenUUID) {

        if (hypixelProfiles.containsKey(givenUUID)) {
            return hypixelProfiles.get(givenUUID);
        } else if (hypixelModule.hypixelVariables.apiUUID != null) {

            if (hypixelModule.hypixelVariables.timeOut <= 0) {

                hypixelModule.hypixelVariables.timeOut = 10;

                new Thread() {

                    public void run() {

                        HypixelAPI.getInstance().setApiKey(hypixelModule.hypixelVariables.apiUUID);
                        Request request = RequestBuilder.newBuilder(RequestType.PLAYER)
                                .addParam(RequestParam.PLAYER_BY_UUID, UUID.fromString(givenUUID))
                                .createRequest();
                        HypixelAPI.getInstance().getAsync(request, (Callback<PlayerReply>) (failCause, result) -> {

                            if (failCause != null) {

                                failCause.printStackTrace();

                            } else {

                                HypixelProfile hypixelProfile = new HypixelProfile();

                                if (result.getPlayer() != null) {

                                    if (result.getPlayer().get("_id") != null) {
                                        hypixelProfile.hypixelProfileID = result.getPlayer().get("_id").getAsString();
                                    }

                                    if (result.getPlayer().get("playername") != null) {
                                        hypixelProfile.hypixelUsername = result.getPlayer().get("playername").getAsString();
                                    }

                                    if (result.getPlayer().get("networkExp") != null) {
                                        hypixelProfile.networkEXP = result.getPlayer().get("networkExp").getAsDouble();
                                    }

                                    if (result.getPlayer().get("lastLogin") != null) {
                                        hypixelProfile.lastLogin = result.getPlayer().get("lastLogin").getAsLong();
                                    }

                                    if (result.getPlayer().get("firstLogin") != null) {
                                        hypixelProfile.firstLogin = result.getPlayer().get("firstLogin").getAsLong();
                                    }

                                    if (result.getPlayer().get("karma") != null) {
                                        hypixelProfile.karma = result.getPlayer().get("karma").getAsLong();
                                    }

                                    if (result.getPlayer().get("displayname") != null) {
                                        hypixelProfile.hypixelDisplayName = result.getPlayer().get("displayname").getAsString();
                                    }

                                    if (result.getPlayer().getAsJsonObject("settings") != null) {
                                        if (result.getPlayer().getAsJsonObject("settings").get("allowFriendRequests") != null) {
                                            hypixelProfile.hypixelProfileSettings.allowFriendRequests = result.getPlayer().getAsJsonObject("settings").get("allowFriendRequests").getAsBoolean();
                                        }
                                        if (result.getPlayer().getAsJsonObject("settings").get("legacyCompass") != null) {
                                            hypixelProfile.hypixelProfileSettings.legacyCompass = result.getPlayer().getAsJsonObject("settings").get("legacyCompass").getAsBoolean();
                                        }
                                        if (result.getPlayer().getAsJsonObject("settings").get("allowGuildRequests") != null) {
                                            hypixelProfile.hypixelProfileSettings.allowGuildRequests = result.getPlayer().getAsJsonObject("settings").get("allowGuildRequests").getAsBoolean();
                                        }
                                    }
                                } else {
                                    hypixelProfile.isBot = true;
                                    hypixelProfile.hypixelProfileID = "BOT";
                                    hypixelProfile.hypixelUsername = "BOT";
                                }

                                hypixelProfiles.put(givenUUID, hypixelProfile);
                                Discraft.getInstance().getLogger().printLine("Discraft", "Cached Hypixel Profile for " + hypixelProfile.hypixelUsername + "!");

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

    public void clearCache() {
        hypixelProfiles.clear();
    }
}
