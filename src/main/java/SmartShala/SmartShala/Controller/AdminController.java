package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Repository.TeacherRepository;
import SmartShala.SmartShala.Service.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    StudentService studentService;


    @Autowired
    ExcelDataExtractorStudent excelDataExtractorStudent;


    @Autowired
    DTOtraforms dtOtraforms;

    @Autowired
    NoticeService noticeService;



    @PostMapping(value = "register/teacher", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Teacher registerTeacher(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestPart("photo") MultipartFile photo, @RequestParam("address") String address, @RequestParam("gender") String gender, @RequestParam("age") int age, @RequestParam("email") String email) throws IOException {


        Teacher teacher = new Teacher();
        teacher.setEmail(email);
        teacher.setTeacherId(id);
        teacher.setName(name);
        teacher.setAge(age);
        teacher.setGender(gender.charAt(0));
        teacher.setPhoto(PhotoEncoder.encodePhotoToBase64(photo));
        teacher.setAddress(address);

        // Pass the teacher and file data to the service
        return adminService.registerTeacher(teacher);


    }


    @PostMapping(value = "register/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Student registerStudent(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("name") String name, @RequestParam("enrollmentNumber") int enrollmentNumber, @RequestParam("class") int Class, @RequestParam("gender") String gender, @RequestParam("age") int age, @RequestParam("address") String address, @RequestParam("email") String email) throws IOException {

        System.out.println(enrollmentNumber);

        Student student = new Student();
        student.setStudentId(enrollmentNumber);
        student.setAge(age);
        student.setAddress(address);
        student.setClassroom(classroomService.getClassRoomByName("class" + Class));
        student.setName(name);
        student.setEmail((email));
        student.setGender(gender.charAt(0));


        // Process the photo (e.g., save to a database or file system)
        student.setPhoto(PhotoEncoder.encodePhotoToBase64(photo));


        // Pass the teacher and file data to the service
        return adminService.registerStudent(student);
    }

    @PostMapping("/register/uploadTeachersExcelSheet")
    List<Teacher> uploadTeachersExcelSheet(@RequestParam("excel") MultipartFile multipartFile) throws IOException {

        if (multipartFile.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {

            return adminService.registerTeachers(ExcelDataExtractorTeacher.getListOfTeachers(multipartFile));
        }

        return List.of();
    }

    @PostMapping("/register/uploadStudentsExcelSheet")
    List<Student> uploadStudentssExcelSheet(@RequestParam("excel") MultipartFile multipartFile) throws IOException {

        if (multipartFile.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {

            return adminService.registerStudents(excelDataExtractorStudent.getListOfStudent(multipartFile));
        }

        return List.of();
    }

    @PostMapping("uploadTestPaper")
    public Answer uploadFolder(@RequestParam  int testId, @RequestParam MultipartFile[] images, @RequestParam int studentId) throws IOException {
        testService.uploadImages(testId, images,studentId);
        return testService.addScannedTheoryAnswers(studentId, testId, images);
    }


    @PostMapping(value = "addClassroom")
    public Classroom addClassroom(@RequestBody Classroom classroom) throws IOException {
        return classroomService.addClassroom(classroom);
    }


    @PostMapping("/addSubject")
    public List<Subject> addSubject(@RequestBody Subject subject, @RequestParam("class") String Class) throws IOException {
        return adminService.addSubject(subject, Class);
    }

    @PostMapping("assignTeacher")
    public Subject assignSubject(@RequestParam int teacherId, @RequestParam int subCode, @RequestParam String className) {
        Subject subject = adminService.assign(subCode, teacherId, className);
        subject.setTeacher(null);
        return subject;
    }


    @GetMapping("/home")
    public List adminHome() {
        Admin admin = new Admin();
        admin.setName("admin1");
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        List<String> classroomNames = classrooms.stream()
                .map(Classroom::getName)
                .collect(Collectors.toList());

        return List.of(admin, classroomNames);

    }


    @GetMapping("/getClassDetails")
    public ClassDto getClassDto(@RequestParam("name") String name) {

      Classroom classroom = classroomService.getClassRoomByName(name);


      ClassDto classDto = new ClassDto();
      classDto.setStudentDtos(DTOtraforms.getStudentDto(classroom.getStudents()));
      classDto.setSubjectDtos(dtOtraforms.subjectDto(classroom.getSubjects()));
      return classDto;
    }


    @Autowired
    SubjectRepository subRepository;


    @GetMapping("getAllTeachers")
    public List<TeacherDto> getAllTeacher() {
        return DTOtraforms.getTeacherDtos(adminService.getAllTeachers());
    }

    @GetMapping("sendToClass")
    public String sendToClassroom(@RequestBody Notice notice, @RequestParam String classname){
        return noticeService.sendToClass(notice, classname);
    }


    @GetMapping("sendToTeachers")
    public String sendToTeachers(@RequestBody Notice notice){
        return noticeService.sendToTeachers(notice);
    }

    @GetMapping("sendToStudents")
    public String sendToStudents(@RequestBody Notice notice){
        return noticeService.sendToStudents(notice);
    }



}
