package nyeblock.Core.ServerCoreTest.Menus.Shop;

import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Menus.MenuBase;

public abstract class ShopBase extends MenuBase {
	public ShopBase(Main mainInstance, Player player, String name) {
		super(mainInstance,player,name);
	}
	
//	public boolean canBuyOption(String name) {
//		boolean canBuy = false;
//		
//		for (MenuOption option : currentMenu.getOptions()) {
//			if (option.getName().equalsIgnoreCase(name)) {
//				
//				exists = true;
//			}
//		}
//		return canBuy;
//	}
}
