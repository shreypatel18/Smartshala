package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.StudentDto;
import SmartShala.SmartShala.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;


   public StudentDto getStudentDto(int id){
       Student student =  studentRepository.findById(id).get();
       StudentDto studentDto = new StudentDto();
       studentDto.setName(student.getName());
       studentDto.setEnrollmentNo(student.getStudentId());
       studentDto.setSubjects(student.getClassroom().getSubjects());
       return studentDto;
    }


}
