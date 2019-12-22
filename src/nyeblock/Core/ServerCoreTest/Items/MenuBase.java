package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Interfaces.MenuOption;
import nyeblock.Core.ServerCoreTest.Interfaces.SubMenu;

public abstract class MenuBase extends ItemBase {
	private Inventory inventory;
	private ArrayList<SubMenu> menus = new ArrayList<>();
	private SubMenu currentMenu;
	
	public void use(ItemStack item) {}
	/**
	 * Default constructor with parameters
	 * @param mainInstance - the plugin instance
	 * @param player - the player
	 */
	public MenuBase(Main mainInstance, Player player, String name) {
		super(mainInstance,player,name);
	}
	/**
	 * Method defined in sub class that gives the player the menu item
	 */
	public ItemStack give() { return null; }
	public void setContents() {}
	public void addSubMenu(SubMenu subMenu) {
		menus.add(subMenu);
	}
	public void open() {
		openMenu(null);
	}
	public void openMenu(String title) {
		SubMenu menu = null;
		menus.clear();
		setContents();
		
		if (title != null) {			
			for (SubMenu menuItem : menus) {
				if (menuItem.getTitle().equalsIgnoreCase(title)) {
					menu = menuItem;
				}
			}
		} else {			
			menu = menus.get(0);
		}
		if (menu != null) {			
			currentMenu = menu;
			inventory = Bukkit.createInventory(null, menu.getSize(), menu.getTitle());
			PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
			MenuBase instance = this;
			
			for(MenuOption option : menu.getOptions()) {				
				inventory.setItem(option.getPosition(), option.getItem());
			}
			if (pd.getMenu() != this) {				
				pd.setMenu(this);
			}
			player.openInventory(inventory);
			mainInstance.getTimerInstance().createTimer2("invCheck_" + player.getUniqueId(), .5, 0, new Runnable() {
				@Override
				public void run() {
					if (!currentMenu.getTitle().equalsIgnoreCase(player.getOpenInventory().getTitle()) && pd.getMenu() == instance) {
						pd.setMenu(null);
						mainInstance.getTimerInstance().deleteTimer("invCheck_" + player.getUniqueId());
					}
				}
			});
		}
	}
	public SubMenu getCurrentMenu() {
		return currentMenu;
	}
}
