package io.github.honoriuss.mapr.repositories.interfaces;

import io.github.honoriuss.mapr.repositories.entities.AEntity;

public interface IMapRRepository<T extends AEntity> {
    T save(T newEntry);
    void delete(String _id);
    T read(String id);
    T update(T updatedEntry);
}
