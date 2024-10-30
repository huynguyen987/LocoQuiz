package entity;

public class Flashcard {
    private int id;
    private String frontText;
    private String backText;
    private String category;
    private String createdAt;
    private String updatedAt;
    private int correctAnswers;
    private int incorrectAnswers;

    public Flashcard() {}

    public Flashcard(int id, String frontText, String backText, String category, String createdAt, String updatedAt, int correctAnswers, int incorrectAnswers) {
        this.id = id;
        this.frontText = frontText;
        this.backText = backText;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
    }

    public Flashcard(int id, String frontText, String backText, String category) {

    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFrontText() { return frontText; }
    public void setFrontText(String frontText) { this.frontText = frontText; }

    public String getBackText() { return backText; }
    public void setBackText(String backText) { this.backText = backText; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getIncorrectAnswers() { return incorrectAnswers; }
    public void setIncorrectAnswers(int incorrectAnswers) { this.incorrectAnswers = incorrectAnswers; }

    @Override
    public String toString() {
        return "Flashcard{" +
                "id=" + id +
                ", frontText='" + frontText + '\'' +
                ", backText='" + backText + '\'' +
                ", category='" + category + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", correctAnswers=" + correctAnswers +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }
}
