package entity;

public class results {
//    CREATE TABLE result(
//                       id INT PRIMARY KEY AUTO_INCREMENT,
//                       user_id INT NOT NULL,
//                       quiz_id INT NOT NULL,
//                       class_id INT NOT NULL,
//                       score FLOAT NOT NULL,
//                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//                       FOREIGN KEY (user_id) REFERENCES users(id),
//                       FOREIGN KEY (quiz_id) REFERENCES quiz(id),
//                       FOREIGN KEY (class_id) REFERENCES class(id)
//);
    private int id;
    private int user_id;
    private int quiz_id;
    private int class_id;
    private float score;
    private String created_at;

    public results(int id, int user_id, int quiz_id, int class_id, float score, String created_at) {
        this.id = id;
        this.user_id = user_id;
        this.quiz_id = quiz_id;
        this.class_id = class_id;
        this.score = score;
        this.created_at = created_at;
    }
    public results(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "results{" + "id=" + id + ", user_id=" + user_id + ", quiz_id=" + quiz_id + ", class_id=" + class_id + ", score=" + score + ", created_at=" + created_at + '}';
    }
    
}


