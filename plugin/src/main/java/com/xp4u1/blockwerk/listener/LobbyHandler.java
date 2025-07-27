package com.xp4u1.blockwerk.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.xp4u1.blockwerk.BlockwerkConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;

import java.util.Comparator;

public class LobbyHandler {
    private final Logger logger;
    private final ProxyServer server;
    private final BlockwerkConfig config;

    public LobbyHandler(ProxyServer server, Logger logger, BlockwerkConfig config) {
        this.server = server;
        this.logger = logger;
        this.config = config;
    }

    @Subscribe
    public void onJoin(PlayerChooseInitialServerEvent event) {
        try {
            RegisteredServer lobbyServer = server.getAllServers()
                    .stream()
                    .filter(registeredServer ->
                            registeredServer.getServerInfo().getName().matches(
                                    config.lobby.serverSelector
                            )
                    )
                    .min(Comparator.comparingInt(server ->
                            server.getPlayersConnected().size()
                    ))
                    .orElseThrow(IllegalStateException::new);

            event.setInitialServer(lobbyServer);
        } catch (IllegalStateException e) {
            event.getPlayer().disconnect(Component.text(
                    "No available lobby servers at the moment.\n"
                            + "Please try again later.",
                    NamedTextColor.RED
            ));
            logger.error("No lobby server available!");
            logger.error("Please check your Kubernetes pods and the selector in the configuration");
        }
    }
}
