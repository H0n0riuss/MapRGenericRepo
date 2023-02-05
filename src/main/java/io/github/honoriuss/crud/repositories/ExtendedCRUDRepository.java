package io.github.honoriuss.crud.repositories;

import io.github.honoriuss.crud.entities.AEntity;

import java.sql.Connection;
import java.util.List;

/**
 * @author H0n0riuss
 */
public class ExtendedCRUDRepository<T extends AEntity> extends CRUDMapRRepository<T> {
    public ExtendedCRUDRepository(Connection connection, String dbPath) {
        super(connection, dbPath);
    }

    public List<T> createMany(List<T> newEntries) {
        return null;
    }

    public List<T> readMany(int amount, int offset, String order) {
        return null;
    }

    public List<T> updateMany(List<T> updatedEntries) {
        return null;
    }

    public List<T> deleteMany(List<String> deleteKeys) {
        return null;
    }

    public T findEntry(String contains, String column) {
        return null;
    }

    public List<T> findMany(String contains, String column, int amount, int offset, String order) {
        return null;
    }

}
