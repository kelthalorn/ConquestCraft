package com.kelthalorn.conquest.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ImportTileEntity extends TileEntity {
	private String customName;
	
    public static void init() {
        GameRegistry.registerTileEntity(ImportTileEntity.class, "import_tile_entity");
    }

    public ImportTileEntity(){
    	
    }
    
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	
	public String getCustomName() {
		return this.customName;
	}
	
	
}
