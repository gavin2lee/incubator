package com.lachesis.core.persistence.service;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 * @param <K>
 * @param <T>
 * @param <S>
 */
public interface IDefaultService<K extends Serializable, T>
		extends ICrudService<K, T>, ISearchableService<T> {

}
