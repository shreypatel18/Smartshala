package SmartShala.SmartShala.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Subject{

    @Id
    @Min(value = 100000, message = "subCode must be at least 6 digits")
    @Max(value = 999999, message = "subCode must be at most 6 digits")
    int subCode;

    String name;

    @ManyToOne
    @JsonBackReference
    Classroom classroom;

    @ManyToOne
    @JsonIgnore
    Teacher teacher;

    public int getSubCode() {
        return subCode;
    }
    public void setSubCode(int subCode) {
        this.subCode = subCode;
    }
    public Classroom getClassroom() {
        return classroom;
    }
    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
