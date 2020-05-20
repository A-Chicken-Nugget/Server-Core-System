package nyeblock.Core.ServerCoreTest.CustomChests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	private HashMap<ChestValue,HashMap<ItemStack,Integer>> chestValueItems = new HashMap<ChestValue,HashMap<ItemStack,Integer>> () {{
		//Common items
		put(ChestValue.COMMON,new HashMap<ItemStack,Integer>() {{
			//Weapons/tools
			put(new ItemStack(Material.WOODEN_SWORD),1);
			put(new ItemStack(Material.WOODEN_AXE),1);
			put(new ItemStack(Material.WOODEN_PICKAXE),1);
			put(new ItemStack(Material.WOODEN_SWORD),1);
			put(new ItemStack(Material.WOODEN_AXE),1);
			put(new ItemStack(Material.WOODEN_PICKAXE),1);
			put(new ItemStack(Material.STONE_SWORD),1);
			put(new ItemStack(Material.STONE_AXE),1);
			put(new ItemStack(Material.STONE_PICKAXE),1);
			//Armor
			put(new ItemStack(Material.LEATHER_BOOTS),1);
			put(new ItemStack(Material.LEATHER_CHESTPLATE),1);
			put(new ItemStack(Material.LEATHER_LEGGINGS),1);
			put(new ItemStack(Material.LEATHER_HELMET),1);
			put(new ItemStack(Material.IRON_BOOTS),1);
			put(new ItemStack(Material.IRON_HELMET),1);
			put(new ItemStack(Material.CHAINMAIL_LEGGINGS),1);
			put(new ItemStack(Material.CHAINMAIL_CHESTPLATE),1);
			//Potions
			put(createPotion(PotionType.INSTANT_DAMAGE,1),1);
		}});
		//Medium items
		put(ChestValue.MEDIUM,new HashMap<ItemStack,Integer>() {{
			//Weapons/tools
			put(new ItemStack(Material.IRON_SWORD),1);
			put(new ItemStack(Material.IRON_PICKAXE),1);
			put(new ItemStack(Material.STONE_PICKAXE),1);
			put(new ItemStack(Material.IRON_AXE),1);
			put(new ItemStack(Material.IRON_LEGGINGS),1);
			put(new ItemStack(Material.IRON_CHESTPLATE),1);
			put(new ItemStack(Material.IRON_BOOTS),1);
			put(new ItemStack(Material.IRON_HELMET),1);
			//Armor
			put(new ItemStack(Material.CHAINMAIL_CHESTPLATE),1);
			put(new ItemStack(Material.CHAINMAIL_LEGGINGS),1);
			put(new ItemStack(Material.CHAINMAIL_HELMET),1);
			put(new ItemStack(Material.CHAINMAIL_CHESTPLATE),1);
			put(new ItemStack(Material.CHAINMAIL_LEGGINGS),1);
			put(new ItemStack(Material.CHAINMAIL_HELMET),1);
			//Potions
			put(createPotion(PotionType.INSTANT_DAMAGE,1),1);
			put(createPotion(PotionType.REGEN,Toolkit.GetRandomNumber(1,2)),1);
			put(createPotion(PotionType.POISON,1),1);
			//Etc
			put(new ItemStack(Material.EXPERIENCE_BOTTLE,Toolkit.GetRandomNumber(25,64)),1);
			put(new ItemStack(Material.BOOKSHELF,Toolkit.GetRandomNumber(5,13)),1);
			put(createEnchantmentBook(Enchantment.DAMAGE_ALL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantmentBook(Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantmentBook(Enchantment.ARROW_DAMAGE,Toolkit.GetRandomNumber(1,2)),1);
		}});
		//High items
		put(ChestValue.HIGH,new HashMap<ItemStack,Integer>() {{
			//Weapons/tools
			put(new ItemStack(Material.DIAMOND_SWORD),1);
			put(createEnchantedItem(Material.DIAMOND_SWORD,Enchantment.DAMAGE_ALL,1),1);
			put(new ItemStack(Material.DIAMOND_AXE),2);
			put(new ItemStack(Material.IRON_AXE),2);
			//Armor
			put(new ItemStack(Material.IRON_LEGGINGS),2);
			put(new ItemStack(Material.IRON_CHESTPLATE),2);
			put(new ItemStack(Material.IRON_BOOTS),2);
			put(new ItemStack(Material.IRON_HELMET),2);
			put(createEnchantedItem(Material.IRON_LEGGINGS,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantedItem(Material.IRON_CHESTPLATE,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantedItem(Material.IRON_BOOTS,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantedItem(Material.IRON_HELMET,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(new ItemStack(Material.DIAMOND_LEGGINGS),1);
			put(new ItemStack(Material.DIAMOND_HELMET),1);
			put(new ItemStack(Material.DIAMOND_BOOTS),1);
			//Potions
			put(createPotion(PotionType.INSTANT_DAMAGE,1),1);
			put(createPotion(PotionType.REGEN,Toolkit.GetRandomNumber(1,2)),1);
			put(createPotion(PotionType.POISON,Toolkit.GetRandomNumber(1,2)),1);
			//Etc
			put(new ItemStack(Material.EXPERIENCE_BOTTLE,Toolkit.GetRandomNumber(25,64)),1);
			put(new ItemStack(Material.BOOKSHELF,Toolkit.GetRandomNumber(5,13)),1);
			put(createEnchantmentBook(Enchantment.DAMAGE_ALL,Toolkit.GetRandomNumber(1,4)),1);
			put(createEnchantmentBook(Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,4)),1);
			put(createEnchantmentBook(Enchantment.ARROW_DAMAGE,Toolkit.GetRandomNumber(1,4)),1);
			put(new ItemStack(Material.ENCHANTING_TABLE),2);
			put(new ItemStack(Material.ANVIL),2);
		}});
		//Legendary items
		put(ChestValue.LEGENDARY,new HashMap<ItemStack,Integer>() {{
			//Weapons/tools
			put(new ItemStack(Material.DIAMOND_SWORD),1);
			put(new ItemStack(Material.DIAMOND_AXE),1);
			put(createEnchantedItem(Material.DIAMOND_SWORD,Enchantment.DAMAGE_ALL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantedItem(Material.DIAMOND_AXE,Enchantment.DAMAGE_ALL,Toolkit.GetRandomNumber(1,2)),1);
			//Armor
			put(new ItemStack(Material.DIAMOND_LEGGINGS),1);
			put(new ItemStack(Material.DIAMOND_HELMET),1);
			put(new ItemStack(Material.DIAMOND_BOOTS),1);
			put(new ItemStack(Material.DIAMOND_CHESTPLATE),1);
			put(createEnchantedItem(Material.DIAMOND_LEGGINGS,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantedItem(Material.DIAMOND_HELMET,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantedItem(Material.DIAMOND_BOOTS,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			put(createEnchantedItem(Material.DIAMOND_CHESTPLATE,Enchantment.PROTECTION_ENVIRONMENTAL,Toolkit.GetRandomNumber(1,2)),1);
			//Etc
//			put(createEnchantmentBook(Enchantment.FIRE_ASPECT,Toolkit.GetRandomNumber(1,3)),1);
		}});
	}};
	private ArrayList<ItemStack> defaultItems = new ArrayList<ItemStack>() {{
		add(new ItemStack(Material.GLASS,Toolkit.GetRandomNumber(10,64)));
		add(new ItemStack(Material.GRAY_CONCRETE,Toolkit.GetRandomNumber(5,45)));
		add(new ItemStack(Material.OAK_PLANKS,Toolkit.GetRandomNumber(5,45)));
		add(new ItemStack(Material.COOKED_BEEF,Toolkit.GetRandomNumber(2,8)));
		add(new ItemStack(Material.COOKED_CHICKEN,Toolkit.GetRandomNumber(2,8)));
		add(new ItemStack(Material.GRAY_CONCRETE,Toolkit.GetRandomNumber(5,45)));
		add(new ItemStack(Material.OAK_PLANKS,Toolkit.GetRandomNumber(5,45)));
		add(new ItemStack(Material.COOKED_BEEF,Toolkit.GetRandomNumber(2,8)));
		add(new ItemStack(Material.COOKED_CHICKEN,Toolkit.GetRandomNumber(2,8)));
		add(new ItemStack(Material.APPLE,Toolkit.GetRandomNumber(2,8)));
		add(new ItemStack(Material.SNOWBALL,Toolkit.GetRandomNumber(1,16)));
		add(new ItemStack(Material.EGG,Toolkit.GetRandomNumber(1,16)));
		add(new ItemStack(Material.BOW));
		add(new ItemStack(Material.ARROW,Toolkit.GetRandomNumber(2,20)));
		add(new ItemStack(Material.COBWEB,Toolkit.GetRandomNumber(1,5)));
	}};
	
	private ItemStack createPotion(PotionType type, int level) {
		Potion pot = new Potion(type);
		pot.setSplash(true);
		return pot.toItemStack(1);
	}
	private ItemStack createEnchantmentBook(Enchantment enchantment, int level) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
		meta.addStoredEnchant(enchantment, level, false);
		item.setItemMeta(meta);
		
		return item;
	}
	public ItemStack createEnchantedItem(Material item, Enchantment enchantment, int level) {
		ItemStack newItem = new ItemStack(item);
		newItem.addEnchantment(enchantment,level);
		
		return newItem;
	}
	public ArrayList<ItemStack> getChestItems(ChestValue value) {
		ArrayList<ItemStack> itemPool = new ArrayList<>();
		ArrayList<ItemStack> items = new ArrayList<>();
		
		for (Map.Entry<ItemStack,Integer> entry : chestValueItems.get(value).entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				itemPool.add(entry.getKey());
			}
		}
		if (value.getShouldIncludeDefault()) {
			itemPool.addAll(defaultItems);
		}
		for (int i = 0; i < Toolkit.GetRandomNumber(value.getMin(),value.getMax()); i++) {
			ItemStack item = itemPool.get(new Random().nextInt(itemPool.size()));
			
			if (!items.contains(item)) {
				items.add(item);
			}
		}
		return items;
	}
}
