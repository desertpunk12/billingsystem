package utils;

import java.sql.*;
import java.util.ArrayList;

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

    public static ResultSet callf(String function, Object ... params) throws SQLException{
        return query(queryJoinParams("SELECT "+function,params));
    }

    public static ResultSet callp(String procedure, Object ... params) throws SQLException{
        return query(queryJoinParams("CALL "+procedure,params));
    }

    public static String queryJoinParams(String porfName,Object[] params){
        String query = porfName;

        query += "(" + params[0];
        for (int i = 1; i < params.length; i++)
            query += "," + params[i];

        return query + ")";

    }

    public static String getOneStringFromResultSet(ResultSet rs) throws SQLException{
        rs.next();
        return rs.getString(1);
    }


    public static int getOneIntFromResultSet(ResultSet rs) throws SQLException{
        rs.next();
        return rs.getInt(1);
    }


    public static void close() throws SQLException{
        if(con!=null && !con.isClosed()){
            con.close();
        }
    }

}
