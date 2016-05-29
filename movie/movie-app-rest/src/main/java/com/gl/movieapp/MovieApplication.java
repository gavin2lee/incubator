package com.gl.movieapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by gavin on 16-5-28.
 */
@SpringBootApplication
public class MovieApplication {
    public static void main(String ... args){
//        System.setProperty("debug", "");

        SpringApplication.run(MovieApplication.class, args);
    }
}
