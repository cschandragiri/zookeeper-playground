package demo.election;

import static java.lang.System.getenv;

import lombok.extern.java.Log;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.retry.RetryNTimes;

import java.util.Random;

@Log
public class LeaderElection {

    private static final int SLEEP_BETWEEN_RETRIES_MS = 100;
    private static final int MAX_RETRIES = 3;
    private static final RetryPolicy retryPolicy = new RetryNTimes(MAX_RETRIES, SLEEP_BETWEEN_RETRIES_MS);
    private static final String ZK_CONNECTION_STRING = getenv("ZK_CONNECTION_STRING");
    private static final String zkNodePath = "/leader-election";
    private static final String INSTANCE_ID = getenv("INSTANCE_ID");


    public static void main(String[] args) throws InterruptedException {
        log.info("Initializing Instance-" + INSTANCE_ID);
        final Random random = new Random();
        final CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_CONNECTION_STRING, retryPolicy);
        client.start();
        final LeaderSelectorListener listener = new LeaderSelectorListener();
        final LeaderSelector selector = new LeaderSelector(client, zkNodePath, listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
        while (true) {
            Thread.sleep(1000);
        }
    }

}