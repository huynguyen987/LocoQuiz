package entity;

public class User {
//    CREATE TABLE user(
//            id INT PRIMARY KEY AUTO_INCREMENT,
//            username VARCHAR(255) NOT NULL,
//    password VARCHAR(255) NOT NULL,
//    mail VARCHAR(255) NOT NULL,
//    role_id INT NOT NULL,
//    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    avatar MEDIUMBLOB NULL,
//    feature_face MEDIUMBLOB NULL,
//    FOREIGN KEY(role_id) REFERENCES role(id)
//            );
    private int id;
    private String username;
    private String password;
    private String mail;
    private int role_id;
    private String created_at;
    private byte[] avatar;
    private byte[] feature_face;

    public User() {
    }

    public User(int id, String username, String password, String mail, int role_id, String created_at, byte[] avatar, byte[] feature_face) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.role_id = role_id;
        this.created_at = created_at;
        this.avatar = avatar;
        this.feature_face = feature_face;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public byte[] getFeature_face() {
        return feature_face;
    }

    public void setFeature_face(byte[] feature_face) {
        this.feature_face = feature_face;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", role_id=" + role_id +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
