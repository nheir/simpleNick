package io.github.nheir.simplenick;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
 
public class SimpleNickListener implements Listener {
 
        public SimpleNick plugin;
       
        public SimpleNickListener(SimpleNick instance) {
                plugin = instance;
        }
 
    	@EventHandler
    	public void onPlayerLogin(PlayerJoinEvent event) {
    		Player p = event.getPlayer();
    		plugin.newPlayer(p);
    	}
}