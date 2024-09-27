package model;

public class Users {
  private int id;
    private String username;
    private String passwordHash;
    private String email;
    private int roleId;
    private String createdAt;
    private String avatarUrl;
    private String featureFaceUrl;

    //constructor for all fields
    public Users(int id, String username, String passwordHash, String email, int roleId, String createdAt, String avatarUrl, String featureFaceUrl) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
        this.featureFaceUrl = featureFaceUrl;
    }

    //constructor for all fields except id and created_at
    public Users(String username, String passwordHash, String email, int roleId, String avatarUrl, String featureFaceUrl) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.roleId = roleId;
        this.avatarUrl = avatarUrl;
        this.featureFaceUrl = featureFaceUrl;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFeatureFaceUrl() {
        return featureFaceUrl;
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

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setFeatureFaceUrl(String featureFaceUrl) {
        this.featureFaceUrl = featureFaceUrl;
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
                ", avatarUrl='" + avatarUrl + '\'' +
                ", featureFaceUrl='" + featureFaceUrl + '\'' +
                '}';
    }

}
