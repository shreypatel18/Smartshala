package SmartShala.SmartShala.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class QuetionPaper {

    @Id
    @GeneratedValue
    int id;

    int theoryMarks;

    @NotNull(message = "Start time cannot be null")
    @NotBlank(message = "Start time cannot be empty or just spaces")
    @Pattern(
            regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",
            message = "Start time must be in the format yyyy-MM-dd HH:mm:ss"
    )
    String startTime;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 480, message = "Duration cannot exceed 480 minutes")
    int duration;

    @OneToMany(cascade = CascadeType.ALL)
    @NotNull(message = "list of mcqs cant be null")
    List<McqTypeQuetion> mcqTypeQuetionList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @NotNull(message = "list of theory quetions cant be null at least send a empty list []")
    List<TheoryQuetion> theoryQuetions = new ArrayList<>();

    int testId;

    public List<McqTypeQuetion> getMcqTypeQuetionList() {
        return mcqTypeQuetionList;
    }
    public void setMcqTypeQuetionList(List<McqTypeQuetion> mcqTypeQuetionList) {
        this.mcqTypeQuetionList = mcqTypeQuetionList;
    }
    public List<TheoryQuetion> getTheoryQuetions() {
        return theoryQuetions;
    }
    public void setTheoryQuetions(List<TheoryQuetion> theoryQuetions) {
        this.theoryQuetions = theoryQuetions;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getTestId() {
        return testId;
    }
    public void setTestId(int testId) {
        this.testId = testId;
    }
    public int getTheoryMarks() {
        return theoryMarks;
    }
    public void setTheoryMarks(int theoryMarks) {
        this.theoryMarks = theoryMarks;
    }
}
