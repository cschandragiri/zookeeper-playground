package demo.lock;

import lombok.extern.java.Log;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.Random;

import static java.lang.System.getenv;

@Log
public class ReentrantLock {

    private static final int SLEEP_BETWEEN_RETRIES_MS = 100;
    private static final int MAX_RETRIES = 3;
    private static final RetryPolicy retryPolicy = new RetryNTimes(MAX_RETRIES, SLEEP_BETWEEN_RETRIES_MS);

    private static final String ZK_CONNECTION_STRING = getenv("ZK_CONNECTION_STRING");
    // Permanent zkNode
    private static final String lockName = "sharedLock";
    private static final String zkNodePath = String.format("/reentrant-lock/%s", lockName);
    public static void main(String[] args) throws Exception {
        log.info("Initializing Instance-" + getenv("INSTANCE_ID"));
        final CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_CONNECTION_STRING, retryPolicy);
        final InterProcessLock interProcessLock = new InterProcessSemaphoreMutex(client, zkNodePath);
        client.start();
        while (true) {
            lockAndDoWork(client, interProcessLock);
        }
    }

    private static void lockAndDoWork(final CuratorFramework client, final InterProcessLock interProcessLock) throws InterruptedException {

        final Random random = new Random();
        // block until connection is established
        client.blockUntilConnected();

        log.info("Node:" + getenv("INSTANCE_ID") + " attempting to acquire lock.");
        try {
            interProcessLock.acquire();
        } catch (Exception e) {
            log.info("Node:" + getenv("INSTANCE_ID") + " got error while acquiring lock");
            throw new RuntimeException("Error while acquiring lock", e);
        }
        log.info("Node:" + getenv("INSTANCE_ID") + " got the shared lock.");
        //Do some work after acquiring lock
        Thread.sleep(random.nextInt(10000) + 1000);

        log.info("Node:" + getenv("INSTANCE_ID") + " releasing the lock.");
        try {
            interProcessLock.release();
        } catch (Exception e) {
            log.info("Node:" + getenv("INSTANCE_ID") + " got error while releasing lock");
            throw new RuntimeException("Error while releasing lock", e);
        }
    }
}