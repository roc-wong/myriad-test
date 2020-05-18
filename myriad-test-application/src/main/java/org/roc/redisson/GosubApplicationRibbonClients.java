package org.roc.redisson;

import com.ribbon.ExternalBusApplicationRibbonConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@RibbonClient(name = "gosub-application", configuration = ExternalBusApplicationRibbonConfig.class)
public class GosubApplicationRibbonClients {}
