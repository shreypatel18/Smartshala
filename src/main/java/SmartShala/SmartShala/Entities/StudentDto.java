package SmartShala.SmartShala.Entities;

import org.tensorflow.op.math.Sub;

import java.util.List;

public class StudentDto {
    int enrollmentNo;
    String name;

    List<Subject>  subjects;
    public int getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(int enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
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
}
