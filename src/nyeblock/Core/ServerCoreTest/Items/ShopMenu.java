package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;

public class ShopMenu extends MenuBase {
	private PlayerHandling playerHandling;
	
	public ShopMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"shop_menu",54);
		playerHandling = mainInstance.getPlayerHandlingInstance();
		
		//
		// Shop menu
		//
		
		//Kitpvp Shop
		ItemStack kitPvp = new ItemStack(Material.IRON_AXE);
		ItemMeta kitPvpMeta = kitPvp.getItemMeta();
		kitPvpMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp Shop");
		kitPvpMeta.setLocalizedName("kitpvp_shop");
		kitPvpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> kitPvpMetaLore = new ArrayList<String>();
		kitPvpMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to view the KitPvp shop");
		kitPvpMeta.setLore(kitPvpMetaLore);
		kitPvp.setItemMeta(kitPvpMeta);
		super.addOption("Shop Menu", 11, kitPvp, new Runnable() {
            @Override
            public void run() {
            	playerHandling.getPlayerData(player).getMenu().openMenu("KitPvp Shop");
            }
		});
		//Sky Wars Shop
		ItemStack skyWars = new ItemStack(Material.GRASS_BLOCK);
		ItemMeta skyWarsMeta = skyWars.getItemMeta();
		skyWarsMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars Shop");
		skyWarsMeta.setLocalizedName("skyWars_shop");
		skyWarsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> skyWarsMetaLore = new ArrayList<String>();
		skyWarsMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to view the Sky Wars shop");
		skyWarsMeta.setLore(skyWarsMetaLore);
		skyWars.setItemMeta(skyWarsMeta);
		super.addOption("Shop Menu", 13, skyWars, new Runnable() {
            @Override
            public void run() {       
            	playerHandling.getPlayerData(player).getMenu().openMenu("Sky Wars Shop");
            }
		});
		//Step Spleef Shop
		ItemStack stepSpleef = new ItemStack(Material.IRON_BOOTS);
		ItemMeta stepSpleefMeta = stepSpleef.getItemMeta();
		stepSpleefMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef Shop");
		stepSpleefMeta.setLocalizedName("stepSpleef_shop");
		stepSpleefMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> stepSpleefMetaLore = new ArrayList<String>();
		stepSpleefMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to view the Step Spleef shop");
		stepSpleefMeta.setLore(stepSpleefMetaLore);
		stepSpleef.setItemMeta(stepSpleefMeta);
		super.addOption("Shop Menu", 15, stepSpleef, new Runnable() {
            @Override
            public void run() {       
            	playerHandling.getPlayerData(player).getMenu().openMenu("Step Spleef Shop");
            }
		});
		//PvP Shop
		ItemStack pvp = new ItemStack(Material.FISHING_ROD);
		ItemMeta pvpMeta = pvp.getItemMeta();
		pvpMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP Shop");
		pvpMeta.setLocalizedName("pvp_shop");
		pvpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> pvpMetaLore = new ArrayList<String>();
		pvpMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to view the PvP shop");
		pvpMeta.setLore(pvpMetaLore);
		pvp.setItemMeta(pvpMeta);
		super.addOption("Shop Menu", 21, pvp, new Runnable() {
            @Override
            public void run() {       
            	mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("PvP Shop");
            }
		});
		
		//
		// KitPvp Shop
		//
		
		
	}

	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Shop Menu" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("shop_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		openMenu("Shop Menu");
	}
}
