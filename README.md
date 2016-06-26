AustraliaCrafting
=================

Built for Spigot 1.8+ with Java 6+

This plugin turns all crafting recipes upside down

![Example](images/example.png)

Commands
--------

`/flipcrafts`

When ran flips all the recipes (or flips them back). Saves value to 
config file to persist across reloads/restarts. 

Requires `uhc.flipcrafts` permission (defaults to OP)

Configuration
-------------

```yaml
flipped: true
```

The default state to start the server in. Will be updated via the 
`flipcrafts` command