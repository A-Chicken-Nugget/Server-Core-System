package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Items.ItemBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;

public abstract class MenuBase extends ItemBase {
	private Inventory inventory;
	private ArrayList<SubMenu> menus = new ArrayList<>();
	protected SubMenu currentMenu;
	
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
	public void use(ItemStack item) {}
	public MenuBase addSubMenu(SubMenu subMenu) {
		menus.add(subMenu);
		
		return this;
	}
	public MenuBase open() {
		openMenu(null,true);
		
		return this;
	}
	public void openMenu(String title, boolean refreshItems) {
		SubMenu menu = null;
		
		if (refreshItems) {			
			menus.clear();
			setContents();
		}
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
			mainInstance.getTimerInstance().createRunnableTimer("invCheck_" + player.getUniqueId(), .5, 0, new Runnable() {
				@Override
				public void run() {
					if (pd.getMenu() == null || !currentMenu.getTitle().equalsIgnoreCase(player.getOpenInventory().getTitle()) && pd.getMenu() == instance) {
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
	public Player getPlayer() {
		return player;
	}
}
