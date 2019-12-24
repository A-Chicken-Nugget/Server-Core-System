package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Items.ItemBase;
import nyeblock.Core.ServerCoreTest.Items.MenuBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.KitPvP;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

@SuppressWarnings("deprecation")
public class PlayerHandling implements Listener {
	private Main mainInstance;
	private HashMap<UUID, PlayerData> playersData = new HashMap<UUID, PlayerData>();
	private HashMap<UUID, ArrayList<Long>> playerChatMessages = new HashMap<>();
	private World world = Bukkit.getWorld("world");
	private boolean worldsChecked = false;

	public PlayerHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
		
		// Save players play time every 3 minutes
		Bukkit.getScheduler().runTaskTimer(mainInstance, new Runnable() {
			@Override
			public void run() {
				DatabaseHandling dh = mainInstance.getDatabaseInstance();
				for (Player ply : Bukkit.getOnlinePlayers()) {
					PlayerData pd = getPlayerData(ply);
					
					if (pd != null) {						
						HashMap<String,Integer> realmXp = pd.getRealmXp();
						HashMap<String,Integer> totalGamesWon = pd.getTotalGamesPlayed();
						HashMap<String,Integer> totalGamesPlayed = pd.getTotalGamesPlayed();
						
						Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
							@Override
							public void run() {     
								String xpString = "";
								
								for (Map.Entry<String,Integer> entry : realmXp.entrySet()) {
									if (xpString.equals("")) {
										xpString = entry.getKey() + " = " + entry.getValue();
									} else {
										xpString += ", " + entry.getKey() + " = " + entry.getValue();
									}
								}
								dh.query("UPDATE users SET timePlayed = (timePlayed + " + ((System.currentTimeMillis()/1000L)-pd.getTimeJoined()) + ") WHERE name = '" + ply.getName() + "'", 0, true);			
								dh.query("UPDATE userXP SET " + xpString + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);

								//Save every players total games won
								String wonString = "";
								
								for (Map.Entry<String,Integer> entry : totalGamesWon.entrySet()) {
									if (wonString.equals("")) {
										wonString = entry.getKey() + " = " + entry.getValue();
									} else {
										wonString += ", " + entry.getKey() + " = " + entry.getValue();
									}
								}
								dh.query("UPDATE userGamesWon SET " + wonString + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);	
								
								//Save every players total games
								String totalString = "";
								
								for (Map.Entry<String,Integer> entry : totalGamesPlayed.entrySet()) {
									if (totalString.equals("")) {
										totalString = entry.getKey() + " = " + entry.getValue();
									} else {
										totalString += ", " + entry.getKey() + " = " + entry.getValue();
									}
								}
								dh.query("UPDATE userTotalGames SET " + totalString + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);	
							}
						});
		            }
				}
			}
		}, 0, 20*180);
	}
	
	//
	// GETTERS
	//

	// Get a specific players player data
	public PlayerData getPlayerData(Player ply) {
		return playersData.get(ply.getUniqueId());
	}
	
	//
	// EVENT HANDLERS
	//

	// Prevent mob spawn
	@EventHandler()
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
			
			event.setFormat(playerData.getUserGroup().getTag() + " " + ply.getName() + ChatColor.BOLD + " §7\u00BB " + ChatColor.RESET + event.getMessage());
			
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
		
		ArrayList<HashMap<String, String>> banQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM bans WHERE uniqueId = '" + uniqueId + "' AND isExpired != 1", 5, false);
		
		//If the user has an active ban
		if (banQuery.size() > 0) {
			HashMap<String, String> banQueryData = banQuery.get(0);
			long difference = ((Integer.parseInt(banQueryData.get("length"))*60) + Integer.parseInt(banQueryData.get("added")) - (System.currentTimeMillis()/1000L));
			
			if (difference <= 0) {
				mainInstance.getDatabaseInstance().query("UPDATE bans SET isExpired = 1 WHERE id = " + banQueryData.get("id"), 0, true);
			} else {
				event.disallow(Result.KICK_BANNED, "You are banned.\n\nExpires in: " + (difference/60) + " minute(s).\n\nReason:" + banQueryData.get("reason"));
			}
		}
		
		String playerIp = event.getAddress().toString().split(":")[0].replace("/","");
		ArrayList<HashMap<String, String>> ipBanQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM ipBans WHERE ip = '" + playerIp + "' AND isExpired != 1", 5, false);
		
		//If the user has an active ban
		if (ipBanQuery.size() > 0) {
			HashMap<String, String> ipBanQueryData = ipBanQuery.get(0);
			long difference = ((Integer.parseInt(ipBanQueryData.get("length"))*60) + Integer.parseInt(ipBanQueryData.get("added")) - (System.currentTimeMillis()/1000L));
			
			if (difference <= 0) {
				mainInstance.getDatabaseInstance().query("UPDATE bans SET isExpired = 1 WHERE id = " + ipBanQueryData.get("id"), 0, true);
			} else {
				event.disallow(Result.KICK_BANNED, "You are banned.\n\nExpires in: " + (difference/60) + " minute(s).\n\nReason:" + ipBanQueryData.get("reason"));
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
					ArrayList<HashMap<String, String>> userQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM users WHERE uniqueId = '" + ply.getUniqueId() + "'", 7, false);
					String ip = ply.getAddress().toString().split(":")[0].replace("/","");
					
					//If the player exists in the users table
					if (userQuery.size() > 0) {
						HashMap<String, String> userQueryData = userQuery.get(0);
						
						playerData.setData(Integer.parseInt(userQueryData.get("id")), Integer.parseInt(userQueryData.get("points")), 0,
								Double.parseDouble(userQueryData.get("timePlayed")), ip,
								UserGroup.fromInt(Integer.parseInt(userQueryData.get("userGroup"))));
						
						//If the players ip has changed from whats in the DB
						if (!userQueryData.get("ip").equals(ip)) {
							//Insert ip into the ipLogs table
							mainInstance.getDatabaseInstance().query("INSERT INTO ipLogs (uniqueId,ip) VALUES ('" + ply.getUniqueId() + "','" + ip + "')", 0, true);
							mainInstance.getDatabaseInstance().query("UPDATE users SET ip = '" + ip + "' WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);
						}
						
						//If the player does not exists in the users table
					} else {
						//Insert the user into the users table
						mainInstance.getDatabaseInstance().query("INSERT INTO users (uniqueId,name,ip) VALUES ('" + ply.getUniqueId() + "','" + ply.getName() + "','" + ip + "')", 0, true);
						
						//Insert ip into the ipLogs table
						mainInstance.getDatabaseInstance().query("INSERT INTO ipLogs (uniqueId,ip) VALUES ('" + ply.getUniqueId() + "','" + ip + "')", 0, true);
						
						//Get the users data from the users table. This is done to get their db id
						userQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM users WHERE uniqueId = '" + ply.getUniqueId() + "'", 1, false);
						HashMap<String, String> userQueryData = userQuery.get(0);
						playerData.setData(Integer.parseInt(userQueryData.get("id")), 0, 0, 0.0, ip, UserGroup.USER);
						
						// Let everyone know this is a new player
						for (Player player : world.getPlayers()) {
							player.sendMessage(ChatColor.YELLOW + "Welcome " + ChatColor.BOLD + ply.getName()
							+ ChatColor.RESET.toString() + ChatColor.YELLOW + " for their first time on the server!");
						}
					}
					mainInstance.getHubInstance().join(ply, false);
				}
			});
			playersData.put(ply.getUniqueId(), playerData);
			playerChatMessages.put(ply.getUniqueId(), new ArrayList<Long>());
			
			playerData.setRealm(Realm.HUB,false,false,true);
		} else {
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
						ply.sendMessage(ChatColor.YELLOW + "Unable to rejoin " + realm.getRealm().toString() + " game. It is not longer active.");
						mainInstance.getGameInstance().joinGame(ply, Realm.HUB);
					}
				} else {
					realm.join(ply, true);
				}
			} else {
				pd.getCurrentRealm().join(ply, false);
			}
		}
		
		//Show/hide players accordingly
		PlayerData pd = getPlayerData(ply);
		ArrayList<Player> players = pd.getCurrentRealm().getPlayersInRealm();
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

		// Check if there are any undeleted worlds that weren't deleted on the previous server shutdown
		if (!worldsChecked) {
			worldsChecked = true;
			MultiverseCore mv = mainInstance.getMultiverseInstance();

			for (MultiverseWorld world : mv.getMVWorldManager().getMVWorlds()) {
				if (!world.getName().toString().matches("world")) {
					MVWorldManager wm = mv.getMVWorldManager();
					wm.deleteWorld(world.getName());
				}
			}
		}
	}

	// Handle when a player leaves the server
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player ply = event.getPlayer();
		PlayerData pd = playersData.get(ply.getUniqueId());
		DatabaseHandling dh = mainInstance.getDatabaseInstance();

		// Remove default quit message
		event.setQuitMessage("");
		
		//Leave realm
		pd.getCurrentRealm().leave(ply, true, null);

		mainInstance.getTimerInstance().createTimer2("leave_" + ply.getUniqueId(), 60, 1, new Runnable() {
			@Override
			public void run() {
				if (!ply.isOnline()) {
					HashMap<String,Integer> realmXp = pd.getRealmXp();
					String xpString = "";
					
					for (Map.Entry<String,Integer> entry : realmXp.entrySet()) {
						if (xpString.equals("")) {
							xpString = entry.getKey() + " = " + entry.getValue();
						} else {
							xpString += ", " + entry.getKey() + " = " + entry.getValue();
						}
					}
					dh.query("UPDATE users SET timePlayed = (timePlayed + " + ((System.currentTimeMillis()/1000L)-pd.getTimeJoined()) + ") WHERE name = '" + ply.getName() + "'", 0, true);			
					dh.query("UPDATE userXP SET " + xpString + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);	
					
					//Remove player data
					playersData.remove(ply.getUniqueId());
					playerChatMessages.remove(ply.getUniqueId());
				}
			}
		});
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
			event.setCancelled(true);
		} else {
			if (getPlayerData(ply).getRealm() == Realm.SKYWARS && event.getBlock().getState() instanceof Chest) {
				event.setCancelled(true);
			}
		}
	}
	// Handle when a player is damaged by an entity
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();
			PlayerData damagerpd = getPlayerData(damager);
			PlayerData damagedpd = getPlayerData(damaged);

			if (!damager.hasPermission("nyeblock.canDamage")) {
				event.setCancelled(true);
			} else if (!damaged.hasPermission("nyeblock.canBeDamaged")) {
				event.setCancelled(true);
			} else if (damagedpd.getRealm() == Realm.PVP && damagedpd.getTeam() != null && damagedpd.getTeam().equals(damagerpd.getTeam())) {
				event.setCancelled(true);
			} else if (damaged.getHealth() <= 0) {
				event.setCancelled(true);
			}
		} else if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}
	}
	// Handle when the player falls
	@EventHandler
	public void onDamageEvent(EntityDamageEvent event) {
		Entity ent = event.getEntity();

		if (ent instanceof Player) {
			Player ply = (Player)ent;
			PlayerData playerData = playersData.get(ply.getUniqueId());

			if (playerData != null) {				
				if (!ply.hasPermission("nyeblock.canBeDamaged")) {
					event.setCancelled(true);
				} else {
					if (event.getCause() == DamageCause.FALL) {
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
			
			if (projectile instanceof Snowball) {
				attacked.damage(0.0001);
				attacked.setVelocity(attacked.getVelocity().add(attacked.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(1)));
			}
		}
	}
	// Handle when a player has died
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player killed = event.getEntity();
		Player attacker = event.getEntity().getKiller();
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
		
		game.playerDeath(killed, (attacker instanceof Player ? attacker : null));
	}
	// Handle when a player shoots a bow
	@EventHandler
	public void onPlayerShootBow(EntityShootBowEvent event) {
		Player ply = (Player) event.getEntity();
		GameBase game = (GameBase)getPlayerData(ply).getCurrentRealm();

		if (ply instanceof Player) {
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
            }
			event.setCancelled(true);
		}
	}
	//TODO WORK ON LATER
//	@EventHandler
//	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
//		Player ply = event.getPlayer();
//		
//		System.out.println("Test: " + ply.getOpenInventory().getTitle());
//	}
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
		
        if (event.getInventory() instanceof EnchantingInventory) {
            EnchantingInventory inv = (EnchantingInventory) event.getInventory();
            inv.setItem(1,new ItemStack(Material.LAPIS_LAZULI,20));
            
            mainInstance.getTimerInstance().createTimer2("lapisReplacement_" + ply.getUniqueId(), .5, 0, new Runnable() {
            	@Override
            	public void run() {
            		inv.setItem(1,new ItemStack(Material.LAPIS_LAZULI,20));
            	}
            });
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
					PlayerData playerData = playersData.get(ply.getUniqueId());
					MenuBase menu = playerData.getMenu();
					
					if (menu != null) {
						if (menu.getCurrentMenu().hasOption(itemMeta.getLocalizedName())) {							
							menu.getCurrentMenu().runOption(itemMeta.getLocalizedName());
						} else {
							playerData.getCustomItem(itemMeta.getLocalizedName()).use(item);
						}
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

		if (event.getAction().toString().matches("RIGHT_CLICK_AIR|RIGHT_CLICK_BLOCK")) {
			ItemStack item = ply.getItemInHand();
			ItemMeta itemMeta = item.getItemMeta();

			if (item != null && itemMeta != null) {
				PlayerData pd = getPlayerData(ply);
				String itemName = itemMeta.getLocalizedName();
				
				ItemBase itemm = pd.getCustomItem(itemName);
		          
				if (itemm != null) {
					pd.getCustomItem(itemName).use(item);
					event.setCancelled(true);
				}
			}
		}
	}
	//Handle when a player interacts with an entity
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType().toString().equals("ITEM_FRAME")) {
			event.setCancelled(true);
		}
	}
}
