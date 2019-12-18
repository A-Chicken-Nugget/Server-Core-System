
package nyeblock.Core.ServerCoreTest.Commands;

import java.util.List;

import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;

public abstract class CommandBase {
	private Main mainInstance;
	public void execute(Player ply, String[] args) {};
	public List<String> autoCompletes(Player player, String[] args) { return null; };
	
	public CommandBase(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	public CommandBase getInstance() {
		return this;
	}
	public Main getMainInstance() {
		return mainInstance;
	}
}
