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

public class ParkourMenu extends MenuBase {
	private boolean isParkourMode;
	
	public ParkourMenu(Main mainInstance, Player player) { 
		super(mainInstance,player,"parkour_menu",9);
		
		setContents();
	}
	
	public void setContents() {
		super.clear();
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
		if (pd.getCustomDataKey("parkour_mode") == null) {
			pd.setCustomDataKey("parkour_mode", "false");
		}
		isParkourMode = Boolean.parseBoolean(pd.getCustomDataKey("parkour_mode"));
		
		//Parkour mode
		ItemStack parkourMode = new ItemStack(isParkourMode ? Material.GREEN_WOOL : Material.RED_WOOL);
		ItemMeta parkourModeMeta = parkourMode.getItemMeta();
		parkourModeMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Parkour Mode " + (isParkourMode ? ("(" + ChatColor.GREEN + "Competitive" + ChatColor.YELLOW + ")") : ("(" + ChatColor.RED + "Normal" + ChatColor.YELLOW + ")")));
		parkourModeMeta.setLocalizedName("parkourMode");
		parkourModeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> parkourModeMetaLore = new ArrayList<String>();
		parkourModeMetaLore.add(ChatColor.YELLOW + "Set your parkour mode.");
		parkourModeMetaLore.add(ChatColor.RESET.toString());
		parkourModeMetaLore.add(ChatColor.YELLOW + "If competitive then you will be timed and");
		parkourModeMetaLore.add(ChatColor.YELLOW + "teleported to the start if you fail.");
		parkourModeMetaLore.add(ChatColor.YELLOW + "Try to beat the top 5 times.");
		parkourModeMetaLore.add(ChatColor.RESET.toString());
		parkourModeMetaLore.add(ChatColor.YELLOW + "If normal then you will not be timed");
		parkourModeMetaLore.add(ChatColor.YELLOW + "and if you fail will be teleported");
		parkourModeMetaLore.add(ChatColor.YELLOW + "to your last checkpoint.");
		parkourModeMeta.setLore(parkourModeMetaLore);
		parkourMode.setItemMeta(parkourModeMeta);
		super.addOption("Game Menu", 4, parkourMode, new Runnable() {
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
		openMenu("Game Menu");
	}
}
