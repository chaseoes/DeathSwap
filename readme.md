# DeathSwap
A Bukkit plugin that automates the DeathSwap minigame created by SethBling.

* **Download**: [http://dev.bukkit.org/bukkit-plugins/deathswap/](http://dev.bukkit.org/bukkit-plugins/deathswap/)
* **Issue Tracker**: [https://github.com/chaseoes/DeathSwap/issues](https://github.com/chaseoes/DeathSwap/issues)
* **Development Builds**: [http://ci.chaseoes.com/job/DeathSwap/](http://ci.chaseoes.com/job/DeathSwap/)

## Commands
_The alias of /ds can be used for all commands._

* `/deathswap` General plugin information.
* `/deathswap join [map]` Joins the specified map. If no map is provided, a random one will be picked.
* `/deathswap leave` Leaves the current map.
* `/deathswap list [map]` Lists players currently in the specified map. If no map is provided, the one you are currently in will be used.
* `/deathswap duel [player]` Sends a request to duel a player in a private match. If no player is specified, a random one that has will be chosen.
* `/ds accept` Accepts a request to duel another player.
* `/ds enable [map]` Enables the specified map. If no map is specified, all maps will be enabled.
* `/ds disable [map]` Disables the specified map. If no map is specified, all maps will be disabled.
* `/ds create <map> <map type>` Creates a new map using the current WorldEdit selection. The map type must be either `public` or `private`.
* `/ds setmax <map> <#>` Sets the maximum number of players that can join a map.

## Permissions
* `deathswap.play` Gives general permissions to play DeathSwap and use the plugin.
* `deathswap.create` Gives permission to use all commands related to creating a map and setting up the plugin.

## Changelog
**v1.0**
* Initial release.
