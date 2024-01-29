package io.github.honoriuss.mapr.generator.interfaces;

import io.github.honoriuss.mapr.repositories.entities.AEntity;

public interface IMapRRepository<T extends AEntity> {
    T create(T entity);
    T read();
    T update(T entity);
    void delete(String _id);
}
