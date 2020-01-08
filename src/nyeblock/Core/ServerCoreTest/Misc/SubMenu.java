package nyeblock.Core.ServerCoreTest.Misc;

import java.util.ArrayList;

import org.bukkit.Material;

import nyeblock.Core.ServerCoreTest.Items.MenuBase;

public class SubMenu {
	private String title;
	private int size;
	protected MenuBase parent;
	private ArrayList<MenuOption> options = new ArrayList<>();
	
	public SubMenu(String title, int size, MenuBase parent) {
		this.title = title;
		this.size = size;
		this.parent = parent;
		
		parent.addSubMenu(this);
	}
	
	public void addOption(int position, Material material, String itemName, ArrayList<String> desc, Runnable action) {
		MenuOption option = new MenuOption(position,this);
		option.setItem(material, itemName, desc);
		option.setAction(action);
		options.add(option);
	}
	public void addShopOption(int position, Material material, String itemName, int cost, ArrayList<String> desc, Runnable action, String uniqueId) {
		ShopMenuOption option = new ShopMenuOption(position,cost,this);
		option.setItem(material, itemName, desc);
		option.setAction(action);
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
	public void runOption(String name) {
		for (MenuOption option : options) {
			if (option.getName().equalsIgnoreCase(name)) {
				option.runAction();
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
	public ArrayList<MenuOption> getOptions() {
		return options;
	}
}
