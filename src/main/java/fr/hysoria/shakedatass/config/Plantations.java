package fr.hysoria.shakedatass.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public enum Plantations {

	SEEDS(Arrays.asList(Material.WHEAT,Material.CARROTS,Material.POTATOES,Material.BEETROOTS)),
	
	STEMS(Arrays.asList(Material.ATTACHED_MELON_STEM,Material.MELON_STEM,Material.ATTACHED_PUMPKIN_STEM,Material.PUMPKIN_STEM)),
	
	SAPLINGS(Arrays.asList(Material.SPRUCE_SAPLING,Material.OAK_SAPLING,Material.JUNGLE_SAPLING,Material.DARK_OAK_SAPLING,Material.BIRCH_SAPLING,Material.ACACIA_SAPLING));

	private List<Material> materials;
	
	private Plantations(List<Material> materials) {
		this.materials = materials;
	}
	
	public List<Material> getMaterials() {
		return this.materials;
	}
	
	
	
}
