package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Module.DBConnect;
import entity.PricePackage;

public class PricePackageDAO {

    // Get all price packages
    public static ArrayList<PricePackage> getAllPricePackages() {
        ArrayList<PricePackage> pricePackages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "SELECT * FROM PricePackages";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                PricePackage pricePackage = new PricePackage();
                pricePackage.setId(rs.getInt("id"));
                pricePackage.setSubjectId(rs.getInt("subject_id"));
                pricePackage.setName(rs.getString("name"));
                pricePackage.setDescription(rs.getString("description"));
                pricePackage.setPrice(rs.getBigDecimal("price"));
                pricePackage.setDurationMonths(rs.getInt("duration_months"));
                pricePackage.setStatus(rs.getString("status"));
                pricePackages.add(pricePackage);
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
        return pricePackages;
    }

    // Get price package by id
    public static PricePackage getPricePackageById(int id) {
        PricePackage pricePackage = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "SELECT * FROM PricePackages WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                pricePackage = new PricePackage();
                pricePackage.setId(rs.getInt("id"));
                pricePackage.setSubjectId(rs.getInt("subject_id"));
                pricePackage.setName(rs.getString("name"));
                pricePackage.setDescription(rs.getString("description"));
                pricePackage.setPrice(rs.getBigDecimal("price"));
                pricePackage.setDurationMonths(rs.getInt("duration_months"));
                pricePackage.setStatus(rs.getString("status"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println(ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return pricePackage;
    }

    // Add price package
    public static int addPricePackage(PricePackage pricePackage) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = new DBConnect().getConnection();
            String sql = "INSERT INTO PricePackages (subject_id, name, description, price, duration_months, status) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pricePackage.getSubjectId());
            ps.setString(2, pricePackage.getName());
            ps.setString(3, pricePackage.getDescription());
            ps.setBigDecimal(4, pricePackage.getPrice());
            ps.setInt(5, pricePackage.getDurationMonths());
            ps.setString(6, pricePackage.getStatus());
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return result;
    }

    // Update price package
    public static int updatePricePackage(PricePackage pricePackage) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = new DBConnect().getConnection();
            String sql = "UPDATE PricePackages SET subject_id = ?, name = ?, description = ?, price = ?, duration_months = ?, status = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pricePackage.getSubjectId());
            ps.setString(2, pricePackage.getName());
            ps.setString(3, pricePackage.getDescription());
            ps.setBigDecimal(4, pricePackage.getPrice());
            ps.setInt(5, pricePackage.getDurationMonths());
            ps.setString(6, pricePackage.getStatus());
            ps.setInt(7, pricePackage.getId());
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return result;
    }

    // Delete price package
    public static int deletePricePackage(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = new DBConnect().getConnection();
            String sql = "DELETE FROM PricePackages WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return result;
    }

    // Get price package by price
    public static PricePackage getPricePackageByPrice(double price) {
        PricePackage pricePackage = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "SELECT * FROM PricePackages WHERE price = ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, price);
            rs = ps.executeQuery();
            if (rs.next()) {
                pricePackage = new PricePackage();
                pricePackage.setId(rs.getInt("id"));
                pricePackage.setSubjectId(rs.getInt("subject_id"));
                pricePackage.setName(rs.getString("name"));
                pricePackage.setDescription(rs.getString("description"));
                pricePackage.setPrice(rs.getBigDecimal("price"));
                pricePackage.setDurationMonths(rs.getInt("duration_months"));
                pricePackage.setStatus(rs.getString("status"));
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
        return pricePackage;
    }

    public static void main(String[] args) {
        System.out.println(getAllPricePackages());
    }
}
