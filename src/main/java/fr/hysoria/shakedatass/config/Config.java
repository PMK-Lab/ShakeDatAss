package fr.hysoria.shakedatass.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import fr.hysoria.shakedatass.ShakeDatAss;
import fr.hysoria.shakedatass.config.GlobalGrowingProperties.Chance;
import fr.hysoria.shakedatass.config.Plantation.Plantations;

public class Config {

	private File file;
	private FileConfiguration yamlConfig;
	private ShakeDatAss shakeDatAss;
	
	public Config(ShakeDatAss shakeDatAss) throws IOException {
		
		File dataFolder = shakeDatAss.getDataFolder();
		
		this.shakeDatAss = shakeDatAss;
		
		//creating default configuration folder
		if(!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		
		//creating config.yml configurations file
		this.file = new File(dataFolder, "config.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
         }
        
        this.yamlConfig = YamlConfiguration.loadConfiguration(this.file);
		
        this.initConfiguration();
        
	}
	
	private void initConfiguration() throws IOException {
		
		final String CONFIG_PREFIX = "shakedatass.";
		
		this.addDefaultConfiguration(CONFIG_PREFIX + "globals.default_need_permission", false);
		
		this.addDefaultConfiguration(CONFIG_PREFIX + "globals.default_particle", Particle.VILLAGER_HAPPY.name());
		
		for (Chance defaultChance : GlobalGrowingProperties.Chance.values()) {
			
			String chanceName = defaultChance.name().toLowerCase();
			
			this.addDefaultConfiguration(CONFIG_PREFIX + "globals.growing_chance." + chanceName + ".permission", false);
			this.addDefaultConfiguration(CONFIG_PREFIX + "globals.growing_chance." + chanceName + ".particle", "default");
			
		}
		
		for (Plantations plantation : Plantations.values()) {
			
			String plantationName = plantation.name().toLowerCase();
			
			this.addDefaultConfiguration(CONFIG_PREFIX + plantationName + ".permission", false);
			
			for (Plantation p : plantation.getMaterials()) {
				
				String materialName = p.name();
				
				this.addDefaultConfiguration(CONFIG_PREFIX + plantationName + "." + materialName + ".permission",false);
				
				for (Chance chance : GlobalGrowingProperties.Chance.plantationValues(plantation)) {
					
					if(chance.chanceValue(plantation) == null) continue;
					
					String chanceName = chance.name().toLowerCase();
					
					this.addDefaultConfiguration(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".permission", false);
					this.addDefaultConfiguration(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".particle", "default");
					this.addDefaultConfiguration(CONFIG_PREFIX + plantationName + "." + materialName + ".growing_chance." + chanceName + ".chance", chance.chanceValue(plantation));
					
				}
				
			}
			
		}		
		
		// saving default values
		this.yamlConfig.save(this.file);
		
	}
	
	@SuppressWarnings("unused")
	private void registerPermission(Permission perm) {
		this.shakeDatAss.getServer().getPluginManager().addPermission(perm);
	}

	private void addDefaultConfiguration(String path, Object obj) {
		
		if(!this.yamlConfig.contains(path)) {
			this.yamlConfig.set(path, obj);
		}
		
	}

	public File getFile() {
		return file;
	}

	public FileConfiguration getYamlConfig() {
		return yamlConfig;
	}

	public void reload() {
		File dataFolder = shakeDatAss.getDataFolder();
		this.file = new File(dataFolder, "config.yml");
		 this.yamlConfig = YamlConfiguration.loadConfiguration(this.file);		
	}

}
