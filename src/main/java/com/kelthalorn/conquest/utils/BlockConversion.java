package com.kelthalorn.conquest.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockConversion {
	
	public Map<String, Block> blockConversionValues;
	
	
	public BlockConversion() {
		blockConversionValues = new HashMap<String, Block>();
		initBlocks();
	}
	
	public void addNewBlock(String blockSign, Block block) {
		
		blockConversionValues.put(blockSign, block);
	}
	
	public Block findBlockBySign(String blockSign) {
		
		if (blockConversionValues.containsKey(blockSign))
			return blockConversionValues.get(blockSign);
		
		return null;
	}
	
	public String findSignFromBlock(Block block) {
		for (String key : blockConversionValues.keySet()) {
			if (blockConversionValues.get(key).equals(block)) {
				return key;
			}
		}
		return null;
	}
	
	private void initBlocks() {
		addNewBlock("\"Air\"", Blocks.air);
		addNewBlock("\"Stone\"", Blocks.stone);
		addNewBlock("\"Planks\"", Blocks.planks);
		addNewBlock("\"SPlanks\"", Blocks.wooden_slab);
		addNewBlock("\"Fence\"", Blocks.acacia_fence);
	}
	
}