package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.SubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

@SuppressWarnings("serial")
public class KitSelector extends MenuBase {
	public KitSelector(Main mainInstance, Player player) {
		super(mainInstance,player,"kit_selector");
	}
	
	public void setContents() {
		GameBase game = (GameBase)mainInstance.getPlayerHandlingInstance().getPlayerData(player).getCurrentRealm();
		SubMenu subMenu = new SubMenu("Kit Selector",9,this);
		
		if (game.getRealm() == Realm.KITPVP) {
			String kitSelected = (game.getPlayerKit(player) == null ? "knight" : game.getPlayerKit(player));
			
			//Knight kit
			subMenu.addOption(1, Material.IRON_CHESTPLATE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Knight", new ArrayList<String>() {{
				add(ChatColor.YELLOW + "- Iron Sword (Sharpness 1)");
				add(ChatColor.YELLOW + "- Iron Armor (Protection 1)");
				add(ChatColor.YELLOW + "- x1 Golden Apple");
				add(ChatColor.RESET.toString());
				if (kitSelected.equalsIgnoreCase("knight")) {
					add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
				} else {
					add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
				}
			}}, new Runnable() {
	            @Override
	            public void run() {       
	            	if (!kitSelected.equalsIgnoreCase("knight")) {
						game.setPlayerKit(player, "knight");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						player.sendMessage(ChatColor.YELLOW + "You equipped the " + ChatColor.BOLD + "Knight" + ChatColor.RESET + ChatColor.YELLOW + " kit!");
						player.closeInventory();
					}
	            }
			});
			
			//Brawler kit
			subMenu.addOption(3, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Brawler", new ArrayList<String>() {{
				add(ChatColor.YELLOW + "- Iron Axe (Sharpness 2)");
				add(ChatColor.YELLOW + "- Leather Armor (Protection 2)");
				add(ChatColor.YELLOW + "- x5 Golden Apple");
				add(ChatColor.RESET.toString());
				if (kitSelected.equalsIgnoreCase("brawler")) {
					add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
				} else {
					add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
				}
			}}, new Runnable() {
	            @Override
	            public void run() {       
	            	if (!kitSelected.equalsIgnoreCase("brawler")) {
						game.setPlayerKit(player, "brawler");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						player.sendMessage(ChatColor.YELLOW + "You equipped the " + ChatColor.BOLD + "Brawler" + ChatColor.RESET + ChatColor.YELLOW + " kit!");
						player.closeInventory();
					}
	            }
			});
			
			//Archer kit
			subMenu.addOption(5, Material.BOW, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Archer", new ArrayList<String>() {{
				add(ChatColor.YELLOW + "- Gold Sword (Sharpness 1)");
				add(ChatColor.YELLOW + "- Chain Armor (Protection 1)");
				add(ChatColor.YELLOW + "- Bow (Power 1)");
				add(ChatColor.YELLOW + "- x40 Arrows");
				add(ChatColor.RESET.toString());
				if (kitSelected.equalsIgnoreCase("archer")) {
					add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
				} else {
					add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
				}
			}}, new Runnable() {
	            @Override
	            public void run() {       
	            	if (!kitSelected.equalsIgnoreCase("archer")) {
						game.setPlayerKit(player, "archer");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						player.sendMessage(ChatColor.YELLOW + "You equipped the " + ChatColor.BOLD + "Archer" + ChatColor.RESET + ChatColor.YELLOW + " kit!");
						player.closeInventory();
					}
	            }
			});
			
			//Wizard kit
			subMenu.addOption(7, Material.POTION, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Wizard", new ArrayList<String>() {{
				add(ChatColor.YELLOW + "- Iron Sword");
				add(ChatColor.YELLOW + "- Leather Armor (Protection 1)");
				add(ChatColor.YELLOW + "- x5 Fire balls");
				add(ChatColor.YELLOW + "- x2 Potion of harm");
				add(ChatColor.YELLOW + "- x1 Potion of regeneration");
				add(ChatColor.RESET.toString());
				if (kitSelected.equalsIgnoreCase("wizard")) {
					add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
				} else {
					add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
				}
			}}, new Runnable() {
	            @Override
	            public void run() {       
	            	if (!kitSelected.equalsIgnoreCase("wizard")) {
						game.setPlayerKit(player, "wizard");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						player.sendMessage(ChatColor.YELLOW + "You equipped the " + ChatColor.BOLD + "Wizard" + ChatColor.RESET + ChatColor.YELLOW + " kit!");
						player.closeInventory();
					}
	            }
			});
		} else if (game.getRealm() == Realm.SKYWARS) {
			String kitSelected = (game.getPlayerKit(player) == null ? "default" : game.getPlayerKit(player));
			
			//Default kit
			subMenu.addOption(4, Material.WOODEN_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Default", new ArrayList<String>() {{
				add(ChatColor.YELLOW + "- Wood Sword");
				add(ChatColor.YELLOW + "- x15 Concrete blocks");
				add(ChatColor.YELLOW + "- x10 Beef");
				add(ChatColor.RESET.toString());
				if (kitSelected.equalsIgnoreCase("default")) {
					add(ChatColor.RED.toString() + ChatColor.BOLD + "Already equipped");	
				} else {
					add(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to equip");			
				}
			}}, new Runnable() {
	            @Override
	            public void run() {       
	            	if (!kitSelected.equalsIgnoreCase("default")) {
						game.setPlayerKit(player, "default");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
						player.sendMessage(ChatColor.YELLOW + "You equipped the " + ChatColor.BOLD + "Default" + ChatColor.RESET + ChatColor.YELLOW + " kit!");
						player.closeInventory();
					}
	            }
			});
		}
	}
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW + "Select Kit" + ChatColor.GREEN + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("kit_selector");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		setContents();
		open();
	}
}
