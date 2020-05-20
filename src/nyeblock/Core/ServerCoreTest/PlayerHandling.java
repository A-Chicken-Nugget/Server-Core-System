package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Items.ItemBase;
import nyeblock.Core.ServerCoreTest.Menus.MenuBase;
import nyeblock.Core.ServerCoreTest.Misc.DamagePlayer;
import nyeblock.Core.ServerCoreTest.Misc.Enums.DBDataType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.KitPvP;
import nyeblock.Core.ServerCoreTest.Realms.HubParkour;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;
import nyeblock.Core.ServerCoreTest.Realms.StickDuel;

@SuppressWarnings("deprecation")
public class PlayerHandling implements Listener {
	private Main mainInstance;
	private HashMap<UUID,PlayerData> playersData = new HashMap<>();
	private HashMap<UUID,DamagePlayer> lastPlayerDamage = new HashMap<>();
	private HashMap<UUID,Player> lastPlayerHit = new HashMap<>();
	private HashMap<UUID,ArrayList<Long>> playerChatMessages = new HashMap<>();
	private HashMap<UUID,ArrayList<Long>> playerActions = new HashMap<>();
	private HashMap<UUID,Long> playerLastUsed = new HashMap<>();

	public PlayerHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
		
		// Save players play time every 3 minutes
		Bukkit.getScheduler().runTaskTimer(mainInstance, new Runnable() {
			@Override
			public void run() {
				for (Player ply : Bukkit.getOnlinePlayers()) {
					PlayerData pd = getPlayerData(ply);
					
					if (pd != null) {
						if (pd.getHiddenStatus()) {							
							ply.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You are currently hidden."));
						}
						Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
							@Override
							public void run() {
								pd.saveData(DBDataType.ALL);
							}
						});
		            }
				}
			}
		}, 0, 20*180);
		
		//Remove hit sound
		mainInstance.getProtocolManagerInstance().addPacketListener(new PacketAdapter(mainInstance, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT){
			@Override
            public void onPacketSending(PacketEvent event) {
            	Player ply = event.getPlayer();
            	PlayerData pd = getPlayerData(ply);
            	
            	if (!pd.getCurrentRealm().getRealm().isGame()) {
            		event.setCancelled(true);
            	} else {
            		Player lastHitFromPly = lastPlayerHit.get(ply.getUniqueId());
            		
            		if (lastHitFromPly != null) {                			
            			PlayerData lastHitFromPd = getPlayerData(lastHitFromPly);
            			
            			if (lastHitFromPd != null) {            				
            				if (lastHitFromPd.getSpectatingStatus() || lastHitFromPd.getHiddenStatus()) {                			
            					event.setCancelled(true);
            				}
            			}
            		}
            	}
			}
		});
		
		//Prevent xp spawn
