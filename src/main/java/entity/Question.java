package entity;

import java.util.List;

public class Question {
    private int sequence;
    private String question;
    private List<String> options; // Changed from List<Object> to List<String>
    private String correct;

    public Question(int sequence, String question, String correct, List<String> options) {
        this.sequence = sequence;
        this.question = question;
        this.correct = correct;
        this.options = options;
    }

    public Question() {

    }

    // Getters and setters
    public int getSequence() { return sequence; }
    public void setSequence(int sequence) { this.sequence = sequence; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getCorrect() { return correct; }
    public void setCorrect(String correct) { this.correct = correct; }

}
