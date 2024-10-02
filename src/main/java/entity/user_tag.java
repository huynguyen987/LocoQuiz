package entity;

public class user_tag {

    private int id;
    private int user_id;
    private int tag_id;

    public user_tag() {
    }

    public user_tag(int id, int user_id, int tag_id) {
        this.id = id;
        this.user_id = user_id;
        this.tag_id = tag_id;
    }

    public user_tag(int user_id, int tag_id) {
        this.user_id = user_id;
        this.tag_id = tag_id;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return "user_tag{" + "id=" + id + ", user_id=" + user_id + ", tag_id=" + tag_id + '}';
    }
    
}
