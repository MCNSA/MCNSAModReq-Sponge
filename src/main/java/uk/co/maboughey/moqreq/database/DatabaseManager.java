package uk.co.maboughey.moqreq.database;

import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {

    private static Connection connect;
    private static ResultSet resultSet;
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    public DatabaseManager() {
        //Connect to database
        connect();
        //Create Tables
        createTables();
    }

    public void createTables(){
        //Check connection does exist
        if (connect != null)
            return;

        try {
            //Create the tables if they don't exist
            preparedStatement = connect.prepareStatement("CREATE TABLE IF NOT EXISTS modReq (" +
                    "id int(6) NOT NULL AUTO_INCREMENT, " +
                    "server varchar(50) NOT NULL" +
                    "user varchar(50) NOT NULL, " +
                    "message TEXT NOT NULL, " +
                    "pos_x double NOT NULL, " +
                    "pos_y double NOT NULL, " +
                    "pos_z double NOT NULL, " +
                    "rot_x double NOT NULL, " +
                    "rot_y double NOT NULL, " +
                    "rot_z double NOT NULL, " +
                    "world varchar(50) NOT NULL" +
                    "status int(2) NOT NULL DEFAULT '0', " +
                    "responder varchar(50), " +
                    "response TEXT, " +
                    "PRIMARY KEY (id))");
            /*
            id is the modreq id
            user is the user that created the modreq
            message is the message the user inputted
            pos_x,y,z is the location where the user submitted their request
            rot_x,y,z is the direction they are facing
            status is the request status: 0=open, 1=claimed, 2=closed but unread, 3=closed and read buy the user
            responder is the mod that has either claimed or closed the request
            response is the message given to the user by the responder
             */
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            ModReq.isEnabled = false;
            ModReq.log.error("Error creating tables in database: "+e.getMessage());
        }

    }

    public static Connection getConnection() {
        //Return null if the plugin is disabled
        if (!ModReq.isEnabled)
            return null;

        //Connect to database if the connection is not present
        if (connect == null)
            connect();

        //give us the connection
        return connect;
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties connProperties = new Properties();
            connProperties.put("user", Configuration.DBUser);
            connProperties.put("password", Configuration.DBPassword);
            connProperties.put("autoReconnect", true);
            connProperties.put("maxReconnects","4");

            connect = DriverManager.getConnection(Configuration.getDatabaseString(), connProperties);

            ModReq.log.info("Connection to Database Established");
            ModReq.isEnabled = true;

        }
        catch (SQLException e){
            ModReq.isEnabled = false;
            ModReq.log.error("Database Error connecting to databse: "+ e.getMessage());
            connect = null;
        }
        catch (ClassNotFoundException e) {
            ModReq.isEnabled = false;
            ModReq.log.error("Could not find mysql connector");
            connect = null;
        }
    }

    public static void close() {
        try {
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
            if(preparedStatement != null)
                preparedStatement.close();
            if(connect != null)
                connect.close();

            connect = null;
        }
        catch(Exception e) {
            ModReq.log.error("Database exception during close. Message was: "+e.getMessage());
        }
    }
}
