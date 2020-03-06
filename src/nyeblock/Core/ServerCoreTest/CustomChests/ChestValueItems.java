package nyeblock.Core.ServerCoreTest.CustomChests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings({ "serial", "deprecation" })
public class ChestValueItems {
	private HashMap<ChestValue,HashMap<Material,Integer>> chestValueItems = new HashMap<ChestValue,HashMap<Material,Integer>> () {{
		//Common items
		put(ChestValue.COMMON,new HashMap<Material,Integer>() {{
			put(Material.WOODEN_SWORD,1);
			put(Material.WOODEN_AXE,1);
			put(Material.WOODEN_PICKAXE,1);
			put(Material.WOODEN_SWORD,1);
			put(Material.WOODEN_AXE,1);
			put(Material.WOODEN_PICKAXE,1);
			put(Material.STONE_SWORD,1);
			put(Material.STONE_AXE,1);
			put(Material.STONE_PICKAXE,1);
			put(Material.LEATHER_BOOTS,1);
			put(Material.LEATHER_CHESTPLATE,1);
			put(Material.LEATHER_LEGGINGS,1);
			put(Material.LEATHER_HELMET,1);
			put(Material.IRON_BOOTS,1);
			put(Material.IRON_HELMET,1);
			put(Material.CHAINMAIL_LEGGINGS,1);
			put(Material.CHAINMAIL_CHESTPLATE,1);
			put(Material.POTION,1);
		}});
		//Medium items
		put(ChestValue.MEDIUM,new HashMap<Material,Integer>() {{
			put(Material.IRON_SWORD,1);
			put(Material.IRON_PICKAXE,1);
			put(Material.STONE_PICKAXE,1);
			put(Material.IRON_AXE,1);
			put(Material.IRON_LEGGINGS,1);
			put(Material.IRON_CHESTPLATE,1);
			put(Material.IRON_BOOTS,1);
			put(Material.IRON_HELMET,1);
			put(Material.CHAINMAIL_CHESTPLATE,1);
			put(Material.CHAINMAIL_LEGGINGS,1);
			put(Material.CHAINMAIL_HELMET,1);
			put(Material.CHAINMAIL_CHESTPLATE,1);
			put(Material.CHAINMAIL_LEGGINGS,1);
			put(Material.CHAINMAIL_HELMET,1);
			put(Material.POTION,1);
			put(Material.EXPERIENCE_BOTTLE,Toolkit.GetRandomNumber(25,64));
			put(Material.BOOKSHELF,Toolkit.GetRandomNumber(5,13));
			put(Material.ENCHANTED_BOOK,1);
		}});
		//High items
		put(ChestValue.HIGH,new HashMap<Material,Integer>() {{
			put(Material.DIAMOND_SWORD,1);
			put(Material.DIAMOND_AXE,1);
			put(Material.IRON_AXE,1);
			put(Material.IRON_LEGGINGS,1);
			put(Material.IRON_CHESTPLATE,1);
			put(Material.IRON_BOOTS,1);
			put(Material.IRON_HELMET,1);
			put(Material.IRON_AXE,1);
			put(Material.IRON_LEGGINGS,1);
			put(Material.IRON_CHESTPLATE,1);
			put(Material.IRON_BOOTS,1);
			put(Material.IRON_HELMET,1);
			put(Material.DIAMOND_LEGGINGS,1);
			put(Material.DIAMOND_HELMET,1);
			put(Material.DIAMOND_BOOTS,1);
			put(Material.EXPERIENCE_BOTTLE,Toolkit.GetRandomNumber(25,64));
			put(Material.BOOKSHELF,Toolkit.GetRandomNumber(5,13));
			put(Material.ENCHANTED_BOOK,1);
			put(Material.ENCHANTING_TABLE,1);
			put(Material.ANVIL,1);
			put(Material.ENCHANTING_TABLE,1);
			put(Material.ANVIL,1);
		}});
		//Legendary items
		put(ChestValue.LEGENDARY,new HashMap<Material,Integer>() {{
			put(Material.DIAMOND_SWORD,1);
			put(Material.DIAMOND_AXE,1);
			put(Material.DIAMOND_LEGGINGS,1);
			put(Material.DIAMOND_HELMET,1);
			put(Material.DIAMOND_BOOTS,1);
			put(Material.DIAMOND_CHESTPLATE,1);
		}});
	}};
	private HashMap<Material,Integer> defaultItems = new HashMap<Material,Integer>() {{
		put(Material.GLASS,Toolkit.GetRandomNumber(10,64));
		put(Material.GRAY_CONCRETE,Toolkit.GetRandomNumber(5,45));
		put(Material.OAK_PLANKS,Toolkit.GetRandomNumber(5,45));
		put(Material.COOKED_BEEF,Toolkit.GetRandomNumber(2,8));
		put(Material.COOKED_CHICKEN,Toolkit.GetRandomNumber(2,8));
		put(Material.GRAY_CONCRETE,Toolkit.GetRandomNumber(5,45));
		put(Material.OAK_PLANKS,Toolkit.GetRandomNumber(5,45));
		put(Material.COOKED_BEEF,Toolkit.GetRandomNumber(2,8));
		put(Material.COOKED_CHICKEN,Toolkit.GetRandomNumber(2,8));
		put(Material.APPLE,Toolkit.GetRandomNumber(2,8));
		put(Material.SNOWBALL,Toolkit.GetRandomNumber(1,16));
		put(Material.EGG,Toolkit.GetRandomNumber(1,16));
		put(Material.BOW,1);
		put(Material.ARROW,Toolkit.GetRandomNumber(2,20));
		put(Material.COBWEB,Toolkit.GetRandomNumber(1,5));
	}};
	private HashMap<Enchantment,Integer> enchantments = new HashMap<Enchantment,Integer>() {{
		put(Enchantment.DAMAGE_ALL,Toolkit.GetRandomNumber(1,4));
		put(Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,4));
		put(Enchantment.ARROW_DAMAGE,Toolkit.GetRandomNumber(1,4));
	}};
	private ArrayList<Potion> potions = new ArrayList<Potion>() {{
		add(new Potion(PotionType.INSTANT_DAMAGE, 1));
		add(new Potion(PotionType.REGEN, Toolkit.GetRandomNumber(1,2)));
		add(new Potion(PotionType.POISON, Toolkit.GetRandomNumber(1,2)));
	}};
	
	public ArrayList<ItemStack> getChestItems(ChestValue value) {
		HashMap<Material,Integer> itemPool = chestValueItems.get(value);
		if (value.getShouldIncludeDefault()) {			
			itemPool.putAll(defaultItems);
		}
		ArrayList<ItemStack> items = new ArrayList<>();
		ArrayList<Material> addedMaterials = new ArrayList<>();
		
		for (int i = 0; i < Toolkit.GetRandomNumber(value.getMin(),value.getMax()); i++) {
			List<Material> keysAsArray = new ArrayList<Material>(chestValueItems.get(value).keySet());
			Material item = keysAsArray.get(new Random().nextInt(keysAsArray.size()));
			
			if (!addedMaterials.contains(item)) {
				ItemStack newItem = new ItemStack(item,chestValueItems.get(value).get(item));
				
				if (item.equals(Material.ENCHANTED_BOOK)) {
					List<Enchantment> keysArray = new ArrayList<Enchantment>(enchantments.keySet());
					Enchantment randEnchantment = keysArray.get(new Random().nextInt(keysArray.size()));
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta)newItem.getItemMeta();
					meta.addStoredEnchant(randEnchantment, enchantments.get(randEnchantment), false);
					newItem.setItemMeta(meta);
				} else if (item.equals(Material.POTION)) {
					Potion pot = potions.get(new Random().nextInt(potions.size()));
					pot.setSplash(true);
					newItem = pot.toItemStack(1);
				}
				
				items.add(newItem);
				addedMaterials.add(item);
			}
		}
		return items;
	}
}
