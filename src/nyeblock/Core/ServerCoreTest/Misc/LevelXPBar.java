package nyeblock.Core.ServerCoreTest.Misc;

import org.bukkit.ChatColor;

public class LevelXPBar {
	public static String getBarText(int length, double currentXP, double nextLevelXP) {
		String text = "";
		double frac = (currentXP/nextLevelXP)*100;
		int receivedXP = (int)((frac/100)*length);
		int availableXP = length-receivedXP;
		
		text += ChatColor.GREEN;
		for (int i = 0; i < receivedXP; i++) {
			text += "|";
		}
		text += ChatColor.YELLOW;
		for (int i = 0; i < availableXP; i++) {
			text += "|";
		}
		
		return text;
	}
}
