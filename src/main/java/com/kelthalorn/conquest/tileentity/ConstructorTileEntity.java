package com.kelthalorn.conquest.tileentity;

import java.util.ArrayList;
import java.util.Random;

import com.kelthalorn.conquest.utils.ConstructionConf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ConstructorTileEntity extends TileEntity implements IUpdatePlayerListBox, IInventory {

	public int id = 0;
	private ItemStack[] inventory;
	private String customName;
	private int delay = 5;
	private boolean isWaiting = true;
	

	private ArrayList<ConstructionConf> constructionConf;
	
    public static void init() {
        GameRegistry.registerTileEntity(ConstructorTileEntity.class, "constructor_tile_entity");
    }

    public ConstructorTileEntity(){
    	this.inventory = new ItemStack[this.getSizeInventory()];
    	this.constructionConf = new ArrayList<ConstructionConf>();
    	Random rand = new Random();
    	this.id = rand.nextInt();
    }
    
	public void setIsWaiting(boolean isWaiting) {
		this.isWaiting = isWaiting;
	}
	
	public boolean isWaiting() {
		return this.isWaiting;
	}
	
	public void setConstructionConf(ArrayList<ConstructionConf> conf) {
		this.constructionConf = conf;
	}
	
	public ArrayList<ConstructionConf> getConstructionConf() {
		return this.constructionConf;
	}
	
	public void setDelay(int i) {
		this.delay += i;
	}

	/**
	 * @return the number of ticks (minus 2) between scheduled updates
	 */
	public int getDelay() {
		return this.delay;
	}

	public void resetDelay() {
		this.delay = 0;
	}
	
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	
	public String getCustomName() {
		return this.customName;
	}
	
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.constructor_tile_entity";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.equals("");
	}

	@Override
	public IChatComponent getDisplayName() {
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return 44;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index < 0 || index >= this.getSizeInventory())
	        return null;
	    return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.getStackInSlot(index) != null) {
	        ItemStack itemstack;

	        if (this.getStackInSlot(index).stackSize <= count) {
	            itemstack = this.getStackInSlot(index);
	            this.setInventorySlotContents(index, null);
	            this.markDirty();
	            return itemstack;
	        } else {
	            itemstack = this.getStackInSlot(index).splitStack(count);

	            if (this.getStackInSlot(index).stackSize <= 0) {
	                this.setInventorySlotContents(index, null);
	            } else {
	                //Just to show that changes happened
	                this.setInventorySlotContents(index, this.getStackInSlot(index));
	            }

	            this.markDirty();
	            return itemstack;
	        }
	    } else {
	        return null;
	    }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = this.getStackInSlot(index);
	    this.setInventorySlotContents(index, null);
	    return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory())
	        return;

	    if (stack != null && stack.stackSize > this.getInventoryStackLimit())
	        stack.stackSize = this.getInventoryStackLimit();
	        
	    if (stack != null && stack.stackSize == 0)
	        stack = null;

	    this.inventory[index] = stack;
	    this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1000;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.getSizeInventory(); i++)
	        this.setInventorySlotContents(i, null);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
	    super.writeToNBT(nbt);

	    NBTTagList list = new NBTTagList();
	    for (int i = 0; i < this.getSizeInventory(); ++i) {
	        if (this.getStackInSlot(i) != null) {
	            NBTTagCompound stackTag = new NBTTagCompound();
	            stackTag.setByte("Slot", (byte) i);
	            this.getStackInSlot(i).writeToNBT(stackTag);
	            list.appendTag(stackTag);
	        }
	    }
	    nbt.setTag("Items", list);

	    if (this.hasCustomName()) {
	        nbt.setString("CustomName", this.getCustomName());
	    }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	    super.readFromNBT(nbt);

	    NBTTagList list = nbt.getTagList("Items", 10);
	    for (int i = 0; i < list.tagCount(); ++i) {
	        NBTTagCompound stackTag = list.getCompoundTagAt(i);
	        int slot = stackTag.getByte("Slot") & 255;
	        this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
	    }

	    if (nbt.hasKey("CustomName", 8)) {
	        this.setCustomName(nbt.getString("CustomName"));
	    }
	}

	@Override
	public void update() {
		
	}
}