//		mainInstance.getProtocolManagerInstance().addPacketListener(new PacketAdapter(mainInstance, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB){
//			@Override
//            public void onPacketSending(PacketEvent event) {
//            	Player ply = event.getPlayer();
////            	PlayerData pd = getPlayerData(ply);
//            	
//            	System.out.println("Test: " + event.getPacket().getIntegers().readSafely(0));
//            	System.out.println("Test2: " + event.getPacket().getIntegers().readSafely(1));
//            	System.out.println("Test3: " + event.getPacket().getIntegers().readSafely(2));
//            	PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
//                destroyEntity.getIntegerArrays().write(0, new int[] { event.getPacket().getIntegers().read(0) });
//     
//                try {
//					mainInstance.getProtocolManagerInstance().sendServerPacket(ply, destroyEntity);
//				} catch (InvocationTargetException e) {
//					e.printStackTrace();
//				}
//            	
////            	System.out.println("Test: " + event.getPacket().getIntegers().read(0));
////            	for (Entity ent : world.getEntities()) {
////            		System.out.println("Blah: " + ent.getEntityId() + " :: " + ent.getType());
////            	}
////            	if (pd.getSpectatingStatus() || pd.getHiddenStatus()) {
////            		((CraftPlayer)ply).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(event.getPacket().getIntegers().read(0)));
////            		event.setCancelled(true);
////            	}
//			}
//		});
	}
	
	//Manage damage logs
	public void logDamage(Player damager, Player damaged) {
		DamagePlayer dp = lastPlayerDamage.get(damaged.getUniqueId());
		
		if (dp != null) {
			dp.setPlayer(damager);
			dp.setTime(System.currentTimeMillis());
		} else {
			lastPlayerDamage.put(damaged.getUniqueId(), new DamagePlayer(damager,System.currentTimeMillis()));
		}
	}
	
	//
	// GETTERS
	//

	public Long getLastUsed(Player ply) {
		return playerLastUsed.get(ply.getUniqueId());
	}
	// Get a specific players player data
	public PlayerData getPlayerData(Player ply) {
		return playersData.get(ply.getUniqueId());
	}
	// Remove a specific players player data
	public void removePlayerData(Player ply) {
		playersData.remove(ply.getUniqueId());
		playerChatMessages.remove(ply.getUniqueId());
		playerActions.remove(ply.getUniqueId());
		playerLastUsed.remove(ply.getUniqueId());
	}
	// Get a specific players last damage info
	public DamagePlayer getLastPlayerDamage(Player ply) {
		return lastPlayerDamage.get(ply.getUniqueId());
	}
	
	//
	// SETTERS
	//
	
	public void setLastUsed(Player ply) {
		playerLastUsed.put(ply.getUniqueId(),System.currentTimeMillis()/1000L);
	}
	
	//
	// EVENT HANDLERS
	//

	//Hide commands from players who do not have access
