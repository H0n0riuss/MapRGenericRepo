package io.github.honoriuss.crud.repositories;

import io.github.honoriuss.crud.entities.AEntity;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;

/**
 * @author H0n0riuss
 */
public class CRUDMapRRepository<T extends AEntity> {
    private Class<T> tClass;
    private String dbPath;
    private final Connection connection;

    public CRUDMapRRepository(Connection connection, String dbPath){
        this.connection = connection;
        this.dbPath = dbPath;
        tClass = getGenericClass();
    }

    public T createEntry(T entry){
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericClass(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
