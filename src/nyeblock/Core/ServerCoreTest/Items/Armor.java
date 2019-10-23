package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import nyeblock.Core.ServerCoreTest.Misc.ArmorRating;
import nyeblock.Core.ServerCoreTest.Misc.WeaponDamage;

public class Armor extends ItemStack 
{
	private int Rating;
	
	public Armor(Material armorType) 
	{
		super(armorType);
		
		Rating = ArmorRating.GetRatingFromMaterial(armorType); 
	}
	
	public final Material getType() 
	{
		return super.getType();
	}
	
	public final int getRating() 
	{
		return Rating;
	}
}
