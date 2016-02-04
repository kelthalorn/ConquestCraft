package com.kelthalorn.conquest.guicontainer;

import com.kelthalorn.conquest.tileentity.ConstructorTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerConstructorTileEntity extends Container{
	
	private ConstructorTileEntity te;
	
	public ContainerConstructorTileEntity(IInventory playerInv, ConstructorTileEntity te) {
		this.te = te;
		byte b0 = 36;
		byte b1 = 8;
        short short0 = 137;
        short short1 = 84;
        short short2 = 10;
		int y, x;
		
		// Tile Entity, Slot 0-19, Slot IDs 0-19
		for (y = 0; y < 4; ++y) {
			for (x = 0; x < 5; ++x) {
	        	this.addSlotToContainer(new Slot(te, x + y * 4, (18 * x) + b1, (18 * y) + short2));
	        }
	    }
		
		for (y = 0; y < 2; ++y) {
			for (x = 0; x < 12; ++x) {
	        	this.addSlotToContainer(new Slot(te, x + y * 2 + 20, (18 * x) + b1, (18 * y) + short1));
	        }
	    }
		
		for (y = 0; y < 3; ++y)
        {
            for (x = 0; x < 9; ++x)
            {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, b0 + x * 18, short0 + y * 18));
            }
        }

        for (x = 0; x < 9; ++x)
        {
            this.addSlotToContainer(new Slot(playerInv, x, b0 + x * 18, 58 + short0));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
    	//return false;
    	return this.te.isUseableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = null;
        Slot slot = (Slot) this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < 9) {
                // From TE Inventory to Player Inventory
                if (!this.mergeItemStack(current, 9, 45, true))
                    return null;
            } else {
                // From Player Inventory to TE Inventory
                if (!this.mergeItemStack(current, 0, 9, false))
                    return null;
            }

            if (current.stackSize == 0)
                slot.putStack((ItemStack) null);
            else
                slot.onSlotChanged();

            if (current.stackSize == previous.stackSize)
                return null;
            slot.onPickupFromSlot(playerIn, current);
        }
        return previous;
    }
    
    class ConstructorSlot extends Slot
    {
        public ConstructorSlot(IInventory inventory, int index, int xPos, int yPos)
        {
            super(inventory, index, xPos, yPos);
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        public int getSlotStackLimit()
        {
            return 1000;
        }
    }
}
