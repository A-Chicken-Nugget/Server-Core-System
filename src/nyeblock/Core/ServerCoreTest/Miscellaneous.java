package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Chest;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_14_R1.AxisAlignedBB;
import net.minecraft.server.v1_14_R1.EntityPlayer;

public class Miscellaneous {
	//Check if a player is within 2 location vectors
	public static boolean playerInArea(Vector loc, Vector l1, Vector l2){
        return new IntRange(l1.getX(), l2.getX()).containsDouble(loc.getX())
                && new IntRange(l1.getY(), l2.getY()).containsDouble(loc.getY())
                &&  new IntRange(l1.getZ(), l2.getZ()).containsDouble(loc.getZ());
    }
	//Used for the player scoreboards. If there is a change on a scoreboard row, update it
	public static void updateScore(Objective o, int score, String name) {
		boolean found = false;
		
		for (String s : o.getScoreboard().getEntries()) {
	        if(o.getScore(s).getScore() == score) {
	        	if (!s.equalsIgnoreCase(name)) {
	        		o.getScoreboard().resetScores(s);
	        		o.getScore(name).setScore(score);
	        	}
	        	found = true;
	        }
	    }
		if (!found) {
			o.getScore(name).setScore(score);
		}
	}
	//Format seconds into mm:ss
	public static String formatSeconds(int timeInSeconds)
	{
	    int hours = timeInSeconds / 3600;
	    int secondsLeft = timeInSeconds - hours * 3600;
	    int minutes = secondsLeft / 60;
	    int seconds = secondsLeft - minutes * 60;

	    String formattedTime = "";
	    if (minutes < 10)
	        formattedTime += "0";
	    formattedTime += minutes + ":";

	    if (seconds < 10)
	        formattedTime += "0";
	    formattedTime += seconds ;

	    return formattedTime;
	}
	//Get the blocks a player is standing on
	public static ArrayList<Block> getBlocksBelowPlayer(Player player) {
		ArrayList<Block> blocks = new ArrayList<>();
		Location location = player.getLocation();
		double x = location.getX();
		double z = location.getZ();
		World world = player.getWorld();
		double yBelow = player.getLocation().getY() - 0.0001;
		
		blocks.add(new Location(world, location.getX() + 0.3, yBelow, z - 0.3).getBlock());
		blocks.add(new Location(world, x - 0.3, yBelow, z - 0.3).getBlock());
		blocks.add(new Location(world, x + 0.3, yBelow, z + 0.3).getBlock());
		blocks.add(new Location(world, x - 0.3, yBelow, z + 0.3).getBlock());        
        
        return blocks;
	}
	
	//TODO:Remove after testing
	public static Chest MakeChest(Location blockLocation) 
	{
		Block block = blockLocation.getBlock();
		block.setType(Material.CHEST);
		
		return (Chest) block;
	}
	
	@SuppressWarnings("deprecation")
	public static Chest MakeChest(Location blockLocation, ArrayList<ItemStack> items) 
	{
		Block block = blockLocation.getBlock();
		block.setType(Material.CHEST);
		
		Chest chest = (Chest) block.getState();
		Inventory inv = chest.getBlockInventory();
		
		for (ItemStack item : items) 
		{
			inv.addItem(item);
		}
		
		return chest;
	}
}
