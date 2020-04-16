package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Kits.KitBase;
import nyeblock.Core.ServerCoreTest.Kits.KitPvP_Archer;
import nyeblock.Core.ServerCoreTest.Kits.KitPvP_Assassin;
import nyeblock.Core.ServerCoreTest.Kits.KitPvP_Brawler;
import nyeblock.Core.ServerCoreTest.Kits.KitPvP_Knight;
import nyeblock.Core.ServerCoreTest.Kits.KitPvP_Wizard;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_Archer;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_Armorer;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_Butcher;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_Enchanter;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_EnderPearler;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_Fireballer;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_Fisherman;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_Knight;
import nyeblock.Core.ServerCoreTest.Kits.SkyWars_StoneMason;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class KitHandling {
	private Main mainInstance;
	private HashMap<Realm,ArrayList<KitBase>> kits;
	
	public KitHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
		
		kits = new HashMap<Realm,ArrayList<KitBase>>() {{
			put(Realm.SKYWARS,new ArrayList<KitBase>() {{
				add(new SkyWars_StoneMason());
				add(new SkyWars_Butcher());
				add(new SkyWars_Knight());
				add(new SkyWars_Archer());
				add(new SkyWars_Fireballer());
				add(new SkyWars_EnderPearler());
				add(new SkyWars_Armorer());
				add(new SkyWars_Enchanter());
				add(new SkyWars_Fisherman());
			}});
			put(Realm.KITPVP,new ArrayList<KitBase>() {{
				add(new KitPvP_Knight());
				add(new KitPvP_Brawler());
				add(new KitPvP_Archer());
				add(new KitPvP_Wizard());
				add(new KitPvP_Assassin());
			}});
		}};
	}
	
	//
	// GETTERS
	//
	
	public KitBase getKitFromUniqueId(Realm realm, String uniqueId) {
		KitBase returnKit = null;
		
		for (KitBase kit : kits.get(realm)) {
			if (kit.isShopKit() && kit.getUniqueId().equals(uniqueId)) {
				returnKit = kit;
				break;
			}
		}
		return returnKit;
	}
	public ArrayList<KitBase> getRealmKits(Realm realm, boolean includeDefaults, boolean includeShopItems) {
		ArrayList<KitBase> returnKits = new ArrayList<>();
		
		for (KitBase kit : kits.get(realm)) {
			if (kit.isShopKit()) {
				if (includeShopItems) {
					returnKits.add(kit);
				}
			} else {
				if (includeDefaults) {
					returnKits.add(kit);
				}
			}
		}
		return returnKits;
	}
	
	//
	// SETTERS
	//
	
	public void setKit(Player player, Realm realm, String name) {
		for (KitBase kit : kits.get(realm)) {
			if (kit.getName().equals(name)) {
				kit.setItems(mainInstance,player);
			}
		}
	}
}
