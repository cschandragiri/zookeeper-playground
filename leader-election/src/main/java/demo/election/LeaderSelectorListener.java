package demo.election;

import static java.lang.System.getenv;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.util.Random;

@Log
@NoArgsConstructor
public class LeaderSelectorListener extends LeaderSelectorListenerAdapter {
    @Override
    public void takeLeadership(final CuratorFramework client) throws Exception {
        log.info(getenv("INSTANCE_ID") + " is the leader now.");
        final Random random = new Random();
        log.info("Doing some random work");
        //Sleep for a random time between 10-1min
        Thread.sleep(random.nextInt(50000) + 10000);
        log.info(getenv("INSTANCE_ID") + " is giving up leadership.");
    }
}
