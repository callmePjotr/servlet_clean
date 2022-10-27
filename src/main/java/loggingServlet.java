import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
//import java.util.logging.Logger;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;





@WebServlet(name = "loggingServlet", urlPatterns = "/loggingServlet")
public class loggingServlet extends HttpServlet {


    protected static final Logger logger = LogManager.getLogger(loggingServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        logger.info("moin");


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


        logger.info(url);
        logger.info(uri);
        logger.info(scheme);
        logger.info(query);
        logger.info(method);
        logger.info(status);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }



}
