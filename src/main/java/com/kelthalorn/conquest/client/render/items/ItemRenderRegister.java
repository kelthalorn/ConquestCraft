package com.kelthalorn.conquest.client.render.items;

import com.kelthalorn.conquest.main.MainConquest;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ItemRenderRegister {

	public static String modid = MainConquest.MODID;
	
    public static void registerItemRenderer() {
    	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    	
    	//RENDER GENERIC ITEMS
    	//registrateStdItem(ModItems.plannerStick, renderItem);
    	
    	//RENDER GENERIC ITEM BLOCKS
    	//registrateStdItemBlock(ModBlocks.wall_frame1);
    	
    	//RENDER SPECIFIC ITEMS & ITEM BLOCKS
    	//renderItem.getItemModelMesher()
	    //.register(Item.getItemFromBlock(ModBlocks.conquest_gate), 0, new ModelResourceLocation(modid + ":" + ((BlockSelectionMarker) ModBlocks.conquest_marker).getName(), "inventory"));
    }
    
    public static void registrateStdItem(Item item, RenderItem renderItem) {
    	renderItem.getItemModelMesher().register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
    
    public static void registrateStdItemBlock(Block block, RenderItem renderItem) {
	    
	}
}
