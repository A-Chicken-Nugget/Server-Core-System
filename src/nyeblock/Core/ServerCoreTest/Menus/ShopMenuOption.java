package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;

public class ShopMenuOption extends MenuOption {
	private Player player;
	private PlayerData playerData;
	private String uniqueId;
	private String displayName;
	private ShopSubMenu subMenu;
	private int cost;
	private boolean isEquipped;
	private boolean multiPurchasable;
	private boolean hasPurchased;
	private int quantity;
	
	public ShopMenuOption(String uniqueId, int position, int cost, SubMenu subMenu, boolean multiPurchasable) {
		super(position,subMenu);
		
		player = subMenu.parent.getPlayer();
		playerData = subMenu.parent.getMainInstance().getPlayerHandlingInstance().getPlayerData(player);
		this.uniqueId = uniqueId;
		this.cost = cost;
		this.subMenu = (ShopSubMenu)subMenu;
		this.multiPurchasable = multiPurchasable;
		hasPurchased = (playerData.getShopItems(uniqueId) != null ? true : false);
		quantity = (hasPurchased ? playerData.getShopItems(uniqueId).getQuantity() : 0);
		isEquipped = (hasPurchased ? playerData.getShopItems(uniqueId).isEquipped() : false);
	}
	
	public void runAction() {
		if (action != null) {
			if (playerData.getShopItems(uniqueId) == null) {				
				if (playerData.getPoints() >= cost) {			
					action.run();
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
	
	//
	// GETTERS
	//
	
	public String getUniqueId() {
		return uniqueId;
	}
	public int getCost() {
		return cost;
	}
	public boolean getMultiPurchasable() {
		return multiPurchasable;
	}
	public String getDisplayName() {
		return displayName;
	}
	public boolean getEquipped() {
		return isEquipped;
	}
	
	//
	// SETTERS
	//
	
	public void setItem(Material material, String itemName, ArrayList<String> desc, ArrayList<String> requirements) {
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
		itemMetaLore.add(ChatColor.RESET.toString());
		//Purchase requirement
		if (requirements != null) {			
			for (String requirementItem : requirements) {			
				itemMetaLore.add(requirementItem);
			}
		} else {
			itemMetaLore.add(ChatColor.YELLOW + "Cost: " + ChatColor.GREEN + cost);
		}
		itemMetaLore.add(ChatColor.RESET.toString());
		//Purchase text
		if (hasPurchased) {
			if (multiPurchasable) {
				if (playerData.getPoints() >= cost) {					
					itemMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to purchase");
				} else {
					itemMetaLore.add(ChatColor.RED + "\u2716 You don't have enough points to purchase");
				}
			} else {
				if (subMenu.getCanEquipItems()) {
					if (isEquipped) {						
						itemMetaLore.add(ChatColor.GREEN + "\u2714 Equiped. Click to unequip");
					} else {
						itemMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to equip");
					}
				} else {
					itemMetaLore.add(ChatColor.GREEN + "Already purchased");
				}
			}
		} else {
			if (playerData.getPoints() >= cost) {					
				itemMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to purchase");
			} else {
				itemMetaLore.add(ChatColor.RED + "\u2716 You don't have enough points to purchase");
			}
		}
		itemMeta.setLore(itemMetaLore);
		item.setItemMeta(itemMeta);
		this.item = item;
	}
}
