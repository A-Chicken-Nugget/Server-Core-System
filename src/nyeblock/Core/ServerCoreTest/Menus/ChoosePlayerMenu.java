package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;

@SuppressWarnings("serial")
public class ChoosePlayerMenu extends MenuBase {
	private ArrayList<Player> playerResults;
	private BiConsumer<Player,Player> biConsumer;
	private MenuBase returnMenu;
	private String menuName;
	
	public ChoosePlayerMenu(Main mainInstance, Player player, MenuBase returnMenu, String menuName) { 
		super(mainInstance,player,"player_result_menu");
		
		this.returnMenu = returnMenu;
		this.menuName = menuName;
	}
	
	public void setContents() {
		int pages = (int)Math.ceil((double)playerResults.size()/12);
		
		for (int page = 0; page < pages; page++) {
			final int currentPage = (page+1);
			int row = 0;
			int playerCount = 0;
			SubMenu subMenu = new SubMenu("Player Results - Page " + (page+1),45,this);
			
			for (int i = 0; i < 12; i++) {
				if (i != 0 && i % 4 == 0) {
					row += 1;
				}
				
				int position = 10 + (i*2) + row;
				int playerIndex = (page*12)+i;
				
				if (playerResults.size() > playerIndex) {
					Player currentPlayer = playerResults.get(playerIndex);
					
					subMenu.createOption(position,currentPlayer.getName(),ChatColor.YELLOW + ChatColor.BOLD.toString() + currentPlayer.getName(),new ArrayList<String>() {{
						add(ChatColor.GREEN + "\u279D \u279D Click to select this player");
					}},new HashMap<ClickType,Runnable>() {{
						put(ClickType.LEFT,new Runnable() {
							@Override
							public void run() {
								biConsumer.accept(player,currentPlayer);
							}
						});
					}});
					playerCount++;
				}
			}
			int size = 0;
			
			if (playerCount >= 8) {
				if (pages-(page+1) > 0) {
					size = 54;
					
					subMenu.createOption(53, Material.ARROW, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Next page", null, new HashMap<ClickType,Runnable>() {{
						put(ClickType.LEFT,new Runnable() {
							@Override
							public void run() {
								openMenu("Player Results - Page " + (currentPage+1),false);
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
				subMenu.createOption(size-9, Material.BARRIER, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Previous page", null, new HashMap<ClickType,Runnable>() {{
					put(ClickType.LEFT,new Runnable() {
						@Override
						public void run() {
							openMenu("Player Results - Page " + (currentPage-1),false);
						}
					});
				}});
			}
			subMenu.createOption(size-5, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Back", null, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						returnMenu.openMenu(menuName,false);
					}
				});
			}});
			subMenu.setSize(size);
		}
	}
	public ChoosePlayerMenu setPlayers(ArrayList<Player> playerResults) {
		this.playerResults = playerResults;
		
		return this;
	}
	public ChoosePlayerMenu onChoose(BiConsumer<Player,Player> biConsumer) {
		this.biConsumer = biConsumer;
		
		return this;
	}
	public void display() {
		setContents();
		open();
	}
}
