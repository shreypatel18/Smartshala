package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.CustomException.AuthorisationException;
import SmartShala.SmartShala.CustomException.TestException;
import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Service.*;
import com.google.api.Http;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@CrossOrigin
public class StudentController {

    @Autowired
    TestService testService;
    @Autowired
    StudentService studentService;
    @Autowired
    PhotoService photoService;

    @GetMapping("getTest")
    QuetionPaper getTest(@RequestParam int tesTid) {
        Test test = testService.getTest(tesTid);

        if (test.getStatus().equals("active")) {
            QuetionPaper quetionPaper = test.getQuetionPaper();
            quetionPaper.setTestId(tesTid);
            return quetionPaper;
        }
        return null;
    }

    @PostMapping("submit")
    ResponseEntity<Answer> submitAnswer(@RequestParam int testId, @RequestBody @Valid Answer answer, Principal principal) {
        if (answer.getStudentId() < 100000 || answer.getStudentId() > 999999)
            throw new TestException("invalid student id it must be 6 digits");
        if (studentService.checkIfStudentCanSubmit(Integer.valueOf(principal.getName()), testId, answer.getStudentId())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(testService.submitAnswer(testId, answer));
        } else {
            throw new AuthorisationException("student with id : " + principal.getName() + " is not authorised to submit");
        }
    }

    @GetMapping("getAiOverview")
    String getAiOverview(@RequestParam int testId, @RequestParam int quetionIndex, @RequestParam String answer) {
        return testService.getAiOverview(answer, testId, quetionIndex);
    }

    @GetMapping("viewAnswers")
    AnswerSheetDto viewAnswerSheet(@RequestParam int studentId, @RequestParam int testId, Principal principal) {
        if (studentId < 100000 || studentId > 999999) throw new TestException("invalid student id it must be 6 digits");
        if (Integer.valueOf(principal.getName()) == studentId) {
            return testService.getAnswerSheetDto(studentId, testId);
        } else {
            throw new AuthorisationException("student with id: " + principal.getName() + " is not authorised to do this action");
        }
    }

    @GetMapping("viewAnswerSheetPhotos")
    List<Photo> viewAnswerSheetPhotos(@RequestParam int studentId, @RequestParam int testId, Principal principal) {
        if (studentId < 100000 || studentId > 999999) throw new TestException("invalid student id it must be 6 digits");
        if (Integer.valueOf(principal.getName()) == studentId) {
            List<Photo> photos = testService.getAnswerSheetImages(studentId, testId);
            return photos;
        } else {
            throw new AuthorisationException("student with id: " + principal.getName() + " is not authorised to do this action");
        }
    }

    @GetMapping("home")
    StudentDto getStudentData(Principal principal) {
        Student student = studentService.getStudent(Integer.parseInt(principal.getName()));
        StudentDto studentDto = DTOtraforms.getStudentDto(student);
        studentDto.setPhoto(photoService.getPhoto(student.getStudentId()));
        return studentDto;
    }

    @GetMapping("viewTests")
    Map<String, List<TestDto>> getTestDtos(@RequestParam int subCode) {
        return testService.getTestBySubjectIdMap(subCode);
    }

    @GetMapping("getResult")
    Result getResult(@RequestParam int studentId, @RequestParam int testId, Principal principal) {
        if (studentId < 100000 || studentId > 999999) throw new TestException("invalid student id it must be 6 digits");
        return testService.getResult(testId, studentId);
    }

    @PostMapping("/uploadPhoto")
    ResponseEntity<String> photo(@RequestParam("photo") MultipartFile photo, Principal principal) {
        photoService.uploadPhoto(photo, Integer.valueOf(principal.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }
}
