package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.Classroom;
import SmartShala.SmartShala.Entities.Notice;
import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Repository.ClassRepo;
import SmartShala.SmartShala.Repository.StudentRepository;
import SmartShala.SmartShala.Repository.TeacherRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;


    @Autowired
    ClassroomService classroomService;

    @Autowired
    MailService mailService;

    String admin_Mail = "shap121815@gmail.com";

    public List<String> sendToEntireSchool(Notice notice) {
        List<String> mails;
        mails = getMailsTeacher(teacherRepository.findAll());
        mails.addAll(getMailsStudents(studentRepository.findAll()));
        return sendMails(mails, notice);
    }

    public List<String> sendToTeachers(Notice notice) {
        List<String> mails;
        mails = getMailsTeacher(teacherRepository.findAll());
        return sendMails(mails, notice);

    }

    public List<String> sendToStudents(Notice notice) {
        List<String> mails;
        mails = getMailsStudents(studentRepository.findAll());
        return sendMails(mails, notice);
    }

    public List<String> sendToClass(Notice notice, String name) {
        Classroom classroom = classroomService.getClassRoomByName(name);
        List<String> mails = getMailsTeacher(classroom.getTeachers());
        mails.addAll(getMailsStudents(classroom.getStudents()));
        return sendMails(mails, notice);

    }


    List<String> getMailsTeacher(List<Teacher> teachers) {
        List<String> mails = new ArrayList<>();
        teachers.forEach((m) -> mails.add(m.getEmail()));
        return mails;
    }

    List<String> getMailsStudents(List<Student> students) {
        List<String> mails = new ArrayList<>();
        students.forEach((m) -> mails.add(m.getEmail()));
        return mails;
    }

    List<String> sendMails(List<String> mails, Notice notice) {
        List<String> failedToSend = new ArrayList<>();
        for (String mail : mails) {
            if (!mailService.sendMail(mail, notice.getSubject(), notice.getBody())) {
                failedToSend.add(mail);
            }
            ;
        }
        return failedToSend;
    }
}
