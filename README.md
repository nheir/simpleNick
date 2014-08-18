Yet Another Nickname Plugin
===========================

Yanp is a simple bukkit plugin to customize the display name of a player.
There is one command:

`\nick [nickname]`

To set a nickname:

`\nick A wonderful nickname`

There is a limitation of 3-20 characters without counting color codes.
To delete a nickname:

`\nick`

There is not config file yet. Nicknames are registered in the file nick.yml

### Permission

    yanp.nick:
    	description: Allow to set or delete nickname
    	default: true

### Version 0.9
This version is working but a player can set whatever he wants as nickname

### Version 1.x
Deny duplicate nickname

Deny nicknames that equal a player name logged:
* Deny when a player wants to set a nickname which is another logged player's name (colored or not)
* Remove a player nickname when a player with the same name logs in

Command "Who has `<nickname/player>`?"
`\nick <a used nickname>`

Tested with 2 players, any feedback is welcome :-)

### TODO

Control on other player nickname for op

### Source

Github repo: 
[nheir/simpleNick](https://github.com/nheir/simpleNick)

