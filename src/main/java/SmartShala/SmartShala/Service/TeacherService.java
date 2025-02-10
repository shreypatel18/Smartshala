package SmartShala.SmartShala.Service;


import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    public Teacher getTeacher(int id){
        return teacherRepository.findById(id).get();
    }


}
