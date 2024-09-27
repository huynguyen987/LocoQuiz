package model;

public class Results {
   private int id;
    private int user_id;
    private int quiz_id;
    private int class_id;
    private double score;
    private String created_at;

    public Results(int id, int user_id, int quiz_id, int class_id, double score, String created_at) {
        this.id = id;
        this.user_id = user_id;
        this.quiz_id = quiz_id;
        this.class_id = class_id;
        this.score = score;
        this.created_at = created_at;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
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
        return "Results{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", quiz_id=" + quiz_id +
                ", class_id=" + class_id +
                ", score=" + score +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
