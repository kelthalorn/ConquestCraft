package com.kelthalorn.conquest.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockSelection {
	private int dimension;
	private BlockPos corner1, corner2;
	private Entity selectedEntity;
	private TileEntity selectedTile;

	//Creates an empty selection
	public BlockSelection(int dimension) {
		this.dimension = dimension;
	}

	public BlockSelection(int dimension, Entity selectedEntity) {
		this.dimension = dimension;
		this.selectedEntity = selectedEntity;
	}
	
	public BlockSelection(int dimension, TileEntity selectedTile) {
		this.dimension = dimension;
		this.selectedTile = selectedTile;
	}

	public BlockSelection(int dimension, BlockPos corner1, BlockPos corner2) {
		this.dimension = dimension;
		this.corner1 = corner1;
		this.corner2 = corner2;
	}

	public void setSelectedEntity(Entity e) {
		selectedEntity = e;
		selectedTile = null;
		corner1 = null;
		corner2 = null;
	}
	
	public void setTileEntity(TileEntity t) {
		selectedTile = t;
		selectedEntity = null;
		corner1 = null;
		corner2 = null;
	}

	/**
	 * Reset selected entity/tile if applicable, else - 
	 * If c is already a corner in this selection, it is removed.
	 * Otherwise, if the selection has an open spot, it is added.
	 * In the case 2 corners are already selected, the selection is reset.
	 * @param c BlockPos to add
	 */
	public void addBlockPos(BlockPos c) {
		if (selectedEntity != null || selectedTile != null) {
			selectedEntity = null;
			selectedTile = null;
			return;
		}
		
		System.out.println("C1 X = " + c.getX());
    	System.out.println("C1 Y = " + c.getY());
    	System.out.println("C1 Z = " + c.getZ());
		if (c.equals(corner1)) { //Remove corner1
			corner1 = corner2;
			corner2 = null;
		}
		else if (c.equals(corner2))  //Remove corner2
			corner2 = null;
		else if (corner1 == null)
			corner1 = c;
		else if (corner2 == null)
			corner2 = c;
		else 
			corner1 = corner2 = null;
	}

	public boolean isEntitySelection() {
		return selectedEntity != null;
	}
	
	public boolean isTileSelection() {
		return selectedTile != null;
	}
	
	public boolean isRegionSelection() {
		return corner1 != null;
	}

	public boolean isEmpty() {
		return corner1 == null && selectedEntity == null && selectedTile == null;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public void reset(int dimension) {
		this.dimension = dimension;
		selectedEntity = null;
		selectedTile = null;
		corner1 = corner2 = null;
	}

	/**
	 * @return A pooled AABB. Do not cache.
	 */
	public AxisAlignedBB getAABB() {
		if (corner2 == null)
			return new AxisAlignedBB(corner1.getX(), corner1.getY(), corner1.getZ(), corner1.getX() + 1, corner1.getY() + 1, corner1.getZ() + 1);
		int minX = getMin(corner1.getX(), corner2.getX());
		int minY = getMin(corner1.getY(), corner2.getY());
		int minZ = getMin(corner1.getZ(), corner2.getZ());
		int maxX = getMax(corner1.getX()+1, corner2.getX()+1);
		int maxY = getMax(corner1.getY()+1, corner2.getY()+1);
		int maxZ = getMax(corner1.getZ()+1, corner2.getZ()+1);
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@SuppressWarnings("unchecked")
	public List<Entity> getEntitiesWithinAABB(EntityPlayer player) {
		return (List<Entity>) player.worldObj.getEntitiesWithinAABBExcludingEntity(player, getAABB());
	}
	
	public List<TileEntity> getTilesWithinAABB(World world) {
		List<TileEntity> list = new ArrayList<TileEntity>();
		AxisAlignedBB selAABB = this.getAABB();
		
		int minX = (int) selAABB.minX;
		int minY = (int) selAABB.minY;
		int minZ = (int) selAABB.minZ;

		int maxX = (int) selAABB.maxX - 1;
		int maxY = (int) selAABB.maxY - 1; 
		int maxZ = (int) selAABB.maxZ - 1;

		for(int x = (minX >> 4); x <= (maxX >> 4); x++) 
			for(int z = (minZ >> 4); z <= (maxZ >> 4); z++) {
				Chunk chunk = world.getChunkFromChunkCoords(x, z);
				if (chunk != null) 
					for(Object obj : chunk.getTileEntityMap().values()) {
						TileEntity entity = (TileEntity)obj;
						if (!entity.isInvalid()) 
							if (entity.getPos().getX() >= minX && entity.getPos().getY() >= minY && entity.getPos().getZ() >= minZ &&
							entity.getPos().getX() <= maxX && entity.getPos().getY() <= maxY && entity.getPos().getZ() <= maxZ)
								list.add(entity);
					}
			}
		return list;
	}

	public Entity getSelectedEntity() {
		return selectedEntity;
	}
	
	public TileEntity getSelectedTile() {
		return selectedTile;
	}

	public BlockPos getCorner1() {
		return corner1;
	}

	public BlockPos getCorner2() {
		return corner2;
	}

	private int getMin(int var1, int var2) {
		return (var1 < var2) ? var1 : var2;
	}

	private int getMax(int var1, int var2) {
		return (var1 > var2) ? var1 : var2;
	}

	public boolean isInvalid() {
		return (selectedTile != null && selectedTile.isInvalid()) || (selectedEntity != null && selectedEntity.isDead);
	}
}
