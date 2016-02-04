package com.kelthalorn.conquest.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConstructionConf {

	private int id;
	private int grpId;
	private String name;
	private int level;
	private int requireLvl;
	private boolean canBeConstruct = false;
	private boolean alreadyDone = false;
	
	ConstructionConf(int id, int grpId) {
		this.id = id;
		this.grpId = grpId;
		
		this.name = "";
		this.level = 0;
		this.requireLvl = 0;
		
	}
	
	public ConstructionConf (int id, int grpId, String name, int lvl, int requireLvl) {
		this(id, grpId);
		
		this.name = name;
		this.level = lvl;
		this.requireLvl = requireLvl;
	}
	
	public int getConstrucitonId() {
		return this.id;
	}
	
	public int getConstrucitonGrpId() {
		return this.grpId;
	}
	
	public String getConstructionName() {
		return this.name;
	}
	
	public int getConstructionLvl() {
		return this.level;
	}
	
	public int getRequireLevel() {
		return this.requireLvl;
	}

	public int getConstructionState() {
		if (this.alreadyDone)
			return 2;
		else if (this.canBeConstruct)
			return 1;
		else
			return 0;
	}
	
	public void playerCanConstruct() {
		
		File file = new File("mods/conquest/config/construction_config.json");		
		
		String json_conf = parseJsonFile(file);
		
		if (json_conf != null) {
			
			JsonElement jelement = new JsonParser().parse(json_conf);
			JsonObject  jobject = jelement.getAsJsonObject();
			
			JsonArray constructions = jobject.getAsJsonArray("player_gain");
			
			for (int g = 0; g < constructions.size() ; g++) {
				JsonObject currentGain = constructions.get(g).getAsJsonObject();
				
				int grpId = currentGain.get("grp_id").getAsInt();
				if (grpId != this.grpId) {
					continue;
				}
				
				int currentLvl = currentGain.get("lvl").getAsInt();
				
				if (currentLvl == this.requireLvl) {
					this.canBeConstruct = true;
				}
				else if (currentLvl > this.requireLvl) {
					this.alreadyDone = true;					
				}
			}			
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
