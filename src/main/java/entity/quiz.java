package entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class quiz {

    private int id;
    private String name;
    private String description;
    private String created_at;
    private String updated_at;
    private int user_id;
    private int type_id;
    private String answer;
    private boolean status;
    private int views;
    private List<Tag> tag;

    public quiz() {
    }

    private static final Gson gson = new Gson();

    // Constructor có thêm tham số tag
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

    // Constructor không có tham số tag
    public quiz(String name, String description, String created_at, String updated_at, int user_id, int type_id, String answer) {
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.type_id = type_id;
        this.answer = answer;
    }

    // Phương thức để phân tích JSON thành danh sách câu hỏi
    public List<Map<String, Object>> getQuestions() {
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        return gson.fromJson(this.answer, listType);
    }

    // Phương thức để thiết lập JSON từ danh sách câu hỏi
    public void setQuestions(List<Map<String, Object>> questions) {
        this.answer = gson.toJson(questions);
    }

    // Phương thức để lấy tên loại quiz dựa trên typeId
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

    // Getters và Setters cho các trường khác
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
}
