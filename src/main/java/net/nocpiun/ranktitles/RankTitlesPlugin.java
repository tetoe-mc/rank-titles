package net.nocpiun.ranktitles;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nocpiun.ranktitles.command.RankCommand;
import net.nocpiun.ranktitles.command.TitlesCommand;
import net.nocpiun.ranktitles.title.Title;
import space.nocp.configx.api.ConfigManager;

import java.util.ArrayList;

public class RankTitlesPlugin {
    private static final ConfigType defaultConfig = new ConfigType(new ArrayList<>());

    private MinecraftServer server;

    public RankTitlesPlugin() {
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStop);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            new RankCommand(dispatcher, this);
            new TitlesCommand(dispatcher, this);
        });
    }

    private void onServerStart(MinecraftServer server) {
        this.server = server;
    }

    private void onServerStop(MinecraftServer server) {
        this.server = null;
    }

    private ConfigType getConfig() {
        return ConfigManager.get()
                .getOrCreateConfig("rank-titles", defaultConfig, ConfigType.class, Title.getAdaptedGson())
                .getObject();
    }

    private void saveConfig(ConfigType config) {
        ConfigManager.get()
                .getOrCreateConfig("rank-titles", defaultConfig, ConfigType.class, Title.getAdaptedGson())
                .save(config, Title.getAdaptedGson());
    }

    public MinecraftServer getServer() {
        return server;
    }

    public ArrayList<Title> getTitles() {
        return getConfig().titles;
    }

    public Title getTitle(String id) {
        for(Title title : getTitles()) {
            if(title.getId().equals(id)) {
                return title;
            }
        }
        return null;
    }

    public Title createTitle(String id, String name) {
        final ConfigType config = getConfig();
        final Title title = new Title(id, name);

        config.titles.add(title);
        saveConfig(config);

        return title;
    }

    public void removeTitle(String id) {
        final ConfigType config = getConfig();
        config.titles.removeIf(title -> title.getId().equals(id));
        saveConfig(config);
    }

    public boolean isTitleExisted(String id) {
        ArrayList<Title> titles = getTitles();
        for(Title title : titles) {
            if(title.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void giveTitleToPlayer(ServerPlayerEntity player, String titleId) {
        final ConfigType config = getConfig();

        for(Title title : config.titles) {
            if(title.getId().equals(titleId)) {
                title.addPlayer(player);
            }
        }

        saveConfig(config);
    }

    public void depriveTitleFromPlayer(ServerPlayerEntity player, String titleId) {
        final ConfigType config = getConfig();

        for(Title title : config.titles) {
            if(title.getId().equals(titleId)) {
                title.removePlayer(player);
            }
        }

        saveConfig(config);
    }
}
