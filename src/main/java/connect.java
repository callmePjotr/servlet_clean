import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class connect {
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss.SSS");
    protected static final Logger logger = LogManager.getLogger(loggingServlet.class);
    protected static Connection connectToDatabase(String uri, String method, String src_ip, String hyph, String version, String auth_user, int status, int size) throws SQLException, ClassNotFoundException, FileNotFoundException {



        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String className = connect.class.getName();

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
                ", message =connecting to database from class " + className);

        //Verbindung herstellen

        String dbDriver = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://127.0.0.1:3306/";
        // Database name to access
        String dbName = "geheim";
        String dbUsername = "root";
        //String dbUsername = "java";
        String dbPassword = "password";
        //String dbPassword = "Password123#";



        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbURL + dbName,
                dbUsername,
                dbPassword);

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
                ", message =formed url for class " + className);



        return con;
    }
}
