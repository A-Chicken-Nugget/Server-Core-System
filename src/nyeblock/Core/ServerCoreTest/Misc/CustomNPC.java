package nyeblock.Core.ServerCoreTest.Misc;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_15_R1.EntityPlayer;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.CustomNPCType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class CustomNPC {
	private Main mainInstance;
	private int id;
	private EntityPlayer entityPlayer;
	private CustomNPCType type;
	private Realm realm;
	
	public CustomNPC(Main mainInstance, int id, EntityPlayer entityPlayer, CustomNPCType type) {
		this.mainInstance = mainInstance;
		this.id = id;
		this.entityPlayer = entityPlayer;
		this.type = type;
	}
	
	public void playerUse(Player player) {
		if (type.equals(CustomNPCType.JOIN_REALM) && realm != null) {
			mainInstance.getRealmHandlingInstance().joinRealm(player, realm);
		} else if (type.equals(CustomNPCType.TO_HUB)) {
			mainInstance.getPlayerHandlingInstance().getPlayerData(player).getCurrentRealm().leave(player, true, Realm.HUB);
		}
	}
	public void showFor(Player player) {
		mainInstance.getCustomNPCManagerInstance().showNPCFor(id, player);
	}
	public void removeFor(Player player) {
		mainInstance.getCustomNPCManagerInstance().removeNPCFor(id, player);
	}
	
	//
	// GETTERS
	//
	
	public int getId() {
		return id;
	}
	public EntityPlayer getEntityPlayer() {
		return entityPlayer;
	}
	
	//
	// SETTERS
	//
	
	public void setRealm(Realm realm) {
		this.realm = realm;
	}
	public static Property getSkinProperty(String name) {
		Player player = Bukkit.getPlayerExact(name);
		Property skinProperty = null;
		
		if (player != null) {			
			Property property = ((CraftPlayer)player).getHandle().getProfile().getProperties().get("textures").iterator().next();
			skinProperty = new Property("textures", property.getValue(), property.getSignature());
		} else {			
			String[] properties = Toolkit.getSkinFromName(name);
			
			if (properties != null) {				
				skinProperty = new Property("textures", properties[0], properties[1]);
			}
		}
		return skinProperty;
	}
}
