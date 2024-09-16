package entity;

import java.sql.Timestamp;

public class UserAnswers {
    private int id;
    private int userQuizId;
    private int questionId;
    private String selectedAnswer;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public UserAnswers() {}

    // Parameterized constructor
    public UserAnswers(int id, int userQuizId, int questionId, String selectedAnswer,
                      Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userQuizId = userQuizId;
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
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

    public int getUserQuizId() {
        return userQuizId;
    }

    public void setUserQuizId(int userQuizId) {
        this.userQuizId = userQuizId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
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
        return "UserAnswer{" +
                "id=" + id +
                ", userQuizId=" + userQuizId +
                ", questionId=" + questionId +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
