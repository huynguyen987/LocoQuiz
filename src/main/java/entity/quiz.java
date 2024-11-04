package entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class quiz extends Competition {

    private int id;
    private String name;
    private String description;
    private String created_at;
    private String updated_at;
    private int user_id;
    private int type_id;
    private String answer; // JSON string representing the list of questions
    private boolean status;
    private int views;
    private List<Tag> tag;

    private static final Gson gson = new Gson();

    // No-argument constructor
    public quiz() {
    }

    // Constructor with tag
    public quiz(int id, String name, String description, String created_at, String updated_at, int user_id, int type_id, String answer, boolean status, int views, List<Tag> tag) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.type_id = type_id;
        this.answer = answer;
        this.status = status;
        this.views = views;
        this.tag = tag;
    }

    // Constructor without tag
    public quiz(String name, String description, String created_at, String updated_at, int user_id, int type_id, String answer) {
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.type_id = type_id;
        this.answer = answer;
    }

    // Constructor with id and name
    public quiz(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Deserialize the JSON string 'answer' into a List<Question>.
     *
     * @return List of Question objects.
     */
    public List<Question> getQuestions() {
        if (this.answer == null || this.answer.isEmpty()) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<List<Question>>() {}.getType();
        return gson.fromJson(this.answer, listType);
    }

    /**
     * Serialize a List<Question> into a JSON string and set it to 'answer'.
     *
     * @param questions List of Question objects.
     */
    public void setQuestions(List<Map<String, Object>> questions) {
        this.answer = gson.toJson(questions);
    }

    /**
     * Get the type name based on type_id.
     *
     * @return Type name as a String.
     */
    public String getTypeName() {
        switch(this.getType_id()) {
            case 1:
                return "Multiple Choice";
            case 2:
                return "Fill in the Blank";
            case 3:
                return "Matching";
            default:
                return "Unknown";
        }
    }

    // Getters and Setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "quiz{" + "id=" + id + ", name=" + name + ", description=" + description + ", created_at=" + created_at + ", updated_at=" + updated_at + ", user_id=" + user_id + ", type_id=" + type_id + ", answer=" + answer + ", status=" + status + ", views=" + views + ", tag=" + tag + '}';
    }

    // Test method
    public static void main(String[] args) {
        quiz q = new quiz();
        // Initialize a sample JSON string for 'answer'
        String sampleAnswerJson = "["
                + "{\"sequence\":1,\"question\":\"What is the capital of France?\",\"correct\":\"Paris\",\"options\":[\"Paris\",\"London\",\"Berlin\",\"Rome\"]},"
                + "{\"sequence\":2,\"question\":\"What is 2 + 2?\",\"correct\":\"4\",\"options\":[\"3\",\"4\",\"5\",\"6\"]}"
                + "]";
        q.setAnswer(sampleAnswerJson); // Set the 'answer' field with JSON

        List<Question> questions = q.getQuestions();
        for (Question question : questions) {
            System.out.println("Sequence: " + question.getSequence());
            System.out.println("Options: " + question.getOptions());
            System.out.println("Correct Answer: " + question.getCorrect());
            System.out.println("-----------------------------");
        }
    }
}
