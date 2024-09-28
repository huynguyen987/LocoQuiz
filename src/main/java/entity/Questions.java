package entity;

import java.sql.Timestamp;
import java.util.List;

public class Questions {
    private int id;
    private int quizId;
    private String content;
    private String questionType;
    private String difficultyLevel;
    private String explanation;
    private List<String> answers; // Danh sách các đáp án
    private int order;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor đầy đủ
    public Questions(int id, int quizId, String content, String questionType,
                    String difficultyLevel, String explanation, List<String> answers,
                    int order, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.quizId = quizId;
        this.content = content;
        this.questionType = questionType;
        this.difficultyLevel = difficultyLevel;
        this.explanation = explanation;
        this.answers = answers;
        this.order = order;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor không có id (sử dụng khi tạo mới câu hỏi)
    public Questions(int quizId, String content, String questionType,
                    String difficultyLevel, String explanation, List<String> answers,
                    int order) {
        this.quizId = quizId;
        this.content = content;
        this.questionType = questionType;
        this.difficultyLevel = difficultyLevel;
        this.explanation = explanation;
        this.answers = answers;
        this.order = order;
    }

    // Getters và Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
}

