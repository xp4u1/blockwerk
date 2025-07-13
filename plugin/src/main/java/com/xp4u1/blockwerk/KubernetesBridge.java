package com.xp4u1.blockwerk;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import okhttp3.Call;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class KubernetesBridge {
    private final Logger logger;
    private final ProxyServer server;
    private final CoreV1Api api;

    private static final String NAMESPACE = "blockwerk";
    private static final String LABEL_SELECTOR = "app=paper";

    public KubernetesBridge(Logger logger, ProxyServer server) {
        this.logger = logger;
        this.server = server;

        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
        } catch (IOException e) {
            logger.error("Unable to initialize Kubernetes client.");
            throw new RuntimeException(e);
        }

        api = new CoreV1Api();
    }

    /**
     * Start a watcher that registers/unregisters Paper servers based on cluster
     * events. Will automatically restart upon failure
     */
    public void watchPods() {
        try {
            Call call = api.listNamespacedPodCall(
                    NAMESPACE, null, null, null, null,
                    LABEL_SELECTOR, null, null, null,
                    null, true, null
            );
            Watch<V1Pod> watch = Watch.createWatch(
                    api.getApiClient(),
                    call,
                    new TypeToken<Watch.Response<V1Pod>>() {
                    }.getType()
            );

            for (Watch.Response<V1Pod> event : watch) {
                V1Pod pod = event.object;
                String name = pod.getMetadata().getName();

                switch (event.type) {
                    case "ADDED":
                    case "MODIFIED":
                        if (pod.getStatus().getPhase().equals("Running"))
                            tryRegisterPod(pod);
                        break;
                    case "DELETED":
                        unregisterServer(name);
                        break;
                }
            }
        } catch (Exception e) {
            logger.error("Kubernetes watch failed ({}). Restarting...", e.getMessage());
            watchPods();
        }
    }

    /**
     * Try to register a pod. Will do nothing if the Paper server is already
     * known to Velocity.
     *
     * @param pod Pod of the Paper server
     */
    public void tryRegisterPod(V1Pod pod) {
        String name = pod.getMetadata().getName();
        if (server.getServer(name).isPresent())
            return;

        registerServer(
                name,
                pod.getStatus().getPodIP(),
                25565
        );
    }

    /**
     * Register a new Paper server.
     *
     * @param name Name used to identify the server
     * @param ip   Adress of the Paper instance
     * @param port Port that the server listens on
     */
    public void registerServer(String name, String ip, int port) {
        ServerInfo serverInfo = new ServerInfo(
                name,
                new InetSocketAddress(ip, port)
        );

        server.registerServer(serverInfo);
        logger.info("Registered new server: {} ({}:{})", name, ip, port);
    }

    /**
     * Unregister a Paper instance by its name.
     *
     * @param name Name of the server
     */
    public void unregisterServer(String name) {
        server.unregisterServer(
                server.getServer(name).get().getServerInfo()
        );
        logger.info("Unregistered server: {}", name);
    }
}
