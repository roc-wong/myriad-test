package org.roc.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@RestController
public class RedissonEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonEndPoint.class);

    @Autowired private RedissonClient redissonClient;

    @RequestMapping("/redisson/lock")
    public String testConnectionTimeout(Long sleep) {
        String lockName = "roc.wong";
        RLock lock = redissonClient.getLock(lockName);
        boolean locked = lock.tryLock();
        try {
            if (locked) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Object> list = new ArrayList();
                int i = 0;
                while (true) {
                    list.add(new OMObject());
                    System.out.println("loop: " + i++);
                }
            }
        } finally {
            if (locked) {
                LOGGER.info("最终释放了锁");
                lock.unlock();
            }
        }
        return "success";
    }

    @RequestMapping("/redisson/print")
    public String print(Long sleep, Integer times) {
        for (int i = 0; i < times; i++) {
            try {
                LOGGER.info("times={}", times);
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return times.toString();
    }

    public static class OMObject {

        private byte[] OM_OBJECT;

        public OMObject() {
            this.OM_OBJECT = new byte[1024 * 1024];
        }
    }
}
