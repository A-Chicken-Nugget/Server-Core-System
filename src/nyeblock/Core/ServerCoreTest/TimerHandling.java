package nyeblock.Core.ServerCoreTest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
	
	public ArrayList<String> getTimers() {
		ArrayList<String> timerss = new ArrayList<>();
		
		for (Map.Entry<String,BukkitTask> entry : timers.entrySet()) {
			timerss.add(entry.getKey());
		}
		return timerss;
	}
	
	/**
	* Creates a timer that calls a method
	* @param name - unique name of the timer
	* @param delay - how often does the timer run (seconds)
	* @param timesToRun - how many times should the timer run
	* @param methodToRun - name of the method to run
	* @param isSuperMethod - is this a method defined in the super class
	* @param args - array of parameters to send into the method
	* @param classInstance - instance of the class that the method is in
	*/
	public void createMethodTimer(String name, double delay, int timesToRun, String methodToRun, boolean isSuperMethod, Object[] args, Object classInstance) {
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
	/**
	* Create a runnable timer
	* @param name - unique name of the timer
	* @param delay - how often does the timer run (seconds)
	* @param timesToRun - how many times should the timer run
	* @param runnable - the runnable to run
	*/
	public void createRunnableTimer(String name, double delay, int timesToRun, Runnable runnable) {
		if (timers.get(name) == null) {
			timesRan.put(name, 0);
			BukkitTask task = Bukkit.getScheduler().runTaskTimer(Bukkit.getServer().getPluginManager().getPlugin("ServerCoreTest"), new Runnable() {
				@Override
				public void run() {
					int ran = timesRan.get(name);
					if (timesRan.get(name) != 0) {
						runnable.run();
						
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
	/**
	* Check if a timer already exists
	* @param name - name of the timer
	* @return whether or not the timer already exists
	*/
	public boolean timerExists(String name) {
		return timers.get(name) != null;
	}
	/**
	* Delete a timer
	* @param name - name of the timer
	*/
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
