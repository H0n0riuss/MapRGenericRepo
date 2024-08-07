package io.github.honoriuss.mapr.repositories;

import io.github.honoriuss.mapr.connections.interfaces.IOjaiConnector;
import io.github.honoriuss.mapr.repositories.entities.AEntity;
import org.ojai.Document;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;

import java.util.UUID;

/**
 * @author H0n0riuss
 */
public class CRUDMapRRepository<T extends AEntity> {
    final Class<T> tClass;
    final String dbPath;
    final Connection connection;

    public CRUDMapRRepository(IOjaiConnector connection, String dbPath, Class<T> tClass) {
        this.connection = connection.getConnection();
        this.dbPath = dbPath;
        this.tClass = tClass;
    }

    /**
     * creates a new Entry
     *
     * @param newEntry _id will be overwritten
     * @return the new Entry with _id from the db
     */
    public T createEntry(T newEntry) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            newEntry.set_id(UUID.randomUUID().toString());
            Document newDoc = connection.newDocument(newEntry);
            store.insert(newDoc);
            return newEntry;
        }
    }

    /**
     * reads an entry with the given id
     *
     * @param _id the id in the table
     * @return the entry associated with the id or nothing
     */
    public T readEntry(String _id) {
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
    public T updateEntry(T updatedEntry) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            Document doc = connection.newDocument(updatedEntry);
            store.replace(doc);
            return updatedEntry;
        }
    }

    /**
     * deletes entry, does nothing if key not exists
     *
     * @param _id the id in the table to delete
     */
    public void deleteEntry(String _id) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            store.delete(_id);
        }
    }
}
