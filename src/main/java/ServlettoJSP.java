//import model.comments;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "ServlettoJSP", urlPatterns = "/ServlettoJSP")
public class ServlettoJSP extends HttpServlet {
    private static String encryptpw = null;
    private static Connection con = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet result = null;
    private static String name = null;
    private static String email = null;
    private static String kommentar = null;
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss.SSS");
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        HashMap<String, String> hash_map = new HashMap<String, String>();

        try {
            con = connect.connectToDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> users = new ArrayList<String>();
        List<String> mails = new ArrayList<String>();
        List<String> comments = new ArrayList<String>();

        try {
            String sql = "SELECT name, email, kommentar FROM kommentare";
            preparedStatement = con.prepareStatement(sql);
            System.out.println(sdf2.format(timestamp) + " " + " " + sql + " - SQL prepared statement");
            result = preparedStatement.executeQuery();
            while (result.next()){
                name = result.getString("name");
                //users.add(name);
                email = result.getString("email");
                //mails.add(email);
                kommentar = result.getString("kommentar");
                comments.add(kommentar);

                hash_map.put(name, kommentar);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(users);
        //System.out.println(mails);
        //System.out.println(comments);
        System.out.println(hash_map);




        StringBuilder error = new StringBuilder();



        if(error.length() > 0){
            request.setAttribute("error", error.toString());
        }else{
            //request.setAttribute("firstName", firstName);
            //request.setAttribute("lasttName", lastName);
            //request.setAttribute("age", age);

        }
        int length = comments.size();
        request.setAttribute("name", users);
        request.setAttribute("email", mails);
        request.setAttribute("length",length);
        request.setAttribute("kommentar", comments);

        request.setAttribute("alles", hash_map);
        //request.setAttribute("comments", new model.comments());

        //comments com = new comments();

        ArrayList list =new ArrayList();
        list.add("Java");
        list.add("Hadoop");
        list.add("Spark");
        list.add("Hive");
        list.add("Python");
        list.add("C++");
        list.add("Machine Learning");
        //com.setComment(list);
        //request.setAttribute("comments2", new model.comments());

        //Verbindung wieder schließen
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getServletContext().getRequestDispatcher("/WEB-INF/blog.jsp").forward(request,response);
        //getServletContext().getRequestDispatcher("/test.jsp").forward(request,response);
    }
}
