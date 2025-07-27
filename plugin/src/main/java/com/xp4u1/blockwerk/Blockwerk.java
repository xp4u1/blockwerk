package com.xp4u1.blockwerk;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.xp4u1.blockwerk.listener.LobbyHandler;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "blockwerk", name = "Blockwerk", version = BuildConstants.VERSION)
public class Blockwerk {
    @Inject
    private Logger logger;
    @Inject
    private ProxyServer server;
    @Inject
    @DataDirectory
    private Path dataDirectory;

    private BlockwerkConfig config;
    private KubernetesBridge kubernetesBridge;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        loadConfiguration();

        kubernetesBridge = new KubernetesBridge(logger, server, config, this::schedule);
        server.getScheduler().buildTask(this, kubernetesBridge::watchPods).schedule();

        if (config.lobby.enabled)
            server.getEventManager().register(this, new LobbyHandler(server, config));
    }

    /**
     * Load the configuration. If no configuration exists, it will copy the default
     * "blockwerk.toml" to the data directory.
     */
    public void loadConfiguration() {
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            logger.error("Unable to create config directory");
            throw new RuntimeException(e);
        }

        config = BlockwerkConfig.loadConfig(dataDirectory.resolve("blockwerk.toml"));
        logger.info(config.kubernetes.namespace);
    }

    /**
     * Start a task using Velocity's scheduler.
     *
     * @param task Function to execute
     */
    public void schedule(Runnable task) {
        server.getScheduler().buildTask(this, task).schedule();
    }
}
