package com.dubious.itunes.statistics.store.mongodb.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dubious.itunes.statistics.store.CleanAndDeploy;

/**
 * Run {@link com.dubious.itunes.statistics.store.mongodb.MongoDbCleanAndDeploy} in a test context.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/dubious/itunes/test/itunes-test-context.xml" })
public class MongoDbCleanAndDeployTest {

    @Resource(name = "mongoDbCleanAndDeploy")
    private CleanAndDeploy cleanAndDeploy;

    /**
     * Run clean and deploy.
     */
    @Test
    public final void cleanAndDeploy() {
        cleanAndDeploy.cleanAndDeploy();
    }
}
