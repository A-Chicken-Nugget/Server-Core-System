package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nyeblock.Core.ServerCoreTest.Main;

public abstract class ItemBase {
	protected Main mainInstance;
	protected String name;
	protected Player player;
	
	public ItemBase(Main mainInstance, Player player, String name) {
		this.mainInstance = mainInstance;
		this.player = player;
		
		mainInstance.getPlayerHandlingInstance().getPlayerData(player).addCustomItem(name, this);
	}
	public ItemBase(String name) {
		this.name = name;
	}
	
	public void use(ItemStack item) {}
	public ItemStack give() { return null; }
	public Main getMainInstance() {
		return mainInstance;
	}
	
	public void setData(Main mainInstance, Player player) {
		this.mainInstance = mainInstance;
		this.player = player;
		
		mainInstance.getPlayerHandlingInstance().getPlayerData(player).addCustomItem(name, this);
	}
}
