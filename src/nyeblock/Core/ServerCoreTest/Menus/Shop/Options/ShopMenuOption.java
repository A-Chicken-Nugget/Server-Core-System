package nyeblock.Core.ServerCoreTest.Menus.Shop.Options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Menus.MenuOption;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class ShopMenuOption extends ShopMenuOptionBase {
	private List<RequirementBase> requirements;
	
	public ShopMenuOption(String uniqueId, int position, int cost, List<RequirementBase> requirements, SubMenu subMenu, boolean multiPurchasable) {
		super(uniqueId,position,subMenu,multiPurchasable);
		
		hasPurchased = (playerData.getShopItem(uniqueId) != null ? true : false);
		quantity = (hasPurchased ? playerData.getShopItem(uniqueId).getQuantity() : 0);
		isEquipped = (hasPurchased ? playerData.getShopItem(uniqueId).isEquipped() : false);
		setCost(cost);
		this.requirements = requirements;
	}
	
	public void use() {
		ShopItem item = playerData.getShopItem(uniqueId);
		
		if (item != null) {
			if (multiPurchasable) {	
				playerData.removePoints(cost);
				playerData.addShopItem(uniqueId,subMenu.getTitle());
				getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
				player.sendMessage(ChatColor.YELLOW + "You have purchased " + ChatColor.GREEN + displayName + ChatColor.YELLOW + "!");
			} else {
				if (!isEquipped) {
					if (subMenu.canEquipItem()) {								
						playerData.getShopItem(uniqueId).setEquipped(true);
						getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						player.sendMessage(ChatColor.YELLOW + "You have equipped " + ChatColor.GREEN + displayName + ChatColor.YELLOW + "!");
					} else {
						player.sendMessage(ChatColor.RED + "Cannot equip item! You can only equip " + subMenu.getMaxEquippedItems() + " item(s) at a time!");
					}
				} else {
					playerData.getShopItem(uniqueId).setEquipped(false);
					getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					player.sendMessage(ChatColor.YELLOW + "You have unequipped " + ChatColor.GREEN + displayName + ChatColor.YELLOW + "!");
				}
			}
		} else {
			playerData.removePoints(cost);
			playerData.addShopItem(uniqueId,subMenu.getTitle());
			getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
			player.sendMessage(ChatColor.YELLOW + "You have purchased " + ChatColor.GREEN + displayName + ChatColor.YELLOW + "!");
		}
	}
	public void runAction(ClickType clickType) {
		Runnable action = clickActions.get(clickType);
		
		if (action != null) {
			if (playerData.getShopItem(uniqueId) == null) {				
				if (playerData.getPoints() >= cost) {
					boolean canDo = true;
					
					if (requirements != null) {						
						for (RequirementBase requirement : requirements) {
							if (!requirement.meetsRequirement(playerData)) {
								canDo = false;
								player.sendMessage(ChatColor.RED + requirement.getFailMessage());
								break;
							}
						}
					}
					if (canDo) {						
						action.run();
					}
				} else {
					player.sendMessage(ChatColor.RED + "You do not have enough points to purchase this item!");
				}
			} else {
				if (subMenu.getCanEquipItems()) {
					action.run();
				}
			}
		}
	}
	public void updateItemInfo() {
		displayName = ChatColor.stripColor(itemName);
		
		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(itemName);
		itemMeta.setLocalizedName(name);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		ArrayList<String> itemMetaLore = new ArrayList<String>();
		//Description
		if (desc != null) {			
			for (String descItem : desc) {			
				itemMetaLore.add(descItem);
			}
		} else {
			itemMetaLore.add("No description for this item.");
		}
		//Purchase requirement
		itemMetaLore.add(ChatColor.RESET.toString());
		itemMetaLore.add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Requirement(s) to purchase");		
		itemMetaLore.add(ChatColor.GREEN.toString() + cost + ChatColor.YELLOW + " points");
		if (requirements != null) {	
			for (RequirementBase requirement : requirements) {				
				itemMetaLore.add(requirement.getDisplayText());
			}
		}
		itemMetaLore.add(ChatColor.RESET.toString());
		//Purchase text
		if (hasPurchased) {
			if (multiPurchasable) {
				if (playerData.getPoints() >= cost) {					
					itemMetaLore.add(ChatColor.GREEN + "\u279D \u279D Left-Click to purchase");
				} else {
					itemMetaLore.add(ChatColor.RED + "\u2716 You don't have enough points");
				}
			} else {
				if (subMenu.getCanEquipItems()) {
					if (isEquipped) {						
						itemMetaLore.add(ChatColor.GREEN + "\u2714 Equipped. Left-Click to unequip");
					} else {
						if (!subMenu.canEquipItem()) {
							itemMetaLore.add(ChatColor.RED + "\u2716 Cannot Equip");
						} else {
							itemMetaLore.add(ChatColor.GREEN + "\u279D Left-Click to equip");
						}
					}
				} else {
					itemMetaLore.add(ChatColor.GREEN + "Already purchased");
				}
			}
		} else {
			if (playerData.getPoints() >= cost) {					
				itemMetaLore.add(ChatColor.GREEN + "\u279D \u279D Left-Click to purchase");
			} else {
				itemMetaLore.add(ChatColor.RED + "\u2716 You don't have enough points");
			}
		}
		itemMeta.setLore(itemMetaLore);
		item.setItemMeta(itemMeta);
		this.item = item;
	}
	
	//
	// GETTERS
	//
	
	//
	// SETTERS
	//
	
	public void setItemInfo(Material material, String itemName, ArrayList<String> desc) {
		this.material = material;
		this.itemName = itemName;
		this.desc = desc;
	}
}