//	@EventHandler
//	public void onPlayerCommandSend(PlayerCommandSendEvent event) {
//		Player ply = event.getPlayer();
//		Collection<String> suggestions = event.getCommands();
//		ArrayList<String> removeSuggestions = new ArrayList<String>();
//		
//		for (String commandSuggestion : suggestions) {
//			CommandBase command = commandHandling.getCommand(commandSuggestion);
//			
//			if (command != null) {
//				PlayerData pd = getPlayerData(ply);
//				
//				if (pd != null) {					
//					if (!command.canExecute(pd.getUserGroup())) {
//						removeSuggestions.add(commandSuggestion);
//					}
//				}
//			}
//		}
//		suggestions.removeAll(removeSuggestions);
//	}
	// Prevent mob spawn
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
			event.setCancelled(true);
		}
	}

	// Only allow players on the same world to talk to each other
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player ply = event.getPlayer();
		boolean canSend = true;
		ArrayList<Long> remove = new ArrayList<>();
		int i = 0;
		
		for (Long message : playerChatMessages.get(ply.getUniqueId())) {
			if (System.currentTimeMillis()-message < 5000) {
				i++;
				
				if (i >= 3) {
					canSend = false;
				}
			} else {
				remove.add(message);
			}
		}
		if (canSend) {
			playerChatMessages.get(ply.getUniqueId()).removeAll(remove);
			String playerWorld = ply.getWorld().getName();
			ArrayList<Player> playersToRemove = new ArrayList<Player>();
			PlayerData playerData = playersData.get(ply.getUniqueId());
			ChatColor chatTextColor = playerData.getChatTextColor();
			ChatColor nameTextColor = playerData.getNameTextColor();
			
			event.setFormat(playerData.getPrimaryUserGroup().getTag() + " "
				+ (nameTextColor != null ? nameTextColor : ChatColor.WHITE)+ ply.getName()
				+ ChatColor.BOLD + " §7\u00BB " + ChatColor.RESET
				+ (chatTextColor != null ? chatTextColor : ChatColor.WHITE) + event.getMessage());
			
			for (Player player : event.getRecipients()) {
				if (!playerWorld.equalsIgnoreCase(player.getWorld().getName())) {
					playersToRemove.add(player);
				}
			}
			event.getRecipients().removeAll(playersToRemove);
		} else {
			ply.sendMessage(ChatColor.YELLOW + "Please wait a few seconds before sending another message!");
			event.setCancelled(true);
		}
		playerChatMessages.get(ply.getUniqueId()).add(System.currentTimeMillis());
	}
	//Hide recipes from being broadcasted
	@EventHandler
	public void onPlayerRecipe(PlayerRecipeDiscoverEvent event) {
		event.setCancelled(true);
	}
	// Keep the players food bar at 100%
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		Player ply = (Player)event.getEntity();
		
		if (ply != null && ply instanceof Player) {			
			if (!ply.hasPermission("nyeblock.canLoseHunger")) {
				if (event.getFoodLevel() < 20) {
					event.setFoodLevel(20);
				}
			}
		}
	}
	//Handle when the player attempts to connect
	@EventHandler
	public void playerPreLogin(AsyncPlayerPreLoginEvent event) {
		UUID uniqueId = event.getUniqueId();
		String ip = event.getAddress().toString().split(":")[0].replace("/","");
		 
		ArrayList<HashMap<String,String>> bansQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM user_bans WHERE uniqueId = '" + uniqueId + "' AND is_expired = 0", false);
		
		if (bansQuery.size() > 0) {
			HashMap<String, String> bansQueryData = bansQuery.get(0);
			long difference = ((Integer.parseInt(bansQueryData.get("length"))*60) + Integer.parseInt(bansQueryData.get("created")) - (System.currentTimeMillis()/1000L));
			
			if (difference <= 0) {
				mainInstance.getDatabaseInstance().query("UPDATE user_bans SET is_expired = 1 WHERE id = " + bansQueryData.get("id"), true);
				
				ArrayList<HashMap<String,String>> ipbansQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM ip_bans WHERE ip = '" + ip + "'", false);
				
				if (ipbansQuery.size() > 0) {
					HashMap<String, String> ipbansQueryData = ipbansQuery.get(0);
					difference = ((Integer.parseInt(ipbansQueryData.get("length"))*60) + Integer.parseInt(ipbansQueryData.get("created")) - (System.currentTimeMillis()/1000L));
					
					if (difference <= 0) {
						mainInstance.getDatabaseInstance().query("UPDATE ip_bans SET is_expired = 1 WHERE id = " + ipbansQueryData.get("id"), true);
					} else {
						event.disallow(Result.KICK_BANNED, "You are banned.\n\nExpires in: " + difference/60 + " minute(s).\n\nReason:" + bansQueryData.get("reason"));
					}
				}
			} else {
        		event.disallow(Result.KICK_BANNED, "You are banned.\n\nExpires in: " + difference/60 + " minute(s).\n\nReason:" + bansQueryData.get("reason"));
			}
		} else {
			ArrayList<HashMap<String,String>> ipbansQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM ip_bans WHERE ip = '" + ip + "' AND is_expired = 0", false);
			
			if (ipbansQuery.size() > 0) {
				HashMap<String, String> ipbansQueryData = ipbansQuery.get(0);
				long difference = ((Integer.parseInt(ipbansQueryData.get("length"))*60) + Integer.parseInt(ipbansQueryData.get("created")) - (System.currentTimeMillis()/1000L));
				
				if (difference <= 0) {
					mainInstance.getDatabaseInstance().query("UPDATE ip_bans SET is_expired = 1 WHERE id = " + ipbansQueryData.get("id"), true);
				} else {
					event.disallow(Result.KICK_BANNED, "You are banned.\n\nExpires in: " + difference/60 + " minute(s).\n\nReason:" + ipbansQueryData.get("reason"));
				}
			}
		}
	}
	// Handle when the player joins the server
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player ply = event.getPlayer();
		
		// Set health/food level
		ply.setHealth(20);
		ply.setFoodLevel(20);

		// Remove default join message
		event.setJoinMessage("");
		
		if (getPlayerData(ply) == null) {
			// Setup player data. If they don't have a profile in the database, create one.
			final PlayerData playerData = new PlayerData(mainInstance,ply);
			
			//Run database queries asynchronously
			Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
				@Override
				public void run() {            	
					playerData.setupData();
				}
			});
			playersData.put(ply.getUniqueId(), playerData);
			playerChatMessages.put(ply.getUniqueId(), new ArrayList<Long>());
			playerActions.put(ply.getUniqueId(), new ArrayList<Long>());
			playerLastUsed.put(ply.getUniqueId(), 0L);
		} else {
			mainInstance.getTimerInstance().deleteTimer("leave_" + ply.getUniqueId());
			PlayerData pd = getPlayerData(ply);
			
			pd.setPlayer(ply);
			if (pd.getRealm() != Realm.HUB) {
				RealmBase realm = pd.getCurrentRealm();
				
				if (realm instanceof GameBase) {
					GameBase game = (GameBase)realm;
					
					if (game.getPlayerCount() < game.getMaxPlayers() 
							&& game.getJoinStatus()) {
						realm.join(ply, true);
					} else {
						ply.sendMessage(ChatColor.YELLOW + "Unable to rejoin " + realm.getRealm().toString() + " game. It is no longer active.");
						mainInstance.getRealmHandlingInstance().joinRealm(ply, Realm.HUB);
					}
				} else {
					realm.join(ply, false);
					
					if (realm instanceof HubParkour) {
						((HubParkour)realm).goToStart(ply);
					}
				}
			} else {
				pd.getCurrentRealm().join(ply, false);
			}
		}
		
		//Show/hide players accordingly
		PlayerData pd = getPlayerData(ply);
		ArrayList<Player> players = pd.getCurrentRealm().getPlayers(false);
		Realm realm = pd.getRealm();
		
		for (Player ply2 : Bukkit.getOnlinePlayers()) {
			if (players.contains(ply2)) {
				PlayerData pd2 = mainInstance.getPlayerHandlingInstance().getPlayerData(ply2);
				
				if (!ply.canSee(ply2)) {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (!Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
							ply.showPlayer(mainInstance,ply2);
						}
					} else {
						ply.showPlayer(mainInstance,ply2);						
					}
				} else {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
							ply.hidePlayer(mainInstance,ply2);
						}
					}
				}
				if (!ply2.canSee(ply)) {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (!Boolean.parseBoolean(pd2.getCustomDataKey("hide_players"))) {
							ply2.showPlayer(mainInstance,ply);
						}
					} else {
						ply2.showPlayer(mainInstance,ply);						
					}
				} else {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (Boolean.parseBoolean(pd2.getCustomDataKey("hide_players"))) {
							ply2.hidePlayer(mainInstance,ply);
						}
					}
				}
			} else {
				if (ply.canSee(ply2)) {					
					ply.hidePlayer(mainInstance,ply2);
				}
				if (ply2.canSee(ply)) {
					ply2.hidePlayer(mainInstance,ply);
				}
			}
		}
	}
	// Handle when a player leaves the server
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player ply = event.getPlayer();
		PlayerData pd = playersData.get(ply.getUniqueId());

		// Remove default quit message
		event.setQuitMessage("");
		
		if (pd.getParty() != null) {
			pd.getParty().playerLeave(ply,true);
		}
		
		pd.saveData(DBDataType.ALL);
		
		mainInstance.getTimerInstance().createRunnableTimer("leave_" + ply.getUniqueId(), 60, 1, new Runnable() {
			@Override
			public void run() {
				if (!ply.isOnline()) {
					//Remove player data
					removePlayerData(ply);
				}
			}
		});
		
		//Leave realm
		pd.getCurrentRealm().leave(ply, true, null);
	}
	//
