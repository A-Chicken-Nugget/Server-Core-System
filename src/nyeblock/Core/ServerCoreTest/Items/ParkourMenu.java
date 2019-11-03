package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;

public class ParkourMenu {
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
	//Set the gui of the item
	public void openMenu(Main mainInstance, Player ply) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Parkour Menu");
		
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		if (pd.getCustomDataKey("parkour_mode") == null) {
			pd.setCustomDataKey("parkour_mode", "false");
		}
		boolean isParkourMode = Boolean.parseBoolean(pd.getCustomDataKey("parkour_mode"));

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
		
		menu.setItem(4, parkourMode);
		
		ply.openInventory(menu);
	}
	public void clickItem(Player ply, String item, Main mainInstance) {
		if (item.equalsIgnoreCase("parkourMode")) {
			PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			boolean isParkourMode = Boolean.parseBoolean(pd.getCustomDataKey("parkour_mode"));
			
			if (isParkourMode) {
				pd.setCustomDataKey("parkour_mode", "false");
				ply.sendMessage(ChatColor.YELLOW + "Parkour mode set to " + ChatColor.RED.toString() + ChatColor.BOLD + "normal");
			} else {
				pd.setCustomDataKey("parkour_mode", "true");
				ply.sendMessage(ChatColor.YELLOW + "Parkour mode set to " + ChatColor.GREEN.toString() + ChatColor.BOLD + "competitive");
			}
			ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
			
			mainInstance.getHubParkourInstance().goToStart(ply);
		}
	}
}
