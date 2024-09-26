package model;

public class Types {
    private int id;
    private String name;
    private String description;

    public Types(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Types(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Types() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Types{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
