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
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class SchematicHandling {
	public Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
	    Set<String> fileList = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
	        for (Path path : stream) {
	            if (!Files.isDirectory(path)) {
	                fileList.add(path.getFileName()
	                    .toString());
	            }
	        }
	    }
	    return fileList;
	}
	
	//Create a schematic in a world based on the game
	public String setSchematic(Realm realm, String worldName) {
		String schemToUse = null;
		ArrayList<File> schems = new ArrayList<File>();
		schems.addAll(Arrays.asList(new File(Bukkit.getPluginManager().getPlugin("WorldEdit").getDataFolder().getAbsolutePath() + "/schematics").listFiles()));
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
		System.out.println("[Core]: Creating new " + realm.toString() + " game. Using map " + mapName[1]);
		
		ClipboardFormat format = ClipboardFormats.findByFile(schem);
		ClipboardReader reader = null;
		try {
			reader = format.getReader(new FileInputStream(schem));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    try {
			Clipboard clipboard = reader.read();
			
			com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(Bukkit.getWorld(worldName));
		              
			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,-1);

		    Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(-42, 64, -6)).ignoreAirBlocks(true).build();

		    try {
		        Operations.complete(operation);
		        editSession.flushSession();
		        schemToUse = mapName[1];

		    } catch (WorldEditException e) {
		        e.printStackTrace();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return schemToUse;
	}
}