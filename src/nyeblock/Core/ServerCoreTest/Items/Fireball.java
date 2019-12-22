package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

public class Fireball extends ItemBase {
	private int amount;
	
	public Fireball(Main mainInstance, Player player, int amount) {
		super(mainInstance,player,"fireball");
		this.amount = amount;
	}
	
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.FIRE_CHARGE,amount);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fire Ball");
		itemMeta.setLocalizedName("fireball");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		GameBase game = (GameBase)mainInstance.getPlayerHandlingInstance().getPlayerData(player).getCurrentRealm();
		
		if (!game.isInGraceBounds(player)) {
			Location spawnAt = player.getEyeLocation().toVector()
					.add(player.getEyeLocation().getDirection().multiply(3))
					.toLocation(player.getWorld());
			org.bukkit.entity.Fireball fireball = player.getWorld().spawn(spawnAt, org.bukkit.entity.Fireball.class);
			fireball.setDirection(player.getEyeLocation().getDirection());
			fireball.setBounce(false);
			fireball.setIsIncendiary(false);
			fireball.setYield(1.75F);
			fireball.setShooter(player);
			for (ItemStack itemm : player.getInventory().getContents()) {
				if (itemm != null) {
					if (itemm.getType().equals(Material.FIRE_CHARGE)) {
						itemm.setAmount(itemm.getAmount() - 1);
					}
				}
			}
		}
	}
}
