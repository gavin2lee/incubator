package com.lachesis.mnis.core.doctree;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.DocTreeService;
import com.lachesis.mnis.core.doctree.entity.ComNavTree;
import com.lachesis.mnis.core.doctree.repository.DocTreeRepository;

@Service("docTreeService")
public class DocTreeServiceImpl implements DocTreeService {

	@Autowired
	private DocTreeRepository docTreeRepository;
	@Override
	public List<ComNavTree> getComNavTrees() {
		return docTreeRepository.getComNavTrees();
	}

}
