package net.nocpiun.ranktitles;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nocpiun.ranktitles.command.RankCommand;
import net.nocpiun.ranktitles.command.TitlesCommand;
import net.nocpiun.ranktitles.title.Title;
import net.nocpiun.ranktitles.utils.Utils;
import space.nocp.configx.api.ConfigManager;
import space.nocp.configx.api.Configuration;

import java.util.ArrayList;

public class RankTitlesPlugin {
    private static final String CONFIG_ID = "rank-titles";
    private static final ConfigType defaultConfig = new ConfigType(new ArrayList<>());

    private Configuration<ConfigType> config;
    private MinecraftServer server;

    public RankTitlesPlugin() {
        config = ConfigManager.get().register(CONFIG_ID, defaultConfig, ConfigType.class);

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

    public MinecraftServer getServer() {
        return server;
    }

    public ArrayList<Title> getTitles() {
        ConfigType configObj = config.get();
        if(configObj == null || !Utils.checkProperty(configObj, "titles")) {
            config.set(new ConfigType(new ArrayList<>()));
            config.save();
        }

        // Get the config again to ensure not getting null
        return config.get().titles;
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
        final ConfigType configObj = config.get();
        final Title title = new Title(id, name);

        configObj.titles.add(title);
        config.set(configObj);
        config.save();

        return title;
    }

    public void removeTitle(String id) {
        final ConfigType configObj = config.get();
        configObj.titles.removeIf(title -> title.getId().equals(id));
        config.set(configObj);
        config.save();
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
        final ConfigType configObj = config.get();

        for(Title title : configObj.titles) {
            if(title.getId().equals(titleId)) {
                title.addPlayer(player);
            }
        }

        config.set(configObj);
        config.save();
    }

    public void depriveTitleFromPlayer(ServerPlayerEntity player, String titleId) {
        final ConfigType configObj = config.get();

        for(Title title : configObj.titles) {
            if(title.getId().equals(titleId)) {
                title.removePlayer(player);
            }
        }

        config.set(configObj);
        config.save();
    }
}
