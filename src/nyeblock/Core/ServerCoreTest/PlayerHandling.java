package nyeblock.Core.ServerCoreTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Games.KitPvP;
import nyeblock.Core.ServerCoreTest.Games.StepSpleef;
import nyeblock.Core.ServerCoreTest.Items.HubMenu;
import nyeblock.Core.ServerCoreTest.Items.KitSelector;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class PlayerHandling implements Listener {
	private Main mainInstance;
	private HashMap<String, PlayerData> playersData = new HashMap<String, PlayerData>();
	private boolean worldsChecked = false;
	
	public PlayerHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
		
		//Timer ran every second
		Bukkit.getScheduler().runTaskTimer(Bukkit.getServer().getPluginManager().getPlugin("ServerCoreTest"), new Runnable() {
			@Override
            public void run() {
        		//Manage hub weather/time
        		World hub = Bukkit.getWorld("world");
        		hub.setTime(1000);
        		if (hub.hasStorm()) {
        			hub.setStorm(false);
        		}
        		//Update players scoreboard
        		for (Player ply : Bukkit.getOnlinePlayers()) {
        			if (ply.getWorld().getName().equalsIgnoreCase("world")) {
        				PlayerData pd = getPlayerData(ply);
        				HashMap<Integer,String> scores = new HashMap<Integer,String>();
        				
        				if (!ply.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "NYEBLOCK (ALPHA)")) {						
        					pd.setObjectiveName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "NYEBLOCK (ALPHA)");
        				}
        				
        				scores.put(5, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
        				scores.put(4, ChatColor.RESET.toString() + ChatColor.RESET.toString());
        				scores.put(3, ChatColor.YELLOW + "Players online: " + ChatColor.GREEN + playersData.size());
        				scores.put(2, ChatColor.RESET.toString());
        				scores.put(1, ChatColor.GREEN + "http://nyeblock.com/");
        				
        				pd.updateObjectiveScores(scores);
        				
        				//Update gamemode
        				if (ply.getGameMode() != GameMode.ADVENTURE) {
							ply.setGameMode(GameMode.ADVENTURE);
						}
        			}
        		}
            }
        }, 0, 10);
	}
	
	//Get a specific players player data
	public PlayerData getPlayerData(Player ply) {
		PlayerData plyData = null;
		
		for (Map.Entry<String, PlayerData> entry : playersData.entrySet()) {
			if (ply.getName().equalsIgnoreCase(entry.getKey())) {
				plyData = entry.getValue();
			}
		}
		return plyData;
	}
	//Prevent mob spawn
	@EventHandler()
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
			event.setCancelled(true);
		}
	}
	//Only allow players on the same world to talk to each other
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
	    String playerWorld = event.getPlayer().getWorld().getName();
	    ArrayList<Player> playersToRemove = new ArrayList<Player>();
	    
	    for(Player player : event.getRecipients()) {
	    	if (!playerWorld.equalsIgnoreCase(player.getWorld().getName())) {
	    		playersToRemove.add(player);
	    	}
	    }
	    event.getRecipients().removeAll(playersToRemove);
	}
	//Keep the players food bar at 100%
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
	  if (event.getFoodLevel() < 20) {
		  event.setFoodLevel(20);
	  }
	}
	//Handle when the player joins the server
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
		Player ply = event.getPlayer();
		
		//Remove default join message
		event.setJoinMessage("");
		
		//Setup player data. If they don't have a profile in the database, create one.
		PlayerData playerData = null;
		ArrayList<HashMap<String,String>> query = mainInstance.getDatabaseInstance().query("SELECT * FROM users WHERE name = '" + ply.getName() + "'", 6, false);
		if (query.size() > 0) {
			HashMap<String,String> queryData = query.get(0);

			playerData = new PlayerData(mainInstance,ply,Integer.parseInt(queryData.get("points")),0,Double.parseDouble(queryData.get("timePlayed")),ply.getAddress().getHostName(),UserGroup.fromInt(Integer.parseInt(queryData.get("userGroup"))));
		} 
		else {
			mainInstance.getDatabaseInstance().query("INSERT INTO users (name,ip) VALUES ('" + ply.getName() + "','" + ply.getAddress() + "')",0,true);
			playerData = new PlayerData(mainInstance,ply,0,0,0.0,ply.getAddress().getHostName(),UserGroup.USER);
		}
		playersData.put(ply.getName(), playerData);
		
		ply.teleport(Bukkit.getWorld("world").getSpawnLocation());
		
		if (!worldsChecked) {
			worldsChecked = true;
			//Delete undeleted game worlds
			MultiverseCore mv = mainInstance.getMultiverseInstance();
			for(MultiverseWorld world : mv.getMVWorldManager().getMVWorlds()) {
				if (!world.getName().toString().matches("world|world_nether|world_the_end")) {
					MVWorldManager wm = mv.getMVWorldManager();
					wm.deleteWorld(world.getName());
				}
			}
		}
    }
	//Handle when a player leaves the server
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player ply = event.getPlayer();
		
		//Remove default quit message
		event.setQuitMessage("");

		//If the player is in a game, remove them
		PlayerData pd = playersData.get(ply.getName());
		mainInstance.getGameInstance().removePlayerFromGame(ply, pd.getRealm());
		
		playersData.remove(ply.getName());
	}
	//Handle when a player respawns
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
				event.setRespawnLocation(new Location(Bukkit.getWorld(ply.getWorld().getName()),randSpawn.getX(), randSpawn.getY(), randSpawn.getZ()));
				playerData.setItems();
				game.setPlayerKit(ply, game.getPlayerKit(ply));
			}
		} else if (playerData.getRealm() == Realm.STEPSPLEEF) {
			GameHandling gh = mainInstance.getGameInstance();
			
			for (StepSpleef gm : gh.getStepSpleefGames()) {
				if (gm.isInServer(ply)) {
					Vector randSpawn = gm.getRandomSpawnPoint();
					event.setRespawnLocation(new Location(Bukkit.getWorld(ply.getWorld().getName()),randSpawn.getX(), randSpawn.getY(), randSpawn.getZ()));
					playerData.setItems();
					ply.setAllowFlight(true);
				}
			}
		}
	}
	//Handle when a player attempts to drop an item
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player ply = event.getPlayer();
		PlayerData playerData = playersData.get(ply.getName());
		
		if (ply.hasPermission("nyeblock.dropItems")) {
			if (!playerData.getPermission("nyeblock.dropItems")) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
		}
		event.setCancelled(true);
	}
	//Handle when a block is broken
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player ply = event.getPlayer();
		PlayerData playerData = playersData.get(ply.getName());
		
		//Check if the player has the proper permission to break blocks
		if (ply.hasPermission("nyeblock.breakBlocks")) {
			if (!playerData.getPermission("nyeblock.breakBlocks")) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
		}
	}

	//Handle when a player is damaged by an entity
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {			
			Player damager = (Player)event.getDamager();
			Player damaged = (Player)event.getEntity();
			
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
	//Handle when the player falls
	@EventHandler
	public void onDamageEvent(EntityDamageEvent event) {
		Entity ply = event.getEntity();
		
		if (ply instanceof Player) {
			PlayerData playerData = playersData.get(ply.getName());
			
			if (!playerData.getPermission("nyeblock.canBeDamaged")) {
				event.setCancelled(true);
			} else {
				if (event.getCause() == DamageCause.FALL) {						
					if (!playerData.getPermission("nyeblock.takeFallDamage") || playerData.getPermission("nyeblock.tempNoDamageOnFall")) {
						playerData.setPermission("nyeblock.tempNoDamageOnFall",false);
						event.setCancelled(true);
					}
				}
			}
		}
	}
	//Handle when a player has died
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player killed = event.getEntity();
		Player attacker = event.getEntity().getKiller();
		//Remove default death message
		event.setDeathMessage("");
		PlayerData playerData = playersData.get(killed.getName());
		
		if (playerData.getRealm() == Realm.HUB) {			
			event.getDrops().clear();
		} else if (playerData.getRealm() == Realm.KITPVP) {
			event.getDrops().clear();
			
			for(KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
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
			
			for(StepSpleef game : mainInstance.getGameInstance().getStepSpleefGames()) {
				if (game.isInServer(killed)) {						
					game.playerDeath(killed);
				}
			}
		}
		killed.remove();
	}
	//Handle when a player shoots a bow
	@EventHandler
	public void onPlayerShootBow(EntityShootBowEvent event) {
		Player ply = (Player)event.getEntity();
		
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
	//Handle explosion events
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Entity ent = event.getEntity();
		
		if (ent instanceof Fireball) {
			event.setCancelled(true);
		}
	}
	//Handle potion splashes
	@EventHandler
    public void onPotionSpash(PotionSplashEvent event){
		Entity ply = event.getEntity();
		
		if (ply instanceof Player) {
			for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
				if (game.isInServer((Player)ply)) {
					if (game.isInGraceBounds((Player)ply)) {
						event.setCancelled(true);
					}
				}
			}
		}
    }
