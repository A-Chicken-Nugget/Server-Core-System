package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import nyeblock.Core.ServerCoreTest.Misc.WeaponDamage;

public class Weapon extends ItemStack 
{
	private double Damage;
	
	public Weapon(Material weaponType) 
	{
		super(weaponType);
		
		Damage = WeaponDamage.GetDamageByMaterial(super.getType());
	}
	
	public final Material getType() 
	{
		return super.getType();
	}
	
	public final double getDamage() 
	{
		return Damage;
	}
}