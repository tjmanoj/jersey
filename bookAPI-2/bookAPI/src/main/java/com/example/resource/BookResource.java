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
                jsonObject.put("authorId", rs.getInt("authorId"));

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
                jsonObject.put("authorId",rs.getInt("authorId"));

                return Response.ok(jsonObject.toString()).build();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Path("/by-author/{authorId}")
    @GET
    public Response getBooksByAuthor(@PathParam("authorId") int authorId){
        try{
            Connection connection = DatabaseConnector.getDbConnection();

            String query = "select a.authorId, a.authorName, a.authorAge,b.bookId, b.bookName from author a inner join book b on b.authorId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,authorId);

            ResultSet rs = statement.executeQuery();

            JSONObject resultObject = new JSONObject();

            if(rs.next()){
                resultObject.put("authorId",rs.getInt("authorId"));
                resultObject.put("authorName",rs.getString("authorName"));
                resultObject.put("authorAge",rs.getInt("authorAge"));

                JSONArray jsonArray = new JSONArray();
                do {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("bookId", rs.getInt("bookId"));
                    jsonObject.put("bookName", rs.getString("bookName"));

                    jsonArray.put(jsonObject);
                }
                while(rs.next());

                resultObject.put("Books",jsonArray);
            }


            return Response.ok(resultObject.toString()).build();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    public String addBook(Book book){
        try {
            Connection connection = DatabaseConnector.getDbConnection();

            String insertQuery = "Insert into book(bookName,authorId) values(?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,book.getBookName());
            preparedStatement.setInt(2,book.getAuthorId());

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
            int authorId = book.getAuthorId();

            String updateBookQuery = "UPDATE book SET bookName = COALESCE(?, bookName), authorId = COALESCE(?, authorId) WHERE bookId = ?";

            PreparedStatement statement = connection.prepareStatement(updateBookQuery);
            statement.setString(1, bookName);

            if (authorId != 0) {
                statement.setInt(2, authorId);
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }

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
