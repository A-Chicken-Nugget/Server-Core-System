package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;

public class ShopSubMenu extends SubMenu {
	private boolean canEquipItems;
	private int maxItemsEquiped;
	private ArrayList<ShopMenuOption> options = new ArrayList<>();
	
	public ShopSubMenu(String title, int size, boolean canEquipItems, int maxItemsEquiped, MenuBase parent) {
		super(title,size,parent);
		
		this.canEquipItems = canEquipItems;
		this.maxItemsEquiped = maxItemsEquiped;
	}
	
	public ArrayList<ShopMenuOption> getEquippedItems() {
		ArrayList<ShopMenuOption> equippedItems = new ArrayList<>();
		
		for (ShopMenuOption option : options) {
			if (option.getEquipped()) {
				equippedItems.add(option);
			}
		}
		return equippedItems;
	}
	public void createOption(int position, Material material, String itemName, ArrayList<String> desc, Runnable action, ArrayList<String> requirements, String uniqueId, int cost, boolean multiPurchasable) {
		if (canEquipItems && multiPurchasable) {
			System.out.println("[Core] Unable to add item " + uniqueId + " to " + title + ". The sub menu does not allow multipurchasable items!");
		} else {			
			ShopMenuOption option = new ShopMenuOption(uniqueId,position,cost,this,multiPurchasable);
			option.setItem(material, itemName, desc, requirements);
			option.setAction(action);
			options.add(option);
			super.options.add(option);
		}
	}
	public void purchaseItem(String uniqueId) {
		for (ShopMenuOption option : options) {
			if (option.getUniqueId().equalsIgnoreCase(uniqueId)) {
				PlayerData pd = parent.getMainInstance().getPlayerHandlingInstance().getPlayerData(parent.getPlayer());
				ShopItem item = pd.getShopItems(uniqueId);
				
				if (item != null) {					
					if (option.getMultiPurchasable()) {	
						pd.removePoints(option.getCost());
						pd.addShopItem(uniqueId);
						pd.getMenu().openMenu(title);
						parent.getPlayer().playSound(parent.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						parent.getPlayer().sendMessage(ChatColor.YELLOW + "You have purchased " + ChatColor.GREEN + option.getDisplayName() + ChatColor.YELLOW + "!");
					} else {
						if (!item.isEquipped()) {
							ArrayList<ShopMenuOption> equippedItems = getEquippedItems();
							
							if (equippedItems.size() < maxItemsEquiped) {								
								pd.getShopItems(uniqueId).setEquipped(true);
								pd.getMenu().openMenu(title);
								parent.getPlayer().playSound(parent.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
								parent.getPlayer().sendMessage(ChatColor.YELLOW + "You have equipped " + ChatColor.GREEN + option.getDisplayName() + ChatColor.YELLOW + "!");
							} else {
								parent.getPlayer().sendMessage(ChatColor.RED + "Cannot equip item! You can only equip " + maxItemsEquiped + " item(s) at a time!");
							}
						} else {
							pd.getShopItems(uniqueId).setEquipped(false);
							pd.getMenu().openMenu(title);
							parent.getPlayer().playSound(parent.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
							parent.getPlayer().sendMessage(ChatColor.YELLOW + "You have unequipped " + ChatColor.GREEN + option.getDisplayName() + ChatColor.YELLOW + "!");
						}
					}
				} else {
					pd.removePoints(option.getCost());
					pd.addShopItem(uniqueId);
					pd.getMenu().openMenu(title);
					parent.getPlayer().playSound(parent.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					parent.getPlayer().sendMessage(ChatColor.YELLOW + "You have purchased " + ChatColor.GREEN + option.getDisplayName() + ChatColor.YELLOW + "!");
				}
			}
		}
	}
	
	//
	// GETTERS
	//
	
	public boolean getCanEquipItems() {
		return canEquipItems;
	}
}
