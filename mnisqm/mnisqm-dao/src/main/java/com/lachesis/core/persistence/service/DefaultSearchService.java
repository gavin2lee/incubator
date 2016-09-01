package com.lachesis.core.persistence.service;

import java.io.Serializable;

public abstract class DefaultSearchService<K extends Serializable, T> extends DefaultCrudService<K, T>
		implements IDefaultService<K, T> {

	public abstract ISearchableDAO getSearchMapper();

}
