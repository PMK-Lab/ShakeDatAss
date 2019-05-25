package fr.hysoria.shakedatass.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public interface SubTabCompleter {

	public List<String> onTabComplete(CommandExecutor exec, Player sender, Command command, String alias, String[] args);
	
}
