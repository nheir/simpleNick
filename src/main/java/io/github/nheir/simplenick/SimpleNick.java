package io.github.nheir.simplenick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleNick extends JavaPlugin {
	@Override
    public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new SimpleNickListener(this), this);
        Player ps[] = this.getServer().getOnlinePlayers();
        for(Player p : ps)
        {
        	String s = this.getConfig().getString(p.getPlayerListName());
        	if(s != null)
        	{
        		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', s)+ChatColor.RESET);
        	}
        }
	}
 
    @Override
    public void onDisable() {
    	this.saveConfig();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equals("nick")) {
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			Player player = (Player) sender;
    			if(args.length == 0)
    				player.setDisplayName(player.getPlayerListName());
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
    				else
    				{
    					player.setDisplayName(nickcolor);
    					this.getConfig().set(player.getPlayerListName(), nick);
    				}
    			}
    		}
    		return true;
    	}
    	return false;
    }


}
