package com.lachesis.core.persistence.service;

import java.io.Serializable;
import java.util.List;

/**
 * @param <K>
 * @param <T>
 * @author Paul Xu.
 * @since 1.0
 */
public interface ICrudService<K extends Serializable, T> extends IService {

    /**
     * @param record
     * @param username
     * @return
     */
    
    Integer saveWithSession( T record, String username);

    /**
     * @param record
     * @param username
     * @return
     */
    
    Integer updateWithSession( T record, String username);

    /**
     * @param record
     * @param username
     * @return
     */
    
    Integer updateSelectiveWithSession( T record, String username);

    /**
     * @param record
     * @param primaryKeys
     * @param accountId
     */
    
    void massUpdateWithSession(T record, List<K> primaryKeys,  Integer accountId);

    /**
     * @param primaryKey
     * @param sAccountId
     * @return
     */
    
    T findByPrimaryKey(K primaryKey, Integer sAccountId);

    /**
     *
     * @param item
     * @param username
     * @param sAccountId
     */
    
    void removeWithSession(T item, String username, Integer sAccountId);

    /**
     *
     * @param items
     * @param username
     * @param sAccountId
     */
    
    void massRemoveWithSession(List<T> items, String username, Integer sAccountId);
}
