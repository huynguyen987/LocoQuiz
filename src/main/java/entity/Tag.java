package entity;

public class Tag {
//    CREATE TABLE Tag(
//                    id INT PRIMARY KEY AUTO_INCREMENT,
//                    name VARCHAR(255) NOT NULL,
//                    description VARCHAR(255) NOT NULL
//);
    private int id;
    private String name;
    private String description;

    public Tag() {
    }

    public Tag(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Tag(String name, String description) {
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
        return "Tag{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }
    
    
    

   
}
