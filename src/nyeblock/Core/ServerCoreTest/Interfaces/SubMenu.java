package nyeblock.Core.ServerCoreTest.Interfaces;

import java.util.ArrayList;

import org.bukkit.Material;

public class SubMenu {
	private String title;
	private int size;
	private ArrayList<MenuOption> options = new ArrayList<>();
	
	public SubMenu(String title, int size) {
		this.title = title;
		this.size = size;
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
	
	//
	// SETTERS
	//
	
	public void addOption(int position, Material material, String itemName, ArrayList<String> desc, Runnable action) {
		MenuOption option = new MenuOption(position);
		option.setItem(material, itemName, desc);
		option.setAction(action);
		options.add(option);
	}
}
