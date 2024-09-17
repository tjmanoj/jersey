package com.example.resource;

import com.example.database.DatabaseConnector;
import com.example.model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    @GET
    public Response getAllBooks(){
        try{
            Connection connection = DatabaseConnector.getDbConnection();

            String query = "select * from book";
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);

            JSONArray jsonArray = new JSONArray();

            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bookId", rs.getInt("bookId"));
                jsonObject.put("bookName", rs.getString("bookName"));
                jsonObject.put("bookAuthor", rs.getString("bookAuthor"));

                jsonArray.put(jsonObject);
            }

            return Response.ok(jsonArray.toString()).build();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/{bookId}")
    public Response getBookById(@PathParam("bookId") int bookId){
        try{
            Connection connection = DatabaseConnector.getDbConnection();

            String query = "select * from book where bookId=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,bookId);

            ResultSet rs = statement.executeQuery();

            if(!rs.next()){
                return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
            }

            else{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bookId",bookId);
                jsonObject.put("bookName",rs.getString("bookName"));
                jsonObject.put("bookAuthor",rs.getString("bookAuthor"));

                return Response.ok(jsonObject.toString()).build();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    public String addBook(Book book){
        try {
            Connection connection = DatabaseConnector.getDbConnection();

            String insertQuery = "Insert into book(bookName,bookAuthor) values(?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,book.getBookName());
            preparedStatement.setString(2,book.getBookAuthor());

            preparedStatement.executeUpdate();

            return "book added successfully";


        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Path("/{bookId}")
    @PUT
    public String updateBook(@PathParam("bookId") int bookId, Book book){
        try {
            Connection connection = DatabaseConnector.getDbConnection();
            String bookName = book.getBookName();
            String bookAuthor = book.getBookAuthor();

            String updateBookQuery = "UPDATE book SET bookName = COALESCE(?, bookName), bookAuthor = COALESCE(?, bookAuthor) WHERE bookId = ?";

            PreparedStatement statement = connection.prepareStatement(updateBookQuery);
            statement.setString(1, bookName);
            statement.setString(2, bookAuthor);
            statement.setInt(3, bookId);

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                return "Book updated successfully";
            }
            else {
                return "Book not found";
            }


        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Path("/{bookId}")
    @DELETE
    public String deleteBook(@PathParam("bookId") int bookId){
        try {
            String deleteQuery = "delete from book where bookId=?";

            Connection connection = DatabaseConnector.getDbConnection();
            PreparedStatement statement = connection.prepareStatement(deleteQuery);

            statement.setInt(1,bookId);

            int affectedRows = statement.executeUpdate();

            if(affectedRows > 0){
                return "Book Deleted successfully";
            }
            else{
                return "Book not found";
            }


        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
