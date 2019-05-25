package fr.hysoria.shakedatass.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpSDASubCommand implements ISubCommand{

	@Override
	public boolean onSubCommand(CommandExecutor exec, CommandSender sender, Command cmd, List<String> args) {
		
		sender.sendMessage("Unvalid uses of this commande : shakedatass [reload/help]");
		
		return true;
	}

	public boolean onSubCommand(SDACommandExecutor sdaCommandExecutor, CommandSender p, Command cmd) {
		return this.onSubCommand(sdaCommandExecutor, p, cmd,Collections.emptyList());
	}

}
