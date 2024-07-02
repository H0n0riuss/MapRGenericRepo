package io.github.honoriuss.mapr.connections.factories;

import io.github.honoriuss.mapr.connections.connectors.OjaiConnector;
import io.github.honoriuss.mapr.connections.interfaces.IOjaiConnector;

/**
 * @author H0n0riuss
 */
public abstract class MapRGenericFactory {
    public static IOjaiConnector getOjaiConnector() {
        return new OjaiConnector();
    }
}
