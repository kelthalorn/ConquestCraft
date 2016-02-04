package com.kelthalorn.conquest.network;

import com.kelthalorn.conquest.client.gui.GuiConstructorTileEntity;
import com.kelthalorn.conquest.guicontainer.ContainerConstructorTileEntity;
import com.kelthalorn.conquest.tileentity.ConstructorTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ConquestGuiHandler implements IGuiHandler {

	public static final int CONSTRUCTOR_TILE_ENTITY_GUI = 100;
	
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	if (ID == CONSTRUCTOR_TILE_ENTITY_GUI)
    		return new ContainerConstructorTileEntity(player.inventory, (ConstructorTileEntity) world.getTileEntity(new BlockPos(x, y, z)));

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	if (ID == CONSTRUCTOR_TILE_ENTITY_GUI)
    		return new GuiConstructorTileEntity(player.inventory, (ConstructorTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
    	
        return null;
    }
}