package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

@SuppressWarnings({"rawtypes","unchecked"})

public class CommandHandling implements CommandExecutor, TabCompleter {
	private Main mainInstance;
	
	public CommandHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (sender instanceof Player) {
			Player ply = (Player) sender;
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			
			if (playerData.getUserGroup() == UserGroup.ADMIN) {
				if (args.length > 0) {
					if (label.equalsIgnoreCase("setpermission")) {
						
//						if (args[0].equalsIgnoreCase("start")) {
//							round.begin();
//						} else if (args[0].equalsIgnoreCase("end")) {
//							round.end();
//						} else {
//							ply.sendMessage(ChatColor.RED + "Please enter a valid command!");
//						}
					} //else if (label.equalsIgnoreCase("kit")) {
//						if (args[0].equalsIgnoreCase("brawler")) {
//							PlayerInventory inv = ply.getInventory();
//							
//							for (ItemStack item : inv) {
//								if (item != null) {								
//									if (item.getType() != Material.DIAMOND) {
//										if (item.getType() != Material.EMERALD) {										
//											inv.remove(item);
//										}
//									}
//								}
//							}
//							
//							ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
//							sword.addEnchantment(Enchantment.KNOCKBACK, 2);
//							sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
//							sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
//							
//							ItemStack[] armor = {
//									new ItemStack(Material.IRON_BOOTS),
//									new ItemStack(Material.IRON_LEGGINGS),
//									new ItemStack(Material.IRON_CHESTPLATE),
//									new ItemStack(Material.IRON_HELMET)
//							};
//							
//							armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
//							armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
//							armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
//							armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
//							
//							ply.getInventory().addItem(sword);
//							ply.getInventory().setArmorContents(armor);
//						} else {
//							ply.sendMessage(ChatColor.RED + "Please enter a valid kit name!");
//						}
//					}
				} else {
					ply.sendMessage(ChatColor.RED + "Please enter a valid command!");
				}
			} else {
				ply.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.");
			}
			return true;
		} else {
			return false;
		}
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> autoCompletes = new ArrayList();
		
		if (command.getLabel().equalsIgnoreCase("setpermission")) {
			if (args.length > 1) {
				for (String permission : Arrays.asList("canBreakBlocks","canUseInventory")) {
					if (permission.contains(args[1])) {
						autoCompletes.add(permission);
					}
				}
			}
		}
		
		return autoCompletes;
	}
}
