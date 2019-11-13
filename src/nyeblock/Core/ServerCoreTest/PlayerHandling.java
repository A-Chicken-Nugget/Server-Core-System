package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Games.KitPvP;
import nyeblock.Core.ServerCoreTest.Games.SkyWars;
import nyeblock.Core.ServerCoreTest.Games.StepSpleef;
import nyeblock.Core.ServerCoreTest.Items.HubMenu;
import nyeblock.Core.ServerCoreTest.Items.KitSelector;
import nyeblock.Core.ServerCoreTest.Items.ParkourMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Misc.TabListPerWorld;

@SuppressWarnings("deprecation")
public class PlayerHandling implements Listener {
	private Main mainInstance;
	private HashMap<String, PlayerData> playersData = new HashMap<String, PlayerData>();
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
						HashMap<Realm,Integer> realmXp = pd.getRealmXp();
						
						Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
							@Override
							public void run() {       				            	
				            	dh.query("UPDATE users SET timePlayed = (timePlayed + " + ((System.currentTimeMillis()/1000L)-getPlayerData(ply).getTimeJoined()) + ") WHERE name = '" + ply.getName() + "'", 0, true);
				            	dh.query("UPDATE userXP SET kitpvp = " + realmXp.get(Realm.KITPVP) + ", skywars = " + realmXp.get(Realm.SKYWARS) + ", stepspleef = " + realmXp.get(Realm.STEPSPLEEF) + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);
							}
						});
		            }
				}
			}
		}, 0, 20*180);
		
		//Manage players tab lists
		Bukkit.getScheduler().runTaskTimer(mainInstance, new Runnable() {
			@Override
			public void run() {
				TabListPerWorld tlpw = new TabListPerWorld();
				Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
				
				for (Player ply : onlinePlayers) {
					World plyWorld = ply.getWorld();
					PlayerData pd = getPlayerData(ply);
					boolean hidePlayers = false;
					
					if (pd != null) {						
						hidePlayers = Boolean.parseBoolean(getPlayerData(ply).getCustomDataKey("hide_players"));
					}
					if (plyWorld.getName().equals("world") && hidePlayers) {
						for (Player ply2 : onlinePlayers) {
							ply.hidePlayer(ply2);
						}
					} else {							
						for (Player ply2 : onlinePlayers) {
							World ply2World = ply2.getWorld();
							
							if (!plyWorld.getName().equals(ply2World.getName())) {
								tlpw.hideForWorld(plyWorld, ply2);
							} else {
								tlpw.showForWorld(ply2World, ply2);
							}
						}
					}
				}
			}
		}, 0, 20*3);
	}
	
	//
	// GETTERS
	//

	// Get a specific players player data
	public PlayerData getPlayerData(Player ply) {
		PlayerData plyData = null;

		for (Map.Entry<String, PlayerData> entry : playersData.entrySet()) {
			if (ply.getName().equalsIgnoreCase(entry.getKey())) {
				plyData = entry.getValue();
			}
		}
		return plyData;
	}
	
	//
	// EVENT HANDLERS
	//

	// Prevent mob spawn
	@EventHandler()
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
			event.setCancelled(true);
		} else {
			System.out.println("Spawning: " + event.getEntity().getName());
		}
	}

	// Only allow players on the same world to talk to each other
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player ply = event.getPlayer();
		String playerWorld = ply.getWorld().getName();
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		PlayerData playerData = playersData.get(ply.getName());

		event.setFormat(playerData.getUserGroup().getTag() + " " + ply.getName() + ": " + event.getMessage());

		for (Player player : event.getRecipients()) {
			if (!playerWorld.equalsIgnoreCase(player.getWorld().getName())) {
				playersToRemove.add(player);
			}
		}
		event.getRecipients().removeAll(playersToRemove);
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
			if (!getPlayerData(ply).getPermission("nyeblock.canLoseHunger")) {
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
		
		// Setup player data. If they don't have a profile in the database, create one.
		final PlayerData playerData = new PlayerData();
		playerData.createScoreboard(ply);
		
		//Run database queries asynchronously
		Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
            @Override
            public void run() {            	
            	ArrayList<HashMap<String, String>> userQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM users WHERE uniqueId = '" + ply.getUniqueId() + "'", 7, false);
            	String ip = ply.getAddress().toString().split(":")[0].replace("/","");
            	
            	//If the player exists in the users table
            	if (userQuery.size() > 0) {
            		HashMap<String, String> userQueryData = userQuery.get(0);
            		
            		playerData.setData(mainInstance, ply, Integer.parseInt(userQueryData.get("id")), Integer.parseInt(userQueryData.get("points")), 0,
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
            		playerData.setData(mainInstance, ply, Integer.parseInt(userQueryData.get("id")), 0, 0, 0.0, ip, UserGroup.USER);
            		
            		// Let everyone know this is a new player
            		for (Player player : world.getPlayers()) {
            			player.sendMessage(ChatColor.YELLOW + "Welcome " + ChatColor.BOLD + ply.getName()
            			+ ChatColor.RESET.toString() + ChatColor.YELLOW + " for their first time on the server!");
            		}
            	}
            	playersData.put(ply.getName(), playerData);
            	playerData.setItems();
            	
            	//Add player to hub
            	mainInstance.getHubInstance().playerJoin(ply);
            }
		});
		
		// Teleport to spawn
		ply.teleport(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));

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
		PlayerData pd = playersData.get(ply.getName());
		DatabaseHandling dh = mainInstance.getDatabaseInstance();

		// Remove default quit message
		event.setQuitMessage("");

		// If the player is in a game, remove them
		mainInstance.getGameInstance().removePlayerFromGame(ply, pd.getRealm());
		
		//If player is in hub
		if (mainInstance.getHubInstance().getPlayers().contains(ply)) {
			mainInstance.getHubInstance().playerLeave(ply);
		}
		
		//If player is in parkour
		if (mainInstance.getHubParkourInstance().getPlayers().contains(ply)) {
			mainInstance.getHubParkourInstance().playerLeave(ply);
		}

		//Update play time
		dh.query("UPDATE users SET timePlayed = (timePlayed + " + ((System.currentTimeMillis()/1000L)-pd.getTimeJoined()) + ") WHERE name = '" + ply.getName() + "'", 0, true);
		
		//Update realm xp
		HashMap<Realm,Integer> realmXp = pd.getRealmXp();
		dh.query("UPDATE userXP SET kitpvp = " + realmXp.get(Realm.KITPVP) + ", skywars = " + realmXp.get(Realm.SKYWARS) + ", stepspleef = " + realmXp.get(Realm.STEPSPLEEF) + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);
		
		//Remove player data
		playersData.remove(ply.getName());
	}

	// Handle when a player respawns
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player ply = event.getPlayer();
		PlayerData playerData = playersData.get(ply.getName());

		if (playerData.getRealm() == Realm.HUB) {
			event.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
			playerData.setItems();
		} else if (playerData.getRealm() == Realm.KITPVP) {
			GameHandling gh = mainInstance.getGameInstance();
			KitPvP game = null;

			for (KitPvP gm : gh.getKitPvpGames()) {
				if (gm.isInServer(ply)) {
					game = gm;
				}
			}
			if (game != null) {
				Vector randSpawn = game.getRandomSpawnPoint();
				event.setRespawnLocation(new Location(Bukkit.getWorld(ply.getWorld().getName()), randSpawn.getX(),
						randSpawn.getY(), randSpawn.getZ()));
				playerData.setItems();
				game.setPlayerKit(ply, game.getPlayerKit(ply));
			}
		} else if (playerData.getRealm() == Realm.STEPSPLEEF) {
			GameHandling gh = mainInstance.getGameInstance();

			for (StepSpleef gm : gh.getStepSpleefGames()) {
				if (gm.isInServer(ply)) {
					Vector randSpawn = gm.getRandomSpawnPoint();
					event.setRespawnLocation(new Location(Bukkit.getWorld(ply.getWorld().getName()), randSpawn.getX(),
							randSpawn.getY(), randSpawn.getZ()));
					playerData.setItems();
					ply.setAllowFlight(true);
				}
			}
		} else if (playerData.getRealm() == Realm.SKYWARS) {
			GameHandling gh = mainInstance.getGameInstance();

			for (SkyWars gm : gh.getSkyWarsGames()) {
				if (gm.isInServer(ply)) {
					Vector randSpawn = gm.getRandomSpawnPoint();
					event.setRespawnLocation(new Location(Bukkit.getWorld(ply.getWorld().getName()), randSpawn.getX(),
							randSpawn.getY(), randSpawn.getZ()));
					playerData.setItems();
					ply.setAllowFlight(true);
				}
			}
		}
	}
	// Handle when a player attempts to drop an item
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player ply = event.getPlayer();
		PlayerData playerData = playersData.get(ply.getName());

		if (ply.hasPermission("nyeblock.canDropItems")) {
			if (!playerData.getPermission("nyeblock.canDropItems")) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
		}
	}
	//Handle when a block is placed
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player ply = event.getPlayer();
		PlayerData playerData = playersData.get(ply.getName());
		
		if (ply.hasPermission("nyeblock.canPlaceBlocks")) {
			if (!playerData.getPermission("nyeblock.canPlaceBlocks")) {
				event.setCancelled(true);
			}
		}
	}
	// Handle when a block is broken
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player ply = event.getPlayer();
		PlayerData playerData = playersData.get(ply.getName());

		// Check if the player has the proper permission to break blocks
		if (ply.hasPermission("nyeblock.canBreakBlocks")) {
			if (!playerData.getPermission("nyeblock.canBreakBlocks")) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
		}
	}
	// Handle when a player is damaged by an entity
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();

			if (damager instanceof Player) {
				PlayerData playerData = playersData.get(damager.getName());

				if (!playerData.getPermission("nyeblock.canDamage")) {
					event.setCancelled(true);
				}
			}
			if (damaged instanceof Player) {
				PlayerData playerData = playersData.get(damager.getName());

				if (!playerData.getPermission("nyeblock.canBeDamaged")) {
					event.setCancelled(true);
				}
			}
		}
	}
	// Handle when the player falls
	@EventHandler
	public void onDamageEvent(EntityDamageEvent event) {
		Entity ply = event.getEntity();

		if (ply instanceof Player) {
			PlayerData playerData = playersData.get(ply.getName());

			if (playerData != null) {				
				if (!playerData.getPermission("nyeblock.canBeDamaged")) {
					event.setCancelled(true);
				} else {
					if (event.getCause() == DamageCause.FALL) {
						if (!playerData.getPermission("nyeblock.canTakeFallDamage")
								|| playerData.getPermission("nyeblock.tempNoDamageOnFall")) {
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
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		Player attacked = (Player)event.getHitEntity();
		Player attacker = (Player)event.getEntity().getShooter();
		
		if (attacked instanceof Player && attacker instanceof Player) {
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
		// Remove default death message
		event.setDeathMessage("");
		PlayerData playerData = playersData.get(killed.getName());

		if (playerData.getRealm() == Realm.HUB) {
			event.getDrops().clear();
		} else if (playerData.getRealm() == Realm.KITPVP) {
			event.getDrops().clear();

			for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
				if (game.isInServer(killed)) {
					if (attacker instanceof Player) {
						game.playerDeath(killed, attacker);
						for (int i = 0; i < 10; i++) {
							attacker.playEffect(killed.getLocation(), Effect.SMOKE, 1);
						}
						attacker.playSound(attacker.getLocation(), Sound.ITEM_TRIDENT_HIT, 10, 1);
					} else {
						game.playerDeath(killed, null);
					}
				}
			}
		} else if (playerData.getRealm() == Realm.STEPSPLEEF) {
			event.getDrops().clear();

			for (StepSpleef game : mainInstance.getGameInstance().getStepSpleefGames()) {
				if (game.isInServer(killed)) {
					game.playerDeath(killed);
				}
			}
		} else if (playerData.getRealm() == Realm.SKYWARS) {
			for (SkyWars game : mainInstance.getGameInstance().getSkyWarsGames()) {
				if (game.isInServer(killed)) {
					if (attacker instanceof Player) {
						game.playerDeath(killed, attacker);
						for (int i = 0; i < 10; i++) {
							attacker.playEffect(killed.getLocation(), Effect.SMOKE, 1);
						}
						attacker.playSound(attacker.getLocation(), Sound.ITEM_TRIDENT_HIT, 10, 1);
					} else {
						game.playerDeath(killed, null);
					}
				}
			}
		}
		killed.remove();
	}
	// Handle when a player shoots a bow
	@EventHandler
	public void onPlayerShootBow(EntityShootBowEvent event) {
		Player ply = (Player) event.getEntity();

		if (ply instanceof Player) {
			for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
				if (game.isInServer(ply)) {
					if (game.isInGraceBounds(ply)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	// Handle explosion events
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Entity ent = event.getEntity();

		if (ent instanceof Fireball) {
			event.setCancelled(true);
		}
	}
	// Handle potion splashes
	@EventHandler
	public void onPotionSpash(PotionSplashEvent event) {

		if (event.getEntity() instanceof Player) {
			Player ply = (Player) event.getEntity();

			for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
				if (game.isInServer(ply)) {
					if (game.isInGraceBounds(ply)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	//Handle when a player presses the f key
	@EventHandler
	public void onItemSwapEvent(PlayerSwapHandItemsEvent event) {
		Player ply = event.getPlayer();
		
		if (!getPlayerData(ply).getPermission("nyeblock.canSwapItems")) {
			event.setCancelled(true);
		}
	}
	// Handle when an item is moved in an inventory menu
	@EventHandler
	public void onPlayerInventoryMove(InventoryClickEvent event) {
		Player ply = (Player) event.getWhoClicked();
		
		if (event != null) {
			ItemStack item = event.getCurrentItem();

			if (item != null) {				
				ItemMeta itemMeta = item.getItemMeta();
				
				if (itemMeta != null) {
					// Track items in menus
					if (event.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Game Menu")) {
						event.getView().close();
						HubMenu hubMenu = new HubMenu();
						hubMenu.clickItem(ply, itemMeta.getLocalizedName(), mainInstance);
					} else if (event.getView().getTitle()
							.equalsIgnoreCase(ChatColor.DARK_GRAY + "Select a Kit")) {
						event.getView().close();
						KitSelector selectKit = new KitSelector(getPlayerData(ply).getRealm());
						selectKit.clickItem(ply, itemMeta.getLocalizedName(), mainInstance);
					} else if (event.getView().getTitle()
							.equalsIgnoreCase(ChatColor.DARK_GRAY + "Parkour Menu")) {
						event.getView().close();
						ParkourMenu select = new ParkourMenu();
						select.clickItem(ply, itemMeta.getLocalizedName(), mainInstance);
					} else {
						String itemName = itemMeta.getLocalizedName();
						PlayerData playerData = playersData.get(ply.getName());
						
						if (itemName.equals("hub_menu")) {
							HubMenu hubMenu = new HubMenu();
							
							hubMenu.openMenu(mainInstance,ply,1);
							event.setCancelled(false);
						} else if (itemName.equals("return_to_hub")) {
							// Remove player from game
							if (playerData.getRealm() == Realm.KITPVP) {
								for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
									if (game.isInServer(ply)) {
										game.playerLeave(ply, true, true);
									}
								}
							} else if (playerData.getRealm() == Realm.STEPSPLEEF) {
								for (StepSpleef game : mainInstance.getGameInstance().getStepSpleefGames()) {
									if (game.isInServer(ply)) {
										game.playerLeave(ply, true, true);
									}
								}
							} else if (playerData.getRealm() == Realm.SKYWARS) {
								for (SkyWars game : mainInstance.getGameInstance().getSkyWarsGames()) {
									if (game.isInServer(ply)) {
										game.playerLeave(ply, true, true);
									}
								}
							}
							event.setCancelled(false);
						} else if (itemName.equals("parkour_menu")) {
							ParkourMenu parkourMenu = new ParkourMenu();

							parkourMenu.openMenu(mainInstance,ply);
							
							event.setCancelled(true);
						} else if (itemName.equals("parkour_start")) {
							mainInstance.getHubParkourInstance().goToStart(ply);
						} else if (itemName.equals("kit_selector")) {
							if (playerData.getRealm() == Realm.KITPVP) {
								for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
									if (game.isInServer(ply)) {
										if (game.isInGraceBounds(ply)) {
											KitSelector selectKit = new KitSelector(Realm.KITPVP);
											
											selectKit.openMenu(ply, mainInstance);
										}
									}
								}
							} else if (playerData.getRealm() == Realm.SKYWARS) {
								for (SkyWars game : mainInstance.getGameInstance().getSkyWarsGames()) {
									if (game.isInServer(ply)) {
										KitSelector selectKit = new KitSelector(Realm.SKYWARS);
										
										selectKit.openMenu(ply, mainInstance);
									}
								}
							}
							event.setCancelled(false);
						} else if (itemName.equals("player_selector")) {
							int currentIndex = Integer.parseInt(playerData.getCustomDataKey("player_selector_index"));
							String worldName = playerData.getCustomDataKey("player_world");
							
							if (playerData.getRealm() == Realm.STEPSPLEEF) {
								for (StepSpleef game : mainInstance.getGameInstance().getStepSpleefGames()) {
									if (game.getWorldName().equalsIgnoreCase(worldName)) {
										ArrayList<Player> playersInGame = game.getPlayersInGame();
										
										if (playersInGame.size() > currentIndex + 1) {
											Player playerToSpec = playersInGame.get(currentIndex + 1);
											
											ply.teleport(playerToSpec);
											itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
													+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
													+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
											playerData.setCustomDataKey("player_selector_index",
													String.valueOf(currentIndex + 1));
										} else {
											if (playersInGame.size() > 0) {
												Player playerToSpec = playersInGame.get(0);
												
												ply.teleport(playerToSpec);
												itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
														+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
														+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
												playerData.setCustomDataKey("player_selector_index", "0");
											} else {
												itemMeta.setDisplayName(ChatColor.YELLOW + "No players to spectate.");
											}
										}
									}
								}
							} else if (playerData.getRealm() == Realm.SKYWARS) {
								for (SkyWars game : mainInstance.getGameInstance().getSkyWarsGames()) {
									if (game.getWorldName().equalsIgnoreCase(worldName)) {
										ArrayList<Player> playersInGame = game.getPlayersInGame();
										
										if (playersInGame.size() > currentIndex + 1) {
											Player playerToSpec = playersInGame.get(currentIndex + 1);
											
											ply.teleport(playerToSpec);
											itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
													+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
													+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
											playerData.setCustomDataKey("player_selector_index",
													String.valueOf(currentIndex + 1));
										} else {
											if (playersInGame.size() > 0) {
												Player playerToSpec = playersInGame.get(0);
												
												ply.teleport(playerToSpec);
												itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
														+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
														+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
												playerData.setCustomDataKey("player_selector_index", "0");
											} else {
												itemMeta.setDisplayName(ChatColor.YELLOW + "No players to spectate.");
											}
										}
									}
								}
							}
							event.getCurrentItem().setItemMeta(itemMeta);
							event.setCancelled(false);
						} else if (itemName.equals("hide_players")) {
							boolean currentStatus = Boolean.parseBoolean(playerData.getCustomDataKey("hide_players"));
							
							if (currentStatus) {
								playerData.setCustomDataKey("hide_players", "false");
								itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + ChatColor.RED.toString() + ChatColor.BOLD + "Disabled");
							} else {
								playerData.setCustomDataKey("hide_players", "true");
								itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + ChatColor.GREEN.toString() + ChatColor.BOLD + "Enabled");
							}
							item.setItemMeta(itemMeta);
						}
					}
					// Block inventory move
					if (ply.hasPermission("nyeblock.canUseInventory")) {
						PlayerData playerData = playersData.get(ply.getName());
						
						if (!playerData.getPermission("nyeblock.canUseInventory")) {
							event.setCancelled(true);
						}
					} else {
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
				String itemName = itemMeta.getLocalizedName();

				if (itemName.equals("hub_menu")) {
					HubMenu hubMenu = new HubMenu();

					hubMenu.openMenu(mainInstance,ply,1);
				} else if (itemName.equals("parkour_menu")) {
					ParkourMenu parkourMenu = new ParkourMenu();

					parkourMenu.openMenu(mainInstance,ply);
					
					event.setCancelled(true);
				} else if (itemName.equals("return_to_hub")) {
					event.setCancelled(true);
					PlayerData playerData = playersData.get(ply.getName());

					// Remove player from game
					if (playerData.getRealm() == Realm.KITPVP) {
						for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
							if (game.isInServer(ply)) {
								game.playerLeave(ply, true, true);
							}
						}
					} else if (playerData.getRealm() == Realm.STEPSPLEEF) {
						for (StepSpleef game : mainInstance.getGameInstance().getStepSpleefGames()) {
							if (game.isInServer(ply)) {
								game.playerLeave(ply, true, true);
							}
						}
					} else if (playerData.getRealm() == Realm.SKYWARS) {
						for (SkyWars game : mainInstance.getGameInstance().getSkyWarsGames()) {
							if (game.isInServer(ply)) {
								game.playerLeave(ply, true, true);
							}
						}
					}
				} else if (itemName.equals("parkour_start")) {
					mainInstance.getHubParkourInstance().goToStart(ply);
				} else if (itemName.equals("kit_selector")) {
					PlayerData playerData = playersData.get(ply.getName());

					if (playerData.getRealm() == Realm.KITPVP) {
						for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
							if (game.isInServer(ply)) {
								if (game.isInGraceBounds(ply)) {
									KitSelector selectKit = new KitSelector(Realm.KITPVP);

									selectKit.openMenu(ply, mainInstance);
								}
							}
						}
					} else if (playerData.getRealm() == Realm.SKYWARS) {
						for (SkyWars game : mainInstance.getGameInstance().getSkyWarsGames()) {
							if (game.isInServer(ply)) {
								KitSelector selectKit = new KitSelector(Realm.SKYWARS);

								selectKit.openMenu(ply, mainInstance);
							}
						}
					}
				} else if (itemName.equals("player_selector")) {
					PlayerData playerData = playersData.get(ply.getName());
					int currentIndex = Integer.parseInt(playerData.getCustomDataKey("player_selector_index"));
					String worldName = playerData.getCustomDataKey("player_world");

					if (playerData.getRealm() == Realm.STEPSPLEEF) {
						for (StepSpleef game : mainInstance.getGameInstance().getStepSpleefGames()) {
							if (game.getWorldName().equalsIgnoreCase(worldName)) {
								ArrayList<Player> playersInGame = game.getPlayersInGame();

								if (playersInGame.size() > currentIndex + 1) {
									Player playerToSpec = playersInGame.get(currentIndex + 1);

									ply.teleport(playerToSpec);
									itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
											+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
											+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
									playerData.setCustomDataKey("player_selector_index",
											String.valueOf(currentIndex + 1));
								} else {
									if (playersInGame.size() > 0) {
										Player playerToSpec = playersInGame.get(0);

										ply.teleport(playerToSpec);
										itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
												+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
												+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
										playerData.setCustomDataKey("player_selector_index", "0");
									} else {
										itemMeta.setDisplayName(ChatColor.YELLOW + "No players to spectate.");
									}
								}
							}
						}
					} else if (playerData.getRealm() == Realm.SKYWARS) {
						for (SkyWars game : mainInstance.getGameInstance().getSkyWarsGames()) {
							if (game.getWorldName().equalsIgnoreCase(worldName)) {
								ArrayList<Player> playersInGame = game.getPlayersInGame();

								if (playersInGame.size() > currentIndex + 1) {
									Player playerToSpec = playersInGame.get(currentIndex + 1);

									ply.teleport(playerToSpec);
									itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
											+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
											+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
									playerData.setCustomDataKey("player_selector_index",
											String.valueOf(currentIndex + 1));
								} else {
									if (playersInGame.size() > 0) {
										Player playerToSpec = playersInGame.get(0);

										ply.teleport(playerToSpec);
										itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
												+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
												+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
										playerData.setCustomDataKey("player_selector_index", "0");
									} else {
										itemMeta.setDisplayName(ChatColor.YELLOW + "No players to spectate.");
									}
								}
							}
						}
					}
					item.setItemMeta(itemMeta);
				} else if (itemName.equals("hide_players")) {
					PlayerData playerData = playersData.get(ply.getName());
					boolean currentStatus = Boolean.parseBoolean(playerData.getCustomDataKey("hide_players"));

					if (currentStatus) {
						playerData.setCustomDataKey("hide_players", "false");
						itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + ChatColor.RED.toString() + ChatColor.BOLD + "Disabled");
					} else {
						playerData.setCustomDataKey("hide_players", "true");
						itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + ChatColor.GREEN.toString() + ChatColor.BOLD + "Enabled");
					}
					item.setItemMeta(itemMeta);
				} else if (itemName.equals("kitpvp_wizard_fireball")) {
					for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
						if (game.isInServer(ply)) {
							if (!game.isInGraceBounds(ply)) {
								Location spawnAt = ply.getEyeLocation().toVector()
										.add(ply.getEyeLocation().getDirection().multiply(3))
										.toLocation(ply.getWorld());
								Fireball fireball = ply.getWorld().spawn(spawnAt, Fireball.class);
								fireball.setDirection(ply.getEyeLocation().getDirection());
								fireball.setBounce(false);
								fireball.setIsIncendiary(false);
								fireball.setYield(1.75F);
								fireball.setShooter(ply);
								for (ItemStack itemm : ply.getInventory().getContents()) {
									if (itemm != null) {
										if (itemm.getType().equals(Material.FIRE_CHARGE)) {
											itemm.setAmount(itemm.getAmount() - 1);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
