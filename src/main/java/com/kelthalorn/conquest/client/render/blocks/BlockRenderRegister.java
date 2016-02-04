package com.kelthalorn.conquest.client.render.blocks;

import com.kelthalorn.conquest.blocks.ModBlocks;
import com.kelthalorn.conquest.main.MainConquest;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderRegister {
	
	public static void registerBlockRenderer() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		regStdBlock(ModBlocks.conquest_construct, renderItem);
		regStdBlock(ModBlocks.conquest_import_marker, renderItem);
		regStdBlock(ModBlocks.conquest_import, renderItem);
    }
	
	public static String modid = MainConquest.MODID;

	public static void regStdBlock(Block block, RenderItem renderItem) {
	    renderItem.getItemModelMesher()
	    .register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
