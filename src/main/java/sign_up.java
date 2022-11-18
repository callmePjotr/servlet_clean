import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@WebServlet(name = "sign_up", urlPatterns = "/sign_up")
public class sign_up extends HttpServlet {
    private static String encryptpw = null;
    private static Connection con = null;
    private static PreparedStatement preparedStatement = null;
    //private static ResultSet result = null;
    private static String pass = null;
    private static String email = null;
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss.SSS");

    protected static final Logger logger = LogManager.getLogger(loggingServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

// Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();
        String src_ip = request.getRemoteAddr();
        String hyph = request.getRemoteUser();
        String version = request.getProtocol();
        String auth_user = String.valueOf(request.getUserPrincipal());

        int status = response.getStatus();
        int  size = request.getContentLength();

        String className = String.valueOf(getClass());
        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

// Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        //Ein paar Logs für Zwischendurch
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());



        logger.info("source_ip =" + src_ip +", hyph =" + hyph + ", auth_user =" + auth_user + " [" +  sdf2.format(timestamp) + " +100] " + "\", method =" + method + ", uri =" + uri + ", version =" +version+ "\", status =" + status +", size = " + size + ", message = intial calling of doPost, classname ="+ className);

        //Mit der Datenbank verbinden wie gehabt
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

        PrintWriter out = response.getWriter();

        //Werte aus dem Formular holen

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
                ", message =receiving parameters");

        System.out.println(sdf2.format(timestamp) + " " + " " + "getting form Paramaters via HTTP POST");
        //String login_new = request.getParameter("login_new");
        String login_new = StringEscapeUtils.escapeHtml4(request.getParameter("login_new"));
        String password_new = request.getParameter("password_new");
        String password_new_confirm = request.getParameter("password_new_confirm");

        //boolean validate_mail = verifyEmail.testUsingRFC5322Regex(login_new);

        if (login_new == null || login_new.length() == 0 || login_new.equals(" ")) {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid Email adress');");
            out.println("location='register.jsp';");
            out.println("</script>");
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
                    ", message =failed to get parameters");
            return;
        } else if (login_new.length() <= 6 && !login_new.contains("@") && !login_new.contains(".")) {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid Email adress');");
            out.println("location='register.jsp';");
            out.println("</script>");

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
                    ", message =invalid email , classname =" + className);
            return;

        }
        /*else if (validate_mail == false){
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalide Email laut RFC-5322 Regex');");
            out.println("location='register.jsp';");
            out.println("</script>");
        }*/

            if (password_new == null || password_new.length() == 0 || password_new.equals(" ")) {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Invalid password form');");
                out.println("location='register.jsp';");
                out.println("</script>");

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
                        ", message =invalid password, classname =" + className);
                return;
            }

            //Passwort hashen
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
                ", message =trying to hash password, classname =" + className);
            try {
                encryptpw = encrypt.send(password_new);
            } catch (NoSuchAlgorithmException e) {
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
                        ", message =failed to generate hash, classname =" + className);

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

            System.out.println(sdf2.format(timestamp) + " " + " " + "password was successfully hashed " + encryptpw);

            //Jetzt beginnt der eigentliche Teil, dazu gehört das Einfügen des Passwortes in die Datenbank und somit das Anlegen eines neuen Nutzers
            if (login_new == null || password_new == null) {
                request.setAttribute("error", "you are missing one of the inputs");
                getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
            } else {

                System.out.println(sdf2.format(timestamp) + " " + " " + "inserting new data into the database if possible");
                try {
                    String sql = "INSERT INTO geheimeszeug (mail, password) values (?,?)";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, login_new);
                    preparedStatement.setString(2, encryptpw);
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
                            ", message =added new user to database, classname =" + className );

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
                            ", message =database exception, classname =" + className );

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
    }


