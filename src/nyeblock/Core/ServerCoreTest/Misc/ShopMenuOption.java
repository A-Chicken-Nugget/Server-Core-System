package nyeblock.Core.ServerCoreTest.Misc;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;

public class ShopMenuOption extends MenuOption {
	private int cost;
	
	public ShopMenuOption(int position, int cost, SubMenu subMenu) {
		super(position,subMenu);
		
		this.cost = cost;
	}
	
	public void runAction() {
		if (action != null) {
			Player player = subMenu.parent.getPlayer();
			PlayerData pd = subMenu.parent.getMainInstance().getPlayerHandlingInstance().getPlayerData(player);
			
			if (pd.getPoints() >= cost) {			
				action.run();
			} else {
				player.sendMessage(ChatColor.RED + "You do not have enough points to purchase this item!");
			}
		}
	}
	public int getCost() {
		return cost;
	}
}
