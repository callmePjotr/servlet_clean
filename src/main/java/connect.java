import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class connect {
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss.SSS");
    protected static Connection connectToDatabase() throws SQLException, ClassNotFoundException, FileNotFoundException {
        //PrintStream o = new PrintStream(new File("B.txt"));
        //PrintStream console = System.out;
        //System.setOut(o);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(sdf2.format(timestamp) + " " + " " + "Connecting to database");
        //Verbindung herstellen

        String dbDriver = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://127.0.0.1:3306/";
        // Database name to access
        String dbName = "geheim";
        String dbUsername = "root";
        //String dbUsername = "java";
        String dbPassword = "";
        //String dbPassword = "Password123#";



        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbURL + dbName,
                dbUsername,
                dbPassword);

        System.out.println(sdf2.format(timestamp) + " " + " " + "Connected Successfully");
        //System.setOut(console);
        return con;
    }
}
