package demo.service;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import static java.lang.System.getenv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Random;

@Log
public class ServiceDiscovery {
    private static final int SLEEP_BETWEEN_RETRIES_MS = 100;
    private static final int MAX_RETRIES = 3;
    private static final RetryPolicy retryPolicy =
            new RetryNTimes(MAX_RETRIES, SLEEP_BETWEEN_RETRIES_MS);
    private static final String ZK_CONNECTION_STRING = getenv("ZK_CONNECTION_STRING");
    private static final String INSTANCE_ID = getenv("INSTANCE_ID");
    private static final String SERVICE_NAME = "discovery-service";
    private static final String SERVICE_PATH = "/services/" + SERVICE_NAME;

    @Data
    @AllArgsConstructor
    private static class InstancePayload {
        private String payload;
    }

    public static void main(String[] args) throws Exception {
        log.info("Initializing Instance-" + INSTANCE_ID);
        final CuratorFramework client =
                CuratorFrameworkFactory.newClient(ZK_CONNECTION_STRING, retryPolicy);
        client.start();

        // Create a ServiceDiscovery instance using JSON serializer
        final JsonInstanceSerializer<InstancePayload> serializer =
                new JsonInstanceSerializer<>(InstancePayload.class);
        final org.apache.curator.x.discovery.ServiceDiscovery serviceDiscovery =
                ServiceDiscoveryBuilder.builder(InstancePayload.class)
                        .client(client)
                        .basePath(SERVICE_PATH)
                        .serializer(serializer)
                        .build();
        // Start the ServiceDiscovery instance
        serviceDiscovery.start();

        // Create a CuratorCache to listen on ServiceInstance changes
        final CuratorCache curatorCache = CuratorCache.builder(client, SERVICE_PATH).build();
        curatorCache.start();
        curatorCache
                .listenable()
                .addListener(
                        (type, oldData, data) -> {
                            switch (type) {
                                case NODE_CREATED:
                                    log.info(
                                            format(
                                                    "[%s] Node registered: %s",
                                                    getenv("INSTANCE_ID"), data.getPath()));
                                    break;
                                case NODE_CHANGED:
                                    log.info(
                                            format(
                                                    "[%s] Node changed: %s",
                                                    getenv("INSTANCE_ID"), data.getPath()));
                                    break;
                            }
                        });

        while (true) {
            simulateRegisterAndDeregister(serviceDiscovery);
        }
    }

    private static void simulateRegisterAndDeregister(
            final org.apache.curator.x.discovery.ServiceDiscovery serviceDiscovery)
            throws Exception {

        final Random random = new Random();
        final ServiceInstance<InstancePayload> instance = getInstance();
        serviceDiscovery.registerService(instance);
        log.info("Instance:" + INSTANCE_ID + " updating metadata");
        final ServiceInstance<InstancePayload> updatedInstance =
                ServiceInstance.<InstancePayload>builder()
                        .id(instance.getId())
                        .name(instance.getName())
                        .payload(
                                new InstancePayload(
                                        format(
                                                "InstanceId:%s :%s",
                                                INSTANCE_ID, randomAlphanumeric(10))))
                        .build();
        serviceDiscovery.updateService(updatedInstance);
        // Sleep for a random time between 10-1min
        Thread.sleep(random.nextInt(50000) + 10000);
        serviceDiscovery.unregisterService(instance);
        // Sleep for a random time between 10-1min
        Thread.sleep(random.nextInt(50000) + 10000);
    }

    private static ServiceInstance<InstancePayload> getInstance() throws Exception {
        final ServiceInstanceBuilder<InstancePayload> instanceBuilder = ServiceInstance.builder();
        return instanceBuilder
                .name("Instances")
                .payload(
                        new InstancePayload(
                                format("InstanceId:%s :%s", INSTANCE_ID, randomAlphanumeric(10))))
                .build();
    }
}
