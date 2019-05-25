package fr.hysoria.shakedatass.config;

import org.bukkit.Particle;

public class GrowingProperties extends GlobalGrowingProperties {

	private int chance;

	public GrowingProperties(boolean permission, Particle particle, int c) {
		super(permission, particle);
		this.chance = c;
	}

	public int getChance() {
		return chance;
	}

}
