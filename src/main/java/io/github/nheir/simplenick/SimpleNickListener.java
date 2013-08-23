package io.github.nheir.simplenick;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;;
 
public class SimpleNickListener implements Listener {
 
        public SimpleNick plugin;
       
        public SimpleNickListener(SimpleNick instance) {
                plugin = instance;
        }
 
    	@EventHandler
    	public void onPlayerLogin(PlayerLoginEvent event) {
    		Player p = event.getPlayer();
    		String s = plugin.getConfig().getString(p.getPlayerListName());
	    	if(s != null)
	    		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', s)+ChatColor.RESET);
    	}
}