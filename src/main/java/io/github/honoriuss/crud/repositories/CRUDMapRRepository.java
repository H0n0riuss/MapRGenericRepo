package io.github.honoriuss.crud.repositories;

import io.github.honoriuss.crud.entities.AEntity;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;

/**
 * @author H0n0riuss
 */
public class CRUDMapRRepository<T extends AEntity> {
    private Class<T> tClass;
    private final String dbPath;
    private final Connection connection;

    public CRUDMapRRepository(Connection connection, String dbPath){
        this.connection = connection;
        this.dbPath = dbPath;

        tClass = getGenericClass();
    }

    public T createEntry(T newEntry){
        return null;
    }

    public T readEntry(String key){
        return null;
    }

    public T updateEntry(T updatedEntry){
        return null;
    }

    public T deleteEntry(String key){
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericClass(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
