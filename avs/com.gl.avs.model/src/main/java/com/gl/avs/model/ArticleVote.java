package com.gl.avs.model;

import java.util.Date;

public class ArticleVote extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3135820321146303345L;
	private Article article;
	private User user;
	private String ip;
	private Date voteAt;
	
	private ArticleVoteType type;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getVoteAt() {
		return voteAt;
	}

	public void setVoteAt(Date voteAt) {
		this.voteAt = voteAt;
	}

	public ArticleVoteType getType() {
		return type;
	}

	public void setType(ArticleVoteType type) {
		this.type = type;
	}
	
	

}
