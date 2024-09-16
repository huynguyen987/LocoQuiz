package entity;

public class roles {
    private int id;
    private String roleName;

    public roles() {
    }

    public roles(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "roles{" + "id=" + id + ", roleName=" + roleName + '}';
    }

}
