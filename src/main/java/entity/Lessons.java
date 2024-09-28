package entity;

import java.sql.Timestamp;

public class Lessons {
    private int id;
    private int subjectId;
    private String title;
    private String content;
    private int order;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private byte[] avatar;

    // Default constructor
    public Lessons() {}

    // Constructor đầy đủ
    public Lessons(int id, int subjectId, String title, String content, int order,
                  String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.title = title;
        this.content = content;
        this.order = order;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor không có id và timestamps (sử dụng khi tạo mới)
    public Lessons(int subjectId, String title, String content, int order, String status) {
        this.subjectId = subjectId;
        this.title = title;
        this.content = content;
        this.order = order;
        this.status = status;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Lessons{" + "id=" + id + ", subjectId=" + subjectId + ", title=" + title + ", content=" + content + ", order=" + order + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", avatar=" + avatar + '}';
    }

   
    
    

   


   

}
