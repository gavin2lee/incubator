package com.lachesis.core.persistence;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author Paul Xu.
 * @since 1.0
 * 
 * @param <K>
 * @param <T>
 */
public interface ICrudGenericDAO<K extends Serializable, T> {

	/**
	 * @param record
	 */
	void insert(T record);

	/**
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(T record);

	/**
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(T record);

	/**
	 * 
	 * @param record
	 * @param primaryKeys
	 */
	void massUpdateWithSession(@Param("record") T record,
			@Param("primaryKeys") List<K> primaryKeys);

	/**
	 * @param id
	 * @return
	 */
	T selectByPrimaryKey(K primaryKey);

	/**
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(K primaryKey);

	/**
	 * 
	 * @param value
	 * @return
	 */
	int insertAndReturnKey(T value);

	/**
	 * 
	 * @param keys
	 */
	@SuppressWarnings("rawtypes")
	void removeKeysWithSession(List keys);

}
