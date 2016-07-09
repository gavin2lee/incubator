package com.gl.avs.model;

import java.util.Date;
import java.util.List;

public class Article extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4714452472455709785L;
	/**
	 * 
	 */
	private String title;
	private User author;
	private Date publishAt;
	
	private List<ArticleGroup> groups;
	private List<KeyWord> keyWords;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public List<ArticleGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ArticleGroup> groups) {
		this.groups = groups;
	}

	public List<KeyWord> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(List<KeyWord> keyWords) {
		this.keyWords = keyWords;
	}
	
}
