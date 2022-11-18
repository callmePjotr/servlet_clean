import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
//import java.util.logging.Logger;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;





@WebServlet(name = "loggingServlet", urlPatterns = "/loggingServlet")
public class loggingServlet extends HttpServlet {

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
    protected static final Logger logger = LogManager.getLogger(loggingServlet.class);
//[07/Nov/2022:13:36:17 +0100]

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        //logger.info("moin");


        String url = request.getRequestURL().toString();

        // Getting servlet request query string.
        String queryString = request.getQueryString();

        // Getting request information without the hostname.
        String uri = request.getRequestURI();

        String method = request.getMethod();
        String andere = String.valueOf(request.authenticate(response));
        int status = response.getStatus();

        // Below we extract information about the request object path
        // information.
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int portNumber = request.getServerPort();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String query = request.getQueryString();

        String src_ip = request.getRemoteAddr();
        String dst_ip = request.getLocalAddr();
        String hyph = request.getRemoteUser();
        String auth_user = String.valueOf(request.getUserPrincipal());
        int  size = request.getContentLength();
        String version = request.getProtocol();


        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.print("Url: " + url + "<br/>");
        pw.print("Uri: " + uri + "<br/>");
        pw.print("Scheme: " + scheme + "<br/>");
        pw.print("Server Name: " + serverName + "<br/>");
        pw.print("Port: " + portNumber + "<br/>");
        pw.print("Context Path: " + contextPath + "<br/>");
        pw.print("Servlet Path: " + servletPath + "<br/>");
        pw.print("Path Info: " + pathInfo + "<br/>");
        pw.print("Query: " + query+ "<br/>");
        pw.print("method: " + method+ "<br/>");
        pw.print("stautus-code" + status+ "<br/>");

/*
        logger.info(url);
        logger.info(uri);
        logger.info(scheme);
        logger.info(query);
        logger.info(method);
        logger.info(status);




        logger.info("Source ip is: " + src_ip);
        logger.info("Destination ip is: " + dst_ip);
*/
        //dies sollte das gängige Logformat sein

        logger.info("source_ip =" + src_ip +", hyph =" + hyph + ", auth_user =" + auth_user + " [" +  sdf2.format(timestamp) + " +100] " + "\", method =" + method + ", uri =" + uri + ", version =" +version+ "\", status =" + status +", size = " + size);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }



}
