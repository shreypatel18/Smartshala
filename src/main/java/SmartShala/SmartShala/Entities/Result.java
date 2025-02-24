package SmartShala.SmartShala.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Result {

    @Id
    @GeneratedValue
    int id;

    @ManyToOne
    @JsonBackReference
    Test test;

    int studentId;

    int mcqMarks;

    int theoryMarks;

    Boolean status;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMcqMarks() {
        return mcqMarks;
    }
    public void setMcqMarks(int mcqMarks) {
        this.mcqMarks = mcqMarks;
    }
    public int getTheoryMarks() {
        return theoryMarks;
    }
    public void setTheoryMarks(int theoryMarks) {
        this.theoryMarks = theoryMarks;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public Test getTest() {
        return test;
    }
    public void setTest(Test test) {
        this.test = test;
    }
}
