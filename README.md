# Rank Titles

[![Author](https://img.shields.io/badge/Author-NriotHrreion-red.svg "Author")](https://github.com/NriotHrreion)
[![LICENSE](https://img.shields.io/badge/License-CC0_1.0-green.svg "LICENSE")](./LICENSE)
[![Stars](https://img.shields.io/github/stars/tetoe-mc/rank-titles.svg?label=Stars&style=flat)](https://github.com/tetoe-mc/rank-titles/stargazers)
[![Github Workflow Status](https://img.shields.io/github/actions/workflow/status/tetoe-mc/rank-titles/build.yml)](https://github.com/tetoe-mc/rank-titles/actions/workflows/build.yml)

## Description

This is a Minecraft Fabric plugin that manages and displays the titles of players before their names.

Because the plugin is a server-side one, thus players don't have to install anything at the client-side to use the features provided by the plugin.

## Download

Check [Releases](https://github.com/tetoe-mc/rank-titles/releases) for the jar file.

## Usage

Drag the downloaded jar file into the mod folder, launch your server, and the plugin is ready to work.

### Commands

You can use the commands below to manage the titles.

- `/rank create <title-id> <title-name>`: Create a title and specify an id for it.
- `/rank remove <title-id>`: Remove a specified title
- `/rank list`: List all created titles
- `/titles give <player> <title-id>`: Give a title to a player
- `/titles deprive <player> <title-id>`: Deprive a title from a player

**Note: When creating a title, you can use `&` to change the text color of the name, but you need to add `""` around the name, or the command won't be executed.**

**e.g.**: `/rank create vip VIP` `/rank create vip_plus "&e&lVIP+"`

## Build from source

1. Download the source-code

```cmd
git clone https://github.com/tetoe-mc/rank-titles.git
cd rank-titles
```

2. Create `secrets.gradle` file in the project folder as the following:

```gradle
// Remember to edit the contents inside the quotes
project.ext.username = "<Your Github Username>"
project.ext.access_token = "<Your Github Token>"
```

3. Run the build task

```cmd
./gradlew build
```

Then you can get the jar file in the build folder.

## LICENSE

[CC0 1.0](./LICENSE)
