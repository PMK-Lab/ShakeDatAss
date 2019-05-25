package fr.hysoria.shakedatass.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface ISubCommand {
	
	public boolean onSubCommand(CommandExecutor exec,CommandSender sender, Command cmd, List<String> args);
	
}
