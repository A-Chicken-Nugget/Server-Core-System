package nyeblock.Core.ServerCoreTest;

import java.io.File;

import org.bukkit.Bukkit;

import com.boydti.fawe.object.clipboard.DiskOptimizedClipboard;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;

import nyeblock.Core.ServerCoreTest.Maps.MapBase;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

public class SchematicHandling {
	//Create a schematic in a world based on the game
	public static void setSchematic(Main mainInstance,GameBase game) {
		MapBase map = game.getMap();
		File schem = map.getSchematicFile();
		
		//Paste schematic
		DiskOptimizedClipboard clipboard = new DiskOptimizedClipboard(schem);
		clipboard.paste(new BukkitWorld(Bukkit.getWorld(game.getWorldName())), BlockVector3.at(-42, 30, -6),false,false,null);
		clipboard.close();
		
		mainInstance.getTimerInstance().createRunnableTimer("schematicWait_" + game.getWorldName(), 1, 1, new Runnable() {
			@Override
			public void run() {				
				game.setSchemStatus(true);
			}
		});
	}
}