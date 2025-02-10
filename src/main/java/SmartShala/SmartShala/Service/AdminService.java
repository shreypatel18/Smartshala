package SmartShala.SmartShala.Service;


import SmartShala.SmartShala.Entities.Classroom;
import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.Subject;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Repository.ClassRepo;
import SmartShala.SmartShala.Repository.StudentRepository;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class AdminService{

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    ClassRepo classRepo;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    ClassroomService classroomService;


    public Teacher registerTeacher(Teacher teacher){

        return teacherRepository.save(teacher);
    }

    public Student registerStudent(Student student){
        return studentRepository.save(student);
    }

    public List<Teacher> registerTeachers(List<Teacher> teacher){

        return teacherRepository.saveAll(teacher);
    }

    public List<Student> registerStudents(List<Student> students){

        return studentRepository.saveAll(students);
    }

    public  String sendMail(){
        return "";
    }

    public Subject assign(int subCode, int teacherId,String className){

            Teacher teacher = teacherRepository.findById(teacherId).get();

     Classroom classroom = classRepo.findByName(className).get();
     classroom.getTeachers().add(teacher);
     classRepo.save(classroom);


     Subject subject = subjectRepository.findById(subCode).get();
     subject.setTeacher(teacher);
     return subjectRepository.save(subject);

    }

    public List<Teacher> getAllTeachers(){
        return teacherRepository.findAll();
    }


    public List<Subject> addSubject(Subject subject, String className) throws IOException {
        Classroom classroom = classroomService.getClassRoomByName(className);
        Subject subject1 = new Subject();
        subject1.setName(subject.getName());
        subject1.setSubCode(subject.getSubCode());
        boolean subjectExists = classroom.getSubjects().stream()
                .anyMatch(existingSubject -> existingSubject.getSubCode() == subject1.getSubCode());

        subject1.setClassroom(classroom);
        // If the subject doesn't exist, add it to the list
        if (!subjectExists) {
            classroom.getSubjects().add(subject1);
        }
        classroomService.updateClassroom(classroom);
        GoogleDriveService.createSubjectFolder(className,subject1.getName());
        return classroomService.getClassRoomByName(classroom.getName()).getSubjects();
    }



}
