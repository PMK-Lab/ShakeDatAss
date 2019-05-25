package fr.hysoria.shakedatass.config;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.hysoria.shakedatass.config.GlobalGrowingProperties.Chance;

public class Properties {

	private Material material;
	private boolean permission;
	private Plantations type;
	private HashMap<GlobalGrowingProperties.Chance, GrowingProperties> growingChances;

	public Properties(Material m,boolean p,Plantations t) {

		this.material = m;
		this.permission = p;
		this.type = t;
		this.growingChances = this.initGrowingChances();	

	}

	public boolean needPermission() {
		return permission;
	}

	public Plantations getType() {
		return this.type;
	}

	public Material getMaterial() {
		return material;
	}

	public HashMap<GrowingProperties.Chance, GrowingProperties> getGrowingChances() {
		return growingChances;
	}

	public void setGrowingChance(Chance c,GrowingProperties g) {

		this.growingChances.replace(c, g);

	}
	
	public void checkGrowingValues() throws Exception {
		
		int i = 0;
		
		for (GrowingProperties gp : this.growingChances.values()) {
			
			i += gp.getChance();
			
		}
		
		if(i != 100) {
			throw new Exception("Total of the growing chances for " + this.material + " must be 100 (not " + i + ")");
		}
		
		
	}

	public HashMap<GrowingProperties.Chance, GrowingProperties> initGrowingChances() {

		HashMap<GrowingProperties.Chance, GrowingProperties> hash = new HashMap<GrowingProperties.Chance, GrowingProperties>();
		
		for (Chance chance : Chance.plantationValues(this.type)) {
			
			hash.put(chance, new GrowingProperties(false, Particle.CLOUD, chance.chanceValue(this.type)));
			
		}
		
		return hash;

	}

	public void apply(Block block, Chance c) {
		
		
		
	}

	public Chance getRandomGrowingChance(Player p) {
		
		int r = (int) (Math.random() * 100);
		System.out.println("Random = " + r);
		
		int last = 0;
		for (Entry<Chance, GrowingProperties> entry : growingChances.entrySet()) {
			
			if(r >= last && r < entry.getValue().getChance()) {
				
				System.out.println("Match : " + entry.getKey());
				
				if(GlobalProperties.INSTANCE().GLOBALS_DEFAULT_GROWING_CHANCES.get(entry.getKey()).isPermission() && !p.hasPermission("shakedatass.globals." + entry.getKey().name().toLowerCase())){
					System.out.println();
					return Chance.NOTHING;
				}
				
				if(entry.getValue().isPermission() && !p.hasPermission("")) {
					System.out.println();
					return Chance.NOTHING;
					
				}else {
					return entry.getKey();
				}
				
			}
			
			last = entry.getValue().getChance();
			
		}
		
		return Chance.NOTHING;
		
	}
	
}
