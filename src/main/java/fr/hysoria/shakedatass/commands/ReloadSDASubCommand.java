package fr.hysoria.shakedatass.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.hysoria.shakedatass.ShakeDatAss;

public class ReloadSDASubCommand implements ISubCommand {

	private ShakeDatAss main;
	
	
	
	public ReloadSDASubCommand(ShakeDatAss m) {
		this.main = m;
	}
	
	@Override
	public boolean onSubCommand(CommandExecutor exec, CommandSender sender, Command cmd, List<String> args) {
		
		sender.sendMessage("Look at the console to see the result of the loading");
		main.getProperties().reload();
		
		return true;
	}

}
