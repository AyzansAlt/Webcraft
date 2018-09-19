package net.discraft.mod.module.visualize;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.Discraft;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.visualize.utils.VisualizeSettings;
import net.discraft.mod.module.visualize.utils.shaderresources.ResourceManagerMotionBlur;
import net.discraft.mod.module.visualize.utils.shaderresources.ResourceMotionBlur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.Map;

public class Module_Visualize extends DiscraftModule {

    public KeyBinding keyTabToggle = new KeyBinding("key.discraft.visualize.tabtoggle", Keyboard.KEY_TAB, "key.discraft.category.module");

    public VisualizeSettings visualizeSettings = new VisualizeSettings();
    public ResourceMotionBlur blurResource;
    public Map resourceMapper;

    public Module_Visualize(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        super(givenModuleID, givenModuleName, givenModuleDescription, givenModuleDescriptionLong, givenModuleAuthor, givenModuleLogo);

        blurResource = new ResourceMotionBlur(this);

        this.keyBindings.add(keyTabToggle);

    }

    public static void applyShader() {
        if (Minecraft.getMinecraft().entityRenderer != null && Minecraft.getMinecraft().world != null) {
            Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        }
    }

    @Override
    public String getCommandPrefix() {
        return "visualize";
    }

    @Override
    public void execute(MinecraftServer givenServer, ICommandSender sender, String[] args) {

        if (args.length > 1) {
            switch (args[1]) {
                /* MOTION BLUR */
                case "motionblur":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.visualizeSettings.enableMotionBlur = true;
                                sender.sendMessage(new TextComponentString("Motion Blur has been " + ChatFormatting.GREEN + "Enabled" + ChatFormatting.RESET + "!"));
                                this.visualizeSettings.saveConfig();
                                break;
                            case "disable":
                                if (this.visualizeSettings.enableMotionBlur && Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
                                    Minecraft.getMinecraft().entityRenderer.stopUseShader();
                                }
                                this.visualizeSettings.enableMotionBlur = false;
                                sender.sendMessage(new TextComponentString("Motion Blur has been " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + "!"));
                                this.visualizeSettings.saveConfig();

                                break;
                            case "set":
                                if (args.length > 3) {
                                    double givenAmount = Double.valueOf(args[3]);

                                    int max = 10;

                                    if (givenAmount <= max) {
                                        this.visualizeSettings.motionBlurAmount = givenAmount;
                                        sender.sendMessage(new TextComponentString("Set blur amount to " + ChatFormatting.GREEN + givenAmount + ChatFormatting.RESET + "!"));
                                        this.visualizeSettings.saveConfig();
                                        Minecraft.getMinecraft().entityRenderer.stopUseShader();
                                    } else {
                                        sender.sendMessage(new TextComponentString("Cannot set motion blur to " + ChatFormatting.GREEN + givenAmount + ChatFormatting.RESET + "! (Higher than " + max + ")"));
                                    }
                                    break;
                                }
                                break;
                        }
                        return;
                    } else {
                        sender.sendMessage(new TextComponentString("Motion Blur is: " + (this.visualizeSettings.enableMotionBlur ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
                    /* TOGGLE-TAB */
                case "toggletab":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.visualizeSettings.enableTabToggle = true;
                                sender.sendMessage(new TextComponentString("Toggle-Tab has been " + ChatFormatting.GREEN + "Enabled" + ChatFormatting.RESET + "!"));
                                this.visualizeSettings.saveConfig();
                                break;
                            case "disable":
                                if (this.visualizeSettings.enableTabToggle && Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
                                    Minecraft.getMinecraft().entityRenderer.stopUseShader();
                                }
                                this.visualizeSettings.enableTabToggle = false;
                                sender.sendMessage(new TextComponentString("Toggle-Tab has been " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + "!"));
                                this.visualizeSettings.saveConfig();

                                break;
                        }
                        return;
                    } else {
                        sender.sendMessage(new TextComponentString("Toggle-Tab is: " + (this.visualizeSettings.enableTabToggle ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
            }
        }

        sender.sendMessage(new TextComponentString(getCommandHelp("/discraft visualize <motionblur> <enable/disable/set>")));
        return;
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {

        /* Get a Minecraft Instance */
        Minecraft mc = Minecraft.getMinecraft();

        /* Motion Blur */
        if (this.visualizeSettings.enableMotionBlur && !Minecraft.getMinecraft().entityRenderer.isShaderActive() && Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            applyShader();
        }

        /* Create the Resource Mapper */
        if (this.resourceMapper == null) {
            try {
                Field[] fieldList = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : fieldList) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.resourceMapper = (Map) field.get(Minecraft.getMinecraft().getResourceManager());
                        break;
                    }
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }

        /* Add motion blur to the resource mapping */
        if (this.resourceMapper != null && !this.resourceMapper.containsKey("motionblur")) {
            this.resourceMapper.put("motionblur", new ResourceManagerMotionBlur(this));
        }

        /* Toggle-Tab */
        if (this.keyTabToggle.isPressed() && this.visualizeSettings.enableTabToggle) {
            this.visualizeSettings.tabToggled = !this.visualizeSettings.tabToggled;
        }

    }

    @Override
    public void onRenderOverlay(RenderGameOverlayEvent event){

        WorldClient world = Minecraft.getMinecraft().world;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        Scoreboard scoreboard = world.getScoreboard();
        ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(0);

        if(event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {

            if (this.visualizeSettings.enableTabToggle && this.visualizeSettings.tabToggled && !Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isKeyDown()) {

                if (Minecraft.getMinecraft().isIntegratedServerRunning()
                        && Minecraft.getMinecraft().player.connection.getPlayerInfoMap().size() <= 1 && scoreObjective == null) {
                    Minecraft.getMinecraft().ingameGUI.getTabList().updatePlayerList(false);
                } else {
                    Minecraft.getMinecraft().ingameGUI.getTabList().updatePlayerList(true);
                    Minecraft.getMinecraft().ingameGUI.getTabList().renderPlayerlist(sr.getScaledWidth(), scoreboard, scoreObjective);
                }

            }

        }
    }

    @Override
    public void onModuleDisable(){
        if(Minecraft.getMinecraft().entityRenderer.isShaderActive()){
            Minecraft.getMinecraft().entityRenderer.stopUseShader();
        }
    }

    @Override
    public void loadConfigurations() {
        visualizeSettings.init();
    }

    @Override
    public void initializeConfigurations() {
        visualizeSettings.loadConfig();
    }

}
