package com.gl.movieapp.model;

import javax.persistence.*;

/**
 * Created by gavin on 16-5-28.
 */
@Entity
public class Movie {
    private Long id;
    private String title;
    private String director;
    private String releaseYear;
    private String genre;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
