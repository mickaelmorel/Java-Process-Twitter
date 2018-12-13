package storm.starter.bigdata.util;

import java.sql.*;

public class SingletonDB {

    private static Connection connection;

    private SingletonDB() {

        String url =  "jdbc:mysql://localhost:3306";//MyProperties.getProperties("mysql_string");
        String username = "root";//MyProperties.getProperties("mysql_user");
        String password = "root";//MyProperties.getProperties("mysql_password");

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

    private static SingletonDB INSTANCE = new SingletonDB();

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

    public static ResultSet selectDB(String sql) {
        ResultSet rs = null;
        try {
            Statement st = (Statement) connection.createStatement();
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rs;
    }
}