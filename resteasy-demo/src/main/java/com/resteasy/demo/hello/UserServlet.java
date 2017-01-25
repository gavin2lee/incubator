package com.resteasy.demo.hello;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("/users")  
public class UserServlet {  
  
    private Map<Integer, UserType> userStore =   
        new ConcurrentHashMap<Integer, UserType>();  
    private AtomicInteger idGenerator = new AtomicInteger();  
  
    @POST  
    @Consumes("application/xml")  
    public Response createUser(UserType user) {  
        user.setId(idGenerator.incrementAndGet());  
        userStore.put(user.getId(), user);  
        System.out.println(user.getName() + " created: "   
            + user.getId());  
        return Response.created(URI.create("/users/"   
            + user.getId())).build();  
    }  
  
    @GET  
    @Path("{id}")  
    @Produces("application/xml")  
    public UserType getUser(@PathParam("id") int id) {  
        UserType u = userStore.get(id);  
        if (u == null) {  
            throw new WebApplicationException(  
                Response.Status.NOT_FOUND);  
        }  
        return u;  
    }  
  
}