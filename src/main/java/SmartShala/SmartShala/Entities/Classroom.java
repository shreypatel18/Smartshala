package SmartShala.SmartShala.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int classId;

    @NotBlank(message = "classname is required")
    @Pattern(regexp = "^class\\d{1,2}$", message = "classname should be in the format 'class' followed by a number (e.g., class10, class1).")
    String name;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    List<Subject> subjects =new ArrayList<>();

    @ManyToMany
    List<Teacher> teachers = new ArrayList<>();


    @OneToMany(mappedBy = "classroom")
    List<Student> students = new ArrayList<>();

    public int getClassId() {
        return classId;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Subject> getSubjects() {
        return subjects;
    }
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }
    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    public List<Student> getStudents() {
        return students;
    }
    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
