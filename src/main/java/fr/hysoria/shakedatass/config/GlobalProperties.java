package fr.hysoria.shakedatass.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;

import fr.hysoria.shakedatass.config.GlobalGrowingProperties.Chance;
import fr.hysoria.shakedatass.config.Plantation.Plantations;

public class GlobalProperties {

	private Config config;

	public GlobalProperties(Config c) {

		this.config = c;

		this.GLOBALS_DEFAULT_GROWING_CHANCES = new HashMap<GlobalGrowingProperties.Chance, GlobalGrowingProperties>();
		this.GLOBALS_PLANTATIONS_PERMISSIONS = new HashMap<Plantations, Boolean>();
		
		this.GROWABLES = new ArrayList<Properties>();
		this.RIPED_STEMS = new ArrayList<Properties>();

		globalProperties = this;
		
	}

	private static GlobalProperties globalProperties;
	
	public static GlobalProperties INSTANCE() {
		return globalProperties;
	}
	
	public Config getConfig() {
		return config;
	}

	///////// Properties ////////////////////
	
	public Particle GLOBALS_DEFAULT_PARTICLE;
	public boolean GLOBALS_DEFAULT_NEDD_PERMISSION;

	public HashMap<GlobalGrowingProperties.Chance, GlobalGrowingProperties> GLOBALS_DEFAULT_GROWING_CHANCES;

	public HashMap<Plantations, Boolean> GLOBALS_PLANTATIONS_PERMISSIONS;
	
	public ArrayList<Properties> GROWABLES;
	public ArrayList<Properties> RIPED_STEMS;

	////////////////////////////////////////
	
