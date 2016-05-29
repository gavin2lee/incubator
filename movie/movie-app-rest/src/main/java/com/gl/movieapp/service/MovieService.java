package com.gl.movieapp.service;

import com.gl.movieapp.model.Movie;
import com.gl.movieapp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gavin on 16-5-28.
 */
@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository repo;

    public List<Movie> retrieveMovies(){
        return repo.findAll();
    }

    public Movie retrieveMovie(Long id){
        return repo.findOne(id);
    }

    public void addMovie(Movie movie){
        repo.save(movie);
    }

    public void modifyMovie(Movie movie){
        Long id = movie.getId();
        if(repo.exists(id)){
            repo.saveAndFlush(movie);
        }
    }

    public void removeMovie(Long id){
        repo.delete(id);
    }
}
