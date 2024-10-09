package Module;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public List<AnswersReader> getAnswersList(String jsonArray) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Deserialize JSON array to List<AnswersReader>
            List<AnswersReader> answersList = mapper.readValue(jsonArray, new TypeReference<List<AnswersReader>>() {});
            return answersList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Test
    public static void main(String[] args) {
        String jsonArray = "[{\"sequence\": 1, \"correct\": \"4\", \"options\": [\"3\", \"4\", \"5\", \"6\"], \"question\": \"What is 2+2?\"}, {\"sequence\": 2, \"correct\": \"5\", \"options\": [\"3\", \"5\", \"10\", \"15\"], \"question\": \"Solve for x: x + 5 = 10.\"}]";

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<AnswersReader> answersList = mapper.readValue(jsonArray, new TypeReference<List<AnswersReader>>() {});
//            giải thích các tham số của hàm readValue
//            jsonArray: chuỗi JSON cần chuyển đổi
//            new TypeReference<List<AnswersReader>>() {}: đối tượng TypeReference chứa thông tin về kiểu dữ liệu cần chuyển đổi
//            List<AnswersReader>: kiểu dữ liệu cần chuyển đổi
//            {} là cú pháp khởi tạo đối tượng TypeReference

            for (AnswersReader answer : answersList) {
                System.out.println(answer.getQuestion()+": "+answer.getOptions());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