//	@EventHandler
//	public void onEntityTargetEvent(EntityTargetEvent event) {
//		Entity ent = event.getEntity();
//		
//		if (ent instanceof ExperienceOrb) {
//			event.setTarget(null);
//			event.setCancelled(true);
//		}
//	}
	//
//	@EventHandler
//	public void onExpBottle(ExpBottleEvent event) {
//		event.getEntity().
//		event.getEntity().getShooter()
//	}
	//When a world loads
	@EventHandler
	public void onWorldInit(WorldInitEvent event) {
		World world = event.getWorld();
		
		if (!world.getName().equals("world")) {			
			world.setAutoSave(false);
			world.setKeepSpawnInMemory(false);
			world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		}
	}
	// Handle when a player respawns
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player ply = event.getPlayer();

		event.setRespawnLocation(playersData.get(ply.getUniqueId()).getCurrentRealm().playerRespawn(ply));
	}
	//Handle when a player moves
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player ply = event.getPlayer();
		
		if (!ply.hasPermission("nyeblock.canMove")) {
			event.setCancelled(true);
		}
	}
	//Handle when a player attempts to pickup an item
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player ply = event.getPlayer();
		PlayerData pd = getPlayerData(ply);
		
		if (pd.getSpectatingStatus() || pd.getHiddenStatus()) {
			event.setCancelled(true);
		}
	}
	// Handle when a player attempts to drop an item
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player ply = event.getPlayer();

		if (!ply.hasPermission("nyeblock.canDropItems")) {
			event.setCancelled(true);
		}
	}
	//Handle when a block is placed
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player ply = event.getPlayer();
		
		if (!ply.hasPermission("nyeblock.canPlaceBlocks")) {
			event.setCancelled(true);
		}
	}
	// Handle when a block is broken
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player ply = event.getPlayer();
		
		if (!ply.hasPermission("nyeblock.canBreakBlocks")) {
			RealmBase realm = getPlayerData(ply).getCurrentRealm();
			Block block = event.getBlock();
			
			if (realm.getRealm() == Realm.STICK_DUEL) {
				if (block.getType().equals(Material.RED_BED)) {
					if (!((GameBase)realm).isGameClosed() && Math.abs(Math.abs(((StickDuel)realm).getPlayerBed(ply).getX())-Math.abs(block.getLocation().getX())) > 2) {
						event.setDropItems(false);
						((StickDuel)realm).bedBreak(block.getLocation().getBlockX(), ply);
					} else {
						event.setCancelled(true);
					}
				} else if (!block.getType().equals(Material.WHITE_WOOL)) {
					event.setCancelled(true);
				}
			} else {				
				event.setCancelled(true);
			}
		} else {
			RealmBase realm = getPlayerData(ply).getCurrentRealm();
			
			if (realm.getRealm() == Realm.SKYWARS) {
				if (((GameBase)realm).getActiveStatus() && event.getBlock().getState() instanceof Chest) {						
					event.setCancelled(true);
				}
			}
		}
	}
	// Handle when a player is damaged by an entity
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity dmgr = event.getDamager();
		Entity dmged = event.getEntity();
		
		if (dmgr instanceof Player && dmged instanceof Player) {
			Player damager = (Player)dmgr;
			Player damaged = (Player)dmged;
			PlayerData damagerpd = getPlayerData(damager);
			PlayerData damagedpd = getPlayerData(damaged);

			if (!damager.hasPermission("nyeblock.canDamage")) {
				event.setCancelled(true);
			} else if (!damaged.hasPermission("nyeblock.canBeDamaged")) {
				event.setCancelled(true);
			} else if ((damagedpd.getRealm() == Realm.PVP_DUELS_FISTS || damagedpd.getRealm() == Realm.PVP_2V2_FISTS) && damagedpd.getTeam() != null && damagedpd.getTeam().equals(damagerpd.getTeam())) {
				event.setCancelled(true);
			} else if (damaged.getHealth() <= 0) {
				event.setCancelled(true);
			} else {
				logDamage(damager,damaged);
			}
			lastPlayerHit.put(damaged.getUniqueId(),damager);
		} else if (dmged instanceof Player) {
			Player damaged = (Player)dmged;
			
			if (dmgr instanceof Snowball) {
				event.setDamage(1.0E-4D);
				logDamage((Player)((Snowball)dmgr).getShooter(),damaged);
			} else if (dmgr instanceof Egg) {
				event.setDamage(1.0E-4D);
				logDamage((Player)((Egg)dmgr).getShooter(),damaged);
			} else if (dmgr instanceof EnderPearl) {
				event.setDamage(1.0E-4D);
				logDamage((Player)((EnderPearl)dmgr).getShooter(),damaged);
			} else if (dmgr instanceof Firework) {
				event.setCancelled(true);
			} else if (dmged.getType().equals(EntityType.ITEM_FRAME)) {
				event.setCancelled(true);
			} else if (dmgr instanceof LightningStrike && getPlayerData((Player)dmged).getRealm().equals(Realm.STICK_DUEL)) {
				event.setCancelled(true);
			}
		}
	}
	// Handle when the player falls
	@EventHandler
	public void onDamageEvent(EntityDamageEvent event) {
		Entity ent = event.getEntity();

		if (ent instanceof Player) {
			Player ply = (Player)ent;
			PlayerData playerData = getPlayerData(ply);
			
			if (playerData != null) {				
				if (!ply.hasPermission("nyeblock.canBeDamaged")) {
					event.setCancelled(true);
				} else {
					DamageCause cause = event.getCause();
					
					if (cause == DamageCause.FALL) {
						if (!ply.hasPermission("nyeblock.canTakeFallDamage")
								|| ply.hasPermission("nyeblock.tempNoDamageOnFall")) {
							playerData.setPermission("nyeblock.tempNoDamageOnFall", false);
							event.setCancelled(true);
						}
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
	//Handle when a player is hit by a projectile
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		Entity entAttacked = event.getHitEntity();
		ProjectileSource entAttacker = event.getEntity().getShooter();
		
		if (entAttacked instanceof Player && entAttacker instanceof Player) {
			Player attacked = (Player)event.getHitEntity();
			Player attacker = (Player)event.getEntity().getShooter();
			
			if (projectile instanceof Fireball) {
				Vector knockback = attacked.getVelocity();
				knockback.add(new Vector(0,.3,0));
				knockback.add(attacker.getLocation().getDirection().multiply(1.5));
				
				attacked.setVelocity(knockback);
				logDamage(attacker,attacked);
			} else if (projectile instanceof FishHook) {
				FishHook hook = (FishHook)event.getEntity();
				Player hookShooter = (Player)entAttacker;
				LivingEntity hitEntity = (LivingEntity)entAttacked;
			      
				double kx = hook.getLocation().getDirection().getX() / 2.5D;
				double kz = hook.getLocation().getDirection().getZ() / 2.5D;
				kx -= kx * 2.0D;
			      
				if (hitEntity.getNoDamageTicks() >= 6.5D)
					return; 
				if (hitEntity.getNoDamageTicks() < 6.5D && hitEntity.getLocation().getWorld().getBlockAt(hitEntity.getLocation()).getType().toString() != "AIR") {
					hitEntity.setNoDamageTicks(0);
				}
			      
				hitEntity.damage(0.001D, hookShooter);
				double upVel = 0.372D;
				if (!hitEntity.isOnGround()) {
					upVel = 0.0D;
				}
			      
				hitEntity.setVelocity(new Vector(kx, upVel, kz));
				hitEntity.setNoDamageTicks(17);
				
				logDamage(attacker,attacked);
			}
		}
	}
	// Handle when a player has died
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player killed = event.getEntity();
		Player attacker = killed.getKiller();
		PlayerData playerData = playersData.get(killed.getUniqueId());
		RealmBase game = playerData.getCurrentRealm();
		
		// Remove default death message
		event.setDeathMessage("");
		
		//Force player to respawn
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainInstance, new Runnable() {
			@Override
			public void run() {				
				killed.spigot().respawn();
			}
		});
		
		if (!killed.hasPermission("nyeblock.shouldDropItemsOnDeath")) {			
			event.getDrops().clear();
		}
		
		if (attacker != null) {
			game.playerDeath(killed, attacker);
			if (game.getRealm().isGame()) {				
				getPlayerData(attacker).addKill(game.getRealm());
				getPlayerData(killed).addDeath(game.getRealm());
			}
		} else {
			Entity ent = killed.getLastDamageCause().getEntity();
			
			if (ent instanceof Player && (Player)ent != killed) {
				game.playerDeath(killed, (Player)ent);		
			} else {
				DamagePlayer dp = lastPlayerDamage.get(killed.getUniqueId());
				
				if (dp != null) {				
					if ((System.currentTimeMillis()-dp.getTime())/1000L < 10) {
						game.playerDeath(killed, dp.getPlayer());	
					} else {
						game.playerDeath(killed, null);	
					}
				} else {					
					game.playerDeath(killed, null);	
				}
			}
		}
	}
	// Handle when a player shoots a bow
	@EventHandler
	public void onPlayerShootBow(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {			
			Player ply = (Player) event.getEntity();
			GameBase game = (GameBase)getPlayerData(ply).getCurrentRealm();
			
			if (game instanceof KitPvP && ((KitPvP)game).isInGraceBounds(ply)) {
				event.setCancelled(true);
			}
		}
	}
	// Handle explosion events
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Entity ent = event.getEntity();

		if (ent instanceof Fireball) {
			Location loc = ent.getLocation();

			for (Player player :  event.getLocation().getWorld().getPlayers()) {
                player.spawnParticle(Particle.SMOKE_LARGE, loc, 100, 0, new Random().nextInt(50)*.01, 0, 0.1);
                player.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 10, 1);
            }
			event.setCancelled(true);
		}
	}
	//Handle when a player changes their world
	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
