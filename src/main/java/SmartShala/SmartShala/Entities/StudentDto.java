package SmartShala.SmartShala.Entities;

import org.tensorflow.op.math.Sub;

import java.util.List;

public class StudentDto {

    int enrollmentNo;

    String name;

    List<Subject>  subjects;

    String className;

    Photo photo;

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
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public Photo getPhoto() {
        return photo;
    }
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
