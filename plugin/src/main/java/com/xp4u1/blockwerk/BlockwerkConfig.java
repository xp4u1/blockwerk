package com.xp4u1.blockwerk;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.net.URL;
import java.nio.file.Path;

public class BlockwerkConfig {
    public KubernetesConfig kubernetes = new KubernetesConfig();
    public LobbyConfig lobby = new LobbyConfig();

    public static class KubernetesConfig {
        public String namespace;
        public String labelSelector;
    }

    public static class LobbyConfig {
        public boolean enabled;
        public String serverSelector;
    }

    public static BlockwerkConfig loadConfig(Path path) {
        URL defaultConfigLocation = BlockwerkConfig.class.getClassLoader()
                .getResource("default-blockwerk.toml");

        try (final CommentedFileConfig config = CommentedFileConfig.builder(path)
                .defaultData(defaultConfigLocation)
                .autosave()
                .preserveInsertionOrder()
                .sync()
                .build()
        ) {
            config.load();
            BlockwerkConfig blockwerkConfig = new BlockwerkConfig();

            // Kubernetes
            blockwerkConfig.kubernetes.namespace = config.get("kubernetes.namespace");
            blockwerkConfig.kubernetes.labelSelector = config.get("kubernetes.label-selector");

            // Lobby
            blockwerkConfig.lobby.enabled = config.get("lobby.enabled");
            blockwerkConfig.lobby.serverSelector = config.get("lobby.server-selector");

            return blockwerkConfig;
        }
    }
}
