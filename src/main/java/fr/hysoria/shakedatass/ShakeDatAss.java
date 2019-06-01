package fr.hysoria.shakedatass;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import fr.hysoria.shakedatass.commands.HelpSDASubCommand;
import fr.hysoria.shakedatass.commands.ReloadSDASubCommand;
import fr.hysoria.shakedatass.commands.SDACommandExecutor;
import fr.hysoria.shakedatass.config.Config;
import fr.hysoria.shakedatass.config.GlobalProperties;

public class ShakeDatAss extends JavaPlugin {
	
	private GlobalProperties properties;
	private Config config;
	
	public GlobalProperties getProperties() {
		
		return this.properties;
		
	}
	
	@Override
	public void onEnable() {
		
		getLogger().warning(" starting **DEV VERSION**");
		
		try {
			this.config = new Config(this);
		} catch (IOException e) {
			getLogger().severe("Can't create configuration file ! Plugin disabled");
			e.printStackTrace();
			return;
		}
		
		this.properties = new GlobalProperties(this.config);		
		properties.load();
		
		this.getServer().getPluginManager().registerEvents(new ShakeDatAssListener(properties), this);	
		
		SDACommandExecutor sdaCommandExecutor = new SDACommandExecutor();
		sdaCommandExecutor.addSubCommand("help", new HelpSDASubCommand());
		sdaCommandExecutor.addSubCommand("reload","shakedatass.commands.reload", new ReloadSDASubCommand(this));
		
		this.getCommand("shakedatass").setExecutor(sdaCommandExecutor);
		
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
	
	
}
