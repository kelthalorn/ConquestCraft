package com.kelthalorn.conquest.main;

import java.util.HashMap;
import java.util.Map;

import com.kelthalorn.conquest.events.ConquestEventHandler;
import com.kelthalorn.conquest.utils.BlockConversion;
import com.kelthalorn.conquest.utils.BlockSelection;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MainConquest.MODID, name = MainConquest.MODNAME, version = MainConquest.VERSION)
public class MainConquest {

	@SidedProxy(clientSide="com.kelthalorn.conquest.main.ClientProxy", serverSide="com.kelthalorn.conquest.main.ServerProxy")
	public static CommonProxy proxy;
	
    public static final String MODID = "conquest";
    public static final String MODNAME = "ConquestCraft Mod";
    public static final String VERSION = "0.0.1";

    @Instance
    public static MainConquest instanceconquest = new MainConquest();
    public static BlockConversion blockConversion = new BlockConversion();
    
    private Map<String, BlockSelection> selections;
    
    ConquestEventHandler events = new ConquestEventHandler();
    
    public MainConquest() {
    	selections = new HashMap<String, BlockSelection>();
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	proxy.preInit(e);
    	
    	//event handler registry
    	FMLCommonHandler.instance().bus().register(events);
    	MinecraftForge.EVENT_BUS.register(events);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
    	proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	proxy.postInit(e);
    }
    
    public BlockSelection getSelection(EntityLivingBase placer) {
		BlockSelection s = selections.get(String.valueOf(placer.getCommandSenderEntity().getEntityId()));
		if (s == null) {
			s = new BlockSelection(placer.dimension);
			selections.put(String.valueOf(placer.getCommandSenderEntity().getEntityId()), s);
		}
		if (s.getDimension() != placer.dimension) {
			s.reset(placer.dimension);
		}
		return s;
	}
}

