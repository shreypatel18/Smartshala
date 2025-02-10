package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Repository.SubjectRepository;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DTOtraforms {

    @Autowired
    SubjectRepository subjectRepository;

    public static StudentDto getStudentDto(Student student){
        StudentDto studentDto = new StudentDto();
        studentDto.setEnrollmentNo(student.getStudentId()) ;
        studentDto.setName(student.getName());
        return studentDto;
    }

    public static TeacherDto getTeacherDto(Teacher teacher){
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setTeacherId(teacher.getTeacherId());
        teacherDto.setName(teacher.getName());
        return teacherDto;
    }

    public SubjectDto  subjectDto(Subject subject){
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setSubCode(subject.getSubCode());
        subjectDto.setSubName(subject.getName());
        if(subject.getTeacher()!=null) {
            subjectDto.setTeacherId(subject.getTeacher().getTeacherId());

            subjectDto.setTeacherName(subject.getTeacher().getName());
        }
        return subjectDto;
    }


    public static List<StudentDto> getStudentDto(List<Student> student){

        List<StudentDto> list = new ArrayList<>();
        for(Student student1 : student) {
            StudentDto studentDto = new StudentDto();
            studentDto.setEnrollmentNo(student1.getStudentId());
            studentDto.setName(student1.getName());
            list.add(studentDto);
        }
        return list;
    }


    public static List<TeacherDto> getTeacherDtos(List<Teacher> teachers){

        List<TeacherDto> teacherDtos = new ArrayList<>();

        for(Teacher teacher: teachers) {
            TeacherDto teacherDto = new TeacherDto();
            teacherDto.setTeacherId(teacher.getTeacherId());
            teacherDto.setName(teacher.getName());
            teacherDtos.add(teacherDto);
        }
        return teacherDtos;
    }

    public List<SubjectDto>  subjectDto(List<Subject> subjects){

        List<SubjectDto> list = new ArrayList<>();
        for(Subject subject1: subjects) {
            SubjectDto subjectDto = new SubjectDto();
            subjectDto.setSubCode(subject1.getSubCode());
            subjectDto.setSubName(subject1.getName());
            if (subject1.getTeacher() != null) {
                subjectDto.setTeacherId(subject1.getTeacher().getTeacherId());
                subjectDto.setTeacherName(subject1.getTeacher().getName());
            }
            list.add(subjectDto);
        }
        return list;
    }

    public  static List<TestDto> getTestDtos(List<Test> tests){

        List<TestDto> testDtos =  new ArrayList<>();
        for(Test test: tests){
            TestDto testDto = new TestDto();
            testDto.setTestId(test.getId());
            testDto.setTestName(test.getName());
            testDtos.add(testDto);
        }
        return testDtos;
    }

    public static Map<String,List<TestDto>> getMapTestDto(List<Test> tests){
        List<TestDto> testDtosU =  new ArrayList<>();
        List<TestDto> testDtosA =  new ArrayList<>();
        List<TestDto> testDtosC =  new ArrayList<>();
        List<TestDto> testDtosCh = new ArrayList<>();
        for(Test test: tests){

            TestDto testDto = new TestDto();
            testDto.setTestId(test.getId());
            testDto.setTestName(test.getName());

            if(test.getStatus().equals("active")) {
                testDtosA.add(testDto);
            }else if(test.getStatus().equals("complete")) {
                testDtosC.add(testDto);
            }else if(test.getStatus().equals("checked")){
                testDtosCh.add(testDto);
            }else{
                testDtosU.add(testDto);
            }
        }
        Map<String , List<TestDto>> testDtoMap = new HashMap<>();
        testDtoMap.put("upcoming",testDtosU);
        testDtoMap.put("active",testDtosA);
        testDtoMap.put("complete",testDtosC);
        testDtoMap.put("checked",testDtosCh);
        return testDtoMap;
    }
}
