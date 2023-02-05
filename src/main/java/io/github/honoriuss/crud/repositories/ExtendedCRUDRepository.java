package io.github.honoriuss.crud.repositories;

import io.github.honoriuss.crud.entities.AEntity;

import java.sql.Connection;

/**
 * @author H0n0riuss
 */
public class ExtendedCRUDRepository<T extends AEntity> extends CRUDMapRRepository<T> {
    public ExtendedCRUDRepository(Connection connection, String dbPath) {
        super(connection, dbPath);
    }
}
