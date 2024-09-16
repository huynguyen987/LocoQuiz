package entity;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class PricePackage {
    private int id;
    private int subjectId;
    private String name;
    private String description;
    private BigDecimal price;
    private int durationMonths;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public PricePackage() {}

    // Parameterized constructor
    public PricePackage(int id, int subjectId, String name, String description,
                        BigDecimal price, int durationMonths, String status,
                        Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMonths = durationMonths;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "PricePackage{" +
                "id=" + id +
                ", subjectId=" + subjectId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", durationMonths=" + durationMonths +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

