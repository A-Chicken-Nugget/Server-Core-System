package nyeblock.Core.ServerCoreTest.Misc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class Toolkit 
{
	public static int GetRandomNumber(int min, int max) 
	{
	    return new Random().nextInt((max - min) + 1) + min;
	}
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
	public static String formatMMSS(int timeInSeconds)
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
		double yBelow = player.getLocation().getY() - .5;
		
		blocks.add(new Location(world, location.getX() + 0.3, yBelow, z - 0.3).getBlock());
		blocks.add(new Location(world, x - 0.3, yBelow, z - 0.3).getBlock());
		blocks.add(new Location(world, x + 0.3, yBelow, z + 0.3).getBlock());
		blocks.add(new Location(world, x - 0.3, yBelow, z + 0.3).getBlock());        
        
        return blocks;
	}
	//Returns if a string is in the proper ip format
	public static boolean isIp (String ip) {
	    try {
	        if ( ip == null || ip.isEmpty() ) {
	            return false;
	        }

	        String[] parts = ip.split( "\\." );
	        if ( parts.length != 4 ) {
	            return false;
	        }

	        for ( String s : parts ) {
	            int i = Integer.parseInt( s );
	            if ( (i < 0) || (i > 255) ) {
	                return false;
	            }
	        }
	        if ( ip.endsWith(".") ) {
	            return false;
	        }

	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	//Get color from string
	public static ChatColor getColorFromString(String color) {
		ChatColor returnColor = ChatColor.WHITE;
		
		if (color != null) {
			returnColor = ChatColor.valueOf(color);
		}
		return returnColor;
	}
	//Get UUID from players name
	public static String getUUIDFromName(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/"+name;
        
        try {
            @SuppressWarnings("deprecation")
            String UUIDJson = IOUtils.toString(new URL(url));           
            if(UUIDJson.isEmpty()) return null;                       
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            String uuid = UUIDObject.get("id").toString();
            return uuid.substring(0,8) + "-" + uuid.substring(8,12) + "-" + uuid.substring(12,16) + "-" + uuid.substring(16,20) + "-" + uuid.substring(20);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
	}
	//Get base64 player skin
	public static String[] getSkinFromName(String name) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();
     
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");  	
        	InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
        	JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
        	String texture = textureProperty.get("value").getAsString();
        	String signature = textureProperty.get("signature").getAsString();
        	
        	return new String[] {texture, signature};
        } catch (IOException e) {
            return null;
        }
    }
}
