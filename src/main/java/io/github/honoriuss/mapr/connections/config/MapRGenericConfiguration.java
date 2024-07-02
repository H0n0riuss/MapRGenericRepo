package io.github.honoriuss.mapr.connections.config;

import io.github.honoriuss.mapr.connections.factories.MapRGenericFactory;
import io.github.honoriuss.mapr.connections.interfaces.IOjaiConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author H0n0riuss
 */
@Configuration
public class MapRGenericConfiguration {
    private final Logger logger = LoggerFactory.getLogger(MapRGenericConfiguration.class);

    @ConditionalOnMissingBean(IOjaiConnector.class)
    public IOjaiConnector getOjaiConnector() {
        var connector = MapRGenericFactory.getOjaiConnector();
        logger.info("Using Ojai Connector: {}", connector.getClass().getName());
        return connector;
    }
}
