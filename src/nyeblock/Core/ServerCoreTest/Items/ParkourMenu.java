package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Interfaces.SubMenu;

@SuppressWarnings("serial")
public class ParkourMenu extends MenuBase {
	public ParkourMenu(Main mainInstance, Player player) { 
		super(mainInstance,player,"parkour_menu");
	}
	
	public void setContents() {
		SubMenu subMenu = new SubMenu("Parkour Menu",9);
		
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
		if (pd.getCustomDataKey("parkour_mode") == null) {
			pd.setCustomDataKey("parkour_mode", "false");
		}
		boolean isParkourMode = Boolean.parseBoolean(pd.getCustomDataKey("parkour_mode"));
		
		//Parkour mode
		subMenu.addOption(4, isParkourMode ? Material.GREEN_WOOL : Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Parkour Mode " + (isParkourMode ? ("(" + ChatColor.GREEN + "Competitive" + ChatColor.YELLOW + ")") : ("(" + ChatColor.RED + "Normal" + ChatColor.YELLOW + ")")), new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Set your parkour mode.");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "If competitive then you will be timed and");
			add(ChatColor.YELLOW + "teleported to the start if you fail.");
			add(ChatColor.YELLOW + "Try to beat the top 5 times.");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "If normal then you will not be timed");
			add(ChatColor.YELLOW + "and if you fail will be teleported");
			add(ChatColor.YELLOW + "to your last checkpoint.");
		}}, new Runnable() {
            @Override
            public void run() {
    			if (isParkourMode) {
    				pd.setCustomDataKey("parkour_mode", "false");
    				player.sendMessage(ChatColor.YELLOW + "Parkour mode set to " + ChatColor.RED.toString() + ChatColor.BOLD + "normal");
    			} else {
    				pd.setCustomDataKey("parkour_mode", "true");
    				player.sendMessage(ChatColor.YELLOW + "Parkour mode set to " + ChatColor.GREEN.toString() + ChatColor.BOLD + "competitive");
    			}
    			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
    			
    			mainInstance.getHubParkourInstance().goToStart(player);
            	player.closeInventory();
            }
		});
		super.addSubMenu(subMenu);
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.CLOCK);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Parkour Menu" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("parkour_menu");
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		setContents();
		open();
	}
}
