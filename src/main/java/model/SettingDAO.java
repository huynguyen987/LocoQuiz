package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Module.DBConnect;
import entity.Setting;

public class SettingDAO {

    // Get all settings
    public ArrayList<Setting> getAllSettings() {
        ArrayList<Setting> settings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBConnect().getConnection();
            String sql = "SELECT * FROM Settings";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Setting setting = new Setting();
                setting.setId(rs.getInt("id"));
                setting.setSettingKey(rs.getString("setting_key"));
                setting.setSettingValue(rs.getString("setting_value"));
                setting.setSettingType(rs.getString("setting_type"));
                setting.setDescription(rs.getString("description"));
                setting.setUserRole(rs.getString("user_role"));
                setting.setCreatedAt(rs.getTimestamp("created_at"));
                setting.setUpdatedAt(rs.getTimestamp("updated_at"));
                settings.add(setting);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { System.err.println(ex); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { System.err.println(ex); }
        }
        return settings;
    }

    // Update setting
    public boolean updateSetting(Setting setting) {
        int n = 0;
        String sql = "UPDATE Settings SET setting_value = ?, setting_type = ?, description = ?, user_role = ? WHERE setting_key = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, setting.getSettingValue());
            pre.setString(2, setting.getSettingType());
            pre.setString(3, setting.getDescription());
            pre.setString(4, setting.getUserRole());
            pre.setString(5, setting.getSettingKey());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Remove setting
    public boolean removeSetting(String settingKey) {
        int n = 0;
        String sql = "DELETE FROM Settings WHERE setting_key = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, settingKey);
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Add setting
    public boolean addSetting(Setting setting) {
        int n = 0;
        String sql = "INSERT INTO Settings (setting_key, setting_value, setting_type, description, user_role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, setting.getSettingKey());
            pre.setString(2, setting.getSettingValue());
            pre.setString(3, setting.getSettingType());
            pre.setString(4, setting.getDescription());
            pre.setString(5, setting.getUserRole());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    //main method
    public static void main(String[] args) {
        SettingDAO settingDAO = new SettingDAO();
        ArrayList<Setting> settings = settingDAO.getAllSettings();
        for (Setting setting : settings) {
            System.out.println(setting.getId() + " " + setting.getSettingKey() + " " + setting.getSettingValue() + " " + setting.getSettingType() + " " + setting.getDescription() + " " + setting.getUserRole() + " " + setting.getCreatedAt() + " " + setting.getUpdatedAt());
        }
    }
}
