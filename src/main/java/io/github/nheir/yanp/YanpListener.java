package io.github.nheir.yanp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
 
public class YanpListener implements Listener {
 
        public Yanp plugin;
       
        public YanpListener(Yanp instance) {
                plugin = instance;
        }
 
    	@EventHandler
    	public void onPlayerLogin(PlayerJoinEvent event) {
    		Player p = event.getPlayer();
    		plugin.newPlayer(p);
    	}
}