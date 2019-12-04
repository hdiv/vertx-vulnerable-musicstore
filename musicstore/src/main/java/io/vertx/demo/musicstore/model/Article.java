package io.vertx.demo.musicstore.model;

public class Article {

	private Integer id;

	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Article(final Integer id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
