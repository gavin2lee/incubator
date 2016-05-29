package com.gl.movieapp.repository;

import com.gl.movieapp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by gavin on 16-5-28.
 */
public interface MovieRepository extends JpaRepository<Movie, Long>{
}
