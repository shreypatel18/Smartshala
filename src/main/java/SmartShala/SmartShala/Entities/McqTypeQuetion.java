package SmartShala.SmartShala.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class McqTypeQuetion{

    @Id
    @GeneratedValue
    int id;

    String quetion;

    @JsonProperty("A")
    private String optionA;

    @JsonProperty("B")
    private String optionB;

    @JsonProperty("C")
    private String optionC;

    @JsonProperty("D")
    private String optionD;

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
    public String getOptionA() {
        return optionA;
    }
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }
    public String getOptionB() {
        return optionB;
    }
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }
    public String getOptionC() {
        return optionC;
    }
    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }
    public String getOptionD() {
        return optionD;
    }
    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }
}