//		Player ply = event.getPlayer();
//		
//		mainInstance.getTimerInstance().createRunnableTimer("clearInventory_" + ply.getUniqueId(), .75, 1, new Runnable() {
//			@Override
//			public void run() {
//				ply.getOpenInventory().close();
//			}
//		});
	}
	// Handle potion splashes
	@EventHandler
	public void onPotionSpash(PotionSplashEvent event) {
		if (event.getEntity() instanceof Player) {
			Player ply = (Player) event.getEntity();
			GameBase game = (GameBase)getPlayerData(ply).getCurrentRealm();

			if (game instanceof KitPvP && ((KitPvP)game).isInGraceBounds(ply)) {
				event.setCancelled(true);
			}
		}
	}
	//Handle when a player presses the f key
	@EventHandler
	public void onItemSwapEvent(PlayerSwapHandItemsEvent event) {
		Player ply = event.getPlayer();
		
		if (!ply.hasPermission("nyeblock.canSwapItems")) {
			event.setCancelled(true);
		}
	}
	//Handle when the player opens an inventory menu
	@EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
		Player ply = (Player) event.getPlayer();
		PlayerData pd = getPlayerData(ply);
		
		if (!pd.getSpectatingStatus()) {			
			if (event.getInventory() instanceof EnchantingInventory) {
				EnchantingInventory inv = (EnchantingInventory) event.getInventory();
				inv.setItem(1,new ItemStack(Material.LAPIS_LAZULI,20));
				
				mainInstance.getTimerInstance().createRunnableTimer("lapisReplacement_" + ply.getUniqueId(), .5, 0, new Runnable() {
					@Override
					public void run() {
						inv.setItem(1,new ItemStack(Material.LAPIS_LAZULI,20));
					}
				});
			}
		} else {
			if (!(event.getView() instanceof CraftingInventory)) {
				event.setCancelled(true);
			}
		}
    }
	//Handle when the player closes an inventory menu
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player ply = (Player)event.getPlayer();
		
		if (event.getInventory() instanceof EnchantingInventory) {
			mainInstance.getTimerInstance().deleteTimer("lapisReplacement_" + ply.getUniqueId());
			event.getInventory().clear();
		}
	}
	// Handle when an item is moved in an inventory menu
	@EventHandler
	public void onPlayerInventoryMove(InventoryClickEvent event) {
		Player ply = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		
		if (event.getInventory() instanceof EnchantingInventory) {
			if (item != null) {
				if (item.getType().equals(Material.LAPIS_LAZULI)) {
					event.setCancelled(true);
				}
			}
		} else {
			if (item != null) {				
				ItemMeta itemMeta = item.getItemMeta();
				
				if (itemMeta != null) {
					ClickType clickType = event.getClick();
					PlayerData playerData = playersData.get(ply.getUniqueId());
					MenuBase menu = playerData.getMenu();
					
					if (menu != null) {						
						ArrayList<Long> remove = new ArrayList<>();
						boolean canDo = true;
						int i = 0;
						
						for (Long message : playerActions.get(ply.getUniqueId())) {
							if (System.currentTimeMillis()-message < 3000) {
								i++;
								
								if (i >= 7) {
									canDo = false;
								}
							} else {
								remove.add(message);
							}
						}
						
						if (canDo) {			
							playerActions.get(ply.getUniqueId()).removeAll(remove);
							if (menu.getCurrentMenu().hasOption(itemMeta.getLocalizedName())) {							
								menu.getCurrentMenu().runOption(itemMeta.getLocalizedName(),clickType);
							} else {
								ItemBase itemm = playerData.getCustomItem(itemMeta.getLocalizedName());
								
								if (itemm != null) {							
									playerData.getCustomItem(itemMeta.getLocalizedName()).use(item);
								}
							}
						} else {
							ply.sendMessage(ChatColor.YELLOW + "Please wait a few seconds before doing this action!");
							event.setCancelled(true);
						}									
						playerActions.get(ply.getUniqueId()).add(System.currentTimeMillis());
					} else {
						ItemBase itemm = playerData.getCustomItem(itemMeta.getLocalizedName());
						
						if (itemm != null) {							
							playerData.getCustomItem(itemMeta.getLocalizedName()).use(item);
						}
					}
					// Block inventory move
					if (!ply.hasPermission("nyeblock.canUseInventory")) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	// Handle when a player uses an item
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event) {
		Player ply = event.getPlayer();
		EquipmentSlot hand = event.getHand();
		
		//If action is done in the players main hand
		if (hand != null) {
			if (hand.equals(EquipmentSlot.HAND)) {
				List<Action> itemClickActions = Arrays.asList(Action.RIGHT_CLICK_AIR,Action.RIGHT_CLICK_BLOCK);
				
				if (itemClickActions.contains(event.getAction())) {
					ItemStack item = ply.getItemInHand();
					ItemMeta itemMeta = item.getItemMeta();
					
					if (item != null && itemMeta != null) {
						PlayerData pd = getPlayerData(ply);
						String itemName = itemMeta.getLocalizedName();
						ItemBase itemm = pd.getCustomItem(itemName);
						
						if (itemm != null) {
							ArrayList<Long> remove = new ArrayList<>();
							boolean canDo = true;
							int i = 0;
							
							for (Long message : playerActions.get(ply.getUniqueId())) {
								if (System.currentTimeMillis()-message < 5000) {
									i++;
									
									if (i >= 5) {
										canDo = false;
									}
								} else {
									remove.add(message);
								}
							}
							
							if (canDo) {			
								playerActions.get(ply.getUniqueId()).removeAll(remove);
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainInstance, new Runnable() {
									@Override
									public void run() {		
										pd.getCustomItem(itemName).use(item);
									}
								});
							} else {
								ply.sendMessage(ChatColor.YELLOW + "Please wait a few seconds before doing this action!");
							}									
							playerActions.get(ply.getUniqueId()).add(System.currentTimeMillis());
							event.setCancelled(true);
						} else {
							if (item.getType().equals(Material.ENDER_PEARL)) {
								GameBase game = ((GameBase)pd.getCurrentRealm());
								
								if (game != null) {
									Long startTime = game.getStartTime();
									
									if (startTime != null && ((System.currentTimeMillis()/1000L)-startTime) < 30) {
										event.setCancelled(true);
										ply.sendMessage(ChatColor.RED + "You will be able to use this item in " + (30-((System.currentTimeMillis()/1000L)-startTime)) + " second(s).");
									}
								}
							}
						}
					}
				}
			}
		} else {
			if (!ply.hasPermission("nyeblock.canBreakBlocks")) {
				event.setCancelled(true);
			}
		}
	}
	//Handle when a player interacts
//	@EventHandler
//	public void onPlayerInteract(PlayerInteractEvent event) {
//		Player ply = event.getPlayer();
//		PlayerData pd = getPlayerData(ply);
//		
////		if (pd.getSpectatingStatus()) {
////			event.setCancelled(true);
////		}
//	}
	//Handle when an item frame is interacted with
	@EventHandler
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		event.setCancelled(true);
	}
	//Handle when an armor stand is interacted with
	@EventHandler
	public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		event.setCancelled(true);
	}
	//Handle when a player interacts with an entity
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		
		if (entity instanceof ItemFrame) {
			event.setCancelled(true);
		}
	}
}
