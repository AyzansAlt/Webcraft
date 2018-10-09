package net.discraft.mod.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.File;
import java.util.ArrayList;

public class DiscraftModule {

    public ArrayList<KeyBinding> keyBindings = new ArrayList<>();

    public String moduleID;
    public String moduleName;
    public String moduleDescription;
    public String moduleDescriptionLong;
    public String moduleAuthor;
    public ResourceLocation moduleLogo;

    public boolean isEnabled = false;

    public DiscraftModule(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        this.moduleID = givenModuleID;
        this.moduleName = givenModuleName;
        this.moduleAuthor = givenModuleAuthor;
        this.moduleLogo = givenModuleLogo;
        this.moduleDescription = givenModuleDescription;
        this.moduleDescriptionLong = givenModuleDescriptionLong;
    }

    public String getCommandPrefix() {
        return "Unknown";
    }

    public void execute(MinecraftServer givenServer, ICommandSender sender, String[] args) throws CommandException {
    }

    public String getCommandHelp(String givenHelp) {
        return "Invalid " + this.moduleName + " command! Try:\n" + ChatFormatting.GREEN + givenHelp + ChatFormatting.RESET;
    }

    public void initializeConfigurations() {
    }

    public void loadConfigurations() {
    }

    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    public void onRenderWorldLast(RenderWorldLastEvent event) {
    }

    public void onClientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
    }

    public void onChatReceiveEvent(ClientChatReceivedEvent event) {
    }

    public void onServerChatEvent(ServerChatEvent event, String givenFormatted, String givenUnformatted) {
    }

    public void onClientTick(TickEvent.ClientTickEvent event) {
    }

    public void onRenderIngameTitle(String givenTitle) {
    }

    public void onRenderOverlay(RenderGameOverlayEvent event) {
    }

    public void onModuleDisable() {
    }

    public void onWorldLoad(WorldEvent.Load event) {
    }

    public void addShutdownHook() {
    }

    public void onRenderBlockHighlightEvent(DrawBlockHighlightEvent event) {
    }

    public File getConfigFile() {
        return (getSettings() != null) ? getSettings().getModuleConfig() : null;
    }

    public ModuleSettings getSettings() {
        return null;
    }

    public void onRenderMainMenu(int width, int height, int mouseX, int mouseY, float partialTicks) {
    }

    public void onMouseEvent(MouseEvent event){
    }

    public void onEntityAttack(AttackEntityEvent event) {
    }
}
