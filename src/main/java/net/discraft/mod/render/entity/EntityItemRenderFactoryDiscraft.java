package net.discraft.mod.render.entity;

import net.discraft.mod.Discraft;
import net.discraft.mod.render.EntityRenderInViewHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityItemRenderFactoryDiscraft implements IRenderFactory<EntityItem>
{
    @Override
    public Render<? super EntityItem> createRenderFor(RenderManager manager)
    {
        return new RenderEntityItemDiscraft(manager, Minecraft.getMinecraft().getRenderItem());
    }

    public class RenderEntityItemDiscraft extends RenderEntityItem
    {
        public RenderEntityItemDiscraft(RenderManager renderManagerIn, RenderItem p_i46167_2_)
        {
            super(renderManagerIn, p_i46167_2_);
        }

        @Override
        public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if(Discraft.getInstance().discraftSettings.enableDiscraft
                    && Discraft.getInstance().discraftSettings.enableOpticraft
                    && EntityRenderInViewHelper.canEntityBeSeenByPlayer(mc.player, entity)) {
                    super.doRender(entity, x, y, z, entityYaw, partialTicks);
            } else {
                super.doRender(entity, x, y, z, entityYaw, partialTicks);
            }
        }

    }

}