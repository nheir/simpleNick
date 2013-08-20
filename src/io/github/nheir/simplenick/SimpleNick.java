package io.github.nheir.simplenick;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleNick extends JavaPlugin {
	@Override
    public void onEnable(){
		getLogger().info("onEnable has been invoked!");
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new SimpleNickListener(this), this);
	}
 
    @Override
    public void onDisable() {
    	getLogger().info("onDisable has been invoked!");
    }
}
