package SmartShala.SmartShala.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Classroom {


    @Id
    int classId;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    List<Subject> subjects;

    @ManyToMany(mappedBy = "classrooms",cascade = CascadeType.ALL)
    @JsonBackReference
    List<Teacher> teachers;


    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    List<Student> students;
}
