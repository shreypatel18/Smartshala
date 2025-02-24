package SmartShala.SmartShala.Service;


import SmartShala.SmartShala.CustomException.ClassRoomNotFoundException;
import SmartShala.SmartShala.CustomException.EntityAlreadyExists;
import SmartShala.SmartShala.CustomException.SubjectAlreadyAdded;
import SmartShala.SmartShala.CustomException.SubjectNotAssigned;
import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Repository.ClassRepo;
import SmartShala.SmartShala.Repository.StudentRepository;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Repository.TeacherRepository;
import org.apache.commons.compress.harmony.pack200.NewAttribute;
import org.apache.poi.util.DocumentFormatException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.tensorflow.op.Op;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    PasswordEncoder passwordEncoder;


    public Admin getAdmin() {
        Admin admin = new Admin();
        admin.setName("admin");
        admin.setId(1200);
        admin.setPassword(passwordEncoder.encode("admin123"));
        return admin;
    }

    public void registerTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public void registerStudent(Student student) {
        studentRepository.save(student);
    }

    public void registerTeachers(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            if (teacherRepository.existsById(teacher.getTeacherId())) continue;
            teacherRepository.save(teacher);
        }
    }

    public void registerStudents(List<Student> students) {

        for (Student student : students) {
            if (studentRepository.existsById(student.getStudentId())) continue;
            studentRepository.save(student);
        }
    }

    public String sendMail() {
        return "";
    }

    public Subject assign(int subCode, int teacherId, String className) {

        if (!teacherRepository.existsById(teacherId))
            throw new SubjectNotAssigned("teacher with id " + teacherId + " does not exist cannot assign subject");
        Teacher teacher = teacherRepository.findById(teacherId).get();
        Classroom classroom = classroomService.getClassRoomByName(className);
        classroom.getTeachers().add(teacher);
        if (!subjectRepository.existsById(subCode))
            throw new SubjectNotAssigned("subject with code " + subCode + " does not exist");
        Subject subject = subjectRepository.findById(subCode).get();
        subject.setTeacher(teacher);
        classroomService.updateClassroom(classroom);
        return subjectRepository.save(subject);

    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }


    public List<Subject> addSubject(Subject subject, String className) throws IOException {
        Classroom classroom = classroomService.getClassRoomByName(className);
        if (subjectRepository.existsById(subject.getSubCode())) {
            throw new SubjectAlreadyAdded("the subject with code " + subject.getSubCode() + " Already Added");
        }
        subject.setClassroom(classroom);
        classroom.getSubjects().add(subject);
        GoogleDriveService.createSubjectFolder(className, String.valueOf(subject.getSubCode()));
        classroomService.updateClassroom(classroom);
        return classroomService.getClassRoomByName(classroom.getName()).getSubjects();
    }


    public void checkIfTeacherAlreadyExists(int id) {
        if (teacherRepository.existsById(id))
            throw new EntityAlreadyExists("Teacher with id " + id + " Already Exists");
    }

    public void checkIfStudentAlreadyExists(int id) {
        if (studentRepository.existsById(id))
            throw new EntityAlreadyExists("Student with id " + id + " Already Exists");
    }

    public void checkContentType(MultipartFile multipartFile) {
        if (!multipartFile.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new DocumentFormatException("only support format is .xlsx your current format is " + multipartFile.getContentType());
        }
    }

}
