package entity;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class Registrations {
    private int id;
    private int userId;
    private int subjectId;
    private Timestamp registrationDate;
    private Timestamp expirationDate;
    private String status;
    private BigDecimal pricePaid;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Registrations() {}

    // Parameterized constructor
    public Registrations(int id, int userId, int subjectId, Timestamp registrationDate,
                        Timestamp expirationDate, String status, BigDecimal pricePaid,
                        Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.subjectId = subjectId;
        this.registrationDate = registrationDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.pricePaid = pricePaid;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        this.pricePaid = pricePaid;
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
        return "Registration{" +
                "id=" + id +
                ", userId=" + userId +
                ", subjectId=" + subjectId +
                ", registrationDate=" + registrationDate +
                ", expirationDate=" + expirationDate +
                ", status='" + status + '\'' +
                ", pricePaid=" + pricePaid +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
