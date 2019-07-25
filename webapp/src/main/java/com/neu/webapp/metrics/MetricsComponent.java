package com.neu.webapp.metrics;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NoOpStatsDClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsComponent {

    private final static Logger logger = LoggerFactory.getLogger(MetricsComponent.class);

    @Value("$(publish.metrics)")
    private boolean publishMetrics;

    @Value("$(publish.server.hostname)")
    private String publishServerHost;

    @Value("$(publish.server.port)")
    private int publishServerPort;

    @Bean
    public StatsDClient metricClient(){
        logger.info("publish metrics: "+publishMetrics);
        if(publishMetrics)
            return new NonBlockingStatsDClient("csye6225", publishServerHost,publishServerPort);

        return new NoOpStatsDClient();
    }

}
