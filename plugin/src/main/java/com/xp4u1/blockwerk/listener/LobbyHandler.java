package com.xp4u1.blockwerk.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.xp4u1.blockwerk.BlockwerkConfig;

public class LobbyHandler {
    private final ProxyServer server;
    private final BlockwerkConfig config;

    public LobbyHandler(ProxyServer server, BlockwerkConfig config) {
        this.server = server;
        this.config = config;
    }

    @Subscribe
    public void onJoin(PlayerChooseInitialServerEvent event) {
        RegisteredServer lobbyServer = server.getAllServers()
                .stream()
                .filter(registeredServer ->
                        registeredServer.getServerInfo().getName().matches(
                                config.lobby.serverSelector
                        )
                )
                .findFirst()
                .get();

        event.setInitialServer(lobbyServer);
    }
}
