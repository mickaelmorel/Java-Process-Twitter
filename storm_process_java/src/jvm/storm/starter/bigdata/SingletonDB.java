package storm.starter.bigdata;

import storm.starter.bigdata.util.MyProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SingletonDB {

    private static Connection connection;

    /**
     * Constructeur privé
     */
    private SingletonDB() {

        String url = MyProperties.getProperties("mysql_string");
        String username = MyProperties.getProperties("mysql_user");
        String password = MyProperties.getProperties("mysql_password");

        System.out.println(url);
        System.out.println(username);
        System.out.println(password);

        System.out.println("Connecting database...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            System.out.println("Error while executing request : " + e);
        }
    }


    /**
     * Instance unique pré-initialisée
     */
    private static SingletonDB INSTANCE = new SingletonDB();

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static synchronized SingletonDB getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonDB();
        }
        return INSTANCE;
    }

    public static void insertDB(String sql) {
        try {
            Statement st = (Statement) connection.createStatement();
            st.executeUpdate(sql);
            System.out.println(sql);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}