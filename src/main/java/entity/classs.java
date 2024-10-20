package entity;

public class classs {
//    CREATE TABLE class(
//                      id INT PRIMARY KEY AUTO_INCREMENT,
//                      name VARCHAR(255) NOT NULL,
//                      class_key VARCHAR(255) NOT NULL,
//                      description VARCHAR(255) NOT NULL,
//                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//                      teacher_id INT NOT NULL,
//                      FOREIGN KEY (teacher_id) REFERENCES users(id)
//);
    private int id;
    private String name;
    private String class_key;
    private String description;
    private String created_at;
    private String updated_at;
    private int teacher_id;
    private String teacher_name; 


    public classs() {
    }

    public classs(int id, String name, String class_key, String description, String created_at, String updated_at, int teacher_id) {
        this.id = id;
        this.name = name;
        this.class_key = class_key;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
    }

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

    public String getClass_key() {
        return class_key;
    }

    public void setClass_key(String class_key) {
        this.class_key = class_key;
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

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    @Override
    public String toString() {
        return "classs{" + "id=" + id + ", name=" + name + ", class_key=" + class_key + ", description=" + description + ", created_at=" + created_at + ", updated_at=" + updated_at + ", teacher_id=" + teacher_id + '}';
    }

   
}
