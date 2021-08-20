package user.uziza.MMACraft;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import user.uziza.MMACraftEvents.Movement;

public class MMACraft extends JavaPlugin implements Listener {
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Movement(), this);
	}
	
	public void onDisable() {
		
	}

}
