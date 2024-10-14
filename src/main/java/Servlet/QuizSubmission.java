package Servlet;

import java.util.Map;

public class QuizSubmission {
    private int quizId;
    private Map<String, String> userAnswers;

    // Getters và Setters
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Map<String, String> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(Map<String, String> userAnswers) {
        this.userAnswers = userAnswers;
    }
}
