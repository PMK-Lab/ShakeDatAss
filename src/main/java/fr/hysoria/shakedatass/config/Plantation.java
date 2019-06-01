package fr.hysoria.shakedatass.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public class Plantation {
	
	private Material material;
	private Integer id;
	private String aliase;
	
	public Plantation(Material m) {
		this.material = m;
		this.aliase = null;
		this.id = null;
	}
	
	public Plantation(Material m, Integer i) {
		this(m);
		this.id = i;	
	}
	
	public Plantation(Material m, Integer i, String a) {
		this(m,i);
		this.aliase = a;
	}
	
	public Plantation(Material m, String a) {
		this(m);
		this.aliase = a;
	}
	
	public Plantations getType() {
		
		return Plantation.Plantations.getTypesOfPlantation(this);
		
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getID() {
		
		return this.id;
		
	}
	
	public String name() {		
		
		return (this.aliase != null) ? this.aliase.toLowerCase() : this.material.name().toLowerCase();
		
	}
	
	public enum Plantations {
		
		SEEDS(Arrays.asList(new Plantation(Material.CROPS,"wheat"),new Plantation(Material.CARROT),new Plantation(Material.POTATO),new Plantation(Material.BEETROOT_BLOCK))),
		
		STEMS(Arrays.asList(new Plantation(Material.MELON_STEM),new Plantation(Material.PUMPKIN_STEM))),
		
		RIPED_STEMS(Arrays.asList(new Plantation(Material.MELON_STEM,"riped_melon_stem"),new Plantation(Material.PUMPKIN_STEM,"riped_melon_stem"))),
		
		SAPLINGS(Arrays.asList(new Plantation(Material.SAPLING,0,"oak_sapling"),new Plantation(Material.SAPLING,1,"spruce_sapling"),new Plantation(Material.SAPLING,2,"birch_sapling"),new Plantation(Material.SAPLING,3,"jungle_sapling"),new Plantation(Material.SAPLING,4,"acacia_sapling"),new Plantation(Material.SAPLING,5,"dark_oak_sapling")));
		
		private List<Plantation> materials;
		
		private Plantations(List<Plantation> materials) {
			this.materials = materials;
		}

		public static Plantations getTypesOfPlantation(Plantation plantation) {
			
			for (Plantation p : SEEDS.materials) {
				if(p.name().equals(plantation.name())) return SEEDS;
			}
			
			for (Plantation p : STEMS.materials) {
				if(p.name().equals(plantation.name())) return STEMS;
			}
			
			for (Plantation p : RIPED_STEMS.materials) {
				if(p.name().equals(plantation.name())) return RIPED_STEMS;
			}
			
			for (Plantation p : SAPLINGS.materials) {
				if(p.name().equals(plantation.name())) return SAPLINGS;
			}
			
			return null;
		}

		public List<Plantation> getMaterials() {
			return this.materials;
		}
		
	}
	
}


