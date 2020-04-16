package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class SkyWarsMenu extends MenuBase {
	public SkyWarsMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"skyWars_menu");
	}
	
	public void setContents() {
		SubMenu subMenu;
		
		//
		// Sky Wars Games Menu
		//
		subMenu = new SubMenu("Sky Wars Games",9,this);
		
		//Solo
		subMenu.createOption(4, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Solo", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.KITPVP) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight other players and");
			add(ChatColor.YELLOW + "try to kill as many as possible.");
			add(ChatColor.YELLOW + "At the end the player with the");
			add(ChatColor.YELLOW + "highest kills wins. Select up to 4 kits.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
						mainInstance.getRealmHandlingInstance().joinRealm(player, Realm.KITPVP);
					}
				});
		}});
		
		//Sky wars
		subMenu.createOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars (Beta)", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.SKYWARS) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Each player starts with their");
			add(ChatColor.YELLOW + "own island. You must craft and");
			add(ChatColor.YELLOW + "build your way to the center of");
			add(ChatColor.YELLOW + "the map for better loot. The");
			add(ChatColor.YELLOW + "last player left wins!");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {       
		            	player.closeInventory();
		            	mainInstance.getRealmHandlingInstance().joinLobby(player, Realm.SKYWARS_LOBBY);
		            }
				});
		}});
	}
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "Sky Wars Games" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("skyWars_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		if (mainInstance.getPlayerHandlingInstance().getPlayerData(player).getLoadedDBInfoStatus()) {			
			open();
		}
	}
}
