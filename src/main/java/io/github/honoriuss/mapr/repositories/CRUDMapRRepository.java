package io.github.honoriuss.mapr.repositories;

import io.github.honoriuss.mapr.entities.AEntity;
import org.ojai.Document;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

/**
 * @author H0n0riuss
 */
public class CRUDMapRRepository<T extends AEntity> {
    protected Class<T> tClass;
    protected final String dbPath;
    protected final Connection connection;

    public CRUDMapRRepository(Connection connection, String dbPath) {
        this.connection = connection;
        this.dbPath = dbPath;

        tClass = getGenericClass();
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

    @SuppressWarnings("unchecked")
    private Class<T> getGenericClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
