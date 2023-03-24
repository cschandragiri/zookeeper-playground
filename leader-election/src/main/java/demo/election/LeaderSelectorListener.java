package demo.election;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.util.Random;

import static java.lang.System.getenv;

@Log
@NoArgsConstructor
public class LeaderSelectorListener extends LeaderSelectorListenerAdapter {
    @Override
    public void takeLeadership(final CuratorFramework client) throws Exception {
        log.info(getenv("INSTANCE_ID") + " is the leader now.");
        final Random random = new Random();
        log.info("Doing some random work");
        //Sleep for a random time between 1min-5mins
        Thread.sleep(random.nextInt(240000) + 60000);
        log.info(getenv("INSTANCE_ID") + " is giving up leadership.");
    }
}
