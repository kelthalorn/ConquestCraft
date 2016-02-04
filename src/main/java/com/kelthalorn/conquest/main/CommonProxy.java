package com.kelthalorn.conquest.main;

import com.kelthalorn.conquest.blocks.ModBlocks;
import com.kelthalorn.conquest.crafting.ModCrafting;
import com.kelthalorn.conquest.items.ModItems;
import com.kelthalorn.conquest.network.ConquestGuiHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    	ModItems.createItems();
    	ModBlocks.createBlocks();    	
    }

    public void init(FMLInitializationEvent e) {
    	ModCrafting.initCrafting();
    	NetworkRegistry.INSTANCE.registerGuiHandler(MainConquest.instanceconquest, new ConquestGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
