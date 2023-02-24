package io.github.honoriuss.mapr.repositories;

import io.github.honoriuss.mapr.repositories.entities.AEntity;
import org.ojai.Document;
import org.ojai.store.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author H0n0riuss
 */
public class ExtendedCRUDMapRRepository<T extends AEntity> extends CRUDMapRRepository<T> {
    public ExtendedCRUDMapRRepository(Connection connection, String dbPath, Class<T> tClass) {
        super(connection, dbPath, tClass);
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
            var queryResult = store.find(query);
            queryResult.forEach(entry -> resList.add(entry.toJavaBean(tClass)));
        }
        return resList;
    }

    public List<T> updateMany(List<T> updatedEntries) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            updatedEntries.forEach(entry -> {
                var doc = connection.newDocument(entry);
                store.insertOrReplace(doc);
            });
        }
        return updatedEntries;
    }

    public void deleteMany(List<String> deleteKeys) {
        try (DocumentStore store = connection.getStore(dbPath)) {
            deleteKeys.forEach(store::delete);
        }
    }

    public List<T> findEntries(String contains, String column, int amount, int offset, String orderBy, String order) {
        var resList = new ArrayList<T>(amount);
        try (DocumentStore store = connection.getStore(dbPath)) {
            QueryCondition condition = connection.newCondition()
                    .like(column, contains)
                    .build();
            Query query = connection.newQuery()
                    .where(condition)
                    .orderBy(orderBy, order)
                    .offset(offset)
                    .limit(amount)
                    .build();
            var queryResult = store.find(query);
            queryResult.forEach(entry -> resList.add(entry.toJavaBean(tClass)));
        }
        return resList;
    }

    public List<T> findEntries(String contains, List<String> columns, int amount, int offset, String orderBy, String order) {
        var resList = new ArrayList<T>(amount);
        try (DocumentStore store = connection.getStore(dbPath)) {
            QueryCondition condition = connection.newCondition();
            for (var column : columns) {
                condition = condition.like(column, contains);
            }
            condition = condition.build();
            Query query = connection.newQuery()
                    .where(condition)
                    .orderBy(orderBy, order)
                    .offset(offset)
                    .limit(amount)
                    .build();
            var queryResult = store.find(query);
            queryResult.forEach(entry -> resList.add(entry.toJavaBean(tClass)));
        }
        return resList;
    }
}
