// File: src/main/java/Module/AnswersReader.java
package Module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
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

//  shuffle options
    public void shuffleOptions() {
        for (int i = 0; i < options.size(); i++) {
            int randomIndex = (int) (Math.random() * options.size());
            String temp = options.get(i);
            options.set(i, options.get(randomIndex));
            options.set(randomIndex, temp);
        }
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

//    test shuffleOptions
    public static void main(String[] args) {
        AnswersReader answersReader = new AnswersReader(1, "A", new ArrayList<>(List.of("A", "B", "C", "D")), "What is the capital of Vietnam?");
        answersReader.shuffleOptions();
        System.out.println(answersReader);
    }
}
