package io.github.honoriuss.mapr.repositories;

import io.github.honoriuss.mapr.entities.AEntity;
import org.ojai.Document;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;

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

    public T createEntry(T newEntry) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            newEntry.set_id(UUID.randomUUID().toString());
            Document newDoc = connection.newDocument(newEntry);
            store.insert(newDoc);
            return newEntry;
        }
    }

    public T readEntry(String _id) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            return store.findById(_id).toJavaBean(tClass);
        }
    }

    public T updateEntry(T updatedEntry) {
        try(DocumentStore store = connection.getStore(dbPath)){
            Document doc = connection.newDocument(updatedEntry);
            store.insert(doc);
            return updatedEntry;
        }
    }

    public void deleteEntry(String _id) {
        try(DocumentStore store = connection.getStore(dbPath)){
            store.delete(_id);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
