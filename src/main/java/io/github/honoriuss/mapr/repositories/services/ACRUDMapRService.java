package io.github.honoriuss.mapr.repositories.services;

import io.github.honoriuss.mapr.connections.interfaces.IOjaiConnector;
import io.github.honoriuss.mapr.repositories.entities.AEntity;
import io.github.honoriuss.mapr.repositories.CRUDMapRRepository;

public abstract class ACRUDMapRService<T extends AEntity> {
    private final CRUDMapRRepository<T> repository;

    public ACRUDMapRService(IOjaiConnector connection, String tablePath, Class<T> tClass) {
        repository = new CRUDMapRRepository<>(connection, tablePath, tClass);
    }

    /**
     * method to create a new Entry in the given tablePath, does a null check
     *
     * @param newEntry has to extend AEntity, _id will be overwritten
     * @return the new Entry with the db _id or null
     */
    public T createEntry(T newEntry) {
        if (newEntry == null)
            return null;
        return repository.createEntry(newEntry);
    }

    /**
     * reads a entry with the given id
     *
     * @param _id the id in the table
     * @return the entry associated with the id or nothing
     */
    public T readEntry(String _id) {
        return repository.readEntry(_id);
    }

    /**
     * updates the entry
     *
     * @param updatedEntry the new Entry Object
     * @return the updated Entry with the same _id
     */
    public T updateEntry(T updatedEntry) {
        if (updatedEntry == null)
            return null;
        return repository.updateEntry(updatedEntry);
    }

    /**
     * deletes entry, does nothing if key not exists
     *
     * @param _id the id in the table to delete
     */
    public void deleteEntry(String _id) {
        repository.deleteEntry(_id);
    }
}
