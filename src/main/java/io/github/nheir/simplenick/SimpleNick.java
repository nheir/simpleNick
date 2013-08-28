package io.github.nheir.simplenick;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleNick extends JavaPlugin {
    private FileConfiguration nickConf = null;
    private File nickFile = null;
    
	@Override
    public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new SimpleNickListener(this), this);
        Player ps[] = this.getServer().getOnlinePlayers();
        for(Player p : ps)
        {
        	String s = this.getCustomConfig().getString(p.getName());
        	if(s != null)
        	{
        		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', s)+ChatColor.RESET);
        	}
        }
	}
 
    @Override
    public void onDisable() {
    	this.saveCustomConfig();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equals("nick")) {
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			Player player = (Player) sender;
    			if(args.length == 0)
    				player.setDisplayName(player.getName());
    			else 
    			{
    				StringBuilder builder = new StringBuilder();
    				int a = args.length;
    				for(String s : args) {
    					a--;
    				    builder.append(s);
    				    if(a > 0)
    				    	builder.append(' ');
    				}
    				String nick = builder.toString();
    				String nickcolor = ChatColor.translateAlternateColorCodes('&', nick)+ChatColor.RESET;
    				if(ChatColor.stripColor(nickcolor).length() > 20)
    					sender.sendMessage("The nickname is too long >20");
    				if(ChatColor.stripColor(nickcolor).length() < 3)
    					sender.sendMessage("The nickname is too short >3");
    				else
    				{
    					player.setDisplayName(nickcolor);
    					this.getCustomConfig().set(player.getName(), nick);
    				}
    			}
    		}
    		return true;
    	}
    	return false;
    }
    
    public void reloadCustomConfig() {
        if (nickFile == null) {
        	nickFile = new File(getDataFolder(), "nick.yml");
        }
        nickConf = YamlConfiguration.loadConfiguration(nickFile);
    }

    public FileConfiguration getCustomConfig() {
        if (nickConf == null) {
            this.reloadCustomConfig();
        }
        return nickConf;
    }

    public void saveCustomConfig() {
        if (nickConf == null || nickFile == null) {
        	return;
        }
        try {
            getCustomConfig().save(nickFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + nickFile, ex);
        }
    }

}
