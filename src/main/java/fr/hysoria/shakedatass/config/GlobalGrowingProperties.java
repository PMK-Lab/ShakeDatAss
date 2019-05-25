package fr.hysoria.shakedatass.config;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Particle;

public class GlobalGrowingProperties{
	
	private boolean permission;
	private Particle particle;

	public GlobalGrowingProperties(boolean permission,Particle particle) {
		this.particle = particle;
		this.permission = permission;
	}
	
	public boolean isPermission() {
		return permission;
	}

	public Particle getParticle() {
		return particle;
	}

	public static enum Chance {

		NOTHING(new SimpleEntry<Plantations, Integer>(Plantations.SEEDS, 75), new SimpleEntry<Plantations, Integer>(Plantations.STEMS, 75), new SimpleEntry<Plantations, Integer>(Plantations.SAPLINGS, 85)),
		
		ONE_STEP(new SimpleEntry<Plantations, Integer>(Plantations.SEEDS, 20)),
		
		FULLY(new SimpleEntry<Plantations, Integer>(Plantations.SEEDS, 5), new SimpleEntry<Plantations, Integer>(Plantations.STEMS, 25), new SimpleEntry<Plantations, Integer>(Plantations.SAPLINGS, 15));

		private HashMap<Plantations, Integer> defaultChances;
		
		@SafeVarargs
		private Chance(SimpleEntry<Plantations, Integer>... plantations) {
			
			this.defaultChances = new HashMap<Plantations, Integer>();
			for (SimpleEntry<Plantations, Integer> simpleEntry : plantations) {
				defaultChances.put(simpleEntry.getKey(), simpleEntry.getValue());
			}
			
		}
		
		public Integer chanceValue(Plantations seeds) {
			
			if(this.defaultChances.containsKey(seeds)) {
				return this.defaultChances.get(seeds);
			}
			return null;
			
		}

		public static List<Chance> plantationValues(Plantations plantation) {
			List<Chance> list = new ArrayList<GlobalGrowingProperties.Chance>();
			
			for (Chance chance : Chance.values()) {
				if(chance.chanceValue(plantation) != null) {
					list.add(chance);
				}				
			}
			
			return list;
		}
		
	}
	
}
