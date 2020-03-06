package nyeblock.Core.ServerCoreTest.Misc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Enums.CustomNPCType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomNPCManager {
	private Main mainInstance;
	private HashMap<Integer,CustomNPC> npcs = new HashMap<>();
	private HashMap<Integer,ArrayList<Player>> npcPlayers = new HashMap<>();
	
	public CustomNPCManager(Main mainInstance) {
		this.mainInstance = mainInstance;
		
		//Listen to packets to tell when an npc has been clicked
		mainInstance.getProtocolManagerInstance().addPacketListener(new PacketAdapter(mainInstance, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY){
		    @Override
		    public void onPacketReceiving(PacketEvent event) {
		    	CustomNPC npc = npcs.get(event.getPacket().getIntegers().read(0));
		    	
		    	if (npc != null) {		    		
		    		npc.playerUse(event.getPlayer());
		    	}
		    }
		});
	}
	
	public CustomNPC spawnNPC(String name, String skinName, CustomNPCType type, Location location) {
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(),"");
		if (skinName != null) {			
			Property property = CustomNPC.getSkinProperty(skinName);
			
			if (property != null) {				
				gameProfile.getProperties().put("textures", property);
			}
		}
		EntityPlayer npc = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), worldServer, gameProfile, new PlayerInteractManager(worldServer));
		CustomNPC cnpc = new CustomNPC(mainInstance,npc.getId(),npc,type);
		
		npcs.put(npc.getId(),cnpc);
		npcPlayers.put(npc.getId(),new ArrayList<Player>());
		npc.spawnIn(worldServer);
		npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		npc.yaw = location.getYaw();
		
		return cnpc;
	}
	public void showNPCFor(int id, Player player) {
		for(Map.Entry<Integer,CustomNPC> npc : npcs.entrySet()) {
			if (npc.getKey() == id) {
				PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
				EntityPlayer entityPlayer = npc.getValue().getEntityPlayer();
				
				connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
				connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
				connection.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (((entityPlayer.yaw) * 256.0F) / 360.0F)));
				Bukkit.getScheduler().scheduleSyncDelayedTask(mainInstance, new Runnable() {
		            @Override
		            public void run() {
		            	connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		            }
		        }, 20);
				npcPlayers.get(id).add(player);
			}
		}
	}
	public void removeNPCFor(int id, Player player) {
		for(Map.Entry<Integer,CustomNPC> npc : npcs.entrySet()) {
			if (npc.getKey() == id) {
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getValue().getId()));
				npcPlayers.get(id).remove(player);
			}
		}
	}
	public void deleteNPC(int id) {
		Iterator<Map.Entry<Integer,CustomNPC>> itr = npcs.entrySet().iterator();
		
		while(itr.hasNext())
		{
			Map.Entry<Integer,CustomNPC> npc = itr.next();
			if (npc.getKey() == id) {
				for (Player ply : npcPlayers.get(id)) {
					if (ply.isOnline()) {
						((CraftPlayer)ply).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getValue().getId()));
					}
				}
				npcPlayers.remove(id);
			}
		}
	}
}