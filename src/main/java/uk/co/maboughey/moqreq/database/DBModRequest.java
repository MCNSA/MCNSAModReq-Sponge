package uk.co.maboughey.moqreq.database;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.type.ModRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBModRequest {

    public static List<ModRequest> getRequests(int status) {
        List<ModRequest> output = new ArrayList<ModRequest>();

        try {
            Connection connection = DatabaseManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM modReq WHERE status=?");
            statement.setInt(1, status);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                ModRequest request = new ModRequest();
                request.id = results.getInt("id");
                request.user = UUID.fromString(results.getString("user"));
                request.message = results.getString("message");
                request.status = results.getInt("status");
                request.response = results.getString("response");
                request.server = results.getString("server");

                //Handle null responder field
                String responder = results.getString("responder");
                if (responder == null) {
                    request.responder = null;
                }
                else {
                    request.responder = UUID.fromString(results.getString("responder"));
                }

                //Location info
                Double pos_x = results.getDouble("pos_x");
                Double pos_y = results.getDouble("pos_y");
                Double pos_z = results.getDouble("pos_z");
                Double rot_x = results.getDouble("rot_x");
                Double rot_y = results.getDouble("rot_y");
                Double rot_z = results.getDouble("rot_z");
                UUID world = UUID.fromString(results.getString("world"));

                request.setLocation(pos_x, pos_y, pos_z, world);
                request.setRotation(rot_x, rot_y, rot_z);

                output.add(request);
            }

        }
        catch (SQLException e) {
            ModReq.log.error("SQL Error retrieving requests: "+e.getMessage());
        }

        return output;
    }
    public static int getCount(int status, UUID uuid) {
        int count = 0;

        try {
            Connection connection = DatabaseManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(id) FROM modReq WHERE status=? AND user=?");
            statement.setInt(1, status);
            statement.setString(2, uuid.toString());
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                count = results.getInt("COUNT(id)");
            }
        }
        catch (SQLException e){
            ModReq.log.error("Sql error getting count "+e.getMessage());
        }
        return count;
    }
    public static int getModCount(int status, UUID uuid) {
        int count = 0;

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = null;
            if (status == 0) {
                statement = connection.prepareStatement("SELECT COUNT(id) FROM modReq WHERE status=?");
            }
            else if (status == 1){
                statement = connection.prepareStatement("SELECT COUNT(id) FROM modReq WHERE status=? AND responder=?");
            }
            statement.setInt(1, status);
            statement.setString(2, uuid.toString());
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                count = results.getInt("COUNT(id)");
            }
        }
        catch (SQLException e){
            ModReq.log.error("Sql error getting count "+e.getMessage());
        }
        return count;
    }
    public static void saveNewRequest(ModRequest request) {
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO modReq (" +
                    "user, " +
                    "message, " +
                    "server, " +
                    "pos_x, " +
                    "pos_y, " +
                    "pos_z, " +
                    "rot_x, " +
                    "rot_y, " +
                    "rot_z"+
                    ") VALUES (?,?,?,?,?,?,?,?,?)");

            statement.setString(1, request.user.toString());
            statement.setString(2, request.message);
            statement.setString(3, request.server);
            statement.setDouble(4, request.location.getX());
            statement.setDouble(5, request.location.getY());
            statement.setDouble(6, request.location.getZ());
            statement.setDouble(7, request.rotation.getX());
            statement.setDouble(8, request.rotation.getY());
            statement.setDouble(9, request.rotation.getZ());

            statement.executeUpdate();
        }
        catch (SQLException e){
            ModReq.log.error("Sql error saving new mod request "+e.getMessage());
        }
    }
    public static void updateRequestDone(ModRequest request){
        try {
            Connection connection = DatabaseManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE modReq SET" +
                    "status=?, " +
                    "responder=?, " +
                    "response=? " +
                    "WHERE id=?");

            statement.setInt(1, request.status);
            statement.setString(2, request.responder.toString());
            statement.setString(3, request.response);
            statement.setInt(4, request.id);

            statement.executeUpdate();
        }
        catch (SQLException e){
            ModReq.log.error("Sql error updating mod request (Completed) "+e.getMessage());
        }
    }
    public static void updateRequestClaimed(ModRequest request) {
        try {
            Connection connection = DatabaseManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE modReq SET" +
                    "status=?, " +
                    "responder=?, " +
                    "WHERE id=?");

            statement.setInt(1, request.status);
            statement.setString(2, request.responder.toString());
            statement.setInt(3, request.id);

            statement.executeUpdate();
        }
        catch (SQLException e){
            ModReq.log.error("Sql error updating mod request (Completed) "+e.getMessage());
        }
    }
    public static void updateRequestRead(ModRequest request) {
        try {
            Connection connection = DatabaseManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE modReq SET" +
                    "status=?, " +
                    "WHERE id=?");

            statement.setInt(1, request.status);
            statement.setInt(2, request.id);

            statement.executeUpdate();
        }
        catch (SQLException e){
            ModReq.log.error("Sql error updating mod request (Completed) "+e.getMessage());
        }
    }
}
