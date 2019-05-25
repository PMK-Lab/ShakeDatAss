package fr.hysoria.shakedatass;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import fr.hysoria.shakedatass.config.GlobalGrowingProperties.Chance;
import fr.hysoria.shakedatass.config.GlobalProperties;
import fr.hysoria.shakedatass.config.GrowingProperties;
import fr.hysoria.shakedatass.config.Plantations;
import fr.hysoria.shakedatass.config.Properties;

public class ShakeDatAssListener implements Listener {

	private GlobalProperties properties;

	public ShakeDatAssListener(GlobalProperties p) {
		this.properties = p;
	}

	@EventHandler
	public void onPlayerSneakingEvent(PlayerToggleSneakEvent event) {

		Player player = event.getPlayer();
		
		if(player == null) return;
		if(player.isFlying()) return;		
		if(!event.isSneaking()) return;

		System.out.println("sneak");
		System.out.println("check global permission");
		if(this.properties.GLOBALS_DEFAULT_NEDD_PERMISSION && !player.hasPermission("shakedatass.globals")) {
			System.out.println("global permission true & player does not have perm");
			return;
		}

		System.out.println("global permission true & player have perm");
		
		for (Block block : this.getBlocksAround(player, 1)) {
			
			System.out.println("Block material : " + block.getBlockData().getMaterial() + " || Block location : " + block.getLocation());
			Properties prop = this.properties.getGrowablesProperties(block.getType());
			
			if(prop == null) {
				System.out.println("Not growable block : " + block);
				continue;
			}
			
			System.out.println("Growable block : " + block);
			
			Plantations propPlantation = prop.getType();
			if(this.properties.isPlantationNeedPermission(propPlantation) && !player.hasPermission("")) {
				System.out.println("global permission plantation : " + propPlantation.name().toLowerCase() +" true & player does not have perm");
				return;
			}
			
			System.out.println("global permission plantation : " + propPlantation.name().toLowerCase() +" true & player have perm");
			
			Chance growingChance = prop.getRandomGrowingChance(player);
			System.out.println(growingChance);
			prop.apply(block,growingChance);
			
		}
		
		

	}
	
	private List<Block> getBlocksAround(Player player, int radius){
		
		List<Block> blocks = new ArrayList<Block>();
		World world = player.getWorld();
		Location playerLoc = player.getLocation();	
		
		if(playerLoc.getY() -playerLoc.getBlockY()  > 0.93) {
			playerLoc.setY(playerLoc.getBlockY() + 1);
		}
		
		for (int x = (radius * -1); x <= radius; x++) {			
			for (int z = (radius * -1); z <= radius; z++) {				
				Block block = world.getBlockAt(new Location(world, playerLoc.getX() + x, playerLoc.getY(), playerLoc.getZ() + z));
				if(block == null) continue;				
				blocks.add(block);		
			}
			
		}
		
		return blocks;
		
	}

}
