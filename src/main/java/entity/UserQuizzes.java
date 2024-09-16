package entity;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class UserQuizzes {
    private int id;
    private int userId;
    private int quizId;
    private Timestamp startTime;
    private Timestamp endTime;
    private BigDecimal score;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public UserQuizzes() {}

    // Parameterized constructor
    public UserQuizzes(int id, int userId, int quizId, Timestamp startTime, Timestamp endTime,
                    BigDecimal score, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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

    // toString method for debugging
    @Override
    public String toString() {
        return "UserQuiz{" +
                "id=" + id +
                ", userId=" + userId +
                ", quizId=" + quizId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", score=" + score +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

