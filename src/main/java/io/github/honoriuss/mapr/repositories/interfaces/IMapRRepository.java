package io.github.honoriuss.mapr.repositories.interfaces;

import io.github.honoriuss.mapr.repositories.entities.AEntity;

public interface IMapRRepository<T extends AEntity> {
    void insert();
    T insert(T newEntry);
}
