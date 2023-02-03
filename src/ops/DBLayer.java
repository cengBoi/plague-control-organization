package ops;
import java.sql.*;

public class DBLayer {
    private static DBLayer layer = null;
    private Connection connection;

    private DBLayer() {}

    public Connection connect() {
        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "oopproject");
            connection = DriverManager.getConnection("jdbc:sqlite:PCO.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static DBLayer getInstance() {
        if (layer == null)
            layer = new DBLayer();
        return layer;
    }
}
