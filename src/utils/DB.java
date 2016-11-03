package utils;

import java.sql.*;

/**
 * Created by dpunk12 on 10/28/2016.
 */
public class DB {

    private static final String DB_IP = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_NAME = "postgres";
    private static String DB_USER;
    private static String DB_PASSWORD;

    private static Connection con;


    public static boolean tryLogin(String username, String password){
       try{
           System.out.println("Trying To Login . . . ");
           getConnection(username, password);
           System.out.println("Login Success!");
           DB_USER = username;
           DB_PASSWORD = password;
           return true;
       }catch (SQLException e){
           System.out.println("Login Failed!");
           e.printStackTrace();
       }
       return false;
    }

    public static Connection getConnection() throws SQLException{
        if(DB_USER==null || DB_USER.isEmpty()){
            System.out.println("You Need To Login First!");
            return null;
        }
        return getConnection(DB_USER,DB_PASSWORD);
    }

    public static Connection getConnection(String username, String password) throws SQLException{
        if(con!=null && !con.isClosed())
            return con;
        else {
            try {
                Class.forName("org.postgresql.Driver");
                con = DriverManager.getConnection("jdbc:postgresql://"+ DB_IP +":"+DB_PORT+"/"+DB_NAME, username, password);
                return con;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Can't Connect To Server");
        return null;
    }

    public static ResultSet query(String query, boolean isUpdate) throws SQLException {
        Statement stmnt = getConnection().createStatement();
        System.out.println("Executing query: "+query);
        if(isUpdate)
            stmnt.executeUpdate(query);
        else
            return stmnt.executeQuery(query);

        stmnt.close();
        close();


        return null;
    }

    public static ResultSet query(String query) throws SQLException{
        return query(query,false);
    }

    public static ResultSet callf(String function, String ... params) throws SQLException{
        String query = "SELECT "+function+"(";

        query+=params[0];

        if(params.length==1)
            return query(query+")");

        for (int i = 1; i < params.length-1; i++) {
            query+=params[i]+",";
        }

        query+=params[params.length-1]+")";

        return query(query);
    }

    public static ResultSet callp(String procedure, String ... params) throws SQLException{
        String query = "CALL " + procedure+"(";

        query+=params[0];

        if(params.length==1)
            return query(query+")");

        for (int i = 1; i < params.length-1; i++) {
            query+=params[i]+",";
        }

        query+=params[params.length-1]+")";

        return query(query);
    }

    public static void close() throws SQLException{
        if(con!=null && !con.isClosed()){
            con.close();
        }
    }

}
