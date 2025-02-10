package SmartShala.SmartShala.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Answer {

    @Id
    @GeneratedValue
    int id;

    @ManyToOne
     @JsonBackReference
    Test test;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Character> mcqAnswers;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> theoryAnswers;

    int studentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }


    public List<Character> getMcqAnswers() {
        return mcqAnswers;
    }

    public void setMcqAnswers(List<Character> mcqAnswers) {
        this.mcqAnswers = mcqAnswers;
    }

    public List<String> getTheoryAnswers() {
        return theoryAnswers;
    }

    public void setTheoryAnswers(List<String> theoryAnswers) {
        this.theoryAnswers = theoryAnswers;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
