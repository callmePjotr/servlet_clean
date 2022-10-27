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
import java.util.logging.Logger;


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
        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        //Hier mal das Logging
        Logger logger = Logger.getLogger(String.valueOf(getClass()));
        logger.info("inside do Post");


// Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        //Eigenen Logs generieren und diese in ein File schreiben
        /*
        PrintStream o = new PrintStream(new File("logs.txt"));
        PrintStream console = System.out;
        System.setOut(o);
        */

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //System.out.println(sdf2.format(timestamp) + " " + " " + "Request received");
        logger.info("Request received");
        //System.out.println(sdf2.format(timestamp) + " " + " " + "Attempting to connect to the database");
        logger.info("Attempting to connect to the database");
        //Mit der Datenbank verbinden
        try {
            con = connect.connectToDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(sdf2.format(timestamp) + " " + " " + "successfully connected with database using param" +" "+ con);
        logger.info("successfully connected with database using param"+" "+ con);
        PrintWriter out = response.getWriter();


        //mittels Post die Werte holen
        //System.out.println(sdf2.format(timestamp) + " " + " " + "getting form Paramaters via HTTP POST");
        logger.info("getting form Paramaters via HTTP POST");


        //Java Commons Library, encodet alle mögliche Zeichen
        String test = StringEscapeUtils.escapeHtml4(request.getParameter("login"));
        System.out.println("Moin hier geht es los-----------------");
        System.out.println(test);
        //System.out.println("ESAPI.accessController found: " + ESAPI.accessController());

        String value = ESAPI.encoder().canonicalize(test);
        String test2 = ESAPI.encoder().encodeForHTML(test);
        value = value.replaceAll("\0", "");
        System.out.println(value);
        System.out.println("This is value of test2 " + test2);
        String test3 = ESAPI.encoder().decodeForHTML(test2);
        System.out.println("This is value of test3 " + test3);


        String login = request.getParameter("login");
        String password = request.getParameter("password");
        System.out.println(sdf2.format(timestamp) + " " + " " + "Parameter for mail is"+" "+ login);
        System.out.println(sdf2.format(timestamp) + " " + " " + "Parameter for password is" + " " + password);

        //jetzt erst einmal den ganzen Bumms überprüfen

        //System.out.println(sdf2.format(timestamp) + " " + " " + "Using Regex to check if the Email has a correct format");
        logger.info("Using Regex to check if the Email has a correct format");
        boolean validate_mail = verifyEmail.testUsingRFC5322Regex(login);
        //System.out.println(sdf2.format(timestamp) + " " + " " + "Validated the Mail successfully");
        logger.info("Validated the Mail successfully");
        String xssCleanLogin = myXSSStrip.stripXSS(login);
        String xssCleanPassword = myXSSStrip.stripXSS(password);

        //soll der Input von XSS befreit werden, kann man ganz einfach die folgenden beiden Zeilen einfügen
        //login = xssCleanLogin;
        //password = xssCleanPassword;
        //muss gut getestet werden, da es möglich ist, dass durch das Parsen des Inputs durch die stripXSS Funktion diese verändert wird ---> so kann sich eventueel der Hash ändern
        //sodass selbst bei einem richtigen Login dieser immer wieder fehlschlägt

       // System.out.println(sdf2.format(timestamp) + " " + " " + login + " - Content of email form");
       // System.out.println(sdf2.format(timestamp) + " " + " " + password +  " - Content of password form ");
        System.out.println(sdf2.format(timestamp) + " " + " " + xssCleanLogin + " - Content of xssStrippedEmail form");
        System.out.println(sdf2.format(timestamp) + " " + " " + xssCleanPassword +  " - Content of xssStrippedPassword form ");

        //System.out.println(sdf2.format(timestamp) + " " + " " + "Checking if parameters are missing");
        logger.info("Checking if parameters are missing");
        if (login == null || login.length() == 0 || login.equals(" ")){
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
        }
        if (password == null || password.length() == 0 || password.equals(" ")){
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid password form');");
            out.println("location='register.jsp';");
            out.println("</script>");
            return;
        }



        //Passwort hashen
        //System.out.println(sdf2.format(timestamp) + " " + " " + "Hashing password");
        logger.info("Hashing password");
        try {
            encryptpw = encrypt.send(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(sdf2.format(timestamp) + " " + " " +"password was successfully hashed " + encryptpw);

        logger.info("password was successfully hashed");
        //System.out.println(login + " " + password);

        if(login==null || password == null){
            request.setAttribute("error", "you are missing one of the inputs");
            getServletContext().getRequestDispatcher("/register.jsp").forward(request,response);
        }else{
            //dosomeStuff --> Passwort überprüfen

            //System.out.println(sdf2.format(timestamp) + " " + " " + "selecting password from the databse for the corresponding user if existing");
            logger.info("selecting password from the databse for the corresponding user if existing");
            try {
                String sql = "SELECT password, mail FROM geheimeszeug WHERE mail = ?";
                preparedStatement = con.prepareStatement(sql);
                //System.out.println(login);
                preparedStatement.setString(1,login);
                System.out.println(sdf2.format(timestamp) + " " + " " + sql + " - SQL prepared statement");
                result = preparedStatement.executeQuery();
                //System.out.println("Hello there!");
                while (result.next()){
                    //System.out.println(result.getString("password"));
                    pass = result.getString("password");
                    email = result.getString("mail");
                }
                //System.out.println("Hello there!");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //System.out.println(sdf2.format(timestamp) + " " + " " + "successfully selected the corresponding data");
            logger.info("successfully selected the corresponding data");
            //System.out.println(result);
            //System.out.println(sdf2.format(timestamp) + " " + " " + "checking if hashes equal each other");
            logger.info("checking if hashes equal each other");
            if(!encryptpw.equals(pass) && !email.equals(login) || encryptpw.equals(pass) && !email.equals(login) || !encryptpw.equals(pass) && email.equals(login)){
                System.out.println(sdf2.format(timestamp) + " " + " " + "pw/mail incorrect");
                getServletContext().getRequestDispatcher("/loginfailed.jsp").forward(request,response);
                request.setAttribute("error","Username or Password incorrect!");
                //System.out.println(sdf2.format(timestamp) + " " + " " + "username or password are incorrect");
                logger.info("username or password are incorrect");
            }else if (encryptpw.equals(pass) || email.equals(login) ){
                System.out.println(sdf2.format(timestamp) + " " + " " + "Login successful");
                //getServletContext().getRequestDispatcher("/ServlettoJSP.java").forward(request,response);
                getServletContext().getRequestDispatcher("/ServlettoJSP").forward(request,response);
                logger.info("user " + login + " was successfully authenticated");
                logger.info("redirecting to blog.jsp");
                //System.out.println(sdf2.format(timestamp) + " " + " " + "user " + login + " was successfully authenticated");
                //System.out.println(sdf2.format(timestamp) + " " + " " + "redirecting to bonk.jsp");
            }else{
                System.out.println(sdf2.format(timestamp) + " " + " " + "pw/mail incorrect");
                getServletContext().getRequestDispatcher("/loginfailed.jsp").forward(request,response);
                request.setAttribute("error","Username or Password incorrect!");
                //System.out.println(sdf2.format(timestamp) + " " + " " + "username or password are incorrect");
                logger.info("username or password are incorrect");
            }
            //System.setOut(console);
        }
    }
}
