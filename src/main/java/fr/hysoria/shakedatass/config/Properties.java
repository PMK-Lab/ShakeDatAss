package fr.hysoria.shakedatass.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.material.Crops;

import fr.hysoria.shakedatass.config.GlobalGrowingProperties.Chance;
import fr.hysoria.shakedatass.config.Plantation.Plantations;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ItemDye;
import net.minecraft.server.v1_12_R1.ItemStack;

public class Properties {
	
	private Plantation plantation;
	private boolean permission;
	private HashMap<GlobalGrowingProperties.Chance, GrowingProperties> growingChances;

	public Properties(Plantation plan,boolean p) {

		this.plantation = plan;
		this.permission = p;
		this.growingChances = this.initGrowingChances();	

		
		
	}

	public String name() {
		return this.plantation.name();
	}
	
	public boolean needPermission() {
		return permission;
	}

	public Plantations getType() {
		return this.plantation.getType();
	}

	public Material getMaterial() {
		return this.plantation.getMaterial();
	}

	public HashMap<GrowingProperties.Chance, GrowingProperties> getGrowingChances() {
		return growingChances;
	}

	public void setGrowingChance(Chance c,GrowingProperties g) {

		this.growingChances.replace(c, g);

	}

	public void checkGrowingValues() throws Exception {

		int i = 0;

		for (Entry<Chance, GrowingProperties> gp : this.growingChances.entrySet()) {
			//System.out.println(this.getMaterial().name() + "  " + gp.getValue().getChance() + "  " + gp.getKey().name());
			i += gp.getValue().getChance();

		}

		if(i != 100) {
			throw new Exception("Total of the growing chances for " + this.plantation.getType().name() + " "  + this.plantation.name() + " must be 100 (not " + i + ")");
		}


	}

	public HashMap<GrowingProperties.Chance, GrowingProperties> initGrowingChances() {

		HashMap<GrowingProperties.Chance, GrowingProperties> hash = new HashMap<GrowingProperties.Chance, GrowingProperties>();

		for (Chance chance : Chance.plantationValues(this.plantation.getType())) {

			hash.put(chance, new GrowingProperties(false, Particle.CLOUD, chance.chanceValue(this.plantation.getType())));

		}

		return hash;

	}

	public void applyParticle(Block b,Chance c) {
		
		World w = b.getWorld();
		w.spawnParticle(this.growingChances.get(c).getParticle(), b.getX() + 0.5, b.getY() + 0.8, b.getZ() + 0.5, 5, 0.5, 0.2, 0.5);
		
	}
	
	@SuppressWarnings("deprecation")
	public void apply(Player player,Block block, Chance c) {

		if(c.equals(Chance.NOTHING)) {
			return;
		}
		
		if(this.plantation.getType().equals(Plantations.SEEDS)) {
			
			BlockState bs = block.getState();
			Crops crops = (Crops) bs.getData();
			
			if(crops.getState().equals(CropState.RIPE)) {
				this.applyParticle(block, c);
				return;
				
			}else if(c.equals(Chance.FULLY)) {
				crops.setState(CropState.RIPE);
				this.applyParticle(block, c);
				
			}else {
				crops.setData((byte) (crops.getData() + 1));
				this.applyParticle(block, c);
				
			}
			
			bs.setData(crops);
			bs.update();

		}else if(this.plantation.getType().equals(Plantations.SAPLINGS)) {

			//YorickReflectionUtil.INSTANCE().applyBoneMeal(block.getLocation());
			Location loc = block.getLocation();
			ItemStack nmsStack = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.INK_SACK, 1, (short) 15));				
					
