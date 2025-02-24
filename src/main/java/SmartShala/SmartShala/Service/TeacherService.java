package SmartShala.SmartShala.Service;


import SmartShala.SmartShala.CustomException.SubjectNotAssigned;
import SmartShala.SmartShala.CustomException.TestException;
import SmartShala.SmartShala.Entities.Subject;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Repository.TeacherRepository;
import SmartShala.SmartShala.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tensorflow.op.math.Sub;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    SubjectRepository subjectRepository;

    public Teacher getTeacher(int id) {
        return teacherRepository.findById(id).get();
    }

    public boolean isTeacherAuthorisedToGenerateTest(int teacherId, int subCode) {
        Teacher teacher = subjectRepository.findById(subCode).orElseThrow(() -> new SubjectNotAssigned("subject with code " + subCode + " not found")).getTeacher();
        if (teacher != null && teacher.getTeacherId() == teacherId) return true;
        return false;
    }

    public boolean checkIfTeacherIsAuthorisedToViewTestData(int testId, int teacherId) {
        Subject subject = testRepository.findById(testId).orElseThrow(() -> new TestException("test with id: " + testId + " not found")).getSubject();
        if (subject.getTeacher().getTeacherId() == teacherId) return true;
        return false;
    }


}
