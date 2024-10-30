package entity;

import java.sql.Timestamp;
import java.util.Date;

public class Competition {
    private int id;
    private String name;
    private String description;
    private int classId;
    private int quizId;
    private int timeLimit; // in seconds
    private int questionCount;
    private boolean shuffleQuestions;
    private Date accessStartTime;
    private Date accessEndTime;
    private Timestamp createdAt;

    // Constructors
    public Competition() {}

    public Competition(int id, String name, String description, int classId, int quizId, int timeLimit, int questionCount, boolean shuffleQuestions, Date accessStartTime, Date accessEndTime, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.classId = classId;
        this.quizId = quizId;
        this.timeLimit = timeLimit;
        this.questionCount = questionCount;
        this.shuffleQuestions = shuffleQuestions;
        this.accessStartTime = accessStartTime;
        this.accessEndTime = accessEndTime;
        this.createdAt = createdAt;
    }

    // Getters and Setters

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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public boolean isShuffleQuestions() {
        return shuffleQuestions;
    }

    public void setShuffleQuestions(boolean shuffleQuestions) {
        this.shuffleQuestions = shuffleQuestions;
    }

    public Date getAccessStartTime() {
        return accessStartTime;
    }

    public void setAccessStartTime(Date accessStartTime) {
        this.accessStartTime = accessStartTime;
    }

    public Date getAccessEndTime() {
        return accessEndTime;
    }

    public void setAccessEndTime(Date accessEndTime) {
        this.accessEndTime = accessEndTime;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
