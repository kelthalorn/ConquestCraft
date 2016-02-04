package com.kelthalorn.conquest.blocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kelthalorn.conquest.main.MainConquest;
import com.kelthalorn.conquest.network.ConquestGuiHandler;
import com.kelthalorn.conquest.tileentity.ConstructorTileEntity;
import com.kelthalorn.conquest.utils.ConstructionConf;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
 
public class BlockConstruct extends BlockContainer
{
	private HashMap<BlockPos, Block> blockMap;
	private boolean end_flag = false;
	
	public BlockConstruct(String unlocalizedName)
	{
		super(Material.wood);
		setUnlocalizedName(unlocalizedName);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	
	public String getName()
	{
		return this.getUnlocalizedName();
	}
	
	@Override
	public int getRenderType()
    {
        return 3;
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ConstructorTileEntity();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		ConstructorTileEntity te = (ConstructorTileEntity) world.getTileEntity(pos);
	    InventoryHelper.dropInventoryItems(world, pos, te);
	    super.breakBlock(world, pos, blockstate);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
	    if (stack.hasDisplayName()) {
	        ((ConstructorTileEntity) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
	    }
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
	    if (!world.isRemote) {

	    	player.openGui(MainConquest.instanceconquest, ConquestGuiHandler.CONSTRUCTOR_TILE_ENTITY_GUI, world, pos.getX(), pos.getY(), pos.getZ());
	    	//world.scheduleUpdate(pos, this, this.tickRate(world));
	    	
	    	    	
	    }
	    return true;
	}
	
	@Override
	public int tickRate(World par1World) {
		return 2;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);

		ConstructorTileEntity te = (ConstructorTileEntity) worldIn.getTileEntity(pos);
		
		if (!te.isWaiting() && !end_flag) {
			
			if (blockMap.isEmpty()) {
				te.setIsWaiting(true);
				setEndFlag(true);
			}
			else {
				Map.Entry<BlockPos,Block> currentEntry = blockMap.entrySet().iterator().next();
				Block currentBlock = currentEntry.getValue();
				BlockPos currentPos = currentEntry.getKey();
				
				blockMap.remove(currentPos);
				Block blockAtPos = worldIn.getBlockState(currentPos).getBlock();

				if (currentBlock == Blocks.air && blockAtPos != Blocks.air) {
					worldIn.setBlockToAir(currentPos);
				}
				else if (currentBlock != blockAtPos) {
					IBlockState currentBlockState = currentBlock.getDefaultState();
					worldIn.setBlockState(currentPos, currentBlockState);
				}
				
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + te.getDelay());
			}
			
			
		}
		else {
			blockMap = new HashMap<BlockPos,Block>();
			
			te.setDelay(5);
			te.setIsWaiting(false);
			
			File file = new File("mods/conquest/config/construciton_files/construct1.json");
			
			String json_conf = parseJsonFile(file);
			
			if (json_conf != null) {
				lunchInitConstruction(worldIn, pos, json_conf);
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + te.getDelay());
			}			
		}
	}
	
	public void setEndFlag(boolean end) {
		this.end_flag = end;
	}
	
	public boolean getEndFlag() {
		return end_flag;
	}
	
	public void lunchInitConstruction(World world, BlockPos pos, String json_conf) {
		
		JsonElement jelement = new JsonParser().parse(json_conf);
		JsonObject  jobject = jelement.getAsJsonObject();
		
		int sizeX = jobject.get("sizeX").getAsInt();
		int sizeY = jobject.get("sizeY").getAsInt();
		int sizeZ = jobject.get("sizeZ").getAsInt();
				
		int yPos = 0, xPos = 0, zPos = 0;
		JsonArray arrY = jobject.getAsJsonArray("datas");
		
		
		for (int y = pos.getY(); y < (pos.getY() + sizeY); y++) {
			
			JsonObject currentObjY = arrY.get(yPos).getAsJsonObject();
			JsonArray arrX = currentObjY.getAsJsonArray("datas");
			xPos = 0;
			
			for (int x = pos.getX() + 1; x < (pos.getX() + 1 + sizeX); x++) {
				
				JsonObject currentObjX = arrX.get(xPos).getAsJsonObject();
				JsonArray arrZ = currentObjX.getAsJsonArray("datas");
				zPos = 0;
				
				for (int z = pos.getZ(); z < (pos.getZ() + sizeZ); z++) {
					
					JsonObject currentObjZ = arrZ.get(zPos).getAsJsonObject();
					String blockSign = currentObjZ.get("block").toString();
					Block currentBlock = MainConquest.blockConversion.findBlockBySign(blockSign);
					
					if (currentBlock != null) {						
						blockMap.put(new BlockPos(x,y,z),currentBlock);
					}
					else
						System.out.println("SIGN "+blockSign+" : Block NON TROUVE");
					
					/*Block currentBlock = world.getBlockState(new BlockPos(x,y,z)).getBlock();
					
					*/
					zPos++;
				}
				xPos++;
			}
			yPos++;
		}
	}
	
	private String parseJsonFile(File file) {
		
		String return_str = null;
		try {
			FileReader fl = new FileReader(file);
			
			BufferedReader br = new BufferedReader(fl);
	    	StringBuilder sb = new StringBuilder();
	    	String line;
			try {
				line = br.readLine();
				
				while (line != null) {
	    	        sb.append(line);
	    	        sb.append(System.lineSeparator());
	    	        line = br.readLine();
	    	    }
	    	    return_str = sb.toString();
	    	    br.close();
	    	    
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		return return_str;
	}
}