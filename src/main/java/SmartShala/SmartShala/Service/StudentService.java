package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.CustomException.EntityNotFoundException;
import SmartShala.SmartShala.CustomException.TestException;
import SmartShala.SmartShala.Entities.Classroom;
import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.StudentDto;
import SmartShala.SmartShala.Entities.Subject;
import SmartShala.SmartShala.Repository.StudentRepository;
import SmartShala.SmartShala.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TestRepository testRepository;

    public Student getStudent(int id) {
        Student student = studentRepository.findById(id).get();
        return student;
    }


    public boolean checkIfStudentCanSubmit(int studentId, int testId, int studentIdInAnswerSheet) {
        Classroom classroom = studentRepository.findById(studentId).get().getClassroom();
        Subject subject = testRepository.findById(testId).orElseThrow(() -> new TestException("test with id: " + testId + " not found")).getSubject();
        if (subject.getClassroom().getClassId() == classroom.getClassId() && studentId == studentIdInAnswerSheet) {
            return true;
        }
        return false;
    }


}
