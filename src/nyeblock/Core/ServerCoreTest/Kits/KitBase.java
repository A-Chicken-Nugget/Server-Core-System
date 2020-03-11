package nyeblock.Core.ServerCoreTest.Kits;

import java.util.HashMap;

import org.bukkit.Material;

public class KitBase {
	private String name;
	private HashMap<Material,Integer> items;
	
	public KitBase(String name, HashMap<Material,Integer> items) {
		this.name = name;
		this.items = items;
	}
	
	public String getName() {
		return name;
	}
	public HashMap<Material,Integer> getItems() {
		return items;
	}
}
