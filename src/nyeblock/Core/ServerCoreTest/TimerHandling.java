package nyeblock.Core.ServerCoreTest;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

public class TimerHandling {
	private HashMap<String,BukkitTask> timers = new HashMap<String,BukkitTask>();
	private HashMap<String,Integer> timesRan = new HashMap<String,Integer>();
	
	public void createTimer(String name, double delay, int timesToRun, String methodToRun, boolean isSuperMethod, Object[] args, Object classInstance) {
		if (timers.get(name) == null) {
			timesRan.put(name, 0);
			BukkitTask task = Bukkit.getScheduler().runTaskTimer(Bukkit.getServer().getPluginManager().getPlugin("ServerCoreTest"), new Runnable() {
				@Override
				public void run() {
					int ran = timesRan.get(name);
					if (timesRan.get(name) != 0) {
						Class<?> params[] = new Class[(args != null ? args.length : 0)];
						
						if (args != null) {	
							for (int i = 0; i < args.length; i++) {
								if (args[i] instanceof Integer) {
									params[i] = Integer.TYPE;
								} else if (args[i] instanceof String) {
									params[i] = String.class;
								} else if (args[i] instanceof Player) {
									params[i] = Player.class;
								} else if (args[i] instanceof Boolean) {
									params[i] = Boolean.class; 
								} else if (args[i] instanceof Realm) {
									params[i] = Realm.class;
								} else if (args[i] instanceof GameBase) {
									params[i] = GameBase.class;
								}
							}
						}
						
						try {  
							if (isSuperMethod) {										
								classInstance.getClass().getSuperclass().getDeclaredMethod(methodToRun, (args != null ? params : null)).invoke(classInstance, args);
							} else {
								classInstance.getClass().getDeclaredMethod(methodToRun, (args != null ? params : null)).invoke(classInstance, args);
							}
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (timesToRun != 0 && ran+1 >= timesToRun) {
							Iterator<Map.Entry<String, BukkitTask>> timersItr = timers.entrySet().iterator();
							while(timersItr.hasNext())
							{
								Map.Entry<String, BukkitTask> entry = timersItr.next();
								if (entry.getKey().equals(name)) {
									entry.getValue().cancel();
									timersItr.remove();
								}
							}
							Iterator<Map.Entry<String, Integer>> timesRanItr = timesRan.entrySet().iterator();
							while(timesRanItr.hasNext())
							{
								Map.Entry<String, Integer> entry = timesRanItr.next();
								if (entry.getKey().equals(name)) {
									timesRanItr.remove();
								}
							}
						}
					}
					timesRan.put(name, ran+1);
				}
			}, 0, (int)(delay*20));
			timers.put(name, task);
		}
	}
	public void createTimer2(String name, double delay, int timesToRun, Runnable runnable) {
		if (timers.get(name) == null) {
			timesRan.put(name, 0);
			BukkitTask task = Bukkit.getScheduler().runTaskTimer(Bukkit.getServer().getPluginManager().getPlugin("ServerCoreTest"), new Runnable() {
				@Override
				public void run() {
					int ran = timesRan.get(name);
					if (timesRan.get(name) != 0) {
						runnable.run();
					}
					timesRan.put(name, ran+1);
				}
			}, 0, (int)(delay*20));
			timers.put(name, task);
		}
	}
	public void deleteTimer(String name) {
		Iterator<Map.Entry<String, BukkitTask>> timersItr = timers.entrySet().iterator();
		while(timersItr.hasNext())
		{
			Map.Entry<String, BukkitTask> entry = timersItr.next();
			if (entry.getKey().equals(name)) {
				entry.getValue().cancel();
				timersItr.remove();
			}
		}
		Iterator<Map.Entry<String, Integer>> timesRanItr = timesRan.entrySet().iterator();
		while(timesRanItr.hasNext())
		{
			Map.Entry<String, Integer> entry = timesRanItr.next();
			if (entry.getKey().equals(name)) {
				timesRanItr.remove();
			}
		}
	}
}
