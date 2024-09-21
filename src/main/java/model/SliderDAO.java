package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entity.Slider;
import Module.DBConnect;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SliderDAO {

    // Get all sliders
    public List<Slider> getAllSliders() {
        List<Slider> sliders = new ArrayList<>();
        String sql = "SELECT * FROM Sliders";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                Slider slider = new Slider();
                slider.setId(rs.getInt("id"));
                slider.setTitle(rs.getString("title"));
                slider.setImageUrl(rs.getString("image_url"));
                slider.setLinkUrl(rs.getString("link_url"));
                slider.setOrder(rs.getInt("order"));
                slider.setStatus(rs.getString("status"));
                slider.setCreatedAt(rs.getTimestamp("created_at"));
                slider.setUpdatedAt(rs.getTimestamp("updated_at"));
                sliders.add(slider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return sliders;
    }

    // Update slider
    public boolean updateSlider(Slider slider) {
        int n = 0;
        String sql = "UPDATE Sliders SET title = ?, image_url = ?, link_url = ?, `order` = ?, status = ? WHERE id = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, slider.getTitle());
            pre.setString(2, slider.getImageUrl());
            pre.setString(3, slider.getLinkUrl());
            pre.setInt(4, slider.getOrder());
            pre.setString(5, slider.getStatus());
            pre.setInt(6, slider.getId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Delete slider
    public boolean deleteSlider(int id) {
        int n = 0;
        String sql = "DELETE FROM Sliders WHERE id = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Add slider
    public boolean addSlider(Slider slider) {
        int n = 0;
        String sql = "INSERT INTO Sliders (title, image_url, link_url, `order`, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, slider.getTitle());
            pre.setString(2, slider.getImageUrl());
            pre.setString(3, slider.getLinkUrl());
            pre.setInt(4, slider.getOrder());
            pre.setString(5, slider.getStatus());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }
    //main method
    public static void main(String[] args) {
        SliderDAO sliderDAO = new SliderDAO();
        List<Slider> sliders = sliderDAO.getAllSliders();
        for (Slider slider : sliders) {
            System.out.println(slider.getId() + " " + slider.getTitle() + " " + slider.getImageUrl() + " " + slider.getLinkUrl() + " " + slider.getOrder() + " " + slider.getStatus() + " " + slider.getCreatedAt() + " " + slider.getUpdatedAt());
        }
    }
}
