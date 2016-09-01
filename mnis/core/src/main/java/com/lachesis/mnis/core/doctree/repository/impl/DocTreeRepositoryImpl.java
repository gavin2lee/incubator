package com.lachesis.mnis.core.doctree.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.doctree.entity.ComNavTree;
import com.lachesis.mnis.core.doctree.repository.DocTreeRepository;
import com.lachesis.mnis.core.mybatis.mapper.DocTreeMapper;

@Repository
public class DocTreeRepositoryImpl implements DocTreeRepository {

	@Autowired
	private DocTreeMapper docTreeMapper;
	
	@Override
	public List<ComNavTree> getComNavTrees() {
		return docTreeMapper.getComNavTrees();
	}

}
