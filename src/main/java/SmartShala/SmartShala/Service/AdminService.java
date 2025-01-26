package SmartShala.SmartShala.Service;


import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Repository.StudentRepository;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService{

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    TeacherRepository teacherRepository;


    public Teacher registerTeacher(Teacher teacher){

        return teacherRepository.save(teacher);
    }

    public Student registerStudent(Student student){
        return studentRepository.save(student);
    }

    public List<Teacher> registerTeachers(List<Teacher> teacher){

        return teacherRepository.saveAll(teacher);
    }
    public  String sendMail(){
        return "";
    }

}
