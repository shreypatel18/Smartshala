package SmartShala.SmartShala.Entities;

import java.util.List;

public class ClassDto {

    List<StudentDto> studentDtos;
    List<SubjectDto> subjectDtos;

    public List<StudentDto> getStudentDtos() {
        return studentDtos;
    }
    public void setStudentDtos(List<StudentDto> studentDtos) {
        this.studentDtos = studentDtos;
    }
    public List<SubjectDto> getSubjectDtos() {
        return subjectDtos;
    }
    public void setSubjectDtos(List<SubjectDto> subjectDtos) {
        this.subjectDtos = subjectDtos;
    }
}
