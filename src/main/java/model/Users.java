package model;

public class Users {
  private int id;
    private String username;
    private String passwordHash;
    private String email;
    private int roleId;
    private String createdAt;
    private byte[] avatar;
    private byte[] featureFace;

    //constructor for all fields
    public Users(int id, String username, String passwordHash, String email, int roleId, String createdAt, byte[] avatar, byte[] featureFace) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.avatar = avatar;
        this.featureFace = featureFace;
    }

    //constructor for all fields except id and created_at
    public Users(String username, String passwordHash, String email, int roleId, byte[] avatarUrl, byte[] featureFaceUrl) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.roleId = roleId;
        this.avatar = avatar;
        this.featureFace = featureFace;
    }

    public Users() {
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public byte[] getFeatureFace() {
        return featureFace;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setFeatureFace(byte[] featureFace) {
        this.featureFace = featureFace;
    }
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", roleId=" + roleId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

}
