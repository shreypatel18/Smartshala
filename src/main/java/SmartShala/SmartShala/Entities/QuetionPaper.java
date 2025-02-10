package SmartShala.SmartShala.Entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class QuetionPaper {

    @Id
    @GeneratedValue
    int id;

    int testId;

    String startTime;
    int duration;

    @OneToMany(cascade = CascadeType.ALL)
    List<McqTypeQuetion> mcqTypeQuetionList;

    @OneToMany(cascade = CascadeType.ALL)
    List<TheoryQuetion> theoryQuetions;

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

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
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
}
