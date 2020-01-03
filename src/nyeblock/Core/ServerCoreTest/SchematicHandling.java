package nyeblock.Core.ServerCoreTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.clipboard.DiskOptimizedClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;

import nyeblock.Core.ServerCoreTest.CustomChests.CustomChestGenerator;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.GameMapInfo;

public class SchematicHandling {
	//Create a schematic in a world based on the game
	public String setSchematic(Main mainInstance,GameBase game) {
		Realm realm = game.getRealm();
		
		ArrayList<File> schems = new ArrayList<File>();
		schems.addAll(Arrays.asList(new File("./plugins/ServerCoreTest/maps").listFiles()));
		ArrayList<File> validSchems = new ArrayList<File>();
		File schem = new File("");
		
		//Pick a random schematic for the world
		for(File file : schems) {
			if (!file.isDirectory()) {				
				String[] gamemode = file.getName().split(Pattern.quote("_"));
				
				if (Integer.parseInt(gamemode[0]) == realm.getValue()) {
					validSchems.add(file);
				}
			}
		}
		schem = validSchems.get(new Random().nextInt(validSchems.size()));
		
		String[] removeExtension = schem.getName().split(Pattern.quote("."));
		String[] mapName = removeExtension[0].split("_");
		System.out.println("[Core] Creating new " + realm.toString() + " game. Using map " + mapName[1]);
		
		BukkitWorld world = new BukkitWorld(Bukkit.getWorld(game.getWorldName()));
		
		//Paste schematic
		DiskOptimizedClipboard clipboard = new DiskOptimizedClipboard(schem);
		clipboard.toClipboard().paste(world, BlockVector3.at(-42, 64, -6),false,false,null);
		clipboard.close();
		
		//Fix lighting
		new BukkitRunnable() {
	        public void run() {
	        	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "/cleanlight world " + game.getWorldName());
			}
	    }.runTask(mainInstance);
		
		game.setSchemStatus(true);
	    
	    return mapName[1];
	}
}