package com.gl.avs.model;

public class ArticleContent extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2782096346063913083L;
	
	private Article article;
	
	private String content;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}
