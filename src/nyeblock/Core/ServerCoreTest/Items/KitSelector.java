package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

public class KitSelector {
	private Realm realm;
	
	public KitSelector(Realm realm) {
		this.realm = realm;
	}
	
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Select Kit" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("kit_selector");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void openMenu(Player ply, Main mainInstance) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Select a Kit");
		String kitSelected = null;
		RealmBase game = mainInstance.getPlayerHandlingInstance().getPlayerData(ply).getCurrentRealm();
		
		if (realm == Realm.KITPVP) {
			kitSelected = game.getPlayerKit(ply);
			
			//Knight kit
			ItemStack knight = new ItemStack(Material.IRON_CHESTPLATE);
			ItemMeta knightMeta = knight.getItemMeta();
			knightMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Knight");
			knightMeta.setLocalizedName("knight");
			knightMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<String> kitPvpMetaLore = new ArrayList<String>();
			kitPvpMetaLore.add(ChatColor.YELLOW + "- Iron Sword (Sharpness 1)");
			kitPvpMetaLore.add(ChatColor.YELLOW + "- Iron Armor (Protection 1)");
			kitPvpMetaLore.add(ChatColor.YELLOW + "- x1 Golden Apple");
			kitPvpMetaLore.add(ChatColor.RESET.toString());
			if (kitSelected.equalsIgnoreCase("knight")) {
				kitPvpMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
			} else {
				kitPvpMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
			}
			knightMeta.setLore(kitPvpMetaLore);
			knight.setItemMeta(knightMeta);
			//Brawler kit
			ItemStack brawler = new ItemStack(Material.IRON_AXE);
			ItemMeta brawlerMeta = brawler.getItemMeta();
			brawlerMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Brawler");
			brawlerMeta.setLocalizedName("brawler");
			brawlerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<String> brawlerMetaLore = new ArrayList<String>();
			brawlerMetaLore.add(ChatColor.YELLOW + "- Iron Axe (Sharpness 2)");
			brawlerMetaLore.add(ChatColor.YELLOW + "- Leather Armor (Protection 2)");
			brawlerMetaLore.add(ChatColor.YELLOW + "- x5 Golden Apple");
			brawlerMetaLore.add(ChatColor.RESET.toString());
			if (kitSelected.equalsIgnoreCase("brawler")) {
				brawlerMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
			} else {
				brawlerMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
			}
			brawlerMeta.setLore(brawlerMetaLore);
			brawler.setItemMeta(brawlerMeta);
			//Archer kit
			ItemStack archer = new ItemStack(Material.BOW);
			ItemMeta archerMeta = archer.getItemMeta();
			archerMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Archer");
			archerMeta.setLocalizedName("archer");
			archerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<String> archerMetaLore = new ArrayList<String>();
			archerMetaLore.add(ChatColor.YELLOW + "- Gold Sword (Sharpness 1)");
			archerMetaLore.add(ChatColor.YELLOW + "- Chain Armor (Protection 1)");
			archerMetaLore.add(ChatColor.YELLOW + "- Bow (Power 1)");
			archerMetaLore.add(ChatColor.YELLOW + "- x40 Arrows");
			archerMetaLore.add(ChatColor.RESET.toString());
			if (kitSelected.equalsIgnoreCase("archer")) {
				archerMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
			} else {
				archerMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
			}
			archerMeta.setLore(archerMetaLore);
			archer.setItemMeta(archerMeta);
			//Wizard kit
			ItemStack wizard = new ItemStack(Material.POTION);
			ItemMeta wizardMeta = wizard.getItemMeta();
			wizardMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Wizard");
			wizardMeta.setLocalizedName("wizard");
			wizardMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			wizardMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			ArrayList<String> wizardMetaLore = new ArrayList<String>();
			wizardMetaLore.add(ChatColor.YELLOW + "- Iron Sword");
			wizardMetaLore.add(ChatColor.YELLOW + "- Leather Armor (Protection 1)");
			wizardMetaLore.add(ChatColor.YELLOW + "- x5 Fire balls");
			wizardMetaLore.add(ChatColor.YELLOW + "- x2 Potion of harm");
			wizardMetaLore.add(ChatColor.YELLOW + "- x1 Potion of regeneration");
			wizardMetaLore.add(ChatColor.RESET.toString());
			if (kitSelected.equalsIgnoreCase("wizard")) {
				wizardMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
			} else {
				wizardMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
			}
			wizardMeta.setLore(wizardMetaLore);
			wizard.setItemMeta(wizardMeta);
			
			menu.setItem(1, knight);
			menu.setItem(3, brawler);
			menu.setItem(5, archer);
			menu.setItem(7, wizard);
			ply.openInventory(menu);
		} else if (realm == Realm.SKYWARS) {
			kitSelected = game.getPlayerKit(ply);
			
			//Default kit
			ItemStack defaultItems = new ItemStack(Material.WOODEN_SWORD);
			ItemMeta defaultItemsMeta = defaultItems.getItemMeta();
			defaultItemsMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Default");
			defaultItemsMeta.setLocalizedName("default");
			defaultItemsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<String> defaultItemsMetaLore = new ArrayList<String>();
			defaultItemsMetaLore.add(ChatColor.YELLOW + "- Wood Sword");
			defaultItemsMetaLore.add(ChatColor.YELLOW + "- x15 Concrete blocks");
			defaultItemsMetaLore.add(ChatColor.YELLOW + "- x10 Beef");
			defaultItemsMetaLore.add(ChatColor.RESET.toString());
			if (kitSelected.equalsIgnoreCase("default")) {
				defaultItemsMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
			} else {
				defaultItemsMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
			}
			defaultItemsMeta.setLore(defaultItemsMetaLore);
			defaultItems.setItemMeta(defaultItemsMeta);
//			//Brawler kit
//			ItemStack brawler = new ItemStack(Material.IRON_AXE);
//			ItemMeta brawlerMeta = brawler.getItemMeta();
//			brawlerMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Brawler");
//			brawlerMeta.setLocalizedName("brawler");
//			brawlerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//			ArrayList<String> brawlerMetaLore = new ArrayList<String>();
//			brawlerMetaLore.add(ChatColor.YELLOW + "- Iron Axe (Sharpness 2)");
//			brawlerMetaLore.add(ChatColor.YELLOW + "- Leather Armor (Protection 2)");
//			brawlerMetaLore.add(ChatColor.YELLOW + "- x5 Golden Apple");
//			brawlerMetaLore.add(ChatColor.RESET.toString());
//			if (kitSelected.equalsIgnoreCase("brawler")) {
//				brawlerMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
//			} else {
//				brawlerMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
//			}
//			brawlerMeta.setLore(brawlerMetaLore);
//			brawler.setItemMeta(brawlerMeta);
//			//Archer kit
//			ItemStack archer = new ItemStack(Material.BOW);
//			ItemMeta archerMeta = archer.getItemMeta();
//			archerMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Archer");
//			archerMeta.setLocalizedName("archer");
//			archerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//			ArrayList<String> archerMetaLore = new ArrayList<String>();
//			archerMetaLore.add(ChatColor.YELLOW + "- Gold Sword (Sharpness 1)");
//			archerMetaLore.add(ChatColor.YELLOW + "- Chain Armor (Protection 1)");
//			archerMetaLore.add(ChatColor.YELLOW + "- Bow (Power 1)");
//			archerMetaLore.add(ChatColor.YELLOW + "- x40 Arrows");
//			archerMetaLore.add(ChatColor.RESET.toString());
//			if (kitSelected.equalsIgnoreCase("archer")) {
//				archerMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
//			} else {
//				archerMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
//			}
//			archerMeta.setLore(archerMetaLore);
//			archer.setItemMeta(archerMeta);
//			//Wizard kit
//			ItemStack wizard = new ItemStack(Material.POTION);
//			ItemMeta wizardMeta = wizard.getItemMeta();
//			wizardMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Wizard");
//			wizardMeta.setLocalizedName("wizard");
//			wizardMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//			wizardMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//			ArrayList<String> wizardMetaLore = new ArrayList<String>();
//			wizardMetaLore.add(ChatColor.YELLOW + "- Iron Sword");
//			wizardMetaLore.add(ChatColor.YELLOW + "- Leather Armor (Protection 1)");
//			wizardMetaLore.add(ChatColor.YELLOW + "- x5 Fire balls");
//			wizardMetaLore.add(ChatColor.YELLOW + "- x2 Potion of harm");
//			wizardMetaLore.add(ChatColor.YELLOW + "- x1 Potion of regeneration");
//			wizardMetaLore.add(ChatColor.RESET.toString());
//			if (kitSelected.equalsIgnoreCase("wizard")) {
//				wizardMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
//			} else {
//				wizardMetaLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
//			}
//			wizardMeta.setLore(wizardMetaLore);
//			wizard.setItemMeta(wizardMeta);
			
			menu.setItem(4, defaultItems);
//			menu.setItem(3, brawler);
//			menu.setItem(5, archer);
//			menu.setItem(7, wizard);
			ply.openInventory(menu);
		}
	}
	public void clickItem(Player ply, String item, Main mainInstance) {
		boolean kitChanged = false;
		RealmBase game = mainInstance.getPlayerHandlingInstance().getPlayerData(ply).getCurrentRealm();
		
		if (realm == Realm.KITPVP) {
			if (item.equalsIgnoreCase("knight")) {
				if (game.isInServer(ply)) {
					if (!game.getPlayerKit(ply).equalsIgnoreCase("knight")) {
						kitChanged = true;
						game.setPlayerKit(ply, "knight");
						ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					}
				}
			} else if (item.equalsIgnoreCase("brawler")) {
				if (game.isInServer(ply)) {
					if (!game.getPlayerKit(ply).equalsIgnoreCase("brawler")) {
						kitChanged = true;
						game.setPlayerKit(ply, "brawler");
						ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					}
				}
			} else if (item.equalsIgnoreCase("archer")) {
				if (game.isInServer(ply)) {
					if (!game.getPlayerKit(ply).equalsIgnoreCase("archer")) {
						kitChanged = true;
						game.setPlayerKit(ply, "archer");
						ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					}
				}
			} else if (item.equalsIgnoreCase("wizard")) {
				if (game.isInServer(ply)) {
					if (!game.getPlayerKit(ply).equalsIgnoreCase("wizard")) {
						kitChanged = true;
						game.setPlayerKit(ply, "wizard");
						ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					}
				}
			}
		} else if (realm == Realm.SKYWARS) {
			if (item.equalsIgnoreCase("default")) {
				if (game.isInServer(ply)) {
					if (!game.getPlayerKit(ply).equalsIgnoreCase("default")) {
						kitChanged = true;
						game.setPlayerKit(ply, "default");
						ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					}
				}
			}
		}
		if (kitChanged) {			
			ply.sendMessage(ChatColor.YELLOW + "You equipped the " + ChatColor.BOLD + item + ChatColor.RESET + ChatColor.YELLOW + " kit!");
		}
	}
}
