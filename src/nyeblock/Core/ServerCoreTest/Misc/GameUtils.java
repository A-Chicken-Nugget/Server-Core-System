package nyeblock.Core.ServerCoreTest.Misc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameUtils {
	//Copy a game world
	public static void copyWorld(Path dir, Path dest) {
		try
		{
			Files.copy(dir,dest);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		File d = dir.toFile();
		
		if(d == null) {			
			System.out.println("Directory does not exist.");
		}
		if(d.isDirectory())
		{
			File[] fs = d.listFiles();
			
			if(fs != null) {
				for(File f : fs) {
					copyWorld(f.toPath(),dest.resolve(f.getName()));					
				}
			}
		}
	}
}