			ItemDye.a(nmsStack, ((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()));
			applyParticle(block, c);

		}else if(this.plantation.getType().equals(Plantations.STEMS)){
			
			byte data = block.getData();
			
			if(data >= 7) {
				
				GlobalProperties globalProperties = GlobalProperties.INSTANCE();
				Properties p = globalProperties.getRipedGrowablesProperties(this.getMaterial());
				
				if(p != null) {
					
					Plantations propPlantation = p.getType();
					if(globalProperties.isPlantationNeedPermission(propPlantation) && !player.hasPermission("shakedatass." + propPlantation.name().toLowerCase())) {
						//System.out.println("global permission plantation : " + propPlantation.name().toLowerCase() +" true & player does not have perm");
						return;
					}
					
					if(p.needPermission() && !player.hasPermission("shakedatass." + propPlantation.name().toLowerCase() + "." + p.name())) {
						return;
					}

					//System.out.println("global permission plantation : " + propPlantation.name().toLowerCase() +" false or player have perm");

					Chance growingChance = p.getRandomGrowingChance(player);
					//System.out.println(growingChance);
					p.apply(player,block,growingChance);
					
				}

				return;
				
			}else {
				
				if(c.equals(Chance.FULLY)) {
					block.setData((byte) 7);
				}else {
					block.setData((byte) (data + 1));
				}
				
				applyParticle(block, c);
				
			}
			
		}else {
			
			//System.out.println("Growable block : " + block);
			
			//System.out.println("product melon or pumpkin");
			World world = block.getWorld();
			Location loc = block.getLocation();

			for (int x = -1; x <= 1; x+=2) {

				Block futureBlock = world.getBlockAt(new Location(world,loc.getBlockX() + x, loc.getBlockY(), loc.getBlockZ()));

				if(this.setStemProduct(futureBlock)) {
					this.applyParticle(block, c);
					return;
				}

			}


			for (int z = -1; z <= 1; z+=2) {	

				Block futureBlock = world.getBlockAt(new Location(world,loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + z));

				if(this.setStemProduct(futureBlock)) {
					this.applyParticle(block, c);
					return;
				}


			}

			return;
			
		}


	}
	
	@SuppressWarnings("unused")
	private void generateTree(Block b) {
		
		
		
	}
	
	private boolean setStemProduct(Block b2) {
		
		if(b2.isEmpty()) {

			if(this.getMaterial().equals(Material.MELON_STEM)) {
				//System.out.println("set melon");
				b2.setType(Material.MELON_BLOCK,true);

			}else {
				//System.out.println("set pumpkin");
				b2.setType(Material.PUMPKIN,true);

			}
			
			return true;

		}
		
		return false;
		
	}

	public Chance getRandomGrowingChance(Player p) {

		int r = (int) (Math.random() * 100);
		//System.out.println("Random = " + r);

		int last = 0;

		List<Entry<Chance, GrowingProperties>> list = new ArrayList<>(this.growingChances.entrySet());
		list.sort(new Comparator<Entry<Chance, GrowingProperties>>() {

			@Override
			public int compare(Entry<Chance, GrowingProperties> o1, Entry<Chance, GrowingProperties> o2) {

				int o1Ordinal = o1.getKey().ordinal();
				int o2Ordinal = o2.getKey().ordinal();

				return (o1Ordinal > o2Ordinal ? 1 : -1);
			}

		});

		for (Entry<Chance, GrowingProperties> entry : list) {

			//System.out.println("Chance : " + entry.getValue().getChance());

			if(entry.getValue().getChance() == 0) continue;
			
			if(r >= last && r < last + entry.getValue().getChance()) {

				//System.out.println("Match : " + entry.getKey());

				if(GlobalProperties.INSTANCE().GLOBALS_DEFAULT_GROWING_CHANCES.get(entry.getKey()).isPermission() && !p.hasPermission("shakedatass.globals." + entry.getKey().name().toLowerCase())){
					//System.out.println("global chance perm on true : " + entry.getKey() + " & player does not have perm");
					return Chance.NOTHING;
				}

				//System.out.println("gloabal chance perm on false : " + entry.getKey() + " or player have perm");

				if(entry.getValue().isPermission() && !p.hasPermission("shakedatass." + this.plantation.getType().name().toLowerCase() + "." + this.plantation.name().toLowerCase() + "." + entry.getKey().name().toLowerCase())) {
					//System.out.println(" chance perm on true : " + entry.getKey() + " & player does not have perm");
					return Chance.NOTHING;

				}

				//System.out.println("chance perm on false : " + entry.getKey() + " or player have perm");
				return entry.getKey();

			}

			last += entry.getValue().getChance();

		}

		return Chance.NOTHING;

	}

}
