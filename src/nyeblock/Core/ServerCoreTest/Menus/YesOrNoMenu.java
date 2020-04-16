package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;

@SuppressWarnings("serial")
public class YesOrNoMenu extends MenuBase {
	private String title;
	private BiConsumer<Player,Boolean> biConsumer;
	
	public YesOrNoMenu(Main mainInstance, Player player, String title) { 
		super(mainInstance,player,"yes_or_no_menu");
		
		this.title = title;
	}
	
	public void setContents() {
		SubMenu subMenu = new SubMenu(title,27,this);
		
		subMenu.createOption(12, Material.GREEN_WOOL, ChatColor.GREEN + "Yes", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to confirm your decision");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					Bukkit.getScheduler().runTask(mainInstance, (Runnable)() -> {
						biConsumer.accept(player,true);
					});
	            }
			});
		}});
		
		subMenu.createOption(14, Material.RED_WOOL, ChatColor.RED + "No", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to cancel your decision");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					Bukkit.getScheduler().runTask(mainInstance, (Runnable)() -> {
						biConsumer.accept(player,false);
					});
	            }
			});
		}});
	}
	public YesOrNoMenu onDecide(BiConsumer<Player,Boolean> biConsumer) {
		this.biConsumer = biConsumer;
		
		return this;
	}
	public void display() {
		setContents();
		open();
	}
}
