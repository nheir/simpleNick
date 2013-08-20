package io.github.nheir.simplenick;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
 
public class SimpleNickListener implements Listener {
 
        public SimpleNick plugin;
       
        public SimpleNickListener(SimpleNick instance) {
                plugin = instance;
        }
 
    	@EventHandler
    	public void onPlayerChat(AsyncPlayerChatEvent event) {
	    	if (event.isCancelled()) {
	    		return;
	    	}
	
	    	String message = "Pouet";
	    	event.setMessage(message);
	    	event.getRecipients().clear();
	    	List<Player> list = java.util.Arrays.asList(Bukkit.getServer().getOnlinePlayers());
	    	event.getRecipients().addAll(list);
    	}
}