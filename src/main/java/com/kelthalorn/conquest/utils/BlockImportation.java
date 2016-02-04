package com.kelthalorn.conquest.utils;

import java.util.ArrayList;
import java.util.List;

public class BlockImportation {

	public String name;
	public String upgrade_lvl;
	public String sizeX;
	public String sizeY;
	public String sizeZ;
	
	public String posX;
	public String posY;
	public String posZ;
	
	public String block;
	
	private List <BlockImportation> datas;
	
	public BlockImportation () {
		
	}
	
	public void initDatas() {
		datas = new ArrayList<BlockImportation>();
	}
	
	public void addBlockImport(BlockImportation blockImp) {
		datas.add(blockImp);
	}	
}