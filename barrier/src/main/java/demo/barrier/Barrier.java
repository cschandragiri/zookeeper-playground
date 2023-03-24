package demo.barrier;

import lombok.extern.java.Log;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.RetryNTimes;

import java.util.Random;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.System.getenv;

@Log
public class Barrier {

    private static final int SLEEP_BETWEEN_RETRIES_MS = 100;
    private static final int MAX_RETRIES = 3;
    private static final RetryPolicy retryPolicy = new RetryNTimes(MAX_RETRIES, SLEEP_BETWEEN_RETRIES_MS);
    private static final int NUMBER_OF_SERVICE_INSTANCES = parseInt(getenv("NUMBER_OF_SERVICE_INSTANCES"));
    //"zoo1:2181,zoo2:2181,zoo3:2181"
    private static final String ZK_CONNECTION_STRING = getenv("ZK_CONNECTION_STRING");
    // Permanent zkNode
    private static final String groupName = "barrier_group";
    private static final String zkNodePath = "/%s/barrier/nodeCount-%s";

    public static void main(String[] args) throws Exception {
        log.info("Initializing Instance-" + getenv("INSTANCE_ID"));
        final Random random = new Random();
        final CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_CONNECTION_STRING, retryPolicy);
        final DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client,
                format(zkNodePath, groupName, NUMBER_OF_SERVICE_INSTANCES), NUMBER_OF_SERVICE_INSTANCES);
        client.start();
        while (true) {
            //do some work like video encoding of partial file
            Thread.sleep(random.nextInt(5000) + 1000);
            //once work is completed, wait for other instances to wrap up their work. See DistributedDoubleBarrier
            waitForOtherClients(client, barrier);
            //since all instances have finished their respective tasks we had been waiting, safe to proceed
        }
    }

    public static void waitForOtherClients(final CuratorFramework client,
                                           final DistributedDoubleBarrier barrier) throws Exception {
        // block until connection is established
        client.blockUntilConnected();
        log.info("Node:" + getenv("INSTANCE_ID") + " waiting on the barrier.");
        barrier.enter();
        log.info("Node:" + getenv("INSTANCE_ID") + " leaving the barrier.");
        barrier.leave();
    }
}
