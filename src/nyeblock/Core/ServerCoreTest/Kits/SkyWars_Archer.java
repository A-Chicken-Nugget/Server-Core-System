package nyeblock.Core.ServerCoreTest.Kits;

import java.util.HashMap;

import org.bukkit.Material;

import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

public class SkyWars_Archer extends KitBase {
	public SkyWars_Archer() {
		super("Archer", new HashMap<InventoryItem,Integer>() {{
			put(new InventoryItem(new ItemStack(Material.BOW)))
		}});
	}
}
