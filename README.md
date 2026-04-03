# LabyStatusBridge

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green)
![LabyMod](https://img.shields.io/badge/LabyMod-ServerAPI-blue)
![Java](https://img.shields.io/badge/Java-17+-red)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## Overview

**LabyStatusBridge** is a Spigot plugin that acts as a bridge between the LabyMod Server API and your own server logic.

It provides a clean, reusable API to:

* Set LabyMod gamemode display
* Control Discord Rich Presence (RPC)
* Handle per-world configurations
* Override status dynamically (e.g. minigames)
* Centralize all LabyMod-related logic

---

## Requirements

* Spigot/Paper **1.21.x**
* LabyMod Server API plugin installed (`server-bukkit`)

Your `plugin.yml` must include:

```yml
depend: [LabyStatusBridge]
```

---

## Getting the API

Use Bukkit's `ServicesManager`:

```java
RegisteredServiceProvider<LabyStatusBridge> provider =
        Bukkit.getServicesManager().getRegistration(LabyStatusBridge.class);

if (provider == null) {
    return;
}

LabyStatusBridge api = provider.getProvider();
```

---

## Core Interface

### `LabyBridgeAPI`

Main entry point for all functionality.

### Methods

#### `void updatePlayer(Player player)`

Updates the LabyMod status for a single player based on:

* temporary overrides (if present)
* otherwise config-based world settings

---

#### `void updateAll()`

Updates all online players.

---

#### `void updateMinigameJoin(Player player, String gameModeName, String discordText)`

Quick helper for minigames.

* sets a temporary status
* uses `with_start` Discord RPC
* immediately applies it

**Example:**

```java
api.updateMinigameJoin(player, "BedWars", "BedWars");
```

---

#### `void setTemporaryStatus(Player player, StatusUpdate update)`

Overrides the player's status.

* takes priority over config
* persists until cleared

---

#### `void clearTemporaryStatus(Player player)`

Removes override → falls back to config again.

---

#### `StatusUpdate resolveStatus(Player player)`

Returns the currently active status:

* temporary (if set)
* otherwise resolved from config

---

#### `boolean isLabyModPlayer(Player player)`

Checks if the player is using LabyMod.

---

## Status Model

### `StatusUpdate`

Represents a full LabyMod state.

#### Constructor

```java
new StatusUpdate(
    String gameMode,
    boolean discordEnabled,
    String discordMode,
    String discordText,
    long discordOffsetSeconds
);
```

---

### Factory Methods

#### `simple(...)`

```java
StatusUpdate.simple("CityBuild", "CityBuild");
```

---

#### `withStart(...)`

```java
StatusUpdate.withStart("BedWars", "BedWars");
```

---

#### `disabled(...)`

```java
StatusUpdate.disabled("Lobby");
```

---

## Discord RPC Modes

Supported modes:

| Mode         | Description        |
| ------------ | ------------------ |
| `simple`     | Just text          |
| `with_start` | Shows elapsed time |
| `with_end`   | Countdown          |
| `reset`      | Clears RPC         |

---

## Config System

### Structure

```yml
update:
  on-join: true
  on-world-change: true
  on-teleport: true
  on-respawn: true

  periodic-refresh:
    enabled: true
    ticks: 100

defaults:
  gamemode: "Lobby"

  discord-rpc:
    enabled: true
    mode: "simple"
    text: "Lobby"
    offset-seconds: 0

worlds:
  world:
    gamemode: "Survival"
    discord-rpc:
      enabled: true
      mode: "simple"
      text: "Survival"
      offset-seconds: 0
```

---

### Resolution Logic

1. Check **temporary override**
2. Check **world-specific config**
3. Fallback to **defaults**

---

## Example Usage

### Simple Update

```java
api.updatePlayer(player);
```

---

### Minigame Join

```java
api.updateMinigameJoin(player, "BedWars", "BedWars");
```

---

### Custom Status

```java
StatusUpdate update = new StatusUpdate(
    "SkyWars",
    true,
    "with_start",
    "SkyWars Match",
    0
);

api.setTemporaryStatus(player, update);
api.updatePlayer(player);
```

---

### Clear Override

```java
api.clearTemporaryStatus(player);
api.updatePlayer(player);
```

---

## Behavior Notes

* Only players using LabyMod receive updates
* Non-LabyMod players are ignored silently
* `null` gamemode removes the display
* `reset` RPC clears Discord presence

---

## Internal Flow

```
Player Event (Join / Teleport / etc.)
        ↓
LabyStatusBridge.updatePlayer(...)
        ↓
resolveStatus(...)
        ↓
applyStatus(...)
        ↓
LabyMod Server API
```

---

## Best Practices

* Use `updateMinigameJoin` for quick integrations
* Use `setTemporaryStatus` for advanced control
* Always call `updatePlayer` after changes
* Avoid calling API every tick (use built-in refresh)

---

## Plugin Integration

### plugin.yml (other plugin)

```yml
softdepend: [LabyStatusBridge]
```

or if required:

```yml
depend: [LabyStatusBridge]
```
