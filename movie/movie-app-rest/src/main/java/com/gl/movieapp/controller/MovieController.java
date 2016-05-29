package com.gl.movieapp.controller;

import com.gl.movieapp.model.Movie;
import com.gl.movieapp.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by gavin on 16-5-28.
 */
@CrossOrigin(
        value={"http://localhost:8888"},
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        })
@RestController
public class MovieController {
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @RequestMapping(value = "/movies", method= RequestMethod.GET, produces = "application/json")
    public List<Movie> getMovies(){
        log.debug("getMovies");
        return movieService.retrieveMovies();
    }

    @RequestMapping(value="/movies/{id}", method=RequestMethod.GET, produces = "application/json")
    public Movie getMovie(@PathVariable Long id){
        log.debug("getMovie");
        return movieService.retrieveMovie(id);
    }

    @RequestMapping(value="/movies", method=RequestMethod.POST, consumes="application/json")
    public void addMovie(@RequestBody Movie movie){
        log.debug("addMovie: {}", movie);
        movieService.addMovie(movie);
    }

    @RequestMapping(value="/movies", method=RequestMethod.PUT, consumes="application/json")
    public void updateMovie(@RequestBody Movie movie){
        log.debug("updateMovie:{}", movie);
        movieService.modifyMovie(movie);
    }

    @RequestMapping(value="/movies/{id}", method=RequestMethod.DELETE)
    public void deleteMovie(@PathVariable Long id){
        log.debug("deleteMovie:{}", id);
        movieService.removeMovie(id);
    }
}
