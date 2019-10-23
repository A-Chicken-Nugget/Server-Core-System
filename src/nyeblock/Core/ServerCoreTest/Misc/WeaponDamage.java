package nyeblock.Core.ServerCoreTest.Misc;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;

@SuppressWarnings("serial")
public class WeaponDamage 
{
	public static int Trident = 9;
	
	public static HashMap<Material, Double> Wood = new HashMap<Material, Double>() 
	{{
		put(Material.WOODEN_SWORD, 4.0);
		put(Material.WOODEN_AXE, 7.0);
		put(Material.WOODEN_PICKAXE, 2.0);
		put(Material.WOODEN_SHOVEL, 2.5);
	}};
	
	public static HashMap<Material, Double> Stone = new HashMap<Material, Double>() 
	{{
		put(Material.STONE_SWORD, 5.0);
		put(Material.STONE_AXE, 9.0);
		put(Material.STONE_PICKAXE , 3.0);
		put(Material.STONE_SHOVEL, 3.5);
	}};
	
	public static HashMap<Material, Double> Iron = new HashMap<Material, Double>() 
	{{
		put(Material.IRON_SWORD, 6.0);
		put(Material.IRON_AXE, 9.0);
		put(Material.IRON_PICKAXE, 4.0);
		put(Material.IRON_SHOVEL, 4.5);
	}};
	
	public static HashMap<Material, Double> Gold = new HashMap<Material, Double>()
	{{
		put(Material.GOLDEN_SWORD, 4.0);
		put(Material.GOLDEN_AXE, 7.0);
		put(Material.GOLDEN_PICKAXE, 2.0);
		put(Material.GOLDEN_SHOVEL, 2.5);
	}};
	
	public static HashMap<Material, Double> Diamond = new HashMap<Material, Double>()
	{{
		put(Material.DIAMOND_SWORD, 7.0);
		put(Material.DIAMOND_AXE, 9.0);
		put(Material.DIAMOND_PICKAXE, 5.0);
		put(Material.DIAMOND_SHOVEL, 5.5);
	}};
	
	public static ArrayList<HashMap<Material, Double>> GetMaterials() 
	{
		return new ArrayList<HashMap<Material, Double>>() 
		{{
			add(Wood);
			add(Stone);
			add(Iron);
			add(Gold);
			add(Diamond);
		}};
	}
	
	public static double GetDamageByMaterial(Material item) 
	{
		ArrayList<HashMap<Material, Double>> materialMaps = WeaponDamage.GetMaterials();
		
		for (HashMap<Material, Double> materials : materialMaps) 
		{
			for (Entry<Material, Double> entry : materials.entrySet()) 
			{
				if (item == entry.getKey()) 
				{
					return entry.getValue();
				}
			}
		}
		
		throw new InvalidParameterException();
	}
}