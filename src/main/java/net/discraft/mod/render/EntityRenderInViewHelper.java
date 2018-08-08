package net.discraft.mod.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

/**
 * Created by Scott on 12/30/2017.
 */
public class EntityRenderInViewHelper {

    public static boolean canEntityBeSeenByPlayer(EntityPlayer givenPlayerThatLooks, TileEntity tileEntity) {

        AxisAlignedBB teAxis = tileEntity.getRenderBoundingBox();

        return rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.maxX, teAxis.maxY, teAxis.maxZ), false, false, false) == null
                ||
                rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.maxX, teAxis.maxY, teAxis.minZ), false, false, false) == null
                ||
                rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.maxX, teAxis.minY, teAxis.maxZ), false, false, false) == null
                ||
                rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.minX, teAxis.maxY, teAxis.maxZ), false, false, false) == null
                ||
                rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.minX, teAxis.minY, teAxis.minZ), false, false, false) == null
                ||
                rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.minX, teAxis.minY, teAxis.maxZ), false, false, false) == null
                ||
                rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.minX, teAxis.maxY, teAxis.minZ), false, false, false) == null
                ||
                rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ), new Vec3d(teAxis.maxX, teAxis.minY, teAxis.minZ), false, false, false) == null;

    }

    public static boolean canEntityBeSeenByPlayer(EntityPlayer givenPlayerThatLooks, Entity otherEntity) {

        return
                (rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                        givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                        new Vec3d(otherEntity.posX, otherEntity.posY + (double) otherEntity.getEyeHeight(), otherEntity.posZ), false, false, false) == null
                        ||
                        rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                                givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                                new Vec3d(otherEntity.posX + 0.5, otherEntity.posY + (double) otherEntity.getEyeHeight(), otherEntity.posZ + 0.5), false, false, false) == null
                        ||
                        rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                                givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                                new Vec3d(otherEntity.posX - 0.5, otherEntity.posY + (double) otherEntity.getEyeHeight(), otherEntity.posZ - 0.5), false, false, false) == null
                        ||
                        rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                                givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                                new Vec3d(otherEntity.posX - 0.5, otherEntity.posY + (double) otherEntity.getEyeHeight(), otherEntity.posZ + 0.5), false, false, false) == null
                        ||
                        rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                                givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                                new Vec3d(otherEntity.posX + 0.5, otherEntity.posY + (double) otherEntity.getEyeHeight(), otherEntity.posZ - 0.5), false, false, false) == null
                        || rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                        givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                        new Vec3d(otherEntity.posX, otherEntity.posY, otherEntity.posZ), false, false, false) == null
                        ||
                        rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                                givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                                new Vec3d(otherEntity.posX, otherEntity.posY, otherEntity.posZ), false, false, false) == null
                        ||
                        rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                                givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                                new Vec3d(otherEntity.posX, otherEntity.posY, otherEntity.posZ), false, false, false) == null
                        ||
                        rayTraceBlocks(givenPlayerThatLooks.world, new Vec3d(
                                givenPlayerThatLooks.posX, givenPlayerThatLooks.posY + (double) givenPlayerThatLooks.getEyeHeight(), givenPlayerThatLooks.posZ),
                                new Vec3d(otherEntity.posX, otherEntity.posY, otherEntity.posZ), false, false, false) == null);
    }

    public static RayTraceResult rayTraceBlocks(World givenWorld, Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = givenWorld.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (!block.isFullCube(iblockstate)) {
                    return null;
                }

                if ((!ignoreBlockWithoutBoundingBox || block.getCollisionBoundingBox(iblockstate, givenWorld, blockpos) != null) && block.canCollideCheck(iblockstate, stopOnLiquid))
                {
                    RayTraceResult movingobjectposition = block.collisionRayTrace(iblockstate, givenWorld, blockpos, vec31, vec32);

                    if (movingobjectposition != null)
                    {
                        return movingobjectposition;
                    }
                }

                RayTraceResult movingobjectposition2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? movingobjectposition2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = givenWorld.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!block1.isFullCube(iblockstate1)) {
                        return null;
                    }

                    if (!ignoreBlockWithoutBoundingBox || block1.getCollisionBoundingBox(iblockstate1, givenWorld, blockpos) != null)
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {
                            RayTraceResult movingobjectposition1 = block1.collisionRayTrace(iblockstate1, givenWorld, blockpos, vec31, vec32);

                            if (movingobjectposition1 != null)
                            {
                                return movingobjectposition1;
                            }
                        }
                        else
                        {
                            movingobjectposition2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? movingobjectposition2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

}
