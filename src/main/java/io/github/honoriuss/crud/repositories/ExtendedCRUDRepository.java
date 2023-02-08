package io.github.honoriuss.crud.repositories;

import io.github.honoriuss.crud.entities.AEntity;
import org.ojai.Document;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.ojai.store.Query;
import org.ojai.store.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author H0n0riuss
 */
public class ExtendedCRUDRepository<T extends AEntity> extends CRUDMapRRepository<T> {
    public ExtendedCRUDRepository(Connection connection, String dbPath) {
        super(connection, dbPath);
    }

    public List<T> createMany(List<T> newEntries) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            for (T newEntry : newEntries) {
                newEntry.set_id(UUID.randomUUID().toString());
                Document doc = connection.newDocument(newEntry);
                store.insert(doc);
            }
            return newEntries;
        }
    }

    public List<T> readMany(int limit, int offset, String orderBy, String order) {
        List<T> resList = new ArrayList<>(limit);
        try (DocumentStore store = connection.getStore(dbPath)) {
            Query query = connection.newQuery()
                    .offset(offset)
                    .limit(limit)
                    .orderBy(orderBy, order)
                    .build();
            store.find(query);
            //TODO
        }
        return resList;
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
