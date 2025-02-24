package SmartShala.SmartShala.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TheoryQuetion {

    @Id
    @GeneratedValue
    int id;

    String quetion;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getQuetion() {
        return quetion;
    }
    public void setQuetion(String quetion) {
        this.quetion = quetion;
    }
}
