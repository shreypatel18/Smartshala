package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Service.AdminService;
import SmartShala.SmartShala.Service.ExcelDataExtractor;
import SmartShala.SmartShala.Service.PhotoEncoder;
import SmartShala.SmartShala.Service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController{
    @Autowired
    AdminService adminService;







    @PostMapping(value = "register/teacher", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Teacher registerTeacher(
            @RequestPart("teacher") String teacherJson,
            @RequestPart("photo") MultipartFile photo) throws IOException {


        // Convert the JSON string into a Teacher object
        ObjectMapper objectMapper = new ObjectMapper();
        Teacher teacher = objectMapper.readValue(teacherJson, Teacher.class);

        // Process the photo (e.g., save to a database or file system)
         teacher.setPhoto(PhotoEncoder.encodePhotoToBase64(photo));


        // Pass the teacher and file data to the service
        return  adminService.registerTeacher(teacher);



    }



    @PostMapping(value = "register/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Student registerStudent(
            @RequestPart("student") String studentJson,
            @RequestPart("photo") MultipartFile photo) throws IOException{


        // Convert the JSON string into a Teacher object
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = objectMapper.readValue(studentJson, Student.class);

        // Process the photo (e.g., save to a database or file system)
        student.setPhoto(PhotoEncoder.encodePhotoToBase64(photo));


        // Pass the teacher and file data to the service
        return adminService.registerStudent(student);
    }

    @PostMapping("/register/uploadTeachersExcelSheet")
    List<Teacher> uploadTeachersExcelSheet(@RequestParam("excel") MultipartFile multipartFile) throws IOException {

                if(multipartFile.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){

                    return   adminService.registerTeachers(ExcelDataExtractor.getListOfTeachers(multipartFile));
                   }

                return List.of();
    }


    }




