//import model.comments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    protected static final Logger logger = LogManager.getLogger(loggingServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        HashMap<String, String> hash_map = new HashMap<String, String>();

        String uri = request.getRequestURI();
        String method = request.getMethod();
        String src_ip = request.getRemoteAddr();
        String hyph = request.getRemoteUser();
        String version = request.getProtocol();
        String auth_user = String.valueOf(request.getUserPrincipal());

        int status = response.getStatus();
        int  size = request.getContentLength();

        String className = String.valueOf(getClass());

        logger.info("source_ip =" + src_ip +", hyph =" + hyph + ", auth_user =" + auth_user + " [" +  sdf2.format(timestamp) + " +100] " + "\", method =" + method + ", uri =" + uri + ", version =" +version+ "\", status =" + status +", size = " + size + ", message = intial calling of doPost, classname ="+ className);

        try {
            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version+
                    "\", status =" + status +
                    ", size = " + size +
                    ", message = Connecting to Database, classname =" + className);
            con = connect.connectToDatabase(uri, method, src_ip, hyph, version, auth_user, status, size);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version+
                    "\", status =" + status +
                    ", size = " + size +
                    ", message = Connecting to Database failed");

            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version+
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =" + e.getMessage());
        }
        List<String> users = new ArrayList<String>();
        List<String> mails = new ArrayList<String>();
        List<String> comments = new ArrayList<String>();

        try {
            String sql = "SELECT name, email, kommentar FROM kommentare";
            preparedStatement = con.prepareStatement(sql);
            System.out.println(sdf2.format(timestamp) + " " + " " + sql + " - SQL prepared statement");
            result = preparedStatement.executeQuery();
            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version +
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =searching for data in database, classname =" + className);
            while (result.next()){
                name = result.getString("name");
                //users.add(name);
                email = result.getString("email");
                //mails.add(email);
                kommentar = result.getString("kommentar");
                comments.add(kommentar);

                hash_map.put(name, kommentar);

                logger.info("source_ip =" + src_ip +
                        ", hyph =" + hyph +
                        ", auth_user =" + auth_user +
                        " [" + sdf2.format(timestamp) +
                        " +100] " +
                        "\", method =" + method +
                        ", uri =" + uri +
                        ", version =" + version +
                        "\", status =" + status +
                        ", size = " + size +
                        ", message =data found, fetching data, classname =" + className);
            }


        } catch (SQLException e) {
            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version +
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =database exeption");

            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version +
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =" + e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println(hash_map);


        logger.info("source_ip =" + src_ip +
                ", hyph =" + hyph +
                ", auth_user =" + auth_user +
                " [" + sdf2.format(timestamp) +
                " +100] " +
                "\", method =" + method +
                ", uri =" + uri +
                ", version =" + version +
                "\", status =" + status +
                ", size = " + size +
                ", message =initializing StringBuilder, classname =" + className);

        StringBuilder error = new StringBuilder();



        if(error.length() > 0){
            request.setAttribute("error", error.toString());

        }else{
            //request.setAttribute("firstName", firstName);
            //request.setAttribute("lasttName", lastName);
            //request.setAttribute("age", age);

        }

        logger.info("source_ip =" + src_ip +
                ", hyph =" + hyph +
                ", auth_user =" + auth_user +
                " [" + sdf2.format(timestamp) +
                " +100] " +
                "\", method =" + method +
                ", uri =" + uri +
                ", version =" + version +
                "\", status =" + status +
                ", size = " + size +
                ", message =setting attributes in class " + className);
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
            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version+
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =closing database connection, classname =" + className );
            con.close();
        } catch (SQLException e) {
            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version+
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =failed to close database connection, classname =" + className );

            logger.info("source_ip =" + src_ip +
                    ", hyph =" + hyph +
                    ", auth_user =" + auth_user +
                    " [" + sdf2.format(timestamp) +
                    " +100] " +
                    "\", method =" + method +
                    ", uri =" + uri +
                    ", version =" + version+
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =" + e.getMessage() +", classname =" + className );
            throw new RuntimeException(e);
        }
        getServletContext().getRequestDispatcher("/WEB-INF/blog.jsp").forward(request,response);
        //getServletContext().getRequestDispatcher("/test.jsp").forward(request,response);
    }
}
