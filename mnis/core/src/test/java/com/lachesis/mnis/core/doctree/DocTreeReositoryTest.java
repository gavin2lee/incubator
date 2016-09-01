package com.lachesis.mnis.core.doctree;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.doctree.entity.ComNavTree;
import com.lachesis.mnis.core.doctree.repository.DocTreeRepository;


public class DocTreeReositoryTest extends SpringTest{

	@Autowired
	DocTreeRepository docTreeRepository;
	
	@Test
	public void testGetComNavTrees(){
		List<ComNavTree> trees = docTreeRepository.getComNavTrees();
		Assert.assertNotNull(trees);
	}
}
