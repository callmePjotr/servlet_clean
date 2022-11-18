import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.owasp.esapi.ESAPI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private static String encryptpw = null;
    private static Connection con = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet result = null;
    private static String pass = null;
    private static String email = null;
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss.SSS");

    private String content = "There is no content yet";

    protected static final Logger logger = LogManager.getLogger(loggingServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        //Java Commons Library, encodet alle mögliche Zeichen
        request.setAttribute("content",content);
        getServletContext().getRequestDispatcher("/register.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //alle Parameter für eine vernünftige Logzeile
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String src_ip = request.getRemoteAddr();
        String hyph = request.getRemoteUser();
        String version = request.getProtocol();
        String auth_user = String.valueOf(request.getUserPrincipal());

        int status = response.getStatus();
        int  size = request.getContentLength();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Die allgemeine Request
        logger.info("source_ip =" + src_ip +", hyph =" + hyph + ", auth_user =" + auth_user + " [" +  sdf2.format(timestamp) + " +100] " + "\", method =" + method + ", uri =" + uri + ", version =" +version+ "\", status =" + status +", size = " + size + ", message = intial calling of doPost");




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
                ", message = intial calling of doPost");



        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");



        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        //Verbindung zur Datenbank
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
                    ", message = Connecting to Database");
            con = connect.connectToDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            String stackTrace = ExceptionUtils.getStackTrace(e);
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

        //Java Commons Library, encodet alle mögliche Zeichen
        String test = StringEscapeUtils.escapeHtml4(request.getParameter("login"));
        System.out.println("Moin hier geht es los-----------------");
        System.out.println(test);


        //ESAPI Aufrufe testen
        String value = ESAPI.encoder().canonicalize(test);
        String test2 = ESAPI.encoder().encodeForHTML(test);
        value = value.replaceAll("\0", "");
        System.out.println(value);
        System.out.println("This is value of test2 " + test2);
        String test3 = ESAPI.encoder().decodeForHTML(test2);
        System.out.println("This is value of test3 " + test3);


        //POST - Werte holen
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
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        System.out.println(sdf2.format(timestamp) + " " + " " + "Parameter for mail is"+" "+ login);
        System.out.println(sdf2.format(timestamp) + " " + " " + "Parameter for password is" + " " + password);

        //Passwort auf Maximallänge überprüfen
        if(login==null || password == null){
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
        }
        else {
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
                    ", message =received parameters");
        }
        //jetzt erst einmal den ganzen Bumms überprüfen

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
                ", message =validating email");
        //Mail validieren
        boolean validate_mail = verifyEmail.testUsingRFC5322Regex(login);


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
                ", message =searching for javascript");
        //erster Strip XSS Filter
        String xssCleanLogin = myXSSStrip.stripXSS(login);
        String xssCleanPassword = myXSSStrip.stripXSS(password);

        //soll der Input von XSS befreit werden, kann man ganz einfach die folgenden beiden Zeilen einfügen
        //login = xssCleanLogin;
        //password = xssCleanPassword;
        //muss gut getestet werden, da es möglich ist, dass durch das Parsen des Inputs durch die stripXSS Funktion diese verändert wird ---> so kann sich eventueel der Hash ändern
        //sodass selbst bei einem richtigen Login dieser immer wieder fehlschlägt


        System.out.println(sdf2.format(timestamp) + " " + " " + xssCleanLogin + " - Content of xssStrippedEmail form");
        System.out.println(sdf2.format(timestamp) + " " + " " + xssCleanPassword +  " - Content of xssStrippedPassword form ");

        //Suche nach leeren Eingabefeldern
        if (login == null || login.length() == 0 || login.equals(" ")){
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
                    ", message =no parameters were given for login");

            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid Email adress');");
            out.println("location='register.jsp';");
            out.println("</script>");
            return;
        } else if (login.length()<=6 && !login.contains("@") && !login.contains(".")) {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid Email adress');");
            out.println("location='register.jsp';");
            out.println("</script>");
            return;
        }else if (validate_mail == false){
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
                    ", version =" + version +
                    "\", status =" + status +
                    ", size = " + size +
                    ", message =invalid email");
        }
        if (password == null || password.length() == 0 || password.equals(" ")){
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
                    ", message =invalid password");


            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid password form');");
            out.println("location='register.jsp';");
            out.println("</script>");
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
                ", message =trying to hash password");
        try {
            encryptpw = encrypt.send(password);
        } catch (NoSuchAlgorithmException e) {
            String stackTrace = ExceptionUtils.getStackTrace(e);
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
                    ", message =failed to generate hash");

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


        if(login==null || password == null){
            request.setAttribute("error", "you are missing one of the inputs");
            getServletContext().getRequestDispatcher("/register.jsp").forward(request,response);
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
                    ", message =login failed");
        }else{
            //prüfe ob der Nutzer vorhanden ist
            try {
                String sql = "SELECT password, mail FROM geheimeszeug WHERE mail = ?";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1,login);
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
                        ", message =searching in database for user");
                //wenn ja, Ergebnis fetchen
                while (result.next()){
                    pass = result.getString("password");
                    email = result.getString("mail");
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
                            ", message =user found, fetching data");
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
            //Passwörter vergleichen
            if(!encryptpw.equals(pass) && !email.equals(login) || encryptpw.equals(pass) && !email.equals(login) || !encryptpw.equals(pass) && email.equals(login)){
                System.out.println(sdf2.format(timestamp) + " " + " " + "pw/mail incorrect");
                getServletContext().getRequestDispatcher("/loginfailed.jsp").forward(request,response);
                request.setAttribute("error","Username or Password incorrect!");
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
                        ", message =login failed, wrong password or username");
                //weiterleiten wenn Passwort korrekt
            }else if (encryptpw.equals(pass) || email.equals(login) ){
                System.out.println(sdf2.format(timestamp) + " " + " " + "Login successful");
                getServletContext().getRequestDispatcher("/ServlettoJSP").forward(request,response);
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
                        ", message =successfull login, redirecting");
            }else{
                System.out.println(sdf2.format(timestamp) + " " + " " + "pw/mail incorrect");
                getServletContext().getRequestDispatcher("/loginfailed.jsp").forward(request,response);
                request.setAttribute("error","Username or Password incorrect!");
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
                        ", message =login failed, wrong password or username");
            }
        }
    }
}
