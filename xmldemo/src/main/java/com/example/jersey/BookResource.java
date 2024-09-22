package com.example.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/books")
public class BookResource {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void addBook(Book book) {
        // Print book details to the console
        System.out.println("Received Book: " + book);
    }

    @Path("/requests")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void addRequest(Request request) {
        // Print request details to the console
        System.out.println("Received Request: " + request);
    }
}
