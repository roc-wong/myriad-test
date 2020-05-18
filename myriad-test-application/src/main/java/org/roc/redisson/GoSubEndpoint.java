package org.roc.redisson;

import com.zts.archietecture.gosub.api.GoSubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author roc
 * @since 2019/11/14 16:50
 */
public class GoSubEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoSubEndpoint.class);

    @Autowired
    private GoSubService goSubService;

    @RequestMapping("/JZJY/110")
    public String testConnectionTimeout(String url, Long sleep) {
        LOGGER.info("url={}, sleep={}", url, sleep);
        return goSubService.randomSleep(url, sleep);
    }
}
