package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Kits.KitBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopEquipSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuTypeOptionItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class SkyWarsShop extends ShopBase {
	public SkyWarsShop(Main mainInstance, Player player) {
		super(mainInstance,player,"skywars_shop");
	}
	
	public void setContents() {
		SubMenu subMenu;
		
		//
		// Shop menu
		//
		subMenu = new SubMenu("Sky Wars Shop",27,this);
		
		//Realm win actions
		subMenu.createOption(12, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Win Actions", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Win actions are played whenever");
			add(ChatColor.YELLOW + "you win in a Sky Wars game.");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "(Can only equip 1 item at a time)");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the win actions");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("Win Actions",false);
				}
			});
		}});
		
		//Realm win actions
		subMenu.createOption(14, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Special Kits", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Special kits that give you");
			add(ChatColor.YELLOW + "different fighting abilities");
			add(ChatColor.YELLOW + "from the default kits.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the special kits");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("Special Kits - Page 1",false);
				}
			});
		}});
		
		//
		// Win Actions
		//
		ShopEquipSubMenu shopSubMenu = new ShopEquipSubMenu("Win Actions",36,true,1,this);
		
		//Rainbow scoreboard title
		shopSubMenu.createShopOption(11, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rainbow Scoreboard Title", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The title of the scoreboard will");
			add(ChatColor.YELLOW + "change colors randomly.");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.useItem("skyWars_rainbow_scoreboard_winAction");
					}
				});
		}}, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(3,Realm.SKYWARS));
		}}, "skyWars_rainbow_scoreboard_winAction", 350, false);
		
		//Fireworks
		shopSubMenu.createShopTypeOption(13, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fireworks", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Random fireworks will be shot");
			add(ChatColor.YELLOW + "into the sky.");
		}}, new ArrayList<ShopMenuTypeOptionItem>() {{
			add(new ShopMenuTypeOptionItem("Red","red",450,Arrays.asList(new LevelRequirement(3,Realm.SKYWARS))));
			add(new ShopMenuTypeOptionItem("Blue","blue",450,Arrays.asList(new LevelRequirement(4,Realm.SKYWARS))));
			add(new ShopMenuTypeOptionItem("Green","green",450,Arrays.asList(new LevelRequirement(5,Realm.SKYWARS))));
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.useItem("skyWars_fireworks_winAction");
					}
				});
		}}, "Color", "skyWars_fireworks_winAction", false);
		
		//Time speed up
		shopSubMenu.createShopOption(15, Material.CLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Time Speed Up", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The timescale will speed up and");
			add(ChatColor.YELLOW + "alternate through day/night quickly.");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.useItem("skyWars_time_speed_up_winAction");
					}
				});
		}}, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(5,Realm.SKYWARS));
		}}, "skyWars_time_speed_up_winAction", 550, false);
		
		//Back
		shopSubMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("Sky Wars Shop",false);
				}
			});
		}});
		
		//
		// Special Kits
		//
		ArrayList<KitBase> kits = mainInstance.getKitHandlingInstance().getRealmKits(Realm.SKYWARS,false,true);
		int pages = (int)Math.ceil((double)kits.size()/12);
		
		for (int page = 0; page < pages; page++) {
			final int currentPage = (page+1);
			int row = 0;
			int kitCount = 0;
			ShopSubMenu shopSubMenu2 = new ShopSubMenu("Special Kits - Page " + (page+1),45,this);
			
			for (int i = 0; i < 12; i++) {
				if (i != 0 && i % 4 == 0) {
					row += 1;
				}
				
				int position = 10 + (i*2) + row;
				int kitIndex = (page*12)+i;
				
				if (kits.size() > kitIndex) {
					KitBase kit = kits.get(kitIndex);
					shopSubMenu2.createShopOption(position, kit.getMaterial(), ChatColor.YELLOW.toString() + ChatColor.BOLD + kit.getName(), kit.getDescription(),
						new HashMap<ClickType,Runnable>() {{
							put(ClickType.LEFT,new Runnable() {
								@Override
								public void run() {
									shopSubMenu2.useItem(kit.getUniqueId());
								}
							});
						}}, kit.getPurchaseRequirements(), kit.getUniqueId(), kit.getCost(), false);
					kitCount++;
				}
			}
			int size = 0;
			
			if (kitCount >= 8) {
				if (pages-(page+1) > 0) {
					size = 54;
					
					shopSubMenu2.createOption(53, Material.ARROW, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Next page", null, new HashMap<ClickType,Runnable>() {{
						put(ClickType.LEFT,new Runnable() {
							@Override
							public void run() {
								openMenu("Special Kits - Page " + (currentPage+1),false);
							}
						});
					}});
				} else {						
					size = 45;
				}
			} else if (kitCount > 4) {
				size = 36;
			} else {
				size = 27;
			}
			if (page != 0) {
				if (size != 54) {
					size += 9;
				}
				shopSubMenu2.createOption(size-9, Material.BARRIER, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Previous page", null, new HashMap<ClickType,Runnable>() {{
					put(ClickType.LEFT,new Runnable() {
						@Override
						public void run() {
							openMenu("Special Kits - Page " + (currentPage-1),false);
						}
					});
				}});
			} else {
				if (size != 54) {
					size += 9;
				}
				shopSubMenu2.createOption(size-9, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Back", null, new HashMap<ClickType,Runnable>() {{
					put(ClickType.LEFT,new Runnable() {
						@Override
						public void run() {
							openMenu("Sky Wars Shop",false);
						}
					});
				}});
			}
			shopSubMenu2.setSize(size);
		}
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "Sky Wars Shop" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("skywars_shop");
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
