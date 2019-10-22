package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_14_R1.AxisAlignedBB;
import net.minecraft.server.v1_14_R1.EntityPlayer;

public class Miscellaneous {
	//Check if a player is within 2 locations
	public static boolean playerInArea(Vector loc1, Vector loc2){
        int x1,x2,y1,y2,z1,z2;
        x1 = loc1.getX() > loc2.getX() ? (int) loc2.getX() : (int) loc1.getX();
        y1 = loc1.getY() > loc2.getY() ? (int) loc2.getY() : (int) loc1.getY();
        z1 = loc1.getZ() > loc2.getZ() ? (int) loc2.getZ() : (int) loc1.getZ();
       
        x2 = ((int) loc1.getX()) == x1 ? (int) loc2.getX() : (int) loc1.getX();
        y2 = ((int) loc1.getY()) == y1 ? (int) loc2.getY() : (int) loc1.getY();
        z2 = ((int) loc1.getZ()) == z1 ? (int) loc2.getZ() : (int) loc1.getZ();
               
        for (int x = x1; x <= x2; x++){
            for (int y = y1; y <= y2; y++){
                for (int z = z1; z <= z2; z++){
                    for (Player p : Bukkit.getOnlinePlayers()){
                        if (p.getLocation().getBlock().getLocation() == new Location(loc1.getWorld(),x,y,z)) return true;
                    }
                }
            }
        }
       
        return false;
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
	public static ArrayList<Block> getBlocksBelow(Player player) {
		ArrayList<Block> blocksBelow = new ArrayList<Block>();
	    EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
	    AxisAlignedBB boundingBox = entityPlayer.getBoundingBox();
	    World world = player.getWorld();
	    double yBelow = player.getLocation().getY() - 0.0001;
	    Block northEast = new Location(world, boundingBox.d(), yBelow, boundingBox.c()).getBlock();
	    Block northWest = new Location(world, boundingBox.a(), yBelow, boundingBox.c()).getBlock();
	    Block southEast = new Location(world, boundingBox.d(), yBelow, boundingBox.f().f()).getBlock();
	    Block southWest = new Location(world, boundingBox.a(), yBelow, boundingBox.f().f()).getBlock();
	    Block[] blocks = {northEast, northWest, southEast, southWest};
	    for (Block block : blocks) {
	        if (!blocksBelow.isEmpty()) {
	            boolean duplicateExists = false;
	            for (int i = 0; i < blocksBelow.size(); i++) {
	                if (blocksBelow.get(i).equals(block)) {
	                    duplicateExists = true;
	                }
	            }
	            if (!duplicateExists) {
	                blocksBelow.add(block);
	            }
	        } else {
	            blocksBelow.add(block);
	        }
	    }
	    return blocksBelow;
	}
}
