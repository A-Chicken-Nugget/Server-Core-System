package nyeblock.Core.ServerCoreTest.Misc;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class TabListPerWorld {
	public void hideForAll(Player player) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			hide(all, player);
		}
	}
	public void hideForWorld(World world, Player toHide) {
		for (Player players : world.getPlayers()) {
			hide(players, toHide);
		}
	}
	public void hide(Player player, Player toHide) {
		if (player == null || player == toHide) {
			return;
		}
		toHide.hidePlayer(player);
	}
	public void showForAll(Player player) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			show(all, player);
		}
	}
	public void showForWorld(World world, Player toShow) {
		for (Player players : world.getPlayers()) {
			show(players, toShow);
		}
	} 
	public void show(Player player, Player toShow) {
		if (player == null || player == toShow) {
			return;
		}
		toShow.showPlayer(player);
	}
}
