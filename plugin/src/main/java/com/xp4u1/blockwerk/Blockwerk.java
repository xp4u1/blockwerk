package com.xp4u1.blockwerk;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(id = "blockwerk", name = "Blockwerk", version = BuildConstants.VERSION)
public class Blockwerk {
    @Inject
    private Logger logger;
    @Inject
    private ProxyServer server;

    private KubernetesBridge kubernetesBridge;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        kubernetesBridge = new KubernetesBridge(logger, server);
        server.getScheduler().buildTask(this, kubernetesBridge::watchPods).schedule();
    }

    @Subscribe
    public void onJoin(PlayerChooseInitialServerEvent event) {
        // todo: lobby selection
        event.setInitialServer(server.getAllServers().stream().findFirst().get());
    }
}
