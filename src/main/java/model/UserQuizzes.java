package model;

public class UserQuizzes {
    private int user_id;
    private int quiz_id;
    private int tag_id;

    public UserQuizzes() {
    }

    public UserQuizzes(int user_id, int quiz_id, int tag_id) {
        this.user_id = user_id;
        this.quiz_id = quiz_id;
        this.tag_id = tag_id;
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

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return "UserQuizzes{" +
                "user_id=" + user_id +
                ", quiz_id=" + quiz_id +
                ", tag_id=" + tag_id +
                '}';
    }

}
