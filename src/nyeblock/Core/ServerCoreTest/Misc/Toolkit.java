package nyeblock.Core.ServerCoreTest.Misc;

import java.util.Random;

public class Toolkit 
{
	public static int GetRandomNumber(int min, int max) 
	{
		Random randomizer = new Random();
		
		return randomizer.nextInt(((max - min) + 1) + min);
	}
}
