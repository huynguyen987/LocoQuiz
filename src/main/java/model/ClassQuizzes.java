package model;

public class ClassQuizzes {
    private int class_id;
    private int quiz_id;

     public ClassQuizzes(int class_id, int quiz_id) {
    this.class_id = class_id;
    this.quiz_id = quiz_id;
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
        return "ClassQuizzes{" + "class_id=" + class_id + ", quiz_id=" + quiz_id + '}';
    }


}
