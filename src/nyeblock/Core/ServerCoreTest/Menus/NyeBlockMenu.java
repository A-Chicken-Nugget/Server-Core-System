package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopEquipSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.UserGroupRequirement;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Misc.Party;
import nyeblock.Core.ServerCoreTest.Misc.SignInput;

@SuppressWarnings("serial")
public class NyeBlockMenu extends MenuBase {
	private PlayerData playerData;
	private Party party;
	
	public NyeBlockMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"nyeblock_menu");
	}
	
	public void checkPartyStatus() {
		Party myParty = playerData.getParty();
		
		if (myParty == null) {
			openMenu("NyeBlock Menu",true);
		}
	}
	
	public void setContents() {
		MenuBase instance = this;
		PlayerHandling playerHandling = mainInstance.getPlayerHandlingInstance();
		playerData = playerHandling.getPlayerData(player);
		party = playerData.getParty();
		SubMenu subMenu;
		
		//
		// Nyeblock menu
		//
		subMenu = new SubMenu("NyeBlock Menu",36,this);
		
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
		
		//Party
		subMenu.createOption(21, Material.OAK_SAPLING, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Party", new ArrayList<String>() {{
			if (party != null) {				
				add(ChatColor.YELLOW + "You are in a party");
				add(ChatColor.GREEN + party.getCreator().getName() + "'s" + ChatColor.YELLOW + " party");
				add(ChatColor.RESET.toString());
				add(ChatColor.YELLOW + String.valueOf(party.getMemberCount()) + "/5 members");
				add(ChatColor.RESET.toString());
				add(ChatColor.GREEN + "\u279D \u279D Click to view party details");
			} else {
				add(ChatColor.YELLOW + "You are not in a party");
				add(ChatColor.RESET.toString());
				add(ChatColor.GREEN + "\u279D \u279D Click to create a party");
			}
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					if (party == null) {						
						Party newParty = new Party(mainInstance,player,5);
						playerData.setParty(newParty);
						party = newParty;
						openMenu("My Party",true);
					} else {
						openMenu("My Party",false);
						checkPartyStatus();
					}
	            }
			});
		}});
		
		//
		// Chat text colors menu
		//
		final ShopEquipSubMenu shopSubMenu = new ShopEquipSubMenu("Chat Text Color",45,true,1,this);
		
		//Grey
		shopSubMenu.createShopOption(12, Material.LIGHT_GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Grey chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("gray-text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.GRAY);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("gray-text_color");
				}
			});
		}}, null, "gray-text_color", 500, false);
		
		//Dark Gray
		shopSubMenu.createShopOption(14, Material.GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Dark Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Dark Grey chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("dark_gray-text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.DARK_GRAY);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("dark_gray-text_color");
				}
			});
		}}, null, "dark_gray-text_color", 500, false);
		
		//Red
		shopSubMenu.createShopOption(20, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Red", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Red chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("red-text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.RED);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("red-text_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "red-text_color", 750, false);
		
		//Blue
		shopSubMenu.createShopOption(22, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Blue", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Blue chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("blue-text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.BLUE);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("blue-text_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "blue-text_color", 750, false);
		
		//Green
		shopSubMenu.createShopOption(24, Material.GREEN_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Green", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Green chat text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("green-text_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu.canEquipItem()) {								
								playerData.setChatTextColor(ChatColor.GREEN);
							}
						} else {
							playerData.setChatTextColor(null);
						}
					}
					shopSubMenu.useItem("green-text_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "green-text_color", 750, false);
		
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
		final ShopEquipSubMenu shopSubMenu2 = new ShopEquipSubMenu("Name Color",45,true,1,this);
		
		//Grey
		shopSubMenu2.createShopOption(12, Material.LIGHT_GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Grey name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("gray-name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.GRAY);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("gray-name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "gray-name_color", 1250, false);
		
		//Dark Gray
		shopSubMenu2.createShopOption(14, Material.GRAY_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Dark Grey", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Dark Grey name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("dark_gray-name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.DARK_GRAY);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("dark_gray-name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "dark_gray-name_color", 1250, false);
		
		//Red
		shopSubMenu2.createShopOption(20, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Red", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Red name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("red-name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.RED);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("red-name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "red-name_color", 1750, false);
		
		//Blue
		shopSubMenu2.createShopOption(22, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Blue", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Blue name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("blue-name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.BLUE);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("blue-name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "blue-name_color", 1750, false);
		
		//Green
		shopSubMenu2.createShopOption(24, Material.GREEN_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Green", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Green name text color.");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					ShopItem item = playerData.getShopItem("green-name_color");
					
					if (item != null) {
						if (!item.isEquipped()) {
							if (shopSubMenu2.canEquipItem()) {								
								playerData.setNameTextColor(ChatColor.GREEN);
							}
						} else {
							playerData.setNameTextColor(null);
						}
					}
					shopSubMenu2.useItem("green-name_color");
				}
			});
		}}, new ArrayList<RequirementBase>() {{
			add(new UserGroupRequirement(UserGroup.VIP));
		}}, "green-name_color", 1750, false);
		
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
					playerData.setNameTextColor(null);
					openMenu("Name Color",true);
					player.sendMessage(ChatColor.YELLOW + "Name text color reset!");
				}
			});
		}});
		
		//
		// Party menus
		//
		
		if (party != null) {
			boolean isPartyCreator = (party.getCreator().equals(player) ? true : false);
			
			//
			// My party
			//
			final SubMenu createPartySubMenu = new SubMenu("My Party",36,this);
			
			if (isPartyCreator) {	
				//Invite player
				createPartySubMenu.createOption(11, Material.OAK_SIGN, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Invite players", new ArrayList<String>() {{
					add(ChatColor.YELLOW + "Invite players on the server");
					add(ChatColor.YELLOW + "to join your party.");
					add(ChatColor.RESET.toString());
					add(ChatColor.GREEN + "\u279D \u279D Click to invite a player");
				}}, new HashMap<ClickType,Runnable>() {{
					put(ClickType.LEFT,new Runnable() {
						@Override
						public void run() {
							new SignInput(mainInstance)
							.line(0, "")
							.line(1, "^^^^^^^^^^^^^^^^")
							.line(2, "Enter player")
							.line(3, "name to invite")
							.result((ply, lines) -> {
								if (lines[0] != "") {				        		
									ArrayList<Player> playerResults = new ArrayList<Player>();
									
									for (Player ply2 : Bukkit.getOnlinePlayers()) {
										if (ply2.getName().toLowerCase().contains(lines[0].toLowerCase())) {
											playerResults.add(ply2);
										}
									}
									if (playerResults.size() > 0) {
										if (playerResults.size() > 1) {
											new ChoosePlayerMenu(mainInstance,player,instance,"My Party")
												.setPlayers(playerResults)
												.onChoose((player,selectedPlayer) -> {
													if (selectedPlayer.equals(player)) {
														player.sendMessage(ChatColor.YELLOW + "You cannot invite yourself!");
													} else if (party.getMembers().contains(selectedPlayer)) {
														player.sendMessage(ChatColor.YELLOW + "Cannot invite a player already in the party!");
													} else {														
														player.sendMessage(ChatColor.YELLOW + "Party invite sent to " + ChatColor.GREEN + selectedPlayer.getName());
														selectedPlayer.sendMessage(ChatColor.YELLOW + "You've been invited to " + ChatColor.GREEN + player.getName() + "'s "
															+ ChatColor.YELLOW + "party. Type "
															+ ChatColor.BOLD + "/acceptPartyInvite"
															+ ChatColor.RESET + ChatColor.YELLOW + " to join!");
														mainInstance.getPlayerHandlingInstance().getPlayerData(selectedPlayer).setPartyInvite(party);
													}
													openMenu("My Party",false);
												}).display();
										} else {
											if (playerResults.get(0).getName().equalsIgnoreCase(lines[0])) {
												Player selectedPlayer = playerResults.get(0);
												
												if (selectedPlayer.equals(player)) {
													player.sendMessage(ChatColor.YELLOW + "You cannot invite yourself!");
												} else if (party.getMembers().contains(selectedPlayer)) {
													player.sendMessage(ChatColor.YELLOW + "Cannot invite a player already in the party!");
												} else {														
													player.sendMessage(ChatColor.YELLOW + "Party invite sent to " + ChatColor.GREEN + selectedPlayer.getName());
													selectedPlayer.sendMessage(ChatColor.YELLOW + "You've been invited to " + ChatColor.GREEN + player.getName() + "'s "
															+ ChatColor.YELLOW + "party. Type "
															+ ChatColor.BOLD + "/acceptPartyInvite"
															+ ChatColor.RESET + ChatColor.YELLOW + " to join!");
													mainInstance.getPlayerHandlingInstance().getPlayerData(selectedPlayer).setPartyInvite(party);
													openMenu("My Party",false);
												}
											} else {
												new ChoosePlayerMenu(mainInstance,player,instance,"My Party")
												.setPlayers(playerResults)
												.onChoose((player,selectedPlayer) -> {
													if (selectedPlayer.equals(player)) {
														player.sendMessage(ChatColor.YELLOW + "You cannot invite yourself!");
													} else if (party.getMembers().contains(selectedPlayer)) {
														player.sendMessage(ChatColor.YELLOW + "Cannot invite a player already in the party!");
													} else {														
														player.sendMessage(ChatColor.YELLOW + "Party invite sent to " + ChatColor.GREEN + selectedPlayer.getName());
														selectedPlayer.sendMessage(ChatColor.YELLOW + "You've been invited to " + ChatColor.GREEN + player.getName() + "'s "
																+ ChatColor.YELLOW + "party. Type "
																+ ChatColor.BOLD + "/acceptPartyInvite"
																+ ChatColor.RESET + ChatColor.YELLOW + " to join!");
														mainInstance.getPlayerHandlingInstance().getPlayerData(selectedPlayer).setPartyInvite(party);
													}
													openMenu("My Party",false);
												}).display();
											}
										}
									} else {
										player.sendMessage(ChatColor.YELLOW + "No users found with the entered name.");
										openMenu("My Party",false);
									}
								} else {
									player.sendMessage(ChatColor.YELLOW + "Please enter a name to search for!");
									openMenu("My Party",false);
								}
							}).show(player);
						}
					});
				}});
			}
			
			//Players in party
			createPartySubMenu.createOption(13, Material.PLAYER_HEAD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Players in party", new ArrayList<String>() {{
				add(ChatColor.YELLOW + "Players in your party.");
				add(ChatColor.RESET.toString());
				add(ChatColor.GREEN + "\u279D \u279D Click to view");
			}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						openMenu("Party Members - Page 1",true);
						checkPartyStatus();
					}
				});
			}});
			
			if (isPartyCreator) {			
				//Delete
				createPartySubMenu.createOption(15, Material.BARRIER, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Destroy party", new ArrayList<String>() {{
					add(ChatColor.GREEN + "\u279D \u279D Click to destory your party");
				}}, new HashMap<ClickType,Runnable>() {{
					put(ClickType.LEFT,new Runnable() {
						@Override
						public void run() {
							new YesOrNoMenu(mainInstance,player,"Delete your party?")
								.onDecide((ply, decision) -> {
									if (decision) {
										playerData.getParty().destroy();
										openMenu("NyeBlock Menu",true);
									} else {
										openMenu("My Party",false);
									}
								}).display();
						}
					});
				}});
			}
			
			//Back
			createPartySubMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						openMenu("NyeBlock Menu",false);
						checkPartyStatus();
					}
				});
			}});
			
			//
			// Party members
			//
			ArrayList<Player> partyMembers = party.getMembers();
			
			int pages = (int)Math.ceil((double)partyMembers.size()/12);
			
			for (int page = 0; page < pages; page++) {
				final int currentPage = (page+1);
				int row = 0;
				int playerCount = 0;
				SubMenu pageSubMenu = new SubMenu("Party Members - Page " + (page+1),45,this);
				
				for (int i = 0; i < 12; i++) {
					if (i != 0 && i % 4 == 0) {
						row += 1;
					}
					
					int position = 10 + (i*2) + row;
					int playerIndex = (page*12)+i;
					
					if (partyMembers.size() > playerIndex) {
						Player currentPlayer = partyMembers.get(playerIndex);
						
						pageSubMenu.createOption(position,currentPlayer.getName(),ChatColor.YELLOW + ChatColor.BOLD.toString() + currentPlayer.getName(),new ArrayList<String>() {{
							if (party.getCreator().equals(currentPlayer)) {
								add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "Party creator");
							}
							if (isPartyCreator) {
								if (!party.getCreator().equals(currentPlayer)) {									
									add(ChatColor.GREEN + "\u279D \u279D Click to kick this player");
								}
							}
						}},new HashMap<ClickType,Runnable>() {{
							if (isPartyCreator) {
								put(ClickType.LEFT,new Runnable() {
									@Override
									public void run() {
										new YesOrNoMenu(mainInstance,player,"Kick " + currentPlayer.getName() + "?")
										.onDecide((ply, decision) -> {
											if (decision) {
												playerData.getParty().kickPlayer(currentPlayer);
												openMenu("My Party",true);
											} else {
												openMenu("Party Members - Page " + currentPage,false);
											}
										}).display();
									}
								});
							}
						}});
						playerCount++;
					}
				}
				int size = 0;
				
				if (playerCount >= 8) {
					if (pages-(page+1) > 0) {
						size = 54;
						
						pageSubMenu.createOption(53, Material.ARROW, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Next page", null, new HashMap<ClickType,Runnable>() {{
							put(ClickType.LEFT,new Runnable() {
								@Override
								public void run() {
									openMenu("Party Members - Page " + (currentPage+1),false);
									checkPartyStatus();
								}
							});
						}});
					} else {						
						size = 45;
					}
				} else if (playerCount > 4) {
					size = 36;
				} else {
					size = 27;
				}
				if (page != 0) {
					if (size != 54) {
						size += 9;
					}
					pageSubMenu.createOption(size-9, Material.BARRIER, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Previous page", null, new HashMap<ClickType,Runnable>() {{
						put(ClickType.LEFT,new Runnable() {
							@Override
							public void run() {
								openMenu("Party Members - Page " + (currentPage-1),false);
								checkPartyStatus();
							}
						});
					}});
				}
				pageSubMenu.createOption(size-5, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Back", null, new HashMap<ClickType,Runnable>() {{
					put(ClickType.LEFT,new Runnable() {
						@Override
						public void run() {
							openMenu("My Party",false);
							checkPartyStatus();
						}
					});
				}});
				pageSubMenu.setSize(size);
			}
		}
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
