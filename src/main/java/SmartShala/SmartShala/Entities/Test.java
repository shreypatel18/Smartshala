package SmartShala.SmartShala.Entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.tensorflow.op.math.Sub;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Test{


    @Id
    @GeneratedValue
    int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "answerkey")
    Answer answerKey;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "test", fetch = FetchType.EAGER)
    List<Answer> answers = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    QuetionPaper quetionPaper;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Result> results = new ArrayList<>();


    @ManyToOne
    Subject subject;
    String name;

    String status;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public QuetionPaper getQuetionPaper() {
        return quetionPaper;
    }

    public void setQuetionPaper(QuetionPaper quetionPaper) {
        this.quetionPaper = quetionPaper;
    }

    public SmartShala.SmartShala.Entities.Subject getSubject() {
        return subject;
    }

    public void setSubject(SmartShala.SmartShala.Entities.Subject subject) {
       this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
