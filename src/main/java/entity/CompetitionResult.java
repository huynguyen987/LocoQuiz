package entity;

import java.sql.Timestamp;
import java.util.Date;

public class CompetitionResult {
    private int id;
    private int competitionId;
    private int userId;
    private int classId;
    private float score;
    private int timeTaken;
    private Timestamp createdAt;

    // Constructors
    public CompetitionResult() {}

    public CompetitionResult(int id, int competitionId, int userId, int classId, float score, int timeTaken, Timestamp createdAt) {
        this.id = id;
        this.competitionId = competitionId;
        this.userId = userId;
        this.classId = classId;
        this.score = score;
        this.timeTaken = timeTaken;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CompetitionResult{" +
                "id=" + id +
                ", competitionId=" + competitionId +
                ", userId=" + userId +
                ", classId=" + classId +
                ", score=" + score +
                ", createdAt=" + createdAt +
                '}';
    }
}
