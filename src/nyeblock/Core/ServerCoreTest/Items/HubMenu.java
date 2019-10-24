package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class HubMenu {
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Server Menu" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("hub_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Set the gui of the item
	public void openMenu(Main mainInstance, Player ply) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Server Menu");
		
		//Kitpvp
		ItemStack kitPvp = new ItemStack(Material.IRON_AXE);
		ItemMeta kitPvpMeta = kitPvp.getItemMeta();
		kitPvpMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp (Beta)");
		kitPvpMeta.setLocalizedName("kitPvP");
		kitPvpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> kitPvpMetaLore = new ArrayList<String>();
		kitPvpMetaLore.add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getKitPvpGames().size() + ChatColor.YELLOW + " games active");
		kitPvpMetaLore.add(ChatColor.RESET.toString());
		kitPvpMetaLore.add(ChatColor.YELLOW + "Fight other players and");
		kitPvpMetaLore.add(ChatColor.YELLOW + "try to kill as many as possible.");
		kitPvpMetaLore.add(ChatColor.YELLOW + "At the end the player with the");
		kitPvpMetaLore.add(ChatColor.YELLOW + "highest kills wins. Select up to 4 kits.");
		kitPvpMeta.setLore(kitPvpMetaLore);
		kitPvp.setItemMeta(kitPvpMeta);
		//Sky wars
		ItemStack skyWars = new ItemStack(Material.GRASS_BLOCK);
		ItemMeta skyWarsMeta = skyWars.getItemMeta();
		skyWarsMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars (Beta)");
		skyWarsMeta.setLocalizedName("skyWars");
		skyWarsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> skyWarsMetaLore = new ArrayList<String>();
		skyWarsMetaLore.add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getSkyWarsGames().size() + " games active");
		skyWarsMetaLore.add(ChatColor.RESET.toString());
		skyWarsMetaLore.add(ChatColor.YELLOW + "Each player starts with their");
		skyWarsMetaLore.add(ChatColor.YELLOW + "own island. You must craft and");
		skyWarsMetaLore.add(ChatColor.YELLOW + "build your way to the center of");
		skyWarsMetaLore.add(ChatColor.YELLOW + "the map for better loot. The");
		skyWarsMetaLore.add(ChatColor.YELLOW + "last player left wins!");
		skyWarsMeta.setLore(skyWarsMetaLore);
		skyWars.setItemMeta(skyWarsMeta);
		//Step Spleef
		ItemStack stepSpleef = new ItemStack(Material.IRON_BOOTS);
		ItemMeta stepSpleefMeta = stepSpleef.getItemMeta();
		stepSpleefMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef (Beta)");
		stepSpleefMeta.setLocalizedName("stepSpleef");
		stepSpleefMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> stepSpleefMetaLore = new ArrayList<String>();
		stepSpleefMetaLore.add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getStepSpleefGames().size() + " games active");
		stepSpleefMetaLore.add(ChatColor.RESET.toString());
		stepSpleefMetaLore.add(ChatColor.YELLOW + "Dodge and weave to survive");
		stepSpleefMetaLore.add(ChatColor.YELLOW + "when the blocks you've walked");
		stepSpleefMetaLore.add(ChatColor.YELLOW + "on get deleted shortly after.");
		stepSpleefMetaLore.add(ChatColor.YELLOW + "The last player left wins!");
		stepSpleefMeta.setLore(stepSpleefMetaLore);
		stepSpleef.setItemMeta(stepSpleefMeta);
		
		menu.setItem(2, kitPvp);
		menu.setItem(4, skyWars);
		menu.setItem(6, stepSpleef);
		
		ply.openInventory(menu);
	}
	public void clickItem(Player ply, String item, Main mainInstance) {
		if (item.equalsIgnoreCase("kitPvP")) {
			mainInstance.getGameInstance().joinGame(ply, Realm.KITPVP);
		} else if (item.equalsIgnoreCase("stepSpleef")) {
			mainInstance.getGameInstance().joinGame(ply, Realm.STEPSPLEEF);
		} else if (item.equalsIgnoreCase("skyWars")) {
			mainInstance.getGameInstance().joinGame(ply, Realm.SKYWARS);
		}
	}
}
