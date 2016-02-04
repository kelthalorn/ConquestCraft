package com.kelthalorn.conquest.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks {

	public static Block conquest_construct;
	public static Block conquest_import_marker;
	public static Block conquest_import;
	
	public static void createBlocks() {
		GameRegistry.registerBlock(conquest_construct = new BlockConstruct("construct_block"), "construct_block");
		GameRegistry.registerBlock(conquest_import_marker = new BlockImportMarker("import_marker_block"), "import_marker_block");
		GameRegistry.registerBlock(conquest_import = new BlockImport("import_block"), "import_block");
	}
}
