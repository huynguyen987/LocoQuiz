package entity;
import java.sql.Timestamp;

public class Quizzes {
    private int id;
    private int subjectId;
    private String title;
    private String description;
    private int timeLimit;
    private double passingScore;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Quizzes() {}

    // Parameterized constructor
    public Quizzes(int id, int subjectId, String title, String description, int timeLimit,
                   double passingScore, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.title = title;
        this.description = description;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public double getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(double passingScore) {
        this.passingScore = passingScore;
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
        return "Quiz{" +
                "id=" + id +
                ", subjectId=" + subjectId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timeLimit=" + timeLimit +
                ", passingScore=" + passingScore +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
