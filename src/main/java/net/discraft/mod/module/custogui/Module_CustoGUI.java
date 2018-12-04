package net.discraft.mod.module.custogui;

import net.discraft.mod.Discraft;
import net.discraft.mod.gui.GuiUtils;
import net.discraft.mod.module.DiscraftModule;
import net.discraft.mod.module.ModuleSettings;
import net.discraft.mod.module.custogui.gui.CustoGUISettings;
import net.discraft.mod.module.custogui.gui.GuiEditElements;
import net.discraft.mod.module.custogui.utils.ElementData;
import net.discraft.mod.module.custogui.utils.GuiElement;
import net.discraft.mod.module.custogui.utils.cps.CpsNode;
import net.discraft.mod.notification.ClientNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static net.minecraft.client.Minecraft.getMinecraft;

public class Module_CustoGUI extends DiscraftModule {

    public static ArrayList<GuiElement> guiElements = new ArrayList<>();

    public KeyBinding keyEditGui = new KeyBinding("key.discraft.custogui.editgui", Keyboard.KEY_K, "key.discraft.category.elementCPS");

    public CustoGUISettings custoGUISettings = new CustoGUISettings(new File("config/discraft_custogui.cfg"));

    public String currentBackgroundURL = custoGUISettings.backgroundImageURL;
    public ResourceLocation backgroundResourceLocation;

    public ElementData elementData = new ElementData();

    public Module_CustoGUI(String givenModuleID, String givenModuleName, String givenModuleDescription, String givenModuleDescriptionLong, String givenModuleAuthor, ResourceLocation givenModuleLogo) {
        super(givenModuleID, givenModuleName, givenModuleDescription, givenModuleDescriptionLong, givenModuleAuthor, givenModuleLogo);

        this.keyBindings.add(keyEditGui);

        this.custoGUISettings.init();

    }

    public static BufferedImage downloadImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.out.println("Errors reading online image: '" + url + "'");
        }
        return null;
    }

    @Override
    public void onRenderOverlay(RenderGameOverlayEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
            for (GuiElement element : guiElements) {
                if (!(mc.currentScreen instanceof GuiEditElements) && element.isEditable) {
                    element.isEditable = false;
                }
                element.onRender(mc, event.getPartialTicks());
            }
        }

    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (event.phase.equals(TickEvent.Phase.START)) {

            Minecraft mc = Minecraft.getMinecraft();

            this.elementData.update();

            for (GuiElement element : guiElements) {
                element.onUpdate(mc);
            }

            if (keyEditGui.isPressed() && mc.player != null && mc.world != null) {
                mc.displayGuiScreen(new GuiEditElements(this));
            }

        }

    }

    @Override
    public void onMouseEvent(MouseEvent event) {
        if (event.getButton() == 0 && event.isButtonstate()) {
            this.elementData.CPSNodes.add(new CpsNode());
        }
    }

    /**
     * Add GUI Element - Add a new GUI Element to the In-game GUI
     *
     * @param givenElement - Given Element to Add
     */
    public void addGuiElement(GuiElement givenElement) {

        if (!guiElements.contains(givenElement)) {
            guiElements.add(givenElement);
        }

    }

    /**
     * Remove GUI Element - Remove a new GUI Element from the In-game GUI
     *
     * @param givenElementUUID - Given Element UUID
     */
    public void removeGuiElement(GuiElement givenElementUUID) {

        ArrayList<GuiElement> newList = new ArrayList<>();

        for (GuiElement element : this.guiElements) {
            if (!element.equals(givenElementUUID)) {
                newList.add(element);
            }
        }

        this.guiElements = newList;

    }

    @Override
    public void onRenderMainMenu(int width, int height, int mouseX, int mouseY, float partialTicks) {

        if (this.custoGUISettings.enableCustomMainMenu) {
            if (this.backgroundResourceLocation != null) {

                int zoomAmount = 1 + this.custoGUISettings.zoomAmount;

                float val = this.custoGUISettings.backgroundZoom ? ((float) (Math.sin(Discraft.getInstance().discraftVariables.smoothSwing / 50 * this.custoGUISettings.zoomSpeed) + 1) / zoomAmount) : 0;

                if (this.custoGUISettings.dynamicBackground) {

                    boolean invert = this.custoGUISettings.dynamicBackgroundInvertMouse;

                    GlStateManager.pushMatrix();
                    GlStateManager.translate((invert ? -mouseX : mouseX / 2) / 25, (invert ? -mouseY : mouseY / 2) / 25, 0);
                    GuiUtils.renderImageCenteredScaled(width / 2, height / 2, this.backgroundResourceLocation, width, height, 1.1f + val);
                    GlStateManager.popMatrix();
                } else {
                    GuiUtils.renderImageCenteredScaled(width / 2, height / 2, this.backgroundResourceLocation, width, height, 1f + val);
                }

            } else {
                try {
                    this.backgroundResourceLocation = getMinecraft().getTextureManager().getDynamicTextureLocation(this.currentBackgroundURL, new DynamicTexture(downloadImage(this.currentBackgroundURL)));
                } catch (NullPointerException e) {
                    this.custoGUISettings.backgroundImageURL = this.custoGUISettings.defaultBackgroundImageURL;
                    this.custoGUISettings.saveConfig();
                    ClientNotification.createNotification(I18n.format("error.notification.unknownbackgroundurl"));
                }
            }

            if (this.currentBackgroundURL != this.custoGUISettings.backgroundImageURL) {
                this.backgroundResourceLocation = null;
                this.currentBackgroundURL = this.custoGUISettings.backgroundImageURL;
            }
        }

    }

    @Override
    public ModuleSettings getSettings() {
        return this.custoGUISettings;
    }

    @Override
    public void onEntityAttack(AttackEntityEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player != null) {
            if (event.getEntity().equals(mc.player)) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                this.elementData.clientReach = Float.valueOf(decimalFormat.format(event.getEntity().getDistance(event.getTarget())));
                System.out.println(this.elementData.clientReach);
            }
        }

    }

    @Override
    public void onEntityHurt(LivingHurtEvent event) {

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player != null) {
            if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource().equals(mc.player)) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                this.elementData.serverReach = Float.valueOf(decimalFormat.format(event.getSource().getTrueSource().getDistance(event.getEntityLiving())));
                System.out.println(this.elementData.serverReach);
            }
        }

    }

}
