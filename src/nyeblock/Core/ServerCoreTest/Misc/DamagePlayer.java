package nyeblock.Core.ServerCoreTest.Misc;

import org.bukkit.entity.Player;

public class DamagePlayer {
	private Player player;
	private Long time;
	
	public DamagePlayer(Player player, long time) {
		this.player = player;
		this.time = time;
	}
	
	public Player getPlayer() {
		return player;
	}
	public long getTime() {
		return time;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
