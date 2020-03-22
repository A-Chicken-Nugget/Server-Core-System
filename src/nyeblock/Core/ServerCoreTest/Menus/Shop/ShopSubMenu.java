package nyeblock.Core.ServerCoreTest.Menus.Shop;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

import nyeblock.Core.ServerCoreTest.Menus.MenuBase;
import nyeblock.Core.ServerCoreTest.Menus.MenuOption;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuOption;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuOptionBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;

public class ShopSubMenu extends SubMenu {
	public ShopSubMenu(String title, int size, MenuBase parent) {
		super(title,size,parent);
	}
	
	public void createShopOption(int position, Material material, String itemName, ArrayList<String> desc, HashMap<ClickType,Runnable> clickActions, ArrayList<RequirementBase> requirements, String uniqueId, int cost, boolean multiPurchasable) {
		ShopMenuOption option = new ShopMenuOption(uniqueId,position,cost,requirements,this,multiPurchasable);
		option.setItemInfo(material, itemName, desc);
		option.setClickActions(clickActions);
		options.add(option);
		
		for (MenuOption optionn : options) {
			if (optionn instanceof ShopMenuOptionBase) {					
				((ShopMenuOptionBase)optionn).updateItemInfo();
			}
		}
	}
	public void useItem(String uniqueId) {
		for (MenuOption option : options) {
			if (option instanceof ShopMenuOptionBase) {				
				if (((ShopMenuOptionBase)option).getUniqueId().equalsIgnoreCase(uniqueId)) {
					((ShopMenuOptionBase)option).use();
				}
			}
		}
	}
	
	//
	// GETTERS
	//
	
	public String getTitle() {
		return title;
	}
	public ArrayList<MenuOption> getOptions() {
		return options;
	}
	public ArrayList<ShopMenuOptionBase> getEquippedItems() {
		ArrayList<ShopMenuOptionBase> equippedItems = new ArrayList<>();
		
		for (MenuOption option : options) {
			if (option instanceof ShopMenuOptionBase) {	
				if (((ShopMenuOptionBase)option).isEquipped()) {
					equippedItems.add(((ShopMenuOptionBase)option));
				}
			}
		}
		return equippedItems;
	}
}
