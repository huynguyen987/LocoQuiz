package entity;

public class class_quiz {
//    CREATE TABLE class_quiz(
//                           class_id INT NOT NULL,
//                           quiz_id INT NOT NULL,
//                           FOREIGN KEY (class_id) REFERENCES class(id),
//                           FOREIGN KEY (quiz_id) REFERENCES quiz(id)
//);
    private int id;
    private int class_id;
    private int quiz_id;

    public class_quiz() {
    }

    public class_quiz(int id, int class_id, int quiz_id) {
        this.id = id;
        this.class_id = class_id;
        this.quiz_id = quiz_id;
    }

    public class_quiz(int class_id, int quiz_id) {
        this.class_id = class_id;
        this.quiz_id = quiz_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    @Override
    public String toString() {
        return "class_quiz{" + "id=" + id + ", class_id=" + class_id + ", quiz_id=" + quiz_id + '}';
    }
}
