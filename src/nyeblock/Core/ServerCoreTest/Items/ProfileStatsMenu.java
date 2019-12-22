package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Interfaces.SubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings({"deprecation","serial"})
public class ProfileStatsMenu extends MenuBase {
	public ProfileStatsMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"profile_stats_menu");
	}
	
	public void setContents() {
		PlayerHandling playerHandling = mainInstance.getPlayerHandlingInstance();
		PlayerData playerData = playerHandling.getPlayerData(player);
		SubMenu subMenu;		
		
		//
		// Profile/Stats menu
		//
		subMenu = new SubMenu("Profile/Stats Menu",27);
		
		subMenu.addOption(11, Material.COMMAND_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "My Stats", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view your stats");
		}}, new Runnable() {
            @Override
            public void run() {
            	playerData.getMenu().openMenu("My Stats");
            }
		});
		subMenu.addOption(13, Material.SPONGE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "My Profile", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view your profile");
		}}, new Runnable() {
            @Override
            public void run() {
            	playerData.getMenu().openMenu("My Profile");
            }
		});
		
		super.addSubMenu(subMenu);
		//
		// My Stats menu
		//
		subMenu = new SubMenu("My Stats",27);
		
		subMenu.addOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Level: " + ChatColor.GREEN + playerData.getLevel(Realm.KITPVP));
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view your stats");
		}}, new Runnable() {
            @Override
            public void run() {
            	player.closeInventory();
            }
		});
		
		super.addSubMenu(subMenu);
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta shopMeta = (SkullMeta)item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "My Profile/Stats" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("profile_stats_menu");
		shopMeta.setOwner(player.getName());
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		setContents();
		open();
	}
}
