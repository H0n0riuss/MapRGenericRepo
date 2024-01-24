package io.github.honoriuss.mapr.generator.interfaces;

import io.github.honoriuss.mapr.repositories.entities.AEntity;

public interface IMapRRepository<T extends AEntity> {
    void delete(String _id);
}
