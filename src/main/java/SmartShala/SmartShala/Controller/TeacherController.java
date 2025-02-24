package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.CustomException.AuthorisationException;
import SmartShala.SmartShala.CustomException.TestException;
import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Service.GoogleDriveService;
import SmartShala.SmartShala.Service.PhotoService;
import SmartShala.SmartShala.Service.TeacherService;
import SmartShala.SmartShala.Service.TestService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    TestService testService;
    @Autowired
    PhotoService photoService;

    @PostMapping("/generateTest")
    ResponseEntity<String> generateTest(@Valid @RequestBody Test test, @RequestParam int subCode, Principal principal) throws IOException {
        if (teacherService.isTeacherAuthorisedToGenerateTest(Integer.valueOf(principal.getName()), subCode)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(testService.generateTest(test.getQuetionPaper(), test.getAnswerKey(), subCode, test.getName()));
        } else {
            throw new AuthorisationException("Teacher with id :" + principal.getName() + " is not authorised to generate test for subject: " + subCode);
        }
    }

    @GetMapping("/getTestData")
    Test getAllAnswerSheets(@RequestParam int testId, Principal principal) {
        if (teacherService.checkIfTeacherIsAuthorisedToViewTestData(testId, Integer.valueOf(principal.getName()))) {
            return testService.getTest(testId);
        } else {
            throw new AuthorisationException("Teacher with id :" + principal.getName() + " is not authorised to view test data for test: " + testId);
        }
    }

    @GetMapping("publishResult")
    String publish(@RequestParam int testId, Principal principal) {
        //as teacher which is authorised to view data is also authorised to publish results
        if (teacherService.checkIfTeacherIsAuthorisedToViewTestData(testId, Integer.valueOf(principal.getName()))) {
            return testService.publish(testId);
        } else {
            throw new AuthorisationException("Teacher with id : " + principal.getName() + " is not authorised to publish results for test: " + testId);
        }
    }

    @GetMapping("/addTheoryMarks")
    Result enterTheoryMarks(@RequestParam int studentId, @RequestParam int testId, @RequestParam int marks, Principal principal) {
        //as teacher who is authorised to view test data is also authorised to add marks
        if (teacherService.checkIfTeacherIsAuthorisedToViewTestData(testId, Integer.valueOf(principal.getName()))) {
            return testService.enterTheoryMarks(studentId, testId, marks);
        } else {
            throw new AuthorisationException("Teacher with id : " + principal.getName() + " is not authorised to add theory marks for test: " + testId);
        }
    }

    @GetMapping("viewAnswerSheetPhotos")
    List<Photo> viewAnswerSheetPhotos(@RequestParam int studentId, @RequestParam int testId, Principal principal) throws IOException {
        if (studentId < 100000 || studentId > 999999) throw new TestException("invalid student id it must be 6 digits");
        if (teacherService.checkIfTeacherIsAuthorisedToViewTestData(testId, Integer.valueOf(principal.getName()))) {
            List<Photo> photos = testService.getAnswerSheetImages(studentId, testId);
            return photos;
        } else {
            throw new AuthorisationException("Teacher with id : " + principal.getName() + " is not authorised view photos  for test: " + testId);
        }
    }

    @GetMapping("home")
    Teacher Home(Principal principal) {
        Teacher teacher = teacherService.getTeacher(Integer.valueOf(principal.getName()));
        teacher.setPhoto(photoService.getPhoto(teacher.getTeacherId()));
        return teacher;
    }

    @GetMapping("/getTests")
    List<Test> getTestBySubject(@RequestParam int subCode, Principal principal) {
        //as teacher who is authorised to generate test for subject is also allowed to view tests of that subject
        if (teacherService.isTeacherAuthorisedToGenerateTest(Integer.valueOf(principal.getName()), subCode)) {
            return testService.getTestsBySubjectId(subCode);
        } else {
            throw new AuthorisationException("Teacher with id : " + principal.getName() + " is not authorised view tests for subject: " + subCode);
        }
    }

    @PostMapping("/uploadPhoto")
    ResponseEntity<String> photo(@RequestParam("photo") MultipartFile photo, Principal principal) {
        photoService.uploadPhoto(photo, Integer.valueOf(principal.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }
}




