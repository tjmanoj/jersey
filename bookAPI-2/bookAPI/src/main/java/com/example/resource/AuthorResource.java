package com.example.resource;

import com.example.database.DatabaseConnector;
import com.example.model.Author;
import com.example.model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    @GET
    public Response getAuthors(){
        try{
            Connection connection = DatabaseConnector.getDbConnection();

            String query = "select * from author";
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);

            JSONArray jsonArray = new JSONArray();

            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("authorId", rs.getInt("authorId"));
                jsonObject.put("authorName", rs.getString("authorName"));
                jsonObject.put("authorAge", rs.getInt("authorAge"));

                jsonArray.put(jsonObject);
            }

            return Response.ok(jsonArray.toString()).build();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/{authorId}")
    public Response getAuthorById(@PathParam("authorId") int authorId){
        try{
            Connection connection = DatabaseConnector.getDbConnection();

            String query = "select * from author where authorId=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,authorId);

            ResultSet rs = statement.executeQuery();

            if(!rs.next()){
                return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
            }

            else{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("authorId",authorId);
                jsonObject.put("authorName",rs.getString("authorName"));
                jsonObject.put("authorAge",rs.getInt("authorAge"));

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
    public String addAuthor(Author author){
        try {
            Connection connection = DatabaseConnector.getDbConnection();

            String insertQuery = "Insert into author(authorName,authorAge) values(?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,author.getAuthorName());
            preparedStatement.setInt(2,author.getAuthorAge());

            preparedStatement.executeUpdate();

            return "Author added successfully";

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Path("/{authorId}")
    @PUT
    public String updateAuthor(@PathParam("authorId") int authorId, Author author){
        try {
            Connection connection = DatabaseConnector.getDbConnection();
            String authorName = author.getAuthorName();
            int authorAge = author.getAuthorAge();

            String updateBookQuery = "UPDATE author SET authorName = COALESCE(?, authorName), authorAge = COALESCE(?, authorAge) WHERE authorId = ?";

            PreparedStatement statement = connection.prepareStatement(updateBookQuery);
            statement.setString(1, authorName);

            if (authorAge != 0) {
                statement.setInt(2, authorAge);
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }


            statement.setInt(3, authorId);

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                return "Author updated successfully";
            }
            else {
                return "Author not found";
            }


        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Path("/{authorId}")
    @DELETE
    public String deleteAuthor(@PathParam("authorId") int authorId){
        try {
            String deleteQuery = "delete from author where authorId=?";

            Connection connection = DatabaseConnector.getDbConnection();
            PreparedStatement statement = connection.prepareStatement(deleteQuery);

            statement.setInt(1,authorId);

            int affectedRows = statement.executeUpdate();

            if(affectedRows > 0){
                return "Author Deleted successfully";
            }
            else{
                return "Author not found";
            }


        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
