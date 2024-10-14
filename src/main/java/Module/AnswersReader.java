package Module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnswersReader {
    private int sequence;
    private String correct;
    private List<String> options;
    private String question;

    // Constructors
    public AnswersReader() {}

    public AnswersReader(int sequence, String correct, List<String> options, String question) {
        this.sequence = sequence;
        this.correct = correct;
        this.options = options;
        this.question = question;
    }

    // Getters and Setters

    @JsonIgnore // Exclude 'correct' from serialization
    @JsonProperty("correct")
    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    @JsonProperty("options")
    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonProperty("sequence")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    // toString
    @Override
    public String toString() {
        return "AnswersReader{" +
                "sequence=" + sequence +
                ", correct='" + correct + '\'' +
                ", options=" + options +
                ", question='" + question + '\'' +
                '}';
    }
}
