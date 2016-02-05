package com.kelthalorn.conquest.client.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kelthalorn.conquest.guicontainer.ContainerConstructorTileEntity;
import com.kelthalorn.conquest.main.MainConquest;
import com.kelthalorn.conquest.tileentity.ConstructorTileEntity;
import com.kelthalorn.conquest.utils.ConstructionConf;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiConstructorTileEntity extends GuiContainer {
	
	private IInventory playerInv;
	private ConstructorTileEntity te;
	private int nbRequires;
	private static final ResourceLocation constructionGuiTextures = new ResourceLocation("conquest:textures/gui/container/constructor_tile_entity.png");
    
	
	public GuiConstructorTileEntity(IInventory playerInv, ConstructorTileEntity te) {
	    super(new ContainerConstructorTileEntity(playerInv, te));
	    this.playerInv = playerInv;
	    this.te = te;
	    this.nbRequires = 0;
	    
	    this.xSize = 230;
        this.ySize = 219;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(constructionGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        
        for (int i = 0; i < this.te.getConstructionConf().size(); i++) {
			ConstructionConf currentConstruction = this.te.getConstructionConf().get(i);
			int k1 = k + 114;
			int k2 = k1 + 18;
			switch(currentConstruction.getConstructionState()) {
				
				case 1:  this.drawTexturedModalRect(k1, l + 5 + 19 * i, 90, 237, 108, 19);
						 showRequires(currentConstruction.getConstructionName(), k, l);
						 if (checkRequires())
							 this.drawTexturedModalRect(k1, l + 5 + 19 * i, 198, 237, 19, 19);
						 break;
				case 2:  this.drawTexturedModalRect(k1, l + 5 + 19 * i, 198, 237, 19, 19);
						 break;
				case 0:  this.drawTexturedModalRect(k2, l + 5 + 19 * i, 0, 237, 90, 19);
						 break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		for (int i = 0; i < this.te.getConstructionConf().size(); i++) {
			ConstructionConf currentConstruction = this.te.getConstructionConf().get(i);
			String const_name = currentConstruction.getConstructionName() + " LVL " + currentConstruction.getConstructionLvl();
			this.fontRendererObj.drawString(const_name, 140, 10 + (i * 20), 4210752);
		}
		/*String s = this.te.getDisplayName().getUnformattedText();
	    this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);            //#404040
	    this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 4210752);      //#404040*/
	}
	
	@Override
    public void initGui() {
            super.initGui();
                       
            ArrayList<ConstructionConf> constructionConf = new ArrayList<ConstructionConf>();
	    	
	    	File file = new File("mods/conquest/config/construction_config.json");		
			
    		String json_conf = parseJsonFile(file);
    		
    		if (json_conf != null) {
    			
    			JsonElement jelement = new JsonParser().parse(json_conf);
    			JsonObject  jobject = jelement.getAsJsonObject();
    			
    			JsonArray constructions = jobject.getAsJsonArray("conf");
    			
    			for (int c = 0; c < constructions.size() ; c++) {
    				JsonObject currentConstruction = constructions.get(c).getAsJsonObject();
    				
    				int constructionId = currentConstruction.get("id").getAsInt();
    				int groupId = currentConstruction.get("grp_id").getAsInt();
    				String constructionName = currentConstruction.get("construction_name").getAsString();
    				int constructionLvl = currentConstruction.get("lvl").getAsInt();
    				int requireLvl = currentConstruction.get("require_lvl").getAsInt();
    				
    				ConstructionConf constConf = new ConstructionConf(constructionId, groupId, constructionName, constructionLvl, requireLvl);
    				constConf.playerCanConstruct();
    				
    				constructionConf.add(constConf);
    			}    			
    		}
    		
    		this.te.setConstructionConf(constructionConf);
			
    		System.out.println("NB CONST TROUVEE = " + this.te.getConstructionConf().size());
    }

    protected void actionPerformed(GuiButton guibutton) {
            //id is the id you give your button
            switch(guibutton.id) {
	            case 1:                              
	            case 2: break;
            }
            
            //Packet code here
            //PacketDispatcher.sendPacketToServer(packet); //send packet
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
    
    private void showRequires(String name, int k, int l) {
    	
    	File file = new File("mods/conquest/config/"+name+".json");		
		
		String json_conf = parseJsonFile(file);
		
		if (json_conf != null) {
			
			this.itemRender.zLevel = 100.0F;
			
			JsonElement jelement = new JsonParser().parse(json_conf);
			JsonObject  jobject = jelement.getAsJsonObject();
    	
	    	JsonArray requirments = jobject.getAsJsonArray("requires");
			this.nbRequires = requirments.size();
			for (int r = 0; r < requirments.size() ; r++) {
				JsonObject currentRequire = requirments.get(r).getAsJsonObject();
				
				String blockName = currentRequire.get("block").toString();
				int quantity = currentRequire.get("quantity").getAsInt();
				
				Block block = MainConquest.blockConversion.findBlockBySign(blockName);
							
		        //this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(block), k + 15, l + 15 + (r * 22));
		        te.setInventorySlotContents(r,new ItemStack(block,quantity));
			}
		}
    }
    
    private boolean checkRequires() {
    	
    	for (int i = 0; i < this.nbRequires; i++) {
    		ItemStack currentStack = te.getStackInSlot(i);
    		
    		boolean found = false;
    		for (int j = 20; j < 44; j++) {
    			ItemStack inventoryStack = te.getStackInSlot(j);
    			
    			if (inventoryStack != null && currentStack.getIsItemStackEqual(inventoryStack)) {
    				found = true;
    				break;
    			}
    		}
    		
    		if (!found)
    			return false;
    	}
    	
    	return true;
    }
    
    @SideOnly(Side.CLIENT)
    static class Button extends GuiButton
        {
            private final ResourceLocation field_146145_o;
            private final int field_146144_p;
            private final int field_146143_q;
            private boolean field_146142_r;

            protected Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_, ResourceLocation p_i1077_4_, int p_i1077_5_, int p_i1077_6_)
            {
                super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
                this.field_146145_o = p_i1077_4_;
                this.field_146144_p = p_i1077_5_;
                this.field_146143_q = p_i1077_6_;
            }

            /**
             * Draws this button to the screen.
             */
            public void drawButton(Minecraft mc, int mouseX, int mouseY)
            {
                if (this.visible)
                {
                    mc.getTextureManager().bindTexture(GuiConstructorTileEntity.constructionGuiTextures);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                    short short1 = 219;
                    int k = 0;

                    if (!this.enabled)
                    {
                        k += this.width * 2;
                    }
                    else if (this.field_146142_r)
                    {
                        k += this.width * 1;
                    }
                    else if (this.hovered)
                    {
                        k += this.width * 3;
                    }

                    this.drawTexturedModalRect(this.xPosition, this.yPosition, k, short1, this.width, this.height);

                    if (!GuiConstructorTileEntity.constructionGuiTextures.equals(this.field_146145_o))
                    {
                        mc.getTextureManager().bindTexture(this.field_146145_o);
                    }

                    this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_146144_p, this.field_146143_q, 18, 18);
                }
            }

            public boolean func_146141_c()
            {
                return this.field_146142_r;
            }

            public void func_146140_b(boolean p_146140_1_)
            {
                this.field_146142_r = p_146140_1_;
            }
        }

    @SideOnly(Side.CLIENT)
    class CancelButton extends GuiConstructorTileEntity.Button
    {

        public CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_)
        {
            super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiConstructorTileEntity.constructionGuiTextures, 112, 220);
        }

        public void drawButtonForegroundLayer(int mouseX, int mouseY)
        {
        	GuiConstructorTileEntity.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
        }
    }
}
