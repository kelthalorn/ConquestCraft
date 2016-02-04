package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumWorldBlockLayer
{
    SOLID("Solid"),
    CUTOUT_MIPPED("Mipped Cutout"),
    CUTOUT("Cutout"),
    TRANSLUCENT("Translucent");
    private final String layerName;

    private static final String __OBFID = "CL_00002152";

    private EnumWorldBlockLayer(String layerNameIn)
    {
        this.layerName = layerNameIn;
    }

    public String toString()
    {
        return this.layerName;
    }
}