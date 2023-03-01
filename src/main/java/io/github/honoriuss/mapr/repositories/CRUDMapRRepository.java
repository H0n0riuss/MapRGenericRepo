package io.github.honoriuss.mapr.repositories;

import io.github.honoriuss.mapr.connections.OjaiConnector;
import io.github.honoriuss.mapr.repositories.entities.AEntity;
import org.ojai.Document;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

/**
 * @author H0n0riuss
 */
public class CRUDMapRRepository<T extends AEntity> {
    final Class<T> tClass;
    final String dbPath;
    final Connection connection;

    public CRUDMapRRepository(String dbPath, Class<T> tClass) {
        this(OjaiConnector.getConnection(), dbPath, tClass);
    }

    public CRUDMapRRepository(Connection connection, String dbPath, Class<T> tClass) {
        this.connection = connection;
        this.dbPath = dbPath;
        this.tClass = tClass;
    }

    /**
     * creates a new Entry
     *
     * @param newEntry _id will be overwritten
     * @return the new Entry with _id from the db
     */
    public T createEntry(@NotNull T newEntry) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            newEntry.set_id(UUID.randomUUID().toString());
            Document newDoc = connection.newDocument(newEntry);
            store.insert(newDoc);
            return newEntry;
        }
    }

    /**
     * reads a entry with the given id
     *
     * @param _id the id in the table
     * @return the entry associated with the id or nothing
     */
    public T readEntry(@NotNull String _id) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            return store.findById(_id).toJavaBean(tClass);
        }
    }

    /**
     * updates the entry
     *
     * @param updatedEntry the new Entry Object
     * @return the updated Entry with the same _id
     */
    public T updateEntry(@NotNull T updatedEntry) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            Document doc = connection.newDocument(updatedEntry);
            store.insert(doc);
            return updatedEntry;
        }
    }

    /**
     * deletes entry, does nothing if key not exists
     *
     * @param _id the id in the table to delete
     */
    public void deleteEntry(@NotNull String _id) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            store.delete(_id);
        }
    }
}
