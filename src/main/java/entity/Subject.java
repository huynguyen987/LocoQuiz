package entity;

import java.sql.Timestamp;
import java.util.List;

public class Subject {
    private int id;
    private String title;
    private String description;
    private String category;
    private String thumbnailUrl;
    private String status;
    private int createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private byte[] avatar;
    private List<Lessons> lessons;

    public Subject(int id, String title, String description, String category,
                   String thumbnailUrl, String status, int createdBy, byte[] avatar,
                   Timestamp createdAt, Timestamp updatedAt, List<Lessons> lessons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.status = status;
        this.createdBy = createdBy;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lessons = lessons;
    }

    // Constructor không có id và timestamps (sử dụng khi tạo mới)
    public Subject(String title, String description, String category,
                   String thumbnailUrl, String status, int createdBy, byte[] avatar) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.status = status;
        this.createdBy = createdBy;
        this.avatar = avatar;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}