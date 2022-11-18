import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


@WebServlet(name = "AddCommentServlet", value = "/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
    private static Connection con = null;
    private static PreparedStatement preparedStatement = null;
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss.SSS");
    protected static final Logger logger = LogManager.getLogger(loggingServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String src_ip = request.getRemoteAddr();
        String hyph = request.getRemoteUser();
        String version = request.getProtocol();
        String auth_user = String.valueOf(request.getUserPrincipal());

        int status = response.getStatus();
        int  size = request.getContentLength();

        String className = String.valueOf(getClass());

        //Parameter holen aus blog.jsp
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
                ", message = getting parameters from blog.jsp, classname =" + className );
        String name = request.getParameter("name");
        String mail_addr = request.getParameter("mail_addr");
        String comment = request.getParameter("comment");

        //Prüfen, ob alle Felder ausgefüllt sind
        PrintWriter out = response.getWriter();

        if (name == null || name.length() == 0 || name.equals(" ") || comment == null || comment.length() == 0 || comment.equals(" ") || mail_addr == null || mail_addr.length() == 0 || mail_addr.equals(" ")){
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Please fill in all the fields');");
            out.println("location='register.jsp';");
            out.println("</script>");
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
                    ", message = empty parameters, classname =" + className );
            return;
        }

        //gucke ob die Mail das richtige Format hat
        boolean validate_mail = verifyEmail.testUsingRFC5322Regex(mail_addr);
        if (!validate_mail){
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalide Email laut RFC-5322 Regex');");
            out.println("location='register.jsp';");
            out.println("</script>");

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
                    ", message = mail validation failed, classname =" + className );
        }


        //Verbindung zur Datenbank herstellen
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
                    ", message = Connecting to Database failed, classname =" + className);

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
                    ", message =" + e.getMessage() +", classname =" + className);
        }

        if (mail_addr == null || name == null || comment == null) {
            request.setAttribute("error", "you are missing one of the inputs");
            getServletContext().getRequestDispatcher("/blog.jsp").forward(request, response);
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
                    ", message = empty parameters, classname =" + className );
        } else {

            System.out.println(sdf2.format(timestamp) + " " + " " + "inserting new data into the database if possible");
            try {

                String sql = "INSERT INTO kommentare (name, email, kommentar) values (?,?,?)";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, mail_addr);
                preparedStatement.setString(3, comment);
                System.out.println(sdf2.format(timestamp) + " " + " " + sql + " - SQL prepared statement");
                preparedStatement.execute();

                con.close();
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
                        ", message =new comment was added , classname =" + className );

                getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);

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
                        ", message =failed to add comment , classname =" + className );

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
        }

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
                    ", message =closing database connection , classname =" + className );
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
                    ", message =failed to close database connection , classname =" + className );

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
    }
}
