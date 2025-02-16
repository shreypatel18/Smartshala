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
    public  String sendToEntireSchool(Notice notice){
    List<String> mails;
    mails = getMailsTeacher(teacherRepository.findAll());
    mails.addAll(getMailsStudents(studentRepository.findAll()));
    sendMails(mails, notice);
    return "success";
    }
    public String sendToTeachers(Notice notice){
        List<String> mails;
        mails = getMailsTeacher(teacherRepository.findAll());
        sendMails(mails, notice);
        return "success";
    }

    public String sendToStudents(Notice notice){
        List<String> mails;
        mails = getMailsStudents(studentRepository.findAll());
        sendMails(mails, notice);
        return "success";
    }

    public String sendToClass(Notice notice, String name){
        Classroom classroom = classroomService.getClassRoomByName(name);
        List<String> mails;
        mails = getMailsTeacher(classroom.getTeachers());
        mails.addAll(getMailsStudents(classroom.getStudents()));
        sendMails(mails, notice);
        return "success";
    }


    List<String> getMailsTeacher(List<Teacher> teachers){
        List<String> mails = new ArrayList<>();
        teachers.forEach((m)->mails.add(m.getEmail()));
        return mails;
    }

    List<String> getMailsStudents(List<Student> students){
        List<String> mails = new ArrayList<>();
        students.forEach((m)->mails.add(m.getEmail()));
        return mails;
    }

    Boolean sendMails(List<String> mails,Notice notice){
        mails.forEach(m-> System.out.println(m));
        System.out.println(notice.getBody());
        System.out.println(notice.getSubject());
        mails.forEach(m->mailService.sendMail("",m,notice.getSubject(), notice.getBody()));
        return true;
     }


}
