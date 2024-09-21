package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Module.DBConnect;
import entity.Registrations;

public class RegistrationsDAO {

    // Get all registrations
    public ArrayList<Registrations> getAllRegistrations() {
        ArrayList<Registrations> registrations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "SELECT * FROM Registrations";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Registrations registration = new Registrations();
                registration.setId(rs.getInt("id"));
                registration.setUserId(rs.getInt("user_id"));
                registration.setSubjectId(rs.getInt("subject_id"));
                registration.setRegistrationDate(rs.getTimestamp("registration_date"));
                registration.setExpirationDate(rs.getTimestamp("expiration_date"));
                registration.setStatus(rs.getString("status"));
                registration.setPricePaid(rs.getBigDecimal("price_paid"));
                registration.setCreatedAt(rs.getTimestamp("created_at"));
                registration.setUpdatedAt(rs.getTimestamp("updated_at"));
                registrations.add(registration);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return registrations;
    }

    // Get registration by id
    public Registrations getRegistrationById(int id) {
        Registrations registration = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "SELECT * FROM Registrations WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                registration = new Registrations();
                registration.setId(rs.getInt("id"));
                registration.setUserId(rs.getInt("user_id"));
                registration.setSubjectId(rs.getInt("subject_id"));
                registration.setRegistrationDate(rs.getTimestamp("registration_date"));
                registration.setExpirationDate(rs.getTimestamp("expiration_date"));
                registration.setStatus(rs.getString("status"));
                registration.setPricePaid(rs.getBigDecimal("price_paid"));
                registration.setCreatedAt(rs.getTimestamp("created_at"));
                registration.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return registration;
    }

    // Add registration
    public boolean addRegistration(Registrations registration) {
        int n = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "INSERT INTO Registrations(user_id, subject_id, registration_date, expiration_date, status, price_paid) VALUES(?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, registration.getUserId());
            ps.setInt(2, registration.getSubjectId());
            ps.setTimestamp(3, registration.getRegistrationDate());
            ps.setTimestamp(4, registration.getExpirationDate());
            ps.setString(5, registration.getStatus());
            ps.setBigDecimal(6, registration.getPricePaid());
            n = ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println(ex);
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return n > 0;
    }

    // Update registration
    public boolean updateRegistration(Registrations registration) {
        int n = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "UPDATE Registrations SET user_id = ?, subject_id = ?, registration_date = ?, expiration_date = ?, status = ?, price_paid = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, registration.getUserId());
            ps.setInt(2, registration.getSubjectId());
            ps.setTimestamp(3, registration.getRegistrationDate());
            ps.setTimestamp(4, registration.getExpirationDate());
            ps.setString(5, registration.getStatus());
            ps.setBigDecimal(6, registration.getPricePaid());
            ps.setInt(7, registration.getId());
            n = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return n > 0;
    }

    // Delete registration
    public boolean deleteRegistration(int id) {
        int n = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "DELETE FROM Registrations WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            n = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return n > 0;
    }
    //main method
    public static void main(String[] args) {
        RegistrationsDAO dao = new RegistrationsDAO();
        ArrayList<Registrations> registrations = dao.getAllRegistrations();
        for (Registrations registration : registrations) {
            System.out.println(registration.getId() + " " + registration.getUserId() + " " + registration.getSubjectId() + " " + registration.getRegistrationDate() + " " + registration.getExpirationDate() + " " + registration.getStatus() + " " + registration.getPricePaid() + " " + registration.getCreatedAt() + " " + registration.getUpdatedAt());
        }
    }
}
