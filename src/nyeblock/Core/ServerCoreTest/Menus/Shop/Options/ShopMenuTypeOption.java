package nyeblock.Core.ServerCoreTest.Menus.Shop.Options;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
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

public class ShopMenuTypeOption extends ShopMenuOptionBase {
	private String displayText;
	private int currentIndex;
	private ArrayList<ShopMenuTypeOptionItem> items;
	private ShopMenuTypeOptionItem selectedOption;
	
	public ShopMenuTypeOption(String uniqueId, int position, String displayText, ArrayList<ShopMenuTypeOptionItem> items, SubMenu subMenu, boolean multiPurchasable) {
		super(uniqueId,position,subMenu,multiPurchasable);
		
		this.displayText = displayText;
		this.items = items;
		currentIndex = (playerData.getCustomDataKey(uniqueId + "::current_index") != null ? Integer.parseInt(playerData.getCustomDataKey(uniqueId + "::current_index")) : 0);
		selectedOption = items.get(currentIndex);
		hasPurchased = (playerData.getShopItem(uniqueId + "::" + selectedOption.getUniqueId()) != null ? true : false);
		quantity = (hasPurchased ? playerData.getShopItem(uniqueId + "::" + selectedOption.getUniqueId()).getQuantity() : 0);
		isEquipped = (hasPurchased ? playerData.getShopItem(uniqueId + "::" + selectedOption.getUniqueId()).isEquipped() : false);
		setCost(selectedOption.getCost());
	}
	
	public void use() {
		String uniqueId = this.uniqueId + "::" + selectedOption.getUniqueId();
		ShopItem item = playerData.getShopItem(uniqueId);
		
		if (item != null) {
			if (multiPurchasable) {	
				playerData.removePoints(cost);
				playerData.addShopItem(uniqueId,subMenu.getTitle());
				getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
				player.sendMessage(ChatColor.YELLOW + "You have purchased " + ChatColor.GREEN + displayName + " for " + selectedOption.getDisplayText() + ChatColor.YELLOW + "!");
			} else {
				if (subMenu.getCanEquipItems()) {					
					if (!isEquipped) {
						if (subMenu.canEquipItem()) {								
							playerData.getShopItem(uniqueId).setEquipped(true);
							getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
							player.sendMessage(ChatColor.YELLOW + "You have equipped " + ChatColor.GREEN + displayName + " for " + selectedOption.getDisplayText() + ChatColor.YELLOW + "!");
						} else {
							player.sendMessage(ChatColor.RED + "Cannot equip item! You can only equip " + subMenu.getMaxEquippedItems() + " item(s) at a time!");
						}
					} else {
						playerData.getShopItem(uniqueId).setEquipped(false);
						getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						player.sendMessage(ChatColor.YELLOW + "You have unequipped " + ChatColor.GREEN + displayName + " for " + selectedOption.getDisplayText() + ChatColor.YELLOW + "!");
					}
				}
			}
		} else {
			playerData.removePoints(cost);
			playerData.addShopItem(uniqueId,subMenu.getTitle());
			getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
			player.sendMessage(ChatColor.YELLOW + "You have purchased " + ChatColor.GREEN + displayName + " for " + selectedOption.getDisplayText() + ChatColor.YELLOW + "!");
		}
	}
	public void runAction(ClickType clickType) {
		Runnable action = clickActions.get(clickType);
		
		if (action != null) {
			if (playerData.getShopItem(uniqueId + "::" + selectedOption.getUniqueId()) == null) {				
				if (playerData.getPoints() >= selectedOption.getCost()) {
					boolean canDo = true;
					
					for (RequirementBase requirement : selectedOption.getRequirements()) {
						if (requirement.meetsRequirement(playerData)) {
							canDo = false;
							player.sendMessage(ChatColor.RED + requirement.getFailMessage());
							break;
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
		} else {
			if (clickType == ClickType.RIGHT) {
				int nextIndex = currentIndex+1;
				
				if (items.size() > nextIndex) {
					playerData.setCustomDataKey(uniqueId + "::current_index",String.valueOf(nextIndex));
					getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
				} else {
					playerData.setCustomDataKey(uniqueId + "::current_index","0");
					getSubMenu().getParent().openMenu(subMenu.getTitle(),true);
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
		//Select type
		itemMetaLore.add(ChatColor.RESET.toString());
		itemMetaLore.add(ChatColor.YELLOW + displayText + ": " + ChatColor.GREEN + selectedOption.getDisplayText());
		itemMetaLore.add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "(Right-Click to cycle)");
		//Purchase requirement
		itemMetaLore.add(ChatColor.RESET.toString());
		itemMetaLore.add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Requirement(s) to purchase");
		if (selectedOption.getRequirements() != null) {
			itemMetaLore.add(ChatColor.YELLOW + "Cost: " + ChatColor.GREEN + selectedOption.getCost());
			for (RequirementBase requirement : selectedOption.getRequirements()) {				
				itemMetaLore.add(requirement.getDisplayText());
			}
		} else {
			itemMetaLore.add(ChatColor.YELLOW + "Cost: " + ChatColor.GREEN + selectedOption.getCost());
		}
		itemMetaLore.add(ChatColor.RESET.toString());
		//Purchase text
		if (hasPurchased) {
			if (multiPurchasable) {
				if (playerData.getPoints() >= cost) {					
					itemMetaLore.add(ChatColor.GREEN + "\u279D \u279D Left-Click to purchase");
				} else {
					itemMetaLore.add(ChatColor.RED + "\u2716 You don't have enough points to purchase");
				}
			} else {
				if (subMenu.getCanEquipItems()) {
					if (isEquipped) {						
						itemMetaLore.add(ChatColor.GREEN + "\u2714 Equiped. Left-Click to unequip");
					} else {
						if (!subMenu.canEquipItem()) {
							itemMetaLore.add(ChatColor.GREEN + "\u279D Left-Click to equip");
						} else {
							itemMetaLore.add(ChatColor.RED + "\u2716 Cannot Equip");
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
	
	public ArrayList<ShopMenuTypeOptionItem> getTypeOptions() {
		return items;
	}
}
