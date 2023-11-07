package io.github.honoriuss.mapr.repositories;

import io.github.honoriuss.mapr.connections.OjaiConnector;
import io.github.honoriuss.mapr.repositories.entities.ADisableEntity;
import org.ojai.store.DocumentStore;

public class DisableCRUDMapRRepository<T extends ADisableEntity> extends CRUDMapRRepository<T> {
    public DisableCRUDMapRRepository(OjaiConnector connection, String dbPath, Class<T> tClass) {
        super(connection, dbPath, tClass);
    }

    public boolean disableEntry(String id) {
        return changeEntry(id, true);
    }

    public boolean enableEntry(String id) {
        return changeEntry(id, false);
    }

    private boolean changeEntry(String id, boolean disable){
        try (DocumentStore store = connection.getStore(dbPath)) {
            var entity = store.findById(id).toJavaBean(tClass);
            if (entity == null) {
                return false;
            }
            entity.disabled = disable;
            var doc = connection.newDocument(entity);
            store.insert(id, doc);

            return true;
        }
    }
}
