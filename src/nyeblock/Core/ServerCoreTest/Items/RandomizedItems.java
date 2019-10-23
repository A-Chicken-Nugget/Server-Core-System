package nyeblock.Core.ServerCoreTest.Items;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import nyeblock.Core.ServerCoreTest.Misc.ArmorRating;
import nyeblock.Core.ServerCoreTest.Misc.Enums;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.WeaponDamage;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;
import nyeblock.Core.ServerCoreTest.Items.Weapon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.data.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class RandomizedItems 
{
	private ArrayList<ItemStack> ItemCatalog = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> ShitItems = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> BadItems = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> AverageItems = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> GoodItems = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> LegendaryItems = new ArrayList<ItemStack>();

	// TODO: Finish the randomized constructor
	public RandomizedItems(ArrayList<ItemStack> items) 
	{
		this.ItemCatalog = items;

		for (ItemStack item : items) 
		{
			// TODO: Simplify process
			// Checks to see if item is a weapon object
			if (item instanceof Weapon) 
			{
				Material weaponType = item.getType();
				
				ArrayList<HashMap<Material, Double>> weaponHashMaps = WeaponDamage.GetMaterials();

				// Each hash map
				for (HashMap<Material, Double> materialMap : weaponHashMaps) 
				{
					// Each entry in the hash map
					for (Map.Entry<Material, Double> entry : materialMap.entrySet()) 
					{
						//if (weaponType.getKey().toString() == entry.getKey().name())
						if (weaponType.getKey().getKey() == entry.getKey().name())
						{
							double weaponDamage = entry.getValue();

							if (weaponDamage <= (double) ChestValue.SHIT.getValue()) 
							{
								ShitItems.add(item);
							} else if (weaponDamage <= (double) ChestValue.BAD.getValue()) 
							{
								BadItems.add(item);
							} else if (weaponDamage <= (double) ChestValue.AVERAGE.getValue()) 
							{
								AverageItems.add(item);
							} else if (weaponDamage <= (double) ChestValue.GOOD.getValue()) 
							{
								GoodItems.add(item);
							} else if (weaponDamage <= (double) ChestValue.LEGENDARY.getValue()) 
							{
								LegendaryItems.add(item);
							}
						}
					}
				}
			} 
			//else if (itemType.equals(new Armor(itemType)))
			else if (item instanceof Armor) 
			{
				ArrayList<HashMap<Material, Integer>> armorHashMaps = ArmorRating.GetArmorMaps();
				Material armorType = item.getType();
				
				for (HashMap<Material, Integer> armorMap : armorHashMaps) 
				{
					for (Map.Entry<Material, Integer> entry : armorMap.entrySet()) 
					{
						if (armorType.getKey().getKey() == entry.getKey().name()) 
						{
							int armorRating = entry.getValue();
							
							if (armorRating <= 2) 
							{
								BadItems.add(item);
							}
							else if ((armorRating <= 4)) 
							{
								AverageItems.add(item);
							}
							else if (armorRating <= 6) 
							{
								GoodItems.add(item);
							}
							else if (armorRating <= 8) 
							{
								LegendaryItems.add(item);
							}
						}
					}
				}
			}
			else 
			{
				continue;
			}
		}
	}

	// TODO: Test the following method
	public ArrayList<ItemStack> GetItemsByTier(ChestValue value, int amount) 
	throws 	InvalidParameterException 
	{
		ChestValue chestValue = ChestValue.values()[value.getValue()];
		ArrayList<ItemStack> itemsByValue = new ArrayList<ItemStack>();
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		switch (chestValue) 
		{
			case SHIT:
				itemsByValue = ShitItems;
				break;
			case BAD:
				itemsByValue = BadItems;
				break;
			case AVERAGE:
				itemsByValue = AverageItems;
				break;
			case GOOD:
				itemsByValue = GoodItems;
				break;
			case LEGENDARY:
				itemsByValue = LegendaryItems;
				break;
			default:
				System.out.println("Pass in a legit chest value!");
				throw new InvalidParameterException();
		}
		
		for (int i = 0; i < amount; i++) 
		{
			int itemIndex = Toolkit.GetRandomNumber(0, (itemsByValue.size() - 1));
			
			items.add(itemsByValue.get(itemIndex));
		}

		return items;
	}
	
	public ArrayList<ItemStack> GetCatalog() 
	{
		return this.ItemCatalog;
	}
}