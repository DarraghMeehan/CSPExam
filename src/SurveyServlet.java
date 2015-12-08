// Fig. 9.27: SurveyServlet.java
// A Web-based survey that uses JDBC from a servlet.

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class SurveyServlet extends HttpServlet {
   private Connection connection;
   private PreparedStatement insertData, results;

   // set up database connection and prepare SQL statements
   public void init( ServletConfig config ) 
      throws ServletException
   {
      // attempt database connection and create PreparedStatements
      try {
        // Connection connect;
            String driver = "com.mysql.jdbc.Driver";
            try {
               Class.forName(driver);
               String url ="jdbc:mysql://localhost:3306/MyForm"; //Database name here
               connection = DriverManager.getConnection(url, "root", "armvye123");  //URL, user and password
            }
            catch (ClassNotFoundException e) {
               e.printStackTrace();
            }
      catch (SQLException e)
      {
         e.printStackTrace();
      }

       insertData = connection.prepareStatement(
         "INSERT INTO form " +
               "( first_name, last_name, id_num, email, telephone, comments) " +
               "VALUES ( ?, ?, ?, ?, ?, ? )" );

         // PreparedStatement to obtain surveyoption table's data
       results = connection.prepareStatement(
         "SELECT first_name, last_name, id_num, email, telephone, comments " +
            "FROM form ORDER BY id"
         );
      }
      // for any exception throw an UnavailableException to 
      // indicate that the servlet is not currently available
      catch ( Exception exception ) {
         exception.printStackTrace();
         throw new UnavailableException( exception.getMessage() );
      }
   }  // end of init method

   // process survey response
   protected void doPost(HttpServletRequest request,
                         HttpServletResponse response)
           throws ServletException, IOException {
      // set up response to client
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      // Entered Parameters saved inder local variables
      // Default values for missing parameters
      String first_name = request.getParameter("first_name");
      if (first_name == "")
         first_name = "NO ENTRY";
      else ;
      String last_name = request.getParameter("last_name");
      if (last_name == "")
         last_name = "NO ENTRY";
      else ;
      String id_number = request.getParameter("id_num");
      if (id_number == "")
         id_number = "NO ENTRY";
      else ;
      String email = request.getParameter("email");
      if (email == "")
         email = "NO ENTRY";
      else ;
      String telephone = request.getParameter("telephone");
      if (telephone == "")
         telephone = "NO ENTRY";
      else ;
      String comments = request.getParameter("comments");
      if (comments == "")
         comments = "NO ENTRY";
      else ;

      // Inserting data into my Database - MyForm
      try {
         insertData.setString(1, first_name);
         insertData.setString(2, last_name);
         insertData.setString(3, id_number);
         insertData.setString(4, email);
         insertData.setString(5, telephone);
         insertData.setString(6, comments);
         insertData.executeUpdate();
      }
      catch (SQLException e) {
         e.printStackTrace();
      }

      String title = "Darragh Meehan CSP Exam";
      String docType =
              "<!doctype html public \"-//w3c//dtd html 4.0" +
              "transitional//en\">\n";
      out.println(docType +
              "<html>\n" +
              "<head><title>" + title + "</title></head>\n" +
              "<body bgcolor=\"f0f0f0\">\n" +
              "<h1 align=\"center\">" + title + "</h1>\n" +
              "<ul>\n" +
              "<li><b>First Name</b>: " +
              request.getParameter("first_name") + "\n" +
              " <li><b>Last Name</b>: " +
              request.getParameter("last_name") + "\n" +
              " <li><b>ID Number</b>: " +
              request.getParameter("id_num") + "\n" +
              " <li><b>Email Address</b>: " +
              request.getParameter("email") + "\n" +
              " <li><b>Telephone Number</b>: " +
              request.getParameter("telephone") + "\n" +
              " <li><b>Comments</b>: " +
              request.getParameter("comments") + "\n" +
              " <form name=\"readhtml\" method=\"get\" action=\"SurveyServlet\">"+
              " <input type=\"submit\" value=\"Go to GET method\"> "+
              " </form></ul>\n" +
              "</body></html>");
   }

   protected void doGet(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
      {
          response.setContentType("text/html");
          // Get a output writer to write the response message into the network socket
          PrintWriter out = response.getWriter();

          Statement stmt = null;

          try {
              String driver = "com.mysql.jdbc.Driver";
              Class.forName( driver );

              String url ="jdbc:mysql://localhost:3306/MyForm"; //Database name here
              connection = DriverManager.getConnection(url, "root", "armvye123");  //URL, user and password

              // Step 2: Create a "Statement" object inside the "Connection"
              stmt = connection.createStatement();

              // Step 3: Execute a SQL SELECT query
              //String sqlStr = String.valueOf(results);

              // Print an HTML page as output of query
              out.println("<html><head><title>Query Results</title></head><body bgcolor=\"f0f0f0\">\n");
              out.println("<h2>Thank you for your query.</h2>");
              //out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging
              /*********************************************/
              //out.println("<html><head><title>Query Results</title></head><body>");
              //out.println("<h2>Thank you for your query.</h2>");
              //out.println("<p>You query is: " + "test" + "</p>"); // Echo for debugging
              ResultSet resultSet = results.executeQuery(); // Send the query to the server

              while(resultSet.next()) {
                  // Print a paragraph <p>...</p> for each row
                  out.println("<p>" +"Firstname: " + resultSet.getString("first_name")
                  + " -- Surname: " + resultSet.getString("last_name")
                  + " -- ID: " + resultSet.getString("id_num")
                  + " -- Email: " + resultSet.getString("email")
                  + " -- Number: " + resultSet.getString("telephone")
                  + " -- Comments: " + resultSet.getString("comments")
                  + "\n***********************************************************************************\n</p>");
              }
              out.println("</body></html>");
          }
          catch (ClassNotFoundException e) {
              e.printStackTrace();
          }
          catch (SQLException e) {
              e.printStackTrace();
          }

          finally {
              out.close();
              try {
                  // Step 5: Close the Statement and Connection
                  if (stmt != null)
                      stmt.close();
                  if (connection != null)
                      connection.close();
              }
              catch (SQLException ex) {
                  ex.printStackTrace();
              }
          }
      }
   }

   // close SQL statements and database when servlet terminates
   public void destroy()
   {
      // attempt to close statements and database connection
      try {
         results.close();
         connection.close();
         insertData.close();
      }

      // handle database exceptions by returning error to client
      catch( SQLException sqlException ) {
         sqlException.printStackTrace();
      }
   }  // end of destroy method
}

