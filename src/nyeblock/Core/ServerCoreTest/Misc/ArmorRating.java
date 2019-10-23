package nyeblock.Core.ServerCoreTest.Misc;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;

@SuppressWarnings("serial")
public class ArmorRating 
{
	public static HashMap<Material, Integer> TurtleShell = new HashMap<Material, Integer>() 
	{{ put(Material.TURTLE_HELMET, 2); }};
	
	public static HashMap<Material, Integer> Leather = new HashMap<Material, Integer>() 
	{{
		put(Material.LEATHER_HELMET, 1);
		put(Material.LEATHER_CHESTPLATE, 3);
		put(Material.LEATHER_LEGGINGS, 2);
		put(Material.LEATHER_BOOTS, 1);
	}};
	
	public static HashMap<Material, Integer> Gold = new HashMap<Material, Integer>() 
	{{
		put(Material.GOLDEN_HELMET, 2);
		put(Material.GOLDEN_CHESTPLATE, 5);
		put(Material.GOLDEN_LEGGINGS, 3);
		put(Material.GOLDEN_BOOTS, 1);
	}};
	
	public static HashMap<Material, Integer> Chainmail = new HashMap<Material, Integer>() 
	{{
		put(Material.CHAINMAIL_HELMET, 2);
		put(Material.CHAINMAIL_CHESTPLATE, 5);
		put(Material.CHAINMAIL_LEGGINGS, 4);
		put(Material.CHAINMAIL_BOOTS, 1);
	}};
	
	public static HashMap<Material, Integer> Iron = new HashMap<Material, Integer>() 
	{{
		put(Material.IRON_HELMET, 2);
		put(Material.IRON_CHESTPLATE, 6);
		put(Material.IRON_LEGGINGS, 5);
		put(Material.IRON_BOOTS, 2);
	}};
	
	public static HashMap<Material, Integer> Diamond = new HashMap<Material, Integer>() 
	{{
		put(Material.DIAMOND_HELMET, 3);
		put(Material.DIAMOND_CHESTPLATE, 8);
		put(Material.DIAMOND_LEGGINGS, 6);
		put(Material.DIAMOND_BOOTS, 3);
	}};
	
	public static ArrayList<HashMap<Material, Integer>> GetArmorMaps() 
	{
		return new ArrayList<HashMap<Material, Integer>>() 
		{{
			add(TurtleShell);
			add(Leather);
			add(Iron);
			add(Gold);
			add(Chainmail);
			add(Diamond);
		}};
	}
	
	public static int GetRatingFromMaterial(Material material) 
	{
		ArrayList<HashMap<Material, Integer>> materialMaps = ArmorRating.GetArmorMaps();
		
		for (HashMap<Material, Integer> rating : materialMaps) 
		{
			for (Entry<Material, Integer> entry : rating.entrySet()) 
			{
				if (material == entry.getKey()) 
				{
					return entry.getValue();
				}
			}
		}
		
		throw new InvalidParameterException();
	}
}