//	@EventHandler
//    public void onPlayerMove(PlayerMoveEvent event) {
//		Player ply = event.getPlayer();
//		PlayerData playerData = playersData.get(ply.getName());
//		
//        if (!playerData.getPermission("nyeblock.showRunningParticles")) {
//        	
//        }
//    }
	//Handle when an item is moved in an inventory menu
	@EventHandler
	public void onPlayerInventoryMove(InventoryClickEvent event) {
		Player ply = (Player)event.getWhoClicked();
		
		//Track items in menus
		if (event.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Server Menu")) {
			if (event.getCurrentItem() != null) {				
				ItemMeta item = event.getCurrentItem().getItemMeta();
				
				if (item != null) {				
					event.getView().close();
					HubMenu hubMenu = new HubMenu();
					hubMenu.clickItem(ply, item.getLocalizedName(), mainInstance);
				}
			}
		} else if (event.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Select a Kit")) {
			if (event.getCurrentItem() != null) {				
				ItemMeta item = event.getCurrentItem().getItemMeta();
				
				if (item != null) {				
					event.getView().close();
					KitSelector selectKit = new KitSelector();
					selectKit.clickItem(ply, item.getLocalizedName(), mainInstance);
				}
			}
		}
		//Block inventory move
		if (ply.hasPermission("nyeblock.useInventory")) {
			PlayerData playerData = playersData.get(ply.getName());
			
			if (!playerData.getPermission("nyeblock.useInventory")) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(true);
		}
	}
	//Handle when a player uses an item
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event) {
		Player ply = event.getPlayer();
		
		if (event.getAction().toString().matches("RIGHT_CLICK_AIR|RIGHT_CLICK_BLOCK")) {
			ItemMeta item = ply.getItemInHand().getItemMeta();
			
			if (item != null) {				
				if (item.getLocalizedName().equals("hub_menu")) {
					HubMenu hubMenu = new HubMenu();
					
					hubMenu.openMenu(ply);
				} else if (item.getLocalizedName().equals("return_to_hub")) {
					event.setCancelled(true);
					PlayerData playerData = playersData.get(ply.getName());
					
					//Remove player from game
					if (playerData.getRealm() == Realm.KITPVP) {						
						for(KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
							if (game.isInServer(ply)) {
								game.playerLeave(ply,true,true);
							}
						}
					} else if (playerData.getRealm() == Realm.STEPSPLEEF) {						
						for(StepSpleef game : mainInstance.getGameInstance().getStepSpleefGames()) {
							if (game.isInServer(ply)) {
								game.playerLeave(ply,true,true);
							}
						}
					}
				} else if (item.getLocalizedName().equals("kit_selector")) {
					for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
						if (game.isInServer(ply)) {
							if (game.isInGraceBounds(ply)) {							
								KitSelector selectKit = new KitSelector();
								
								selectKit.openMenu(ply, mainInstance);
							}
						}
					}
				} else if (item.getLocalizedName().equals("kitpvp_wizard_fireball")) {
					for (KitPvP game : mainInstance.getGameInstance().getKitPvpGames()) {
						if (game.isInServer(ply)) {
							if (!game.isInGraceBounds(ply)) {							
								Location spawnAt = ply.getEyeLocation().toVector().add(ply.getEyeLocation().getDirection().multiply(3)).toLocation(ply.getWorld());
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
