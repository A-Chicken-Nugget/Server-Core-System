package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class ReturnToLobby extends ItemBase {
	Realm toRealm;
	
	public ReturnToLobby(Main mainInstance, Realm toRealm, Player player) {
		super(mainInstance,player,"return_to_lobby");
		
		this.toRealm = toRealm;
	}
	
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.LEAD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW + "Return to Lobby" + ChatColor.GREEN + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("return_to_lobby");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);

    	pd.getCurrentRealm().leave(player, true, toRealm);
	}
}