	public void load() {

		final FileConfiguration config = this.config.getYamlConfig();		
		final String CONFIG_PREFIX = "shakedatass.";

		this.GLOBALS_DEFAULT_NEDD_PERMISSION = config.getBoolean(CONFIG_PREFIX + "globals.default_need_permission");

		Particle gettedParticle;
		try {
			gettedParticle = Particle.valueOf(config.getString(CONFIG_PREFIX + "globals.default_particle"));
		} catch (Exception e) {
			gettedParticle = null;
		}
		if(gettedParticle == null) {
			Bukkit.getLogger().severe("Unrecognize global particle given in configuration (set to default CLOUD)");
			this.GLOBALS_DEFAULT_PARTICLE = Particle.CLOUD;
		}else {
			this.GLOBALS_DEFAULT_PARTICLE = gettedParticle;
		}

		for (Chance globalChance : GlobalGrowingProperties.Chance.values()) {

			String chanceName = globalChance.name().toLowerCase();

			if(!config.contains(CONFIG_PREFIX + "globals.growing_chance." + chanceName + ".permission") || !config.contains(CONFIG_PREFIX + "globals.growing_chance." + chanceName + ".particle")) {
				Bukkit.getLogger().severe("Missing or broken " + globalChance.name() + " global growing chance section in configuration file");
				this.GLOBALS_DEFAULT_GROWING_CHANCES.put(globalChance, new GlobalGrowingProperties(this.GLOBALS_DEFAULT_NEDD_PERMISSION, this.GLOBALS_DEFAULT_PARTICLE));				
			}else {

				Boolean permission = config.getBoolean(CONFIG_PREFIX + "globals.growing_chance." + chanceName + ".permission");	
				
				Particle particle;
				if(config.getString(CONFIG_PREFIX + "globals.growing_chance." + chanceName + ".particle").equals("default")) {
					particle = this.GLOBALS_DEFAULT_PARTICLE;
				}else {
					
					try {
						particle = Particle.valueOf(config.getString(CONFIG_PREFIX + "globals.growing_chance." + chanceName + ".particle"));
					} catch (Exception e) {
						particle = null;
					}
					
					if(particle == null) {
						Bukkit.getLogger().severe("Unrecognize global particle given in configuration (set to default +" + this.GLOBALS_DEFAULT_PARTICLE.name() + ")");
						particle = this.GLOBALS_DEFAULT_PARTICLE;
					}else {
						this.GLOBALS_DEFAULT_PARTICLE = gettedParticle;
					}
					
				}

				this.GLOBALS_DEFAULT_GROWING_CHANCES.put(globalChance, new GlobalGrowingProperties(permission, this.GLOBALS_DEFAULT_PARTICLE));	

			}		

		}		


		for (Plantations plantation : Plantations.values()) {

			String plantationName = plantation.name().toLowerCase();
			boolean plantationPerm;

			if(!config.contains(CONFIG_PREFIX + plantationName + ".permission")) {

				Bukkit.getLogger().severe("Missing or broken " + plantationName + " global permission in configuration file (set to false)");
				plantationPerm = false;

			}else {
				plantationPerm = config.getBoolean(CONFIG_PREFIX + plantationName + ".permission");
			}

			this.GLOBALS_PLANTATIONS_PERMISSIONS.put(plantation, plantationPerm);

			for (Plantation p : plantation.getMaterials()) {

				String materialName = p.name();
				boolean growablesPerm;
				
				if(!config.contains(CONFIG_PREFIX + plantationName + "." + materialName + ".permission")) {
					
					Bukkit.getLogger().severe("Missing or broken " + materialName + " global permission in configuration file (set to false)");
					growablesPerm = false;
					
				}else {
					growablesPerm = config.getBoolean(CONFIG_PREFIX + plantationName + "." + materialName + ".permission");
				}
				
				Properties prop = new Properties(p, growablesPerm);
				
				for (Chance chance : GlobalGrowingProperties.Chance.plantationValues(plantation)) {

					if(chance.chanceValue(plantation) == null) continue;

					String chanceName = chance.name().toLowerCase();
					
					boolean chancePerm;					
					if(!config.contains(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".permission")) {
						
						Bukkit.getLogger().severe("Missing or broken " + materialName + ":" + chanceName + " permission in configuration file (set to false)");
						chancePerm = false;
						
					}else {
						chancePerm = config.getBoolean(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".permission");
					}
					

					Particle chanceParticle;					
					if(!config.contains(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".particle")) {
						
						Bukkit.getLogger().severe("Missing or broken " + materialName + ":" + chanceName + " particle in configuration file (set to default)");
						chanceParticle = this.GLOBALS_DEFAULT_GROWING_CHANCES.get(chance).getParticle();
						
					}else {
						String chanceParticleName = config.getString(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".particle");
						if(chanceParticleName.equals("default")) {
							
							chanceParticle = this.GLOBALS_DEFAULT_GROWING_CHANCES.get(chance).getParticle();
							
						}else {
							
							chanceParticle = Particle.valueOf(chanceParticleName);
							if(chanceParticle == null) {
								
								Bukkit.getLogger().severe("Unrecognize " + plantationName + ":" + chanceName + " particle given in configuration (set to default +" + this.GLOBALS_DEFAULT_GROWING_CHANCES.get(chance).getParticle() + ")");
								chanceParticle = this.GLOBALS_DEFAULT_GROWING_CHANCES.get(chance).getParticle();
								
							}
							
						}
					}
					
					int chanceValue;					
					if(!config.contains(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".chance")) {
						
						Bukkit.getLogger().severe("Missing or broken " + materialName + ":" + chanceName + " growing chance in configuration file (set to default)");
						chanceValue = chance.chanceValue(plantation);
						
					}else {
						chanceValue = config.getInt(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".chance");
					}
					
					GrowingProperties gp = new GrowingProperties(chancePerm, chanceParticle, chanceValue);
					prop.setGrowingChance(chance, gp);

				}
				
				try {
					prop.checkGrowingValues();
				} catch (Exception e) {
					e.printStackTrace();
					prop.initGrowingChances();
				}

				if(plantation.equals(Plantations.RIPED_STEMS)) {
					
					this.RIPED_STEMS.add(prop);
					
				}else {
					
					this.GROWABLES.add(prop);
					
				}
				
			}

		}
		
		Bukkit.getLogger().info("Configuration succesfully loaded !");

	}

	public void reload() {

		this.GLOBALS_DEFAULT_GROWING_CHANCES.clear();
		this.GLOBALS_PLANTATIONS_PERMISSIONS.clear();
		
		this.RIPED_STEMS.clear();
		
		this.GROWABLES.clear();
		
		this.config.reload();
		
		this.load();

	}
	
	public Properties getGrowablesProperties(Material m) {
		
		for (Properties prop : this.GROWABLES) {			
			if(prop.getMaterial().equals(m)) {				
				return prop;				
			}			
		}	
		return null;
		
	}
	
	public Properties getRipedGrowablesProperties(Material m) {
		
		for (Properties prop : this.RIPED_STEMS) {			
			if(prop.getMaterial().equals(m)) {				
				return prop;				
			}			
		}	
		return null;
		
	}
	
	public boolean isPlantationNeedPermission(Plantations p) {
		return this.GLOBALS_PLANTATIONS_PERMISSIONS.get(p).booleanValue();
		
	}

}
