package entity;

public class class_user {
//    CREATE TABLE class_user(
//                           class_id INT NOT NULL,
//                           user_id INT NOT NULL,
//                           FOREIGN KEY (class_id) REFERENCES class(id),
//                           FOREIGN KEY (user_id) REFERENCES users(id)
//);
    private int id;
    private int class_id;
    private int user_id;

    public class_user() {
    }

    public class_user(int id, int class_id, int user_id) {
        this.id = id;
        this.class_id = class_id;
        this.user_id = user_id;
    }

    public class_user(int class_id, int user_id) {
        this.class_id = class_id;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "class_user{" + "id=" + id + ", class_id=" + class_id + ", user_id=" + user_id + '}';
    }

    
}
