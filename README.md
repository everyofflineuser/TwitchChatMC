# 🎮 TwitchChat for Minecraft Fabric

[![Fabric](https://img.shields.io/badge/Modloader-Fabric-1976d2?logo=fabric)](https://fabricmc.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21+-62b47a?logo=minecraft)](https://www.minecraft.net/)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL3.0-green.svg)](https://github.com/everyofflineuser/TwitchChatMC/blob/dev/LICENSE)
[![Twitch4J](https://img.shields.io/badge/Dependency-Twitch4J-9146ff?logo=twitch\&logoColor=white)](https://modrinth.com/mod/twitch4j)

A Fabric mod that brings your Twitch chat directly into Minecraft. Messages are displayed in the in-game chat with a **Twitch icon prefix** (via built-in resource pack) and colored usernames.

![TwitchChat in action](https://i.postimg.cc/PqBDW8vx/2025-09-17-18-35-41.png) *TwitchChat in action*

![TwitchChat with Streamotes](https://i.postimg.cc/9F8rPqvh/2025-09-17-23-46-32.png) *TwitchChat with Streamotes*

---

## ✨ Features

* 📺 Display Twitch chat directly in Minecraft
* 🟣 Twitch **icon prefix**
* 🎨 Usernames use Twitch’s assigned color (default white) [WIP]
* ⚙️ Easy setup with `/twitchchat` command
* 📁 Configurable via JSON file
* 🔄 Auto connect/disconnect when entering/leaving a world or connect/disconnect a server

---

## 📦 Installation

### Requirements

* [Fabric Loader](https://fabricmc.net/use/)
* [Fabric API](https://modrinth.com/mod/fabric-api)
* [Twitch4J (Required)](https://modrinth.com/mod/twitch4j)
* [Streamotes (Recommended)](https://modrinth.com/plugin/streamotes)

### Steps

1. Install **Fabric Loader 1.21.8**
2. Download and place in `mods/`:

    * Fabric API
    * Twitch4J
    * Streamotes (optional)
    * TwitchChat
3. Launch Minecraft

---

## 🎮 Usage

### Command

```
/twitchchat <username>
```

Example:

```
/twitchchat minecraft
```

### Config file

Edit `config/twitchchat.json`:

```json
{
  "twitchChannel": "minecraft"
}
```

---

## 🖼 Message Format

```
[TwitchIcon] Username: message
```

* Prefix is a **Twitch icon** from the built-in resource pack
* Username uses Twitch color (or white by default)
* Message text is light gray

---

## ⚙️ Settings

* In-game commands
* `config/twitchchat.json`

---

## 🔧 Compatibility

* Minecraft **1.21+**
* Fabric Loader
* Twitch4J (required)
* Streamotes (recommended)
* Compatible with most mods that don’t override chat

---

## ❓ FAQ

**Q: Mod doesn’t work?**
A: Check Twitch4J is installed – it’s required.

**Q: Messages not showing?**
A: Verify the Twitch channel name and that the stream is live.

**Q: Can I change colors?**
A: Not yet. Planned for future updates.

---

## 🐛 Bug Reports

1. Ensure dependencies are installed
2. Check game logs
3. Report issues on \[GitHub Issues]

---

## 📜 License

Licensed under the **GPL-3.0**. See [LICENSE](https://github.com/everyofflineuser/TwitchChatMC/blob/dev/LICENSE).

---

## 🤝 Credits

* Fabric team – mod loader
* Twitch4J devs – Twitch API library
* Streamotes team – emote integration
* Minecraft modding community – support and ideas