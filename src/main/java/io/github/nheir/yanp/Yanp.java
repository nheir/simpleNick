package io.github.nheir.yanp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Yanp extends JavaPlugin implements TabCompleter {
	// nick.yml
    private FileConfiguration nickConf = null;
    private File nickFile = null;
    // Contains <nickname uncolor, player> for connected players and players that set a nickname in nick.yml
    private HashMap<String, OfflinePlayer > nick2Player = new HashMap<String, OfflinePlayer>();
    
	@Override
    public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new YanpListener(this), this);
        if( ! this.getCustomConfig().getKeys(false).contains("players"))
        {
        	for(OfflinePlayer p : this.getServer().getOfflinePlayers())
	        {
        		// Migration UUID
                String s = this.getCustomConfig().getString(p.getName());
	        	if(s != null)
	        	{
	        		getLogger().info("Migrate player: "+p.getName() + " (" + p.getUniqueId().toString()+")");
	        		this.getCustomConfig().set(p.getUniqueId().toString(), s);
		        	this.getCustomConfig().set(p.getName(), null);
	        	}
	        	// Import nick.yml
	        	s = this.getCustomConfig().getString(p.getUniqueId().toString());
	        	if(s != null)
	        	{
	        		if(p.isOnline())
	        			p.getPlayer().setDisplayName(ChatColor.translateAlternateColorCodes('&', s)+ChatColor.RESET);
	        		s = ChatColor.translateAlternateColorCodes('&', s);
		        	s = ChatColor.stripColor(s);
		        	nick2Player.put(s, p);
	        	}
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
    		} else if(sender.hasPermission("yanp.nick")){
    			Player player = (Player) sender;
    			if(args.length == 0)
    			{
    				removeNick(player);
    				player.sendMessage(ChatColor.GREEN + "Nickname removed");
    			}
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
    				changeNick(player, nick);
    			}
    		}
    		return true;
    	}
    	return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    	if (!command.getName().equalsIgnoreCase("nick")) {
            return null;
        }
    	ArrayList<String> L = new ArrayList<String>();
    	if(args.length == 0)
		{
    		L.addAll(nick2Player.keySet());
		}
		else 
		{
			StringBuilder builder = new StringBuilder();
			int size = 0;
			int a = args.length;
			for(String s : args) {
				a--;
			    builder.append(s);
			    if(a > 0) {
			    	builder.append(' ');
			    	size += 1 + s.length();
			    }
			    
			}
			String nick = builder.toString();
			for(String key : nick2Player.keySet())
			{
				if(key.startsWith(nick)) {
					L.add(key.substring(size));
				}
			}
		}
    	Collections.sort(L, String.CASE_INSENSITIVE_ORDER);
    	if(sender instanceof Player) {
    		Player p = (Player) sender;
    		L.remove(ChatColor.stripColor(p.getDisplayName()));
    	}
    	return L;
    }
    
    public boolean newPlayer(Player p) {
    	if(nick2Player.containsValue(p))
    	{
    		String s = this.getCustomConfig().getString(p.getUniqueId().toString());
	    	if(s != null)
	    	{
	    		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', s)+ChatColor.RESET);
	    		p.sendMessage(ChatColor.GREEN + "Your nickname is : " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', s));
	    	}
	    	return true;
    	}
    	if(nick2Player.containsKey(p.getName()))
    	{
    		OfflinePlayer pb = nick2Player.get(p.getName());
    		if(pb.isOnline())
    			pb.getPlayer().sendMessage("Your nickname is disabled because you are using the name of a player");
    		removeNick(p.getName());
    	}
    	nick2Player.put(p.getName(), p);
    	return true;
    }
    
    public boolean changeNick(Player p, String nick) {
    	String nickcolor = ChatColor.translateAlternateColorCodes('&', nick)+ChatColor.RESET;
		String nickuncolor = ChatColor.stripColor(nickcolor);
		if(nickuncolor.length() < 3)
			p.sendMessage("The nickname is too short <3");
		else if(nickuncolor.length() > 20)
			p.sendMessage("The nickname is too long >20");
		else if(nick2Player.containsKey(nickuncolor) && !nick2Player.get(nickuncolor).getName().equals(p.getName()))
			p.sendMessage("This nickname is used by "+nick2Player.get(nickuncolor).getName());
		else {
			String prevnick = ChatColor.stripColor(p.getDisplayName());
			p.setDisplayName(nickcolor);
			nick2Player.remove(prevnick);
			nick2Player.put(nickuncolor, p);
			this.getCustomConfig().set(p.getUniqueId().toString(), nick);
			
			p.setDisplayName(nickcolor);
			p.sendMessage(ChatColor.GREEN + "Your nickname is : " + ChatColor.RESET + nickcolor);
			return true;
		}
    	return false;
    }
    
    public boolean removeNick(Player p) {
    	if(p.getName().equals(p.getDisplayName()))
    		return false;
    	String nickuncolor = ChatColor.stripColor(p.getDisplayName());
    	p.setDisplayName(p.getName());
    	removeNick(nickuncolor);
    	nick2Player.put(p.getName(), p);
    	return true;
    }
    
    public boolean removeNick(String nickuncolor) {
    	OfflinePlayer p = nick2Player.get(nickuncolor);
    	nick2Player.remove(nickuncolor);
    	this.getCustomConfig().set(p.getUniqueId().toString(),null);
    	return true;
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
