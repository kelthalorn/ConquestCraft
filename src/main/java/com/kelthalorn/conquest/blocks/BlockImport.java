package com.kelthalorn.conquest.blocks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kelthalorn.conquest.main.MainConquest;
import com.kelthalorn.conquest.tileentity.ImportTileEntity;
import com.kelthalorn.conquest.utils.BlockImportation;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockImport extends Block implements ITileEntityProvider {
	
	private final int dimension_max = 30;
	private int xSize;
	private int ySize;
	private int zSize;
	private int xPosInc;
	private int yPosInc;
	private int zPosInc;
	
	public BlockImport(String unlocalizedName)
	{
		super(Material.wood);
		setUnlocalizedName(unlocalizedName);
		setCreativeTab(CreativeTabs.tabBlock);
		
		xSize = ySize = zSize = 0;
		xPosInc = yPosInc = zPosInc = 0;
	}	
	
	public String getName()
	{
		return this.getUnlocalizedName();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ImportTileEntity();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		super.breakBlock(world, pos, blockstate);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
	    if (!world.isRemote) {
	    	initImport(world, pos);
	    	
	    	if (xSize != 0 && ySize != 0 && zSize != 0) {
	    		
	    		BlockImportation blockImpComplete = new BlockImportation();
	    		blockImpComplete.name = "test_construct";
	    		blockImpComplete.upgrade_lvl = "1";
	    		blockImpComplete.sizeX = String.valueOf(xSize);;
	    		blockImpComplete.sizeY = String.valueOf(ySize);;
	    		blockImpComplete.sizeZ = String.valueOf(zSize);;
	    		
	    		blockImpComplete.initDatas();

	    		for (int cptY = 1; cptY <= ySize; cptY++) {
	    			
	    			BlockImportation currentYImp = new BlockImportation();
	    			currentYImp.posY = String.valueOf(cptY);
	    			currentYImp.initDatas();
	    			
	    			for (int cptX = 1; cptX <= xSize; cptX++) {
	    				
	    				BlockImportation currentXImp = new BlockImportation();
		    			currentXImp.posX = String.valueOf(cptX);
		    			currentXImp.initDatas();
		    			
		    			for (int cptZ = 1; cptZ <= zSize; cptZ++) {
		    				
		    				BlockImportation currentZImp = new BlockImportation();
			    			currentZImp.posZ = String.valueOf(cptZ);
			    			
			    			int x = pos.getX() + (cptX * xPosInc);
			    			int y = pos.getY() + (cptY * yPosInc) -1;
			    			int z = pos.getZ() + (cptZ * zPosInc);
			    			System.out.println("Xpos = "+x+"; Ypos = "+y+"; Zpos = "+z+"; BLOCK = "+world.getBlockState(new BlockPos(x,y,z)).getBlock().getUnlocalizedName());
			    			
			    			String currentBlock = MainConquest.blockConversion.findSignFromBlock(world.getBlockState(new BlockPos(x,y,z)).getBlock()).replace("\"", "");
			    			currentZImp.block = currentBlock;			    			
			    			
			    			currentXImp.addBlockImport(currentZImp);
		    			}
		    			
		    			currentYImp.addBlockImport(currentXImp);
	    			}
	    			
	    			blockImpComplete.addBlockImport(currentYImp);
	    		}
	    		
	    		GsonBuilder builder = new GsonBuilder();
		        Gson gson = builder.create();
		        
		        BufferedWriter bw = null;
		        try {

					File file = new File("/assets/conquest/construct_config_files/test.json");

					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter("/assets/conquest/construct_config_files/test.json");
					bw = new BufferedWriter(fw);
					bw.write(gson.toJson(blockImpComplete));
					

					System.out.println("Done");

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	    	}
	    }
	    
	    return true;
	}
	
	private void initImport(World world, BlockPos pos) {
		boolean found = false;
    	for (int x = pos.getX(); x <= pos.getX() + this.dimension_max; x++) {
    		
    		if (found)
    			break;
    		
    		if (world.getBlockState(new BlockPos(x, pos.getY(), pos.getZ())).getBlock() instanceof BlockImportMarker) {
    			xSize = x - pos.getX();
    			xPosInc = 1;
    			found = true;
    		}
    	}
    	
    	if (!found) {
    		for (int x = pos.getX(); x >= pos.getX() - this.dimension_max; x--) {
    			
	    		if (found)
	    			break;
	    		
	    		if (world.getBlockState(new BlockPos(x, pos.getY(), pos.getZ())).getBlock() instanceof BlockImportMarker) {
	    			xSize = pos.getX() - x;
	    			xPosInc = -1;
	    			found = true;
	    		}
	    	}
    		
    		if (!found) {
    			System.out.println("AUCUN MARKER TROUVE EN X");
    			xSize = ySize = zSize = 0;
    		}
    	}

    	if (found){
    		found = false;
    		
    		for (int z = pos.getZ(); z <= pos.getZ() + this.dimension_max; z++) {
    			
	    		if (found)
	    			break;
	    		
	    		if (world.getBlockState(new BlockPos(pos.getX(), pos.getY(), z)).getBlock() instanceof BlockImportMarker) {
	    			zSize = z - pos.getZ();
	    			zPosInc = 1;
	    			found = true;
	    		}
	    	}
	    	
	    	if (!found) {
	    		for (int z = pos.getZ(); z >= pos.getZ() - this.dimension_max; z--) {

		    		if (found)
		    			break;
		    		
		    		if (world.getBlockState(new BlockPos(pos.getX(), pos.getY(), z)).getBlock() instanceof BlockImportMarker) {
		    			zSize = pos.getZ() - z;
		    			zPosInc = -1;
		    			found = true;
		    		}
		    	}
	    		
	    		if (!found) {
	    			System.out.println("AUCUN MARKER TROUVE EN Z");
	    			xSize = ySize = zSize = 0;
	    		}
	    	}

	    	if (found){
	    		found = false;
	    		
	    		for (int y = pos.getY(); y <= pos.getY() + this.dimension_max; y++) {

		    		if (found)
		    			break;
		    		
		    		if (world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock() instanceof BlockImportMarker) {
		    			ySize = y - pos.getY() + 1;
		    			yPosInc = 1;
		    			found = true;
		    		}
		    	}
		    	
		    	if (!found) {

		    		for (int y = pos.getY(); y >= pos.getY() - this.dimension_max; y--) {

			    		if (found)
			    			break;
			    		
			    		if (world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock() instanceof BlockImportMarker) {
			    			ySize = pos.getY() - y + 1;
			    			yPosInc = -1;
			    			found = true;
			    		}
			    	}
		    		
		    		if (!found) {
		    			System.out.println("AUCUN MARKER TROUVE EN Y");
		    			xSize = ySize = zSize = 0;
		    		}
		    	}
	    	}
    	}
    
    
    	System.out.println("xSize = "+xSize+"; ySize = "+ySize+"; zSize = "+zSize);
	}
}
