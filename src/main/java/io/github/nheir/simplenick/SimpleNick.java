package io.github.nheir.simplenick;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
	// nick.yml
    private FileConfiguration nickConf = null;
    private File nickFile = null;
    // Contains <nickname uncolor, player name> for connected players and players that set a nickname in nick.yml
    private HashMap<String, String> nick2Player = new HashMap<String, String>();
    
	@Override
    public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new SimpleNickListener(this), this);
        Player ps[] = this.getServer().getOnlinePlayers();
        // Import nick.yml
        for(String p : this.getCustomConfig().getKeys(false))
        {
        	String s = this.getCustomConfig().getString(p);
        	s = ChatColor.translateAlternateColorCodes('&', s);
        	s = ChatColor.stripColor(s);
        	nick2Player.put(s, p);
        }
        // Set connected player nicknames
        for(Player p : ps)
        {
        	String s = this.getCustomConfig().getString(p.getName());
        	if(s != null)
        	{
        		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', s)+ChatColor.RESET);
        	}
        	else // By default, the nickname of a player is his name
        		nick2Player.put(p.getName(), p.getName());
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
    				removeNick(player);
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
    
    public boolean newPlayer(Player p) {
    	if(nick2Player.containsValue(p.getName()))
    	{
    		String s = this.getCustomConfig().getString(p.getName());
	    	if(s != null)
	    	{
	    		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', s)+ChatColor.RESET);
	    		p.sendMessage(ChatColor.GREEN + "Your nickname is : " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', s));
	    	}
	    	return true;
    	}
    	if(nick2Player.containsKey(p.getName()))
    	{
    		Player pb = this.getServer().getPlayer((nick2Player.get(p.getName())));
    		if(pb != null)
    		{
    			pb.sendMessage("Your nickname is disabled because you are using the name of a player");
    			removeNick(pb);
    		}
    		else
    			removeNick(p.getName());
	    	return true;
    	}
    	nick2Player.put(p.getName(), p.getName());
    	return true;
    }
    
    public boolean changeNick(Player p, String nick) {
    	String nickcolor = ChatColor.translateAlternateColorCodes('&', nick)+ChatColor.RESET;
		String nickuncolor = ChatColor.stripColor(nickcolor);
		if(nickuncolor.length() < 3)
			p.sendMessage("The nickname is too short <3");
		else if(nickuncolor.length() > 20)
			p.sendMessage("The nickname is too long >20");
		else if(!nickuncolor.equals(p.getName()) && nick2Player.containsValue(nickuncolor))
			p.sendMessage("This nickname is the name of a player!");
		else if(nick2Player.containsKey(nickuncolor) && !nick2Player.get(nickuncolor).equals(p.getName()))
			p.sendMessage("This nickname is already used!");
		else {
			p.setDisplayName(nickcolor);
			p.sendMessage(ChatColor.GREEN + "Your nickname is : " + ChatColor.RESET + nickcolor);
			String prevnick = ChatColor.stripColor(p.getDisplayName());
			nick2Player.remove(prevnick);
			nick2Player.put(nickuncolor, p.getName());
			p.setDisplayName(nickcolor);
			this.getCustomConfig().set(p.getName(), nick);
			return true;
		}
    	return false;
    }
    
    public boolean removeNick(Player p) {
    	if(!p.getName().equals(p.getDisplayName()))
    		return false;
    	String nickuncolor = ChatColor.stripColor(p.getDisplayName());
    	p.setDisplayName(p.getName());
    	removeNick(nickuncolor);
    	nick2Player.put(p.getName(), p.getName());
    	return true;
    }
    
    public boolean removeNick(String nickuncolor) {
    	String name = nick2Player.get(nickuncolor);
    	nick2Player.remove(nickuncolor);
    	this.getCustomConfig().set(name,null);
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
