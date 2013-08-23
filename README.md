simpleNick
==========

SimpleNick is a simple bukkit plugin to customize the display name of a player.
There is one command:

`\nick [nickname]`

To set a nickname:

`\nick A wonderful nickname`

There is a limitation of 20 characters without counting color codes.
To delete a nickname:

`\nick`

There is not config file yet. Nicknames are registered in the file nick.yml

### Ideas

A command "Who has <nickname/player>?"

Deny duplicate nickname

Deny nicknames that equal a player name logged:
*  Deny when a player wants to set a nickname which is another logged player's name (colored or not)
*  Remove a player nickname when a player with the same name logs in
