package SmartShala.SmartShala.Security;

import SmartShala.SmartShala.CustomException.AuthorisationException;
import SmartShala.SmartShala.Entities.Admin;
import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Repository.StudentRepository;
import SmartShala.SmartShala.Repository.TeacherRepository;
import SmartShala.SmartShala.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User.UserBuilder builder = User.builder();

        if (userName.length() == 6) {
            Student student = studentRepository.findById(Integer.parseInt(userName)).orElseThrow(() -> new AuthorisationException("User not found for username: " + userName));
            builder.username(String.valueOf(student.getStudentId())).password(student.getPassword()).roles("STUDENT");
        } else if (userName.length() == 5) {
            Teacher teacher = teacherRepository.findById(Integer.parseInt(userName)).orElseThrow(() -> new AuthorisationException("User not found for username: " + userName));
            builder.username(String.valueOf(teacher.getTeacherId())).password(teacher.getPassword()).roles("TEACHER");
        } else if (userName.length() == 4) {
            Admin admin = adminService.getAdmin();
            builder.username(String.valueOf(admin.getId())).password(admin.getPassword()).roles("ADMIN");
        } else {
            throw new AuthorisationException("Invalid username login failed");
        }
        return builder.build();
    }
}
