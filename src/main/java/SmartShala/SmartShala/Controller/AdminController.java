package SmartShala.SmartShala.Controller;

import SmartShala.SmartShala.CustomException.EntityAlreadyExists;
import SmartShala.SmartShala.CustomException.TestException;
import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.apache.coyote.Response;
import org.apache.poi.util.DocumentFormatException;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin")
@CrossOrigin
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    TestService testService;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    ExcelDataExtractorStudent excelDataExtractorStudent;
    @Autowired
    DTOtraforms dtOtraforms;
    @Autowired
    NoticeService noticeService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(value = "register/teacher")
    public ResponseEntity<String> registerTeacher(@RequestParam("id") int id, @RequestParam("name") String name, @RequestPart(value = "photo", required = false) MultipartFile photo, @RequestParam("address") String address, @RequestParam("gender") String gender, @RequestParam("age") int age, @RequestParam("email") String email, @RequestParam(value = "password", required = false) String password) throws IOException {
        // Manual validation
        if (id < 10000 || id > 99999) {
            throw new IllegalArgumentException("ID must be exactly 5 digits");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        adminService.checkIfTeacherAlreadyExists(id);
        Teacher teacher = new Teacher();
        teacher.setEmail(email);
        teacher.setTeacherId(id);
        teacher.setName(name);
        teacher.setAge(age);
        teacher.setGender(gender.charAt(0));
        teacher.setAddress(address);
        teacher.setPassword(passwordEncoder.encode(password));
        if (photo != null && !photo.isEmpty()) {
            Photo photo1 = PhotoEncoder.encodePhotoToBase64(photo);
            photo1.setId(id);
            teacher.setPhoto(photo1);
        }
        adminService.registerTeacher(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body("registered successfully");
    }

    @PostMapping(value = "register/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerStudent(@RequestParam("photo") MultipartFile photo, @RequestParam("name") String name, @RequestParam("enrollmentNumber") int enrollmentNumber, @RequestParam("class") int Class, @RequestParam("gender") String gender, @RequestParam("age") int age, @RequestParam("address") String address, @RequestParam("email") String email, @RequestParam(value = "password", required = false) String password) throws IOException {
        // Manual validation
        if (enrollmentNumber < 100000 || enrollmentNumber > 999999) {
            throw new IllegalArgumentException("ID must be exactly  digits");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        adminService.checkIfStudentAlreadyExists(enrollmentNumber);
        Student student = new Student();
        student.setStudentId(enrollmentNumber);
        student.setAge(age);
        student.setAddress(address);
        student.setClassroom(classroomService.getClassRoomByName("class" + Class));
        student.setName(name);
        student.setEmail((email));
        student.setGender(gender.charAt(0));
        student.setPassword(passwordEncoder.encode(password));
        if (photo != null && !photo.isEmpty()) {
            Photo photo1 = PhotoEncoder.encodePhotoToBase64(photo);
            photo1.setId(enrollmentNumber);
            student.setPhoto(photo1);
        }
        adminService.registerStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("registered successfully");
    }

    @PostMapping("/register/uploadTeachersExcelSheet")
    public ResponseEntity<String> uploadTeachersExcelSheet(@RequestParam("excel") MultipartFile multipartFile) throws IOException {
        System.out.println("in upload");
        adminService.checkContentType(multipartFile);
        System.out.println("check content type");
        adminService.registerTeachers(ExcelDataExtractorTeacher.getListOfTeachers(multipartFile));
        return ResponseEntity.status(HttpStatus.CREATED).body("registered successfully");
    }

    @PostMapping("/register/uploadStudentsExcelSheet")
    public ResponseEntity<String> uploadStudentssExcelSheet(@RequestParam("excel") MultipartFile multipartFile) throws IOException {
        adminService.checkContentType(multipartFile);
        adminService.registerStudents(excelDataExtractorStudent.getListOfStudent(multipartFile));
        return ResponseEntity.status(HttpStatus.CREATED).body("registered successfully");
    }

    @PostMapping("uploadTestPaper")
    public ResponseEntity<String> uploadFolder(@RequestParam int testId, @RequestParam MultipartFile[] images, @RequestParam int studentId) {
        if (studentId < 100000 || studentId > 999999) throw new TestException("invalid student id it must be 6 digits");
        for (MultipartFile file : images) {
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equalsIgnoreCase("image/jpeg") && !contentType.equalsIgnoreCase("image/jpg"))) {
                throw new DocumentFormatException("unable to upload photos format must be jpeg or jpg");
            }
        }
        testService.uploadImages(testId, images, studentId);
        testService.addScannedTheoryAnswers(studentId, testId, images);
        return ResponseEntity.status(HttpStatus.CREATED).body("photos uploaded successfully");
    }

    @PostMapping("/addClassroom")
    public ResponseEntity<Classroom> addClassroom(@RequestBody @Valid Classroom classroom) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(classroomService.addClassroom(classroom));
    }

    @PostMapping("/addSubject")
    public ResponseEntity<List<Subject>> addSubject(@RequestBody @Valid Subject subject, @RequestParam("class") String Class) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.addSubject(subject, Class));
    }

    @PostMapping("/assignTeacher")
    public ResponseEntity<Subject> assignSubject(@RequestParam int teacherId, @RequestParam int subCode, @RequestParam String className) {
        Subject subject = adminService.assign(subCode, teacherId, className);
        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
    }

    @GetMapping("/home")
    public List adminHome() {
        Admin admin = adminService.getAdmin();
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        List<String> classroomNames = classrooms.stream().map(Classroom::getName).collect(Collectors.toList());
        return List.of(admin, classroomNames);
    }

    @GetMapping("/getClassDetails")
    public ClassDto getClassDto(@RequestParam("name") String name) {
        return dtOtraforms.getClassroomDto(name);
    }

    @GetMapping("getAllTeachers")
    public List<TeacherDto> getAllTeacher() {
        return DTOtraforms.getTeacherDtos(adminService.getAllTeachers());
    }

    @GetMapping("sendToClass")
    public List<String> sendToClassroom(@RequestBody @Valid Notice notice, @RequestParam String classname) {
        return noticeService.sendToClass(notice, classname);
    }

    @GetMapping("sendToTeachers")
    public List<String> sendToTeachers(@RequestBody Notice notice) {
        return noticeService.sendToTeachers(notice);
    }

    @GetMapping("sendToStudents")
    public List<String> sendToStudents(@RequestBody Notice notice) {
        return noticeService.sendToStudents(notice);
    }
}
