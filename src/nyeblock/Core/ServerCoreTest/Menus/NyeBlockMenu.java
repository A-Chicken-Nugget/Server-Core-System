package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.UserGroupRequirement;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

@SuppressWarnings("serial")
public class NyeBlockMenu extends MenuBase {
	public NyeBlockMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"nyeblock_menu");
	}
	
	public void setContents() {
		PlayerHandling playerHandling = mainInstance.getPlayerHandlingInstance();
		PlayerData playerData = playerHandling.getPlayerData(player);
		SubMenu subMenu;
		
		//
		// Nyeblock menu
		//
		subMenu = new SubMenu("NyeBlock Menu",27,this);
		
		//My Profile
		subMenu.createOption(11, Material.SPONGE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "My Profile", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Your nyeblock profile. This is still");
			add(ChatColor.YELLOW + "in development.");
			add(ChatColor.RESET.toString());
			add(ChatColor.RED + "\u2716 Currently not available \u2716");
		}}, null);
		
		//Chat Text Color
		subMenu.createOption(13, Material.OAK_SIGN, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Chat Text Color", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "This will be the color of the text");
			add(ChatColor.YELLOW + "you send in chat.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the chat colors");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
	            	openMenu("Chat Text Color",false);
	            }
			});
		}});
		
		//Name Color
		subMenu.createOption(15, Material.DARK_OAK_SIGN, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Name Color", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "This will be the color of your name");
			add(ChatColor.YELLOW + "when talk in chat.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the name colors");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
	            	openMenu("Name Color",false);
	            }
			});
		}});
		
		//
		// Chat text colors menu
		//
		final ShopSubMenu shopSubMenu = new ShopSubMenu("Chat Text Color",45,true,1,this);
		
		//Grey
		shopSubMenu.createShopOption(12, Material.LIGHT_GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Grey chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("grey_text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.GRAY);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("grey_text_color");
				}
			});
		}}, null, "grey_text_color", 500, false);
		
		//Dark Gray
		shopSubMenu.createShopOption(14, Material.GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Dark Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Dark Grey chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("darkGrey_text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.DARK_GRAY);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("darkGrey_text_color");
				}
			});
		}}, null, "darkGrey_text_color", 500, false);
		
		//Red
		shopSubMenu.createShopOption(20, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Red", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Red chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("red_text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.RED);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("red_text_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "red_text_color", 750, false);
		
		//Blue
		shopSubMenu.createShopOption(22, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Blue", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Blue chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("blue_text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.BLUE);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("blue_text_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "blue_text_color", 750, false);
		
		//Green
		shopSubMenu.createShopOption(24, Material.GREEN_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Green", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Green chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("green_text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.GREEN);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("green_text_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "green_text_color", 750, false);
		
		//Back
		shopSubMenu.createOption(36, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("NyeBlock Menu",false);
				}
			});
		}});
		
		//Reset
		shopSubMenu.createOption(44, Material.BARRIER, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Reset", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Reset your chat text color to default.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					for (ShopItem item : playerData.getShopItems()) {
						if (item.getMenuName().equalsIgnoreCase("Chat Text Color") && item.isEquipped()) {
							item.setEquipped(false);
						}
					}
					playerData.setChatTextColor(null);
					openMenu("Chat Text Color",true);
					player.sendMessage(ChatColor.YELLOW + "Chat text color reset!");
				}
			});
		}});
		
		//
		// Name colors menu
		//
		final ShopSubMenu shopSubMenu2 = new ShopSubMenu("Name Color",45,true,1,this);
		
		//Grey
		shopSubMenu2.createShopOption(12, Material.LIGHT_GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Grey name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("grey_name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.GRAY);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("grey_name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "grey_name_color", 1250, false);
		
		//Dark Gray
		shopSubMenu2.createShopOption(14, Material.GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Dark Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Dark Grey name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("darkGrey_name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.DARK_GRAY);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("darkGrey_name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "darkGrey_name_color", 1250, false);
		
		//Red
		shopSubMenu2.createShopOption(20, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Red", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Red name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("red_name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.RED);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("red_name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "red_name_color", 1750, false);
		
		//Blue
		shopSubMenu2.createShopOption(22, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Blue", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Blue name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("blue_name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.BLUE);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("blue_name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "blue_name_color", 1750, false);
		
		//Green
		shopSubMenu2.createShopOption(24, Material.GREEN_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Green", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Green name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("green_name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.GREEN);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("green_name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "green_name_color", 1750, false);
		
		//Back
		shopSubMenu2.createOption(36, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("NyeBlock Menu",false);
				}
			});
		}});
		
		//Reset
		shopSubMenu2.createOption(44, Material.BARRIER, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Reset", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Reset your name text color to default.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					for (ShopItem item : playerData.getShopItems()) {
						if (item.getMenuName().equalsIgnoreCase("Name Color") && item.isEquipped()) {
							item.setEquipped(false);
						}
					}
					playerData.setChatTextColor(null);
					openMenu("Name Color",true);
					player.sendMessage(ChatColor.YELLOW + "Name text color reset!");
				}
			});
		}});
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.BEACON);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "NyeBlock Menu" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("nyeblock_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		if (mainInstance.getPlayerHandlingInstance().getPlayerData(player).getLoadedDBInfoStatus()) {			
			open();
		}
	}
}
