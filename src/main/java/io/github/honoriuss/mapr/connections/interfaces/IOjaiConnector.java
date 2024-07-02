package io.github.honoriuss.mapr.connections.interfaces;

import org.ojai.store.Connection;

public interface IOjaiConnector {
    Connection getConnection();
}
