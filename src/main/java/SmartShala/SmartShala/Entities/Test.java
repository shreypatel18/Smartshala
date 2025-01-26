package SmartShala.SmartShala.Entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Test{


    @Id
    @GeneratedValue
    int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "answerkey")
    Answer answerKey;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "test")
    List<Answer> answers;

    @OneToMany(cascade = CascadeType.ALL)
    List<McqTypeQuetion> mcqs;

    @OneToMany(cascade = CascadeType.ALL)
    List<TheoryQuetion> theoryQuetions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<McqTypeQuetion> getMcqs() {
        return mcqs;
    }

    public void setMcqs(List<McqTypeQuetion> mcqs) {
        this.mcqs = mcqs;
    }

    public List<TheoryQuetion> getTheoryQuetions() {
        return theoryQuetions;
    }

    public void setTheoryQuetions(List<TheoryQuetion> theoryQuetions) {
        this.theoryQuetions = theoryQuetions;
    }


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answer getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(Answer answerKey) {
        this.answerKey = answerKey;
    }
}
