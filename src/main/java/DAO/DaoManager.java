package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoManager {
    private Connection conn;
    private final String URL;
    private final String USER;
    private final String PASS;
    private static DaoManager singleton;

    private DaoManager() {
        this.conn = null;
        this.URL = System.getenv("DB_URL") != null
                ? System.getenv("DB_URL")
                : "jdbc:mysql://localhost:3306/fernanPop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Madrid";
        this.USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "db_user";
        this.PASS = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "db_password";
    }

    public static DaoManager getSingletonInstance() {
        if (singleton == null) singleton = new DaoManager();
        return singleton;
    }

    public Connection getConn() {
        return conn;
    }

    public void open() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw e;
        }
    }

    public void close() throws SQLException {
        try {
            if (this.conn != null) this.conn.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
