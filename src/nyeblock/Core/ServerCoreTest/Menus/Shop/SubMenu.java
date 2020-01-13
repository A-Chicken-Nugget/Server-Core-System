package nyeblock.Core.ServerCoreTest.Menus.Shop;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

import nyeblock.Core.ServerCoreTest.Menus.MenuBase;
import nyeblock.Core.ServerCoreTest.Menus.MenuOption;

public class SubMenu {
	protected String title;
	private int size;
	private MenuBase parent;
	protected ArrayList<MenuOption> options = new ArrayList<>();
	
	public SubMenu(String title, int size, MenuBase parent) {
		this.title = title;
		this.size = size;
		this.parent = parent;
		
		parent.addSubMenu(this);
	}
	
	public void createOption(int position, Material material, String itemName, ArrayList<String> desc, HashMap<ClickType,Runnable> clickActions) {
		MenuOption option = new MenuOption(position,this);
		option.setItem(material, itemName, desc);
		option.setClickActions(clickActions);
		options.add(option);
	}
	public boolean hasOption(String name) {
		boolean exists = false;
		
		for (MenuOption option : options) {
			if (option.getName().equalsIgnoreCase(name)) {
				exists = true;
			}
		}
		return exists;
	}
	public void runOption(String name, ClickType clickType) {
		for (MenuOption option : options) {
			if (option.getName().equalsIgnoreCase(name)) {
				option.runAction(clickType);
			}
		}
	}
	
	//
	// GETTERS
	//
	
	public String getTitle() {
		return title;
	}
	public int getSize() {
		return size;
	}
	public MenuBase getParent() {
		return parent;
	}
	public ArrayList<MenuOption> getOptions() {
		return options;
	}
}
