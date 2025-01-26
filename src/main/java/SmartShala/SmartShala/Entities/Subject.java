package SmartShala.SmartShala.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subject {

    @Id
    int subCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    Classroom classroom;


    @ManyToMany(mappedBy = "subjects", cascade = CascadeType.ALL)
    List<Teacher> teachers;
}
