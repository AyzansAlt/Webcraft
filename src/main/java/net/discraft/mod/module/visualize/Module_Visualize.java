package net.discraft.mod.module.visualize;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.ModuleSettings;
import net.discraft.mod.module.visualize.utils.VisualizeSettings;
import net.discraft.mod.module.visualize.utils.shaderresources.ResourceManagerMotionBlur;
import net.discraft.mod.module.visualize.utils.shaderresources.ResourceMotionBlur;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

public class Module_Visualize extends DiscraftModule {

    public KeyBinding keyTabToggle = new KeyBinding("key.discraft.visualize.tabtoggle", Keyboard.KEY_TAB, "key.discraft.category.elementCPS");

    public VisualizeSettings visualizeSettings = new VisualizeSettings(new File("config/discraft_visualize.cfg"));
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

    public static Color getColor(String col) {
        switch (col.toLowerCase()) {
            case "black":
                return Color.BLACK;
            case "blue":
                return Color.BLUE;
            case "cyan":
                return Color.CYAN;
            case "darkgray":
                return Color.DARK_GRAY;
            case "gray":
                return Color.GRAY;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            case "lightgray":
                return Color.LIGHT_GRAY;
            case "magneta":
                return Color.MAGENTA;
            case "orange":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            case "red":
                return Color.RED;
            case "white":
                return Color.WHITE;
        }
        return null;
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
                            case "reset":
                                this.visualizeSettings.motionBlurAmount = this.visualizeSettings.DEFAULTmotionBlurAmount;
                                sender.sendMessage(new TextComponentString("Motion Blur has been RESET to " + ChatFormatting.GREEN + this.visualizeSettings.DEFAULTmotionBlurAmount));
                                this.visualizeSettings.saveConfig();
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
                    /* BLOCK-HIGHLIGHT */
                case "blockhighlight":
                    if (args.length > 2) {
                        switch (args[2]) {
                            case "enable":
                                this.visualizeSettings.enableCustomBlockHighlight = true;
                                sender.sendMessage(new TextComponentString("Block-Highlight has been " + ChatFormatting.GREEN + "Enabled" + ChatFormatting.RESET + "!"));
                                this.visualizeSettings.saveConfig();
                                break;
                            case "disable":
                                this.visualizeSettings.enableCustomBlockHighlight = false;
                                sender.sendMessage(new TextComponentString("Block-Highlight has been " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + "!"));
                                this.visualizeSettings.saveConfig();
                                break;
                            case "thickness":
                                if (args.length > 3) {
                                    switch (args[3]) {
                                        case "set":
                                            if (args.length > 3) {
                                                try {
                                                    float givenHighlight = Float.parseFloat(args[4]);
                                                    this.visualizeSettings.blockHighlightSettings.blockHighlightThickness = givenHighlight;
                                                    sender.sendMessage(new TextComponentString("Block-Highlight thickness has been set to " + ChatFormatting.GREEN + givenHighlight));
                                                    this.visualizeSettings.saveConfig();
                                                } catch (NumberFormatException e) {
                                                    sender.sendMessage(new TextComponentString(ChatFormatting.RED + "Invalid highlight thickness! (Try '" + ChatFormatting.YELLOW + "set 2.0" + ChatFormatting.RED + "')"));
                                                }
                                                break;
                                            }
                                            break;
                                        case "reset":
                                            this.visualizeSettings.blockHighlightSettings.blockHighlightThickness = this.visualizeSettings.blockHighlightSettings.DEFAULTblockHighlightThickness;
                                            sender.sendMessage(new TextComponentString("Block-Highlight thickness has been RESET to " + ChatFormatting.GREEN + this.visualizeSettings.blockHighlightSettings.DEFAULTblockHighlightThickness));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                    }
                                }
                                break;
                            case "color":
                                if (args.length > 3) {
                                    switch (args[3]) {
                                        case "set":
                                            if (args.length > 3) {
                                                String givenColor = args[4];
                                                if (givenColor.startsWith("0x")) {
                                                    givenColor.replace("0x", "");
                                                    this.visualizeSettings.blockHighlightSettings.blockHighlightColor = Integer.decode(givenColor);
                                                    sender.sendMessage(new TextComponentString("Block-Highlight color has been set to " + ChatFormatting.GREEN + givenColor));
                                                    this.visualizeSettings.saveConfig();
                                                    break;
                                                } else {

                                                    if (getColor(givenColor) != null) {
                                                        Color color = getColor(givenColor);
                                                        this.visualizeSettings.blockHighlightSettings.blockHighlightColor = getIntFromColor(color.getRed(), color.getGreen(), color.getBlue());
                                                        sender.sendMessage(new TextComponentString("Block-Highlight color has been set to " + ChatFormatting.GREEN + givenColor));
                                                        this.visualizeSettings.saveConfig();
                                                        break;
                                                    }

                                                }
                                            }
                                            break;
                                        case "reset":
                                            this.visualizeSettings.blockHighlightSettings.blockHighlightColor = this.visualizeSettings.blockHighlightSettings.DEFAULTblockHighlightColor;
                                            sender.sendMessage(new TextComponentString("Block-Highlight color has been RESET to " + ChatFormatting.GREEN + this.visualizeSettings.blockHighlightSettings.DEFAULTblockHighlightColor));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                        case "togglechroma":
                                            this.visualizeSettings.blockHighlightSettings.blockIsChroma = !this.visualizeSettings.blockHighlightSettings.blockIsChroma;
                                            sender.sendMessage(new TextComponentString("Block-Highlight chroma has been set to " + (this.visualizeSettings.blockHighlightSettings.blockIsChroma ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                            this.visualizeSettings.saveConfig();
                                            break;

                                    }
                                }
                            case "chroma":
                                if (args.length > 3) {
                                    switch (args[3]) {
                                        case "enable":
                                            this.visualizeSettings.blockHighlightSettings.blockIsChroma = true;
                                            sender.sendMessage(new TextComponentString("Block-Highlight chroma has been " + ChatFormatting.GREEN + "Enabled" + ChatFormatting.RESET + "!"));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                        case "disable":
                                            this.visualizeSettings.blockHighlightSettings.blockIsChroma = false;
                                            sender.sendMessage(new TextComponentString("Block-Highlight chroma has been " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + "!"));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                        case "set":
                                            if (args.length > 3) {
                                                try {
                                                    int givenSpeed = Integer.parseInt(args[4]);
                                                    if (givenSpeed < 1000 && givenSpeed >= 1) {
                                                        this.visualizeSettings.blockHighlightSettings.blockChromaSpeed = givenSpeed;
                                                        sender.sendMessage(new TextComponentString("Block-Highlight chroma speed has been set to " + ChatFormatting.GREEN + givenSpeed));
                                                        this.visualizeSettings.saveConfig();
                                                    } else {
                                                        sender.sendMessage(new TextComponentString(ChatFormatting.RED + "Invalid chroma speed! Cannot be higher than 1000 or lower than 1!"));
                                                    }
                                                    break;
                                                } catch (NumberFormatException e) {
                                                    sender.sendMessage(new TextComponentString(ChatFormatting.RED + "Invalid chroma speed! (Try '" + ChatFormatting.YELLOW + "set 2.0" + ChatFormatting.RED + "')"));
                                                }
                                            }
                                            break;
                                        case "reset":
                                            this.visualizeSettings.blockHighlightSettings.blockChromaSpeed = this.visualizeSettings.blockHighlightSettings.DEFAULTblockChromaSpeed;
                                            sender.sendMessage(new TextComponentString("Block-Highlight chroma speed has been RESET to " + ChatFormatting.GREEN + this.visualizeSettings.blockHighlightSettings.DEFAULTblockHighlightColor));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                    }
                                    break;
                                } else {
                                    sender.sendMessage(new TextComponentString("Block-Highlight chroma is: " + (this.visualizeSettings.blockHighlightSettings.blockIsChroma ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                                    break;
                                }
                            case "alpha":
                                if (args.length > 3) {
                                    switch (args[3]) {
                                        case "set":
                                            try {
                                                float givenAlpha = Float.parseFloat(args[4]);
                                                if (givenAlpha >= 0 && givenAlpha <= 1) {
                                                    this.visualizeSettings.blockHighlightSettings.blockAlpha = givenAlpha;
                                                    sender.sendMessage(new TextComponentString("Block-Highlight alpha has been set to " + ChatFormatting.GREEN + givenAlpha));
                                                    this.visualizeSettings.saveConfig();
                                                    break;
                                                }
                                            } catch (NumberFormatException e) {
                                                sender.sendMessage(new TextComponentString(ChatFormatting.RED + "Invalid alpha value! (Try '" + ChatFormatting.YELLOW + "set 0.5" + ChatFormatting.RED + "')"));
                                            }
                                            break;
                                        case "reset":
                                            this.visualizeSettings.blockHighlightSettings.blockAlpha = this.visualizeSettings.blockHighlightSettings.DEFAULTblockAlpha;
                                            sender.sendMessage(new TextComponentString("Block-Highlight alpha has been RESET to " + ChatFormatting.GREEN + this.visualizeSettings.blockHighlightSettings.DEFAULTblockAlpha));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                    }
                                    break;
                                } else {
                                    sender.sendMessage(new TextComponentString("Block-Highlight alpha is: " + ChatFormatting.GREEN + this.visualizeSettings.blockHighlightSettings.blockAlpha));
                                    break;
                                }
                            case "fill":
                                if (args.length > 3) {
                                    switch (args[3]) {
                                        case "enable":
                                            this.visualizeSettings.blockHighlightSettings.enableFill = true;
                                            sender.sendMessage(new TextComponentString("Block-Highlight fill has been " + ChatFormatting.GREEN + "Enabled" + ChatFormatting.RESET + "!"));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                        case "disable":
                                            this.visualizeSettings.blockHighlightSettings.enableFill = false;
                                            sender.sendMessage(new TextComponentString("Block-Highlight fill has been " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + "!"));
                                            this.visualizeSettings.saveConfig();
                                            break;
                                    }
                                    break;
                                }
                        }
                        return;
                    } else {
                        sender.sendMessage(new TextComponentString("Block-Highlight is: " + (this.visualizeSettings.enableCustomBlockHighlight ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.GRAY + "Disabled")));
                        return;
                    }
            }
        }

        sender.sendMessage(new TextComponentString(getCommandHelp("/discraft visualize <motionblur> <enable/disable/set/reset>")));
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

        String shaderGroupName = "unknown";

        /* If shader group is not null, set the shaders name */
        if(Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
            shaderGroupName = Minecraft.getMinecraft().entityRenderer.getShaderGroup().getShaderGroupName();
        }

        /* If motion blur disabled but active, disable it */
        if (!this.visualizeSettings.enableMotionBlur && shaderGroupName.equalsIgnoreCase("motionblur:motionblur")) {
            Minecraft.getMinecraft().entityRenderer.stopUseShader();
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
    public void onRenderOverlay(RenderGameOverlayEvent event) {

        WorldClient world = Minecraft.getMinecraft().world;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        Scoreboard scoreboard = world.getScoreboard();
        ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(0);

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {

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
    public void onModuleDisable() {
        if (Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
            Minecraft.getMinecraft().entityRenderer.stopUseShader();
        }
    }

    @Override
    public void loadConfigurations() {
        visualizeSettings.loadConfig();
    }

    @Override
    public void initializeConfigurations() {
        visualizeSettings.init();
    }

    @Override
    public void onRenderBlockHighlightEvent(DrawBlockHighlightEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        if (this.visualizeSettings.enableCustomBlockHighlight) {

            if (mc.objectMouseOver != null) {

                RayTraceResult position = mc.player.rayTrace(6.0, event.getPartialTicks());

                /* If the type of hit is a block */
                if (position != null || position.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {

                    /* Get the selected block and state */
                    IBlockState blockState = mc.player.world.getBlockState(position.getBlockPos());
                    Block block = blockState.getBlock();

                    if (!blockState.getMaterial().equals(Material.AIR) && !blockState.getMaterial().equals(Material.AIR)) {

                        /* Get Rendering Attributes */
                        float lineThickness = this.visualizeSettings.blockHighlightSettings.blockHighlightThickness;

                        /* Get Block Bounding Box */
                        AxisAlignedBB box = block.getSelectedBoundingBox(blockState, mc.world, position.getBlockPos()).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

                        /* Make sure line thickness is larger than 0 */
                        if (lineThickness > 0) {

                            /* Cancel original event */
                            event.setCanceled(true);

                            if (this.visualizeSettings.blockHighlightSettings.blockIsChroma) {
                                float time = System.currentTimeMillis() % (10000L / this.visualizeSettings.blockHighlightSettings.blockChromaSpeed) / (10000.0f / this.visualizeSettings.blockHighlightSettings.blockChromaSpeed);
                                Color color = Color.getHSBColor(time, 1.0f, 1.0f);
                                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, this.visualizeSettings.blockHighlightSettings.blockAlpha);
                            } else {
                                Color color = new Color(this.visualizeSettings.blockHighlightSettings.blockHighlightColor);
                                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, this.visualizeSettings.blockHighlightSettings.blockAlpha);
                            }

                            /* Create new GL matrix */
                            GlStateManager.pushMatrix();
                            GlStateManager.enableBlend();
                            GlStateManager.disableTexture2D();
                            GlStateManager.depthMask(false);

                            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

                            GL11.glLineWidth(this.visualizeSettings.blockHighlightSettings.blockHighlightThickness);

                            if (this.visualizeSettings.blockHighlightSettings.enableFill) {
                                GuiUtils.renderBoundingBoxFilled(box);
                            } else {
                                GuiUtils.renderBoundingBox(box);
                            }

                            GlStateManager.depthMask(true);
                            GlStateManager.enableTexture2D();
                            GlStateManager.disableBlend();
                            GlStateManager.popMatrix();

                        }

                    }

                    return;
                }

            }

        }

    }

    public int getIntFromColor(int Red, int Green, int Blue) {
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    @Override
    public ModuleSettings getSettings() {
        return this.visualizeSettings;
    }

}
