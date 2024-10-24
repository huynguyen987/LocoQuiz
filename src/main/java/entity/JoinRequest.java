package entity;

import java.sql.Timestamp;

public class JoinRequest {
    private int classId;
    private int userId;
    private String username;
    private String email;
    private Timestamp requestedAt;

    public JoinRequest() {
    }

    public JoinRequest(int classId, int userId, String username, String email, Timestamp requestedAt) {
        this.classId = classId;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.requestedAt = requestedAt;
    }

    // Getters and Setters

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Timestamp requestedAt) {
        this.requestedAt = requestedAt;
    }

    @Override
    public String toString() {
        return "JoinRequest{" +
                "classId=" + classId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", requestedAt=" + requestedAt +
                '}';
    }
}
