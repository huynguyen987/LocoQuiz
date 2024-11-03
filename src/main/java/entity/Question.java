package entity;

import java.util.List;

public class Question {
    private int sequence;
    private String questionText;
    private List<String> options; // Changed from List<Object> to List<String>
    private String correctAnswer;

    public Question(int sequence, String questionText, String correctAnswer, List<String> options) {
        this.sequence = sequence;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public Question() {

    }

    // Getters and setters
    public int getSequence() { return sequence; }
    public void setSequence(int sequence) { this.sequence = sequence; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

}
