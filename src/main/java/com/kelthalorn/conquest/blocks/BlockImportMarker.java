package com.kelthalorn.conquest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockImportMarker extends Block {
	public BlockImportMarker(String unlocalizedName)
	{
		super(Material.wood);
		setUnlocalizedName(unlocalizedName);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	
	public String getName()
	{
		return this.getUnlocalizedName();
	}
}
