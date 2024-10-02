package entity;

public class role {
//    CREATE TABLE role(
//                     id INT PRIMARY KEY AUTO_INCREMENT,
//                     name VARCHAR(255) NOT NULL,
//                     description VARCHAR(255) DEFAULT NULL
//);
    private int id;
    private String name;
    private String description;

    public role() {
    }

    public role(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "role{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }

   
}